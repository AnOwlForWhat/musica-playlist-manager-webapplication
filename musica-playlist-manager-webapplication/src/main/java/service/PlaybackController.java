    package service;

import dsa.HistoryStack;
import model.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class PlaybackController {
    private static PlaybackController instance;
    public PlaylistManager playlistManager;
    public HistoryStack historyStack;
    public HashMap<String, Song> songMap;
    public ArrayList<Song> songList;
    public Song currentPlayingSong;
    public dsa.CircularLinkedList.Node currentPlaylistNode;
    public boolean isShuffle = false;
    public boolean isRepeat = true;
    public int currentShuffleIndex = 0;
    private Stack<Integer> shuffleIndexHistory = new Stack<>();
    // Danh sach bai da tron tu songList (dung khi playlist trong va Shuffle bat)
    private ArrayList<Song> libraryShuffleList = new ArrayList<>();
    private int libraryShuffleIndex = 0;
    private static final Random RANDOM = new Random();
    private PlaybackController() {
        playlistManager = new PlaylistManager();
        historyStack = new HistoryStack();
        songMap = new HashMap<>();
        songList = new ArrayList<>();
        
        loadHardcodedSongs();
    }

    public static synchronized PlaybackController getInstance() {
        if (instance == null) {
            instance = new PlaybackController();
        }
        return instance;
    }

    private void loadHardcodedSongs() {
        songMap.clear();
        songList.clear();
        addSongToLibrary(new Song("S01", "Khong The Say", "Peter Griffin Cover", 240, "data/music/khong-the-say.mp3"));
        addSongToLibrary(new Song("S02", "Nguoi Im Lang Gap Nguoi Hay Noi", "Peter Griffin Cover", 252, "data/music/nguoi-im-lang-gap-nguoi-hay-noi.mp3"));
        addSongToLibrary(new Song("S03", "Truoc Khi Em Ton Tai", "Peter Griffin Cover", 210, "data/music/truoc-khi-em-ton-tai.mp3"));
        addSongToLibrary(new Song("S04", "Nhan Sinh Quan", "Peter Griffin Cover", 266, "data/music/nhan-sinh-quan.mp3"));
        addSongToLibrary(new Song("S05", "Phep Mau", "Mounter", 255, "data/music/phep-mau.mp3"));
        addSongToLibrary(new Song("S06", "Tung Ngay Yeu Em", "buitruonglinh", 221, "data/music/tung-ngay-yeu-em.mp3"));
        addSongToLibrary(new Song("S07", "To Te Ti", "Wren Evans Official", 195, "data/music/to-te-ti.mp3"));
    }

    private void addSongToLibrary(Song song) {
        songMap.put(song.getId(), song);
        songList.add(song);
    }

    public Song getSongById(String id) {
        return songMap.get(id);
    }

    public List<Song> searchSongsByTitle(String titleQuery) {
        List<Song> result = new ArrayList<>();
        String queryLower = titleQuery.toLowerCase();
        for (Song song : songList) {
            if (song.getTitle().toLowerCase().contains(queryLower)) {
                result.add(song);
            }
        }
        return result;
    }

    public List<Song> getSortedLibrary() {
        return songList;
    }

    public void addSongToPlaylist(Song song) {
        playlistManager.addSong(song);
    }

    public boolean toggleShuffle() {
        this.isShuffle = !this.isShuffle;
        if (this.isShuffle) {
            // --- Fix Bug 1: Shuffle playlist va dam bao vi tri [0] khong phai bai dang phat ---
            playlistManager.shufflePlaylist();
            this.currentShuffleIndex = 0;
            if (currentPlayingSong != null && !playlistManager.shuffleList.isEmpty()) {
                if (playlistManager.shuffleList.get(0).getId().equals(currentPlayingSong.getId())) {
                    // Swap vi tri [0] xuong cuoi de tranh phat lai bai vua nghe
                    int last = playlistManager.shuffleList.size() - 1;
                    Song temp = playlistManager.shuffleList.get(0);
                    playlistManager.shuffleList.set(0, playlistManager.shuffleList.get(last));
                    playlistManager.shuffleList.set(last, temp);
                }
            }
            // --- Fix Bug 2: Khoi tao libraryShuffleList cho truong hop playlist rong ---
            rebuildLibraryShuffleList();
        }
        return this.isShuffle;
    }

    // Xao tron songList thanh libraryShuffleList, loai tru bai dang phat khoi vi tri dau
    private void rebuildLibraryShuffleList() {
        libraryShuffleList = new ArrayList<>(songList);
        // Fisher-Yates tren libraryShuffleList
        for (int i = libraryShuffleList.size() - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            Song temp = libraryShuffleList.get(i);
            libraryShuffleList.set(i, libraryShuffleList.get(j));
            libraryShuffleList.set(j, temp);
        }
        libraryShuffleIndex = 0;
        // Dam bao vi tri [0] khong phai bai dang phat
        if (currentPlayingSong != null && !libraryShuffleList.isEmpty()) {
            if (libraryShuffleList.get(0).getId().equals(currentPlayingSong.getId())) {
                int last = libraryShuffleList.size() - 1;
                Song temp = libraryShuffleList.get(0);
                libraryShuffleList.set(0, libraryShuffleList.get(last));
                libraryShuffleList.set(last, temp);
            }
        }
    }

    public void playedSong(Song song) {
        if (song != null) {
            if (currentPlayingSong == null || !currentPlayingSong.getId().equals(song.getId())) {
                if (currentPlayingSong != null) {
                    historyStack.push(currentPlayingSong); 
                }
                currentPlayingSong = song;
                currentPlaylistNode = playlistManager.playlist.getNode(song.getId());

                // Neu dang shuffle: dong bo currentShuffleIndex ve sau vi tri bai vua chon
                if (isShuffle && playlistManager.shuffleList != null) {
                    int idx = playlistManager.shuffleList.indexOf(song);
                    if (idx != -1) {
                        currentShuffleIndex = idx + 1;
                    }
                }
            }
        }
    }
        
    public Song nextTrack() {       
        // Push current song to history before moving to next
        if (currentPlayingSong != null) {
            historyStack.push(currentPlayingSong);
        }
        
        Song nextSong = null;
        
        if (playlistManager.isEmpty()) {
            if (songList.isEmpty()) return null;
            if (isShuffle) {
                // --- Fix Bug 2: Dung libraryShuffleList thay vi Random thuan tuy ---
                // Het vong (phat du tat ca bai) thi tron lai va bat dau vong moi
                if (libraryShuffleList.isEmpty() || libraryShuffleIndex >= libraryShuffleList.size()) {
                    rebuildLibraryShuffleList();
                }
                nextSong = libraryShuffleList.get(libraryShuffleIndex);
                libraryShuffleIndex++;
            } else {
                if (currentPlayingSong == null) {
                    nextSong = songList.get(0);
                } else {
                    int idx = songList.indexOf(currentPlayingSong);
                    if (idx == -1 || idx + 1 >= songList.size()) {
                        nextSong = songList.get(0);
                    } else {
                        nextSong = songList.get(idx + 1);
                    }
                }
            }
        } else if (isShuffle) {          
            int totalSongs = playlistManager.shuffleList.size();          
            if (totalSongs > 0) {            
                if (currentShuffleIndex >= totalSongs) {
                    playlistManager.shufflePlaylist();
                    currentShuffleIndex = 0;
                }
                nextSong = playlistManager.shuffleList.get(currentShuffleIndex);
                shuffleIndexHistory.push(currentShuffleIndex);
                currentShuffleIndex++;
            }
        } else {
            if (playlistManager.playlist.head != null) {
                if (currentPlaylistNode == null) {
                    currentPlaylistNode = playlistManager.playlist.head;
                    nextSong = currentPlaylistNode.info;
                } else {
                    currentPlaylistNode = currentPlaylistNode.next;
                    nextSong = currentPlaylistNode.info;
                }
            }
        }
        
        // Update current playing song
        if (nextSong != null) {
            currentPlayingSong = nextSong;
        }
        
        return nextSong;
    }
    public Song prevTrack() {
        if (historyStack.isEmpty()) return null;

        if (isShuffle && !shuffleIndexHistory.isEmpty()) {
            currentShuffleIndex = shuffleIndexHistory.pop();
        }  

        Song prevSong = historyStack.pop(); 
        currentPlayingSong = prevSong;
        return prevSong;
    }
}
