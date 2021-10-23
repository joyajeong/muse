package au.edu.unsw.infs3634.musicrecommender;

import java.util.ArrayList;

public class Album {

    private ArrayList<Artist> artists;
    private String id;
    private ArrayList<Image> images;
    private String name;

    public Album(String id, ArrayList<Artist> artists, ArrayList<Image> images, String name) {
        this.id = id;
        this.artists = artists;
        this.images = images;
        this.name = name;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public String getName() {
        return name;
    }
}
