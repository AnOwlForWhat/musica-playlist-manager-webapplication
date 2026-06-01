package dsa;

import model.Song;

public class HistoryStack {
    private static class StackNode {
        Song info;
        StackNode next;

        StackNode(Song song) {
            this.info = song;
            this.next = null;
        }
    }
    private StackNode top;
    private int size;

    public HistoryStack() {
        top = null;
        size = 0;
    }
    public boolean isEmpty() {
        return top == null;
    }

    public int getSize() {
        return size;
    }

    public void push(Song x) {
        StackNode q = new StackNode(x);
        q.next = top;
        top = q;
        size++;
    }

    public Song pop() {
        if (isEmpty()) return null;
        Song x = top.info;
        top = top.next;
        size--;
        return x;
    }

    public Song peek() {
        if (isEmpty()) return null;
        return top.info;
    }

    public void clear() {
        top = null;
        size = 0;
    }
}
