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
    private SongService songService;
    static ArrayList<LikedSong> likedSongs = new ArrayList<LikedSong>();
    private static final String TAG = "SongListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        Log.d(TAG, "onCreate() number of liked songs: " + String.valueOf(likedSongs.size()));
        //Only add the initial songs once
        //need to fix this
        if (likedSongs.size() < 5) {
            addSongs();
//            songService = new SongService(getApplicationContext());
//            addRecentlyPlayedSongs();
        }

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
            public void onClick(View view, int position) {
                Log.i("location", "in onClick()");
                final LikedSong song = likedSongs.get(position);
                Log.i("name of song clicked", song.getName());
                Intent intent = new Intent(SongListActivity.this, SongDetailActivity.class);
                intent.putExtra("SONG_ID", song.getId());
                startActivity(intent);
            }
        };
        Log.d(TAG, "onStart() number of liked songs: " + String.valueOf(likedSongs.size()));

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

//    private void addRecentlyPlayedSongs() {
//        songService.getRecentlyPlayedTracks(() -> {
//            ArrayList<Song> songs = songService.getSongs();
//            for (int i = 0; i < 10; i++) {
//                Log.d(TAG, "adding recently played songs");
//                Song currentSong = songs.get(i);
//                String description = "A song called " + currentSong.getName() + " by "
//                        + LikedSong.formatArtistNames(currentSong.getArtists()) + " in the album "
//                        + currentSong.getAlbum().getName();
//                likedSongs.add(new LikedSong(currentSong.getId(), currentSong.getName(),
//                        currentSong.getArtists(), currentSong.getAlbum(), "POP", description,
//                        4, currentSong.getAlbum().getImages().get(1).getURL()));
//            }
//        });
//    }

    private void addSongs() {
        ArrayList<Artist> artist1 = new ArrayList<>();
        String[] genres = {"pop"};
        artist1.add(new Artist("4yvcSjfu4PC0CYQyLy4wSq", "Glass Animals", genres));
        ArrayList<Artist> artist2 = new ArrayList<>();
        artist2.add(new Artist("1zNqQNIdeOUZHb8zbZRFMX", "Swan Lee", genres));
        artist2.add(new Artist("5ZS223C6JyBfXasXxrRqOk", "Jhene Aiko", genres));
        ArrayList<Image> image1 = new ArrayList<>();
        image1.add(new Image("https://i.scdn.co/image/ab67616d0000b2735843d11205f6dd6a2ab5f967",
                300, 300));

        ArrayList<Image> image2 = new ArrayList<>();
        image2.add(new Image("https://i2.wp.com/marvelousgeeksmedia.com/wp-content/uploads/2021/05/heat-waves-1615254349.jpeg?ssl=1",
                300, 300));
        Album album1 = new Album("2kAqjStKcwlDD59H0llhGC", artist2, image1, "Shang-Chi and The Legend of The Ten Rings: The Album");
        Album album2 = new Album("5bfpRtBW7RNRdsm3tRyl3R", artist1, image2, "Dreamland");

        likedSongs.add(new LikedSong("3USxtqRwSYz57Ewm6wWRMp", "Heat Waves", artist1, album2,"POP", "A song called Heat Waves by Glass Animals in the album Dreamland", 4, "https://i2.wp.com/marvelousgeeksmedia.com/wp-content/uploads/2021/05/heat-waves-1615254349.jpeg?ssl=1"));
        likedSongs.add(new LikedSong("0zaoWwS8RpE3LSDdmkg8TC", "In The Dark (with Jhene Aiko)", artist2, album1,"POP", "A song from the Shang-Chi soundtrack", 5, "https://i.scdn.co/image/ab67616d0000b2735843d11205f6dd6a2ab5f967"));
    }
}