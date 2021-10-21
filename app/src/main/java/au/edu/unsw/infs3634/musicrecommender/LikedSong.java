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
        Log.i("Where", "in getLikedSongs()");

        ArrayList<LikedSong> songs = SongListActivity.likedSongs;
//        ArrayList<Artist> artist1 = new ArrayList<>();
//        artist1.add(new Artist("2kxP07DLgs4xlWz8YHlvfh", "NIKI"));
//
//        ArrayList<Artist> artist2 = new ArrayList<>();
//        artist2.add(new Artist("1zNqQNIdeOUZHb8zbZRFMX", "Swan Lee"));
//        artist2.add(new Artist("5ZS223C6JyBfXasXxrRqOk", "Jhene Aiko"));
//
//        songs.add(new LikedSong("06nIuUCXydh4DcVfFhJa4R", "Every Summertime", artist1, "POP", "A song from the Shang-Chi soundtrack", 4, "https://i.scdn.co/image/ab67616d0000b2735843d11205f6dd6a2ab5f967"));
//        songs.add(new LikedSong("0zaoWwS8RpE3LSDdmkg8TC", "In The Dark (with Jhene Aiko)", artist2, "POP", "A song from the Shang-Chi soundtrack", 5, "https://i.scdn.co/image/ab67616d0000b2735843d11205f6dd6a2ab5f967"));

        return songs;
    }

    public static LikedSong getLikedSong(String id) {
        // Implement a method that returns one song based on it's id
        ArrayList<LikedSong> list = getLikedSongs();
        for (LikedSong s : list) {
            if (s.getId().equals(id)) {
                Log.i("Match?", "match found");
                Log.i("Song name", s.getName());
                return s;
            }
        }
        Log.i("Where", "end of getLikedSongs() -> no matches");
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

    @Override
    public String toString() {
        return "song name: " + this.getName() +
                ", song artist: " + this.getArtists();
    }
}