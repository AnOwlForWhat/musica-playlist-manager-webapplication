package dsa;

import model.Song;
import java.util.ArrayList;
import java.util.List;

public class CircularLinkedList {
    
    public static class Node {
        public Song info;
        public Node next;
        
        public Node(Song info) {
            this.info = info;
            this.next = null;
        }
    }

    public Node head;
    public Node tail;
    public int size;

    public CircularLinkedList() {
        head = tail = null;
        size = 0;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public Song addLast(Song x) {
        Node newNode = new Node(x);
        if (isEmpty()) {
            head = tail = newNode;         
            tail.next = head;
        } else {
            tail.next = newNode;
            tail = newNode;
            tail.next = head;
        }
        size++;
        return x;
    }

    public boolean remove(String id) {
        if (isEmpty()) return false;

        Node p = head;
        Node prev = tail;

        do {
            if (p.info.getId().equals(id)) {
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

    public boolean contains(String id) {
        if (isEmpty()) return false;
        Node p = head;
        do {
            if (p.info.getId().equals(id)) return true;
            p = p.next;
        } while (p != head);
        return false;
    }

    public Node getNode(String id) {
        if (isEmpty()) return null;
        Node p = head;
        do {
            if (p.info.getId().equals(id)) return p;
            p = p.next;
        } while (p != head);
        return null;
    }

    public void clear() {
        head = tail = null;
        size = 0;
    }

    public java.util.List<Song> toList() {
        java.util.List<Song> list = new java.util.ArrayList<>();
        if (isEmpty()) return list;
        Node p = head;
        do {
            list.add(p.info);
            p = p.next;
        } while (p != head);
        return list;
    }
}
