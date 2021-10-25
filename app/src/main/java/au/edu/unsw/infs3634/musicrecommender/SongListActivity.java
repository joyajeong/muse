package au.edu.unsw.infs3634.musicrecommender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SongListActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    static ArrayList<LikedSong> likedSongs = new ArrayList<LikedSong>();
    private static final String TAG = "SongListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        //Set title of menu
        getSupportActionBar().setTitle("Your Songs");
        getSupportActionBar().setElevation(0);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.yourSongs);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView = findViewById(R.id.rvSongs);

        RecyclerViewAdapter.ClickListener listener = new RecyclerViewAdapter.ClickListener() {
            @Override
            public void onSongClick(View view, String id) {
                Log.i("location", "in onClick()");
                launchSongDetailActivity(id);
            }
        };
        Log.d(TAG, "onStart() number of liked songs: " + String.valueOf(likedSongs.size()));

        adapter = new RecyclerViewAdapter(likedSongs, listener);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    // Called when the user clicks on a row in the recycler view
    private void launchSongDetailActivity(String id) {
        Intent intent = new Intent(SongListActivity.this, SongDetailActivity.class);
        intent.putExtra("SONG_ID", id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_song_list, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setQueryHint("Songs and artists");
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
            case R.id.sortRating:
                //sort by ratings
                adapter.sort(3);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recommendations:
                Log.d(TAG, "recommendations selected");
                Intent intent = new Intent(SongListActivity.this, MainActivity.class);
                startActivity(intent);
                SongListActivity.this.overridePendingTransition(0, 0);
                return true;
            case R.id.yourSongs:
                Log.d(TAG, "your Songs");
                return true;
        }
        return false;
    }
}