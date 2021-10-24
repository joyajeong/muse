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

public class ArtistService {
    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Artist> artists = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;

    public ArtistService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public ArrayList<Song> getArtist(final VolleyCallBack callBack) {
        String URL = "https://api.spotify.com/v1/artists/0TnOYISbd1XYRBk9myaseg";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            Artist a = gson.fromJson(object.toString(), Artist.class);
                            Log.i("test", a.toString());
                            artists.add(a);
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

    public void followArtist(Artist artist) {
        JSONObject payload = preparePutPayload(artist);
        JsonObjectRequest jsonObjectRequest = prepareFollowArtistRequest(payload);
        queue.add(jsonObjectRequest);
    }

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

    private JSONObject preparePutPayload(Artist artist) {
        JSONArray idArray = new JSONArray();
        idArray.put(artist.getId());
        Log.i("Artist ID: ", artist.getId());
        JSONObject ids = new JSONObject();
        try {
            ids.put("ids", idArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ids;
    }

}

