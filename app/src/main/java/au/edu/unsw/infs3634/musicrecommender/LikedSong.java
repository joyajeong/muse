package au.edu.unsw.infs3634.musicrecommender;

import android.util.Log;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class LikedSong {
    private String id;
    private String name;
    private ArrayList<Artist> artists;
    private String genre;
    private String description;
    private int rating;
    private String imageURL;
    private static final String TAG = "LikedSong";

    public LikedSong(String id, String name, ArrayList<Artist> artists, String genre, String description, int rating, String imageURL) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.genre = genre;
        this.description = description;
        this.rating = rating;
        this.imageURL = imageURL;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String genre) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public static ArrayList<LikedSong> getLikedSongs() {
        ArrayList<LikedSong> songs = SongListActivity.likedSongs;
        return songs;
    }

    public static LikedSong getLikedSong(String id) {
        // Implement a method that returns one song based on it's id
        ArrayList<LikedSong> list = getLikedSongs();
        for (LikedSong s : list) {
            if (s.getId().equals(id)) {
                Log.d(TAG, "Match found");
                Log.d(TAG, "Song name: " + s.getName());
                return s;
            }
        }
        Log.d(TAG, "At end of getLikedSongs() -> no matches");
        return null;
    }

    public static String formatArtistNames(ArrayList<Artist> artists) {
        String artistNamesArray[] = new String[artists.size()];
        for (int i = 0; i < artists.size(); i++) {
            artistNamesArray[i] = artists.get(i).getName();
        }
        String names = Arrays.toString(artistNamesArray)
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .trim();           //remove trailing spaces from partially initialized arrays
        return names;
    }
}