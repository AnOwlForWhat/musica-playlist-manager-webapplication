package model;

public class Song {
    public String id;
    public String title;
    public String artist;
    public int duration; // second
    public String filePath; // path toi file mp3
    public Song next; // duong dna toi bat hat tiep theo bang CLL

    public Song() {
    }

    public Song(String id, String title, String artist, int duration, String filePath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.filePath = filePath;
        this.next = null;
    }
}
