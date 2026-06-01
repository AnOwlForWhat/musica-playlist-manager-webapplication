package dsa;

import model.Song;

public class Node {
    public Song info;
    public Node next;
    public Node prev; 

    public Node() {
    }

    public Node(Song x, Node next, Node prev) {
        this.info = x;
        this.next = next;
        this.prev = prev;
    }

    public Node(Song x) {
        this(x, null, null);
    }
}
