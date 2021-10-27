package au.edu.unsw.infs3634.musicrecommender;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ArtistService {
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private static final String TAG = "ArtistService";

    public ArtistService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    //Follows the artists on the user's Spotify acount
    public void followArtist(Artist artist) {
        JSONObject payload = preparePutPayload(artist);
        JsonObjectRequest jsonObjectRequest = prepareFollowArtistRequest(payload);
        queue.add(jsonObjectRequest);
    }

    //Prepares the PUT request to follow artists
    private JsonObjectRequest prepareFollowArtistRequest(JSONObject payload) {
        return new JsonObjectRequest(Request.Method.PUT, "https://api.spotify.com/v1/me/following?type=artist", payload, response -> {
        }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
    }

    //Prepares the PUT request body i.e. the id of the artist
    private JSONObject preparePutPayload(Artist artist) {
        JSONArray idArray = new JSONArray();
        idArray.put(artist.getId());
        JSONObject ids = new JSONObject();
        try {
            ids.put("ids", idArray);
            Log.d(TAG, "Artist being followed: " + idArray.getString(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ids;
    }

}

