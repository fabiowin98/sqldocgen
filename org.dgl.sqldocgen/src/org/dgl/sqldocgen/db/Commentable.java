package org.dgl.sqldocgen.db;

import java.io.Serializable;

public abstract class Commentable implements Serializable {

    private int id;
    private String name;
    private String comment;

    public Commentable() {
        id = 0;
        name = "";
        comment = "";
    }

    public void setComment(String comment) {
        this.comment = comment.length() > 1 ? Character.toUpperCase(comment.charAt(0)) + comment.substring(1) : comment;
    }

    public String getComment() {
        return comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return ((Commentable) obj).getName().equals(getName());
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public String toString() {
        return getName();
    }

}
