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

    public Song addLast(Song x) {
        if (isEmpty()) {
            head = tail = x;         
            tail.setNext(head);
        } else {
            tail.setNext(x);
            tail = x;
            tail.setNext(head);
        }
        size++;
        return x;
    }

    public boolean remove(String id) {
        if (isEmpty()) return false;

        Song p = head;
        Song prev = tail;

        do {
            if (p.getId().equals(id)) {
                if (size == 1) {
                    head = tail = null;
                } else {
                    prev.setNext(p.getNext());
                    if (p == head) {
                        head = p.getNext();
                    }
                    if (p == tail) {
                        tail = prev;
                    }
                }
                size--;
                return true;
            }
            prev = p;
            p = p.getNext();
        } while (p != head);

        return false;
    }

    public void clear() {
        head = tail = null;
        size = 0;
    }
}
