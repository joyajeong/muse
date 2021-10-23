package au.edu.unsw.infs3634.musicrecommender;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;

public class SongListActivity extends AppCompatActivity {
    private ArrayList<LikedSong> songs;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    static ArrayList<LikedSong> likedSongs = new ArrayList<LikedSong>();
    private static final String TAG = "SongListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        //Only add the initial songs once
        //need to fix this
        if (likedSongs.size() < 5) {
            addSongs();
        }

        //Set title of menu
        getSupportActionBar().setTitle("Your Songs");
        getSupportActionBar().setElevation(0);

    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView = findViewById(R.id.rvSongs);
        RecyclerViewAdapter.ClickListener listener = new RecyclerViewAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.i("location", "in onClick()");
                final LikedSong song = likedSongs.get(position);
                Log.i("name of song clicked", song.getName());
                Intent intent = new Intent(SongListActivity.this, SongDetailActivity.class);
                intent.putExtra("SONG_ID", song.getId());
                startActivity(intent);
            }
        };
        adapter = new RecyclerViewAdapter(likedSongs, listener);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_song_list, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setQueryHint("Search for songs");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.sortSongName:
                //sort by song name
                adapter.sort(1);
                return true;
            case R.id.sortArtist:
                //sort by artist name
                adapter.sort(2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addSongs() {
        ArrayList<Artist> artist1 = new ArrayList<>();
        String[] genres = {"pop"};
        artist1.add(new Artist("4yvcSjfu4PC0CYQyLy4wSq", "Glass Animals", genres));
        ArrayList<Artist> artist2 = new ArrayList<>();
        artist2.add(new Artist("1zNqQNIdeOUZHb8zbZRFMX", "Swan Lee", genres));
        artist2.add(new Artist("5ZS223C6JyBfXasXxrRqOk", "Jhene Aiko", genres));
        likedSongs.add(new LikedSong("3USxtqRwSYz57Ewm6wWRMp", "Heat Waves", artist1, "POP", "A song called Heat Waves by Glass Animals in the album Dreamland", 4, "https://i2.wp.com/marvelousgeeksmedia.com/wp-content/uploads/2021/05/heat-waves-1615254349.jpeg?ssl=1"));
        likedSongs.add(new LikedSong("0zaoWwS8RpE3LSDdmkg8TC", "In The Dark (with Jhene Aiko)", artist2, "POP", "A song from the Shang-Chi soundtrack", 5, "https://i.scdn.co/image/ab67616d0000b2735843d11205f6dd6a2ab5f967"));
    }
}