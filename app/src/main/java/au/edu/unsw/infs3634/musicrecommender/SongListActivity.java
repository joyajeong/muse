package au.edu.unsw.infs3634.musicrecommender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    static ArrayList<LikedSong> likedSongs = new ArrayList<>();
    private static final String TAG = "SongListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        //Set title of menu
        getSupportActionBar().setTitle("Your Songs");
        getSupportActionBar().setElevation(0);

        //Set bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.yourSongs);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //The RecyclerView is set up in onStart() instead of onCreate() so that if the activity is
        //started (e.g. coming from the SongDetailActivity), edited data such as the rating can
        //be updated and reflected in the RecyclerView

        //Get the RecyclerView and implement ClickListener
        recyclerView = findViewById(R.id.rvSongs);
        RecyclerViewAdapter.ClickListener listener = new RecyclerViewAdapter.ClickListener() {
            @Override
            public void onSongClick(View view, String id) {
                Log.d(TAG, "Song id " + id + "clicked");
                launchSongDetailActivity(id);
            }
        };

        //Created an adapter and supply the song data to be displayed
        adapter = new RecyclerViewAdapter(likedSongs, listener);
        recyclerView.setAdapter(adapter);

        //Set linear layout of RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    //When the user clicks on a row in the RecyclerView, detailed screen starts
    private void launchSongDetailActivity(String id) {
        Intent intent = new Intent(SongListActivity.this, SongDetailActivity.class);
        intent.putExtra("SONG_ID", id);
        startActivity(intent);
    }

    //Instantiate menu
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

    //Reacts to when user interacts with the sort menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.sortSongName:
                //Sort by song name
                adapter.sort(1);
                return true;
            case R.id.sortArtist:
                //Sort by artist name
                adapter.sort(2);
                return true;
            case R.id.sortRating:
                //Sort by ratings
                adapter.sort(3);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //When user interacts with the bottom navigation bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recommendations:
                Log.d(TAG, "Recommendations selected");
                Intent intent = new Intent(SongListActivity.this, MainActivity.class);
                startActivity(intent);
                SongListActivity.this.overridePendingTransition(0, 0);
                return true;
            case R.id.yourSongs:
                Log.d(TAG, "Currently on Your Songs");
                return true;
        }
        return false;
    }
}