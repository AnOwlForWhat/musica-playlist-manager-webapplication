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

        int cmp = x.getTitle().compareToIgnoreCase(p.info.getTitle());
        if (cmp < 0) {
            p.left = insert(p.left, x);
        } else if (cmp > 0) {
            p.right = insert(p.right, x);
        } else {
            if (x.getId().compareTo(p.info.getId()) < 0) {
                p.left = insert(p.left, x);
            } else {
                p.right = insert(p.right, x);
            }
        }
        return p;
    }

    public Song search(String title, String id) {
        return search(root, title, id);
    }

    private Song search(BSTNode p, String title, String id) {
        if (p == null) return null;
        
        int cmp = title.compareToIgnoreCase(p.info.getTitle());
        if (cmp < 0){
            return search(p.left, title, id);
        } else if(cmp > 0){
            return search(p.right, title,id);
        }else{
              if(p.info.getId().equalsIgnoreCase(id)) return p.info;
              else{
                    int cmpID = id.compareTo(p.info.getId());
                    if(cmpID <0) 
                        return search(p.left, title, id);
                    else return search(p.right, title, id);
              }
        }
        
        
        
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
