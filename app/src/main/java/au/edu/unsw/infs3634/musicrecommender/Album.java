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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
