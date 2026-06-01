package dsa;

import model.Song;
import java.util.ArrayList;
import java.util.List;

class BSTNode {
    public Song info;
    public BSTNode left;
    public BSTNode right;

    public BSTNode() {
    }

    public BSTNode(Song x) {
        this.info = x;
        this.left = this.right = null;
    }
}

public class MyBSTree {
    public BSTNode root;

    public MyBSTree() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void insert(Song x) {
        root = insert(root, x);
    }

    private BSTNode insert(BSTNode p, Song x) {
        if (p == null) {
            return new BSTNode(x);
        }

        int cmp = x.title.compareToIgnoreCase(p.info.title);
        if (cmp < 0) {
            p.left = insert(p.left, x);
        } else if (cmp > 0) {
            p.right = insert(p.right, x);
        } else {
            if (x.id.compareTo(p.info.id) < 0) {
                p.left = insert(p.left, x);
            } else {
                p.right = insert(p.right, x);
            }
        }
        return p;
    }

    public Song search(String title) {
        return search(root, title);
    }

    private Song search(BSTNode p, String title) {
        if (p == null) return null;
        if (p.info.title.equalsIgnoreCase(title)) return p.info;

        int cmp = title.compareToIgnoreCase(p.info.title);
        if (cmp < 0) return search(p.left, title);
        return search(p.right, title);
    }

    public List<Song> getSortedSongs() {
        List<Song> sortedList = new ArrayList<>();
        inorder(root, sortedList);
        return sortedList;
    }

    private void inorder(BSTNode p, List<Song> list) {
        if (p != null) {
            inorder(p.left, list);
            list.add(p.info);
            inorder(p.right, list);
        }
    }

    public void clear() {
        root = null;
    }
}
