    package service;

import dsa.HistoryStack;
import model.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PlaybackController {
    private static PlaybackController instance;
    public PlaylistManager playlistManager;
    public HistoryStack historyStack;
    public HashMap<String, Song> songMap;
    public ArrayList<Song> songList;
    public Song currentPlayingSong;
    public boolean isShuffle = false;
    public boolean isRepeat = true;
    public int currentShuffleIndex = 0;
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
        addSongToLibrary(new Song("S01", "COme my way", "j970", 240, "data/music/chung-ta.mp3"));
        addSongToLibrary(new Song("S02", "sadweaf", "sfeaf ", 252, "data/music/noi-nay-co-anh.mp3"));
        addSongToLibrary(new Song("S03", "eafaef", "sdeafe", 210, "data/music/lac-troi.mp3"));
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
                nextSong = playlistManager.shuffleList.get(currentShuffleIndex);
                currentShuffleIndex++;
                if (currentShuffleIndex >= totalSongs) {
                    playlistManager.shufflePlaylist();
                    currentShuffleIndex = 0;
                }
            }
        }else{
            if (currentPlayingSong == null) {
                nextSong = playlistManager.playlist.head;
            } else {
                nextSong = currentPlayingSong.getNext();
            }
        }
        playedSong(nextSong);
        
        return nextSong;
    }
    public Song prevTrack() {
        if (historyStack.isEmpty()) return null;

        Song prevSong = historyStack.pop(); 
        currentPlayingSong = prevSong;
        return prevSong;
    }
}
