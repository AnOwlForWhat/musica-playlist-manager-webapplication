package service;

import dsa.CircularLinkedList;
import model.Song;
import java.util.ArrayList;
import java.util.Random;

public class PlaylistManager {
    public CircularLinkedList playlist;
    public ArrayList<Song> shuffleList; //de sau nay dung chuc nang shuffle nha may con ga

    public PlaylistManager() {
        playlist = new CircularLinkedList();
        shuffleList = new ArrayList<>();
    }

    public void addSong(Song song) {
        Song playlistSong = playlist.addLast(song);
        shuffleList.add(playlistSong);
    }

    public void removeSong(String songId) {
        playlist.remove(songId);
        
        for (int i = 0; i < shuffleList.size(); i++) {
        Song s = shuffleList.get(i);
        if (s.getId().equals(songId)) {
            shuffleList.remove(i);
            break; 
        }
    }
    }
    
    public void shufflePlaylist(){
        if(shuffleList == null || shuffleList.isEmpty()){
            return;
        }
        Random random = new Random();
        int n = shuffleList.size();
        
        //Fisher-Yates
        for(int i = n - 1 ; i > 0; i--){
            int j = random.nextInt(i + 1);
            
            Song temp = shuffleList.get(i);
            shuffleList.set(i, shuffleList.get(j));
            shuffleList.set(j, temp);
        }
    }
    public boolean isEmpty() {
        return playlist.isEmpty();
    }
    public void shufflePlaylist(){
        if(shuffleList == null || shuffleList.isEmpty()){
            return;
        }
        Random random = new Random();
        int n = shuffleList.size();
        
        //Fisher-Yates
        for(int i = n - 1 ; i > 0; i--){
            int j = random.nextInt(i + 1);
            
            Song temp = shuffleList.get(i);
            shuffleList.set(i, shuffleList.get(j));
            shuffleList.set(j, temp);
        }
    }
}
