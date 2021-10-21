package au.edu.unsw.infs3634.musicrecommender;

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
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.Toast;
import java.util.ArrayList;

public class SongListActivity extends AppCompatActivity {
    private ArrayList<LikedSong> songs;
    private RecyclerViewAdapter adapter;
    static ArrayList<LikedSong> likedSongs = new ArrayList<LikedSong>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

//        songs = LikedSong.getLikedSongs();
//        LikedSong newSong = (LikedSong) getIntent().getExtras().getParcelable("NEW_SONG");

        addSongs();
//        likedSongs.add(newSong);

//        Log.i("checking songs", songs.get(1).getName());
        RecyclerView recyclerView = findViewById(R.id.rvSongs);

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
        artist1.add(new Artist("2kxP07DLgs4xlWz8YHlvfh", "NIKI"));

        ArrayList<Artist> artist2 = new ArrayList<>();
        artist2.add(new Artist("1zNqQNIdeOUZHb8zbZRFMX", "Swan Lee"));
        artist2.add(new Artist("5ZS223C6JyBfXasXxrRqOk", "Jhene Aiko"));
        //Only add the initial songs once
        if (likedSongs.size() < 2) {
            likedSongs.add(new LikedSong("06nIuUCXydh4DcVfFhJa4R", "Every Summertime", artist1, "POP", "A song from the Shang-Chi soundtrack", 4, "https://i.scdn.co/image/ab67616d0000b2735843d11205f6dd6a2ab5f967"));
            likedSongs.add(new LikedSong("0zaoWwS8RpE3LSDdmkg8TC", "In The Dark (with Jhene Aiko)", artist2, "POP", "A song from the Shang-Chi soundtrack", 5, "https://i.scdn.co/image/ab67616d0000b2735843d11205f6dd6a2ab5f967"));
        }
    }
}