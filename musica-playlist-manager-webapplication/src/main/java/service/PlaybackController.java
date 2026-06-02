    package service;

import dsa.MyBSTree;
import dsa.HistoryStack;
import model.Song;

import java.util.List;
import java.util.Random;

public class PlaybackController {
    private static PlaybackController instance;
    public PlaylistManager playlistManager;
    public HistoryStack historyStack;
    public MyBSTree songLibrary;
    public Song currentPlayingSong;
    public boolean isShuffle = false;
    public boolean isRepeat = true;

    private PlaybackController() {
        playlistManager = new PlaylistManager();
        historyStack = new HistoryStack();
        songLibrary = new MyBSTree();
        
        loadHardcodedSongs();
    }

    public static synchronized PlaybackController getInstance() {
        if (instance == null) {
            instance = new PlaybackController();
        }
        return instance;
    }

    private void loadHardcodedSongs() {
        songLibrary.clear();
        songLibrary.insert(new Song("S01", "COme my way", "j970", 240, "data/music/chung-ta.mp3"));
        songLibrary.insert(new Song("S02", "sadweaf", "sfeaf ", 252, "data/music/noi-nay-co-anh.mp3"));
        songLibrary.insert(new Song("S03", "eafaef", "sdeafe", 210, "data/music/lac-troi.mp3"));
    }

    public Song searchSongInLibrary(String title) {
        return songLibrary.search(title);
    }

    public List<Song> getSortedLibrary() {
        return songLibrary.getSortedSongs();
    }

    public void addSongToPlaylist(Song song) {
        playlistManager.addSong(song);
    }

    public void playSong(Song song) {
        if (song != null) {
            if (currentPlayingSong != null) {
                historyStack.push(currentPlayingSong); 
            }
            currentPlayingSong = song;
        }
    }

    public Song nextTrack() {       
        if (playlistManager.isEmpty()) {
            return null;
        }
        Song nextSong = null;
        if (isShuffle) {          
            int totalSongs = playlistManager.shuffleList.size();          
            if (totalSongs > 0) {
                Random random = new Random();
                int randomIndex = random.nextInt(totalSongs);
                nextSong = playlistManager.shuffleList.get(randomIndex);
            }
        } else {
            if (currentPlayingSong == null) {
                nextSong = playlistManager.playlist.head;
            } else {
                nextSong = currentPlayingSong.getNext();
            }
        }
        playSong(nextSong);
        
        return nextSong;
    }

    public Song prevTrack() {
        if (historyStack.isEmpty()) return null;

        Song prevSong = historyStack.pop(); 
        currentPlayingSong = prevSong;
        return prevSong;
    }
}
