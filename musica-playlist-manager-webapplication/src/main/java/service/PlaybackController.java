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

    public void playedSong(Song song) {
        if (song != null) {
            if (currentPlayingSong == null || !currentPlayingSong.getId().equals(song.getId())) {
                if (currentPlayingSong != null) {
                    historyStack.push(currentPlayingSong); 
                }
                currentPlayingSong = song;
                currentPlaylistNode = playlistManager.playlist.getNode(song.getId());
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
