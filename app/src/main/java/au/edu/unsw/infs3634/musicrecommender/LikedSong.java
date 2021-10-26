package au.edu.unsw.infs3634.musicrecommender;

import android.util.Log;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LikedSong extends Song {

    private String genre;
    private String description;
    private int rating;
    private String imageURL;
    private static final String TAG = "LikedSong";

    public LikedSong(String id, String name, ArrayList<Artist> artists, Album album, String genre, String description, int rating, String imageURL) {
        super(id, name, artists, album);
        this.genre = genre;
        this.description = description;
        this.rating = rating;
        this.imageURL = imageURL;
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
        //Returns a matching song based on it's ID
        ArrayList<LikedSong> list = getLikedSongs();
        for (LikedSong s : list) {
            if (s.getId().equals(id)) {
                Log.d(TAG, "Match found for song: " + s.getName());
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
                .trim(); //remove trailing spaces from partially initialized arrays
        return names;
    }

    public static List getLikedSongIds() {
        List<String> ids = new ArrayList<String>();  ;
            for (LikedSong song : getLikedSongs()) {
                ids.add(song.getId());
            }
        return ids;
    }
}