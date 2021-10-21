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
import com.google.gson.JsonSyntaxException;

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

    public RecommendationService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    private String seedArtists[] = {"1zNqQNIdeOUZHb8zbZRFMX", "5ZS223C6JyBfXasXxrRqOk"};

    private String seedTracks[] = {"0zaoWwS8RpE3LSDdmkg8TC"};

    private String[] getSeedArtists() {
        for (int i = 0; i < LikedSong.getLikedSongs().size(); i++) {
            seedArtists[i] = LikedSong.getLikedSongs().get(i).getArtists().get(i).getId();
        }
        return seedArtists;
    }

    public ArrayList<Song> getRecommendedSong(final VolleyCallBack callBack) {
        String URL;
//        if (LikedSong.getLikedSongs().size() > 1) {
//            //how to add as many seed artists/tracks as i want
//            Log.i("where", "in getting seeds");
//            seedArtists = getSeedArtists();
//            getRequest = "https://api.spotify.com/v1/recommendations?limit=1&market=AU"
//                    + "&seed_artists=" + seedArtists[0] + "%2C" + seedArtists[1]
//                    + "&seed_genres=" + "pop"
//                    + "&seed_tracks=" + seedTracks[0];
//        } else {
        URL = "https://api.spotify.com/v1/recommendations?limit=10&market=AU"
                + "&seed_artists=" + seedArtists[0] + "%2C" + seedArtists[1]
                + "&seed_genres=" + "pop"
                + "&seed_tracks=" + seedTracks[0];
//    }

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
                    // TODO: Handle error

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

