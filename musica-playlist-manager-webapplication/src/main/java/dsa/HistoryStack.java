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

    public void remove(String id) {
        if (isEmpty()) return;
        while (top != null && top.info.getId().equals(id)) {
            top = top.next;
            size--;
        }
        if (top == null) return;
        StackNode current = top;
        while (current.next != null) {
            if (current.next.info.getId().equals(id)) {
                current.next = current.next.next;
                size--;
            } else {
                current = current.next;
            }
        }
    }

    public void push(Song x) {
        remove(x.getId());
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

    public java.util.List<Song> toList() {
        java.util.List<Song> list = new java.util.ArrayList<>();
        StackNode p = top;
        while (p != null) {
            list.add(p.info);
            p = p.next;
        }
        return list;
    }
}
