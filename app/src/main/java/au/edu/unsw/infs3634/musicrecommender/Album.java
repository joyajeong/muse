package au.edu.unsw.infs3634.musicrecommender;

import java.util.ArrayList;

public class Album {

    private ArrayList<Artist> artists;
    private String id;
    private ArrayList<Image> images;

    public Album(String id, ArrayList<Artist> artists, ArrayList<Image> images) {
        this.id = id;
        this.artists = artists;
        this.images = images;
    }

    public ArrayList<Image> getImages() {
        return images;
    }
}
