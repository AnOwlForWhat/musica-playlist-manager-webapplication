package service;

import dsa.CircularLinkedList;
import model.Song;
import java.util.ArrayList;

public class PlaylistManager {
    public CircularLinkedList playlist;
    public ArrayList<Song> shuffleList; //de sau nay dung chuc nang shuffle nha may con ga

    public PlaylistManager() {
        playlist = new CircularLinkedList();
        shuffleList = new ArrayList<>();
    }

    public void addSong(Song song) {
        playlist.addLast(song);
        shuffleList.add(song);
    }

    public void removeSong(String songId) {
        playlist.remove(songId);
    }

    public boolean isEmpty() {
        return playlist.isEmpty();
    }
}
