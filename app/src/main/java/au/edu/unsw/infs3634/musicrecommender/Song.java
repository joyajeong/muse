package au.edu.unsw.infs3634.musicrecommender;

import java.util.ArrayList;

public class Song {

    private String id;
    private String name;
    private ArrayList<Artist> artists;
    private Album album;

    public Song(String id, String name, ArrayList<Artist> artists, Album album) {
        this.name = name;
        this.id = id;
        this.artists = artists;
        this.album = album;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public Album getAlbum() {
        return album;
    }
}
