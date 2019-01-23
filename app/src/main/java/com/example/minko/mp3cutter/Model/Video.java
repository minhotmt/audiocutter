package com.example.minko.mp3cutter.Model;

/**
 * Created by HP 6300 Pro on 5/8/2018.
 */

public class Video {

    int id;
    String name;
    String uri;

    public Video(int id, String name, String uri) {
        this.id = id;
        this.name = name;
        this.uri = uri;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
