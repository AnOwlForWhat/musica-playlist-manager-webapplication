package dsa;

import model.Song;

public class CircularLinkedList {
    public Song head;
    public Song tail;
    public int size;

    public CircularLinkedList() {
        head = tail = null;
        size = 0;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void addLast(Song x) {
        Song newSong = new Song(x.id, x.title, x.artist, x.duration, x.filePath);
        if (isEmpty()) {
            head = tail = newSong;
            tail.next = head; 
        } else {
            tail.next = newSong;
            tail = newSong;
            tail.next = head; 
        }
        size++;
    }

    public boolean remove(String id) {
        if (isEmpty()) return false;

        Song p = head;
        Song prev = tail;

        do {
            if (p.id.equals(id)) {
                if (size == 1) {
                    head = tail = null;
                } else {
                    prev.next = p.next;
                    if (p == head) {
                        head = p.next;
                    }
                    if (p == tail) {
                        tail = prev;
                    }
                }
                size--;
                return true;
            }
            prev = p;
            p = p.next;
        } while (p != head);

        return false;
    }

    public void clear() {
        head = tail = null;
        size = 0;
    }
}
