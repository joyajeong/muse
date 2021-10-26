package au.edu.unsw.infs3634.musicrecommender;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecommendationService {
    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Artist> artists = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private static final String TAG = "RecomendationService";

    public RecommendationService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void emptySongs() {
        songs.removeAll(songs);
    }

    //Gets an artist each from the 2 most recently liked songs
    private String[] getSeedArtists() {
        //Default artists when user has not liked any songs yet
        String artists[] = {"1zNqQNIdeOUZHb8zbZRFMX", "5ZS223C6JyBfXasXxrRqOk"};
        int numOfLikedSongs = LikedSong.getLikedSongs().size();

        if (numOfLikedSongs >= 2) {
            Log.d(TAG, "Getting latest seed artists");
            for (int i = 1; i < 3; i++) {
                artists[i-1] = LikedSong.getLikedSongs().get(numOfLikedSongs - i).getArtists().get(0).getId();
            }
        }
        return artists;
    }

    //Gets the 2 latest liked songs
    private String[] getSeedTracks() {
        //Default songs when user has not liked any songs yet
        String tracks[] = {"0zaoWwS8RpE3LSDdmkg8TC", "06nIuUCXydh4DcVfFhJa4R"};
        int numOfLikedSongs = LikedSong.getLikedSongs().size();

        if (numOfLikedSongs >= 2) {
            for (int i = 1; i < 3; i++) {
                tracks[i-1] = LikedSong.getLikedSongs().get(numOfLikedSongs - i).getId();
            }
        }
        return tracks;
    }

    //Gets recommended songs using a Spotify API
    public ArrayList<Song> getRecommendedSongs(final VolleyCallBack callBack) {
        //The limit for the number of seeds for recommendations (e.g. artists, tracks, genres) is 5.
        //Therefore, 2 artists, 2 tracks and 1 genre is chosen
        String seedArtists[] = getSeedArtists();
        String seedTracks[] = getSeedTracks();

        String URL = "https://api.spotify.com/v1/recommendations?limit=10&market=AU"
                + "&seed_artists=" + seedArtists[0] + "%2C" + seedArtists[1]
                + "&seed_genres=" + "pop"
                + "&seed_tracks=" + seedTracks[0] + "%2C" + seedTracks[1];

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("tracks");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            Song song = gson.fromJson(object.toString(), Song.class);
                            songs.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                }, error -> {
                    Log.e(TAG, "GET Request Failed");
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return songs;
    }
}

