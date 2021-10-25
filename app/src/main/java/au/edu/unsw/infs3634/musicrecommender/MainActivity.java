package au.edu.unsw.infs3634.musicrecommender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daprlabs.cardstack.SwipeDeck;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

//Swiping feature source code from https://github.com/aaronbond/Swipe-Deck

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private SwipeDeck cardStack;
    private ArrayList<Song> recommendedTracks;
    private RecommendationService recommendationService;
    private static final String TAG = "MainActivity";
    private ArrayList<LikedSong> likedSongs = SongListActivity.likedSongs;
    private SongService songService;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting launch");
        // on below line we are initializing our array list and swipe deck.
        recommendationService = new RecommendationService(getApplicationContext());
        getRecommendedSongs();
        cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);

        //Display personalised menu title
        Date currentLocalTime = Calendar.getInstance().getTime();
        DateFormat date = new SimpleDateFormat("HHmm");
        date.setTimeZone(TimeZone.getTimeZone("GMT+11"));
        String localTime = date.format(currentLocalTime);
        int currentTime = Integer.parseInt(localTime);
        Log.i("current time: ", localTime);
        String greeting = "Hello";
        //need to check this
        if (currentTime >= 1200 && currentTime < 1800) {
            greeting = "Good Afternoon";
        } else if (currentTime >= 1800) {
            greeting = "Good Night";
        } else if (currentTime >= 0 && currentTime < 1200) {
            greeting = "Good Morning";
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        getSupportActionBar().setTitle(greeting + ", " + sharedPreferences.getString("display_name", "No User"));
        getSupportActionBar().setElevation(0);

        //Add recently played songs (from Spotify)
        if (likedSongs.size() < 3) {
            songService = new SongService(getApplicationContext());
            addRecentlyPlayedSongs();
        }

        //Setting event callback to the card stack.
        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedRight(int position) {
                Song currentSong = recommendedTracks.get(position);
                String description = "A song called " + currentSong.getName() + " by "
                        + LikedSong.formatArtistNames(currentSong.getArtists()) + " in the album "
                        + currentSong.getAlbum().getName();

                //Check that a rating has been chosen before adding to Liked Songs list
                //AND that the song doesn't already exist in their list
                if (DeckAdapter.numStars > 0 && noDuplicates(currentSong.getId())) {
                    //put this in a separate method
                    likedSongs.add(new LikedSong(currentSong.getId(), currentSong.getName(),
                            currentSong.getArtists(), currentSong.getAlbum(), "POP", description, DeckAdapter.numStars,
                            currentSong.getAlbum().getImages().get(1).getURL()));
                    showToast("Song added");
//                    artistService = new ArtistService(getApplicationContext());
//                    getArtist();
                } else {
                    //This doesnt go with the noDuplicates thing
                    showToast("Please give the song a rating");
                }
            }

            @Override
            public void cardSwipedLeft(int position) {

            }

            @Override
            public void cardsDepleted() {
                // this method is called when no card is present
                Toast.makeText(MainActivity.this, "No more songs present", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "cardsDepleted: creating new recommendations...");
                recommendationService.emptySongs();
                getRecommendedSongs();
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.recommendations);
    }
    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.recommendations);
    }
    private void getRecommendedSongs() {
        recommendationService.getRecommendedSongs(() -> {
            recommendedTracks = recommendationService.getSongs();

            // on below line we are creating a variable for our adapter class and passing array list to it.
            final DeckAdapter adapter = new DeckAdapter(recommendedTracks, getApplicationContext());

            // on below line we are setting adapter to our card stack.
            cardStack.setAdapter(adapter);
        });
    }

    private boolean noDuplicates(String id) {
        ArrayList<String> trackIDs = new ArrayList<>();
        for (int i = 0; i < LikedSong.getLikedSongs().size(); i++) {
            trackIDs.add(LikedSong.getLikedSongs().get(i).getId());
        }
        if (trackIDs.contains(id)) {
            return false;
        }
        return true;
    }

//    private void getArtist() {
//        artistService.getArtist(() -> {
//            recommendedArtists = artistService.getArtists();
//            for (int i = 0; i < recommendedArtists.size(); i++) {
//                for (int j = 0; j < recommendedArtists.get(i).getGenres().length; j++) {
//                    genres.add(recommendedArtists.get(i).getGenres()[i]);
//                    Log.d(TAG, "genres: " + recommendedArtists.get(i).getGenres()[i]);
//                }
//            }
//        });
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recommendations:
                Log.i("TAG", "recommendations selected");
                return true;
            case R.id.yourSongs:
                Log.i("TAG", "your Songs");
                Intent intent = new Intent(MainActivity.this, SongListActivity.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(0, 0);
                return true;
        }
        return false;
    }

    private void addRecentlyPlayedSongs() {
        songService.getRecentlyPlayedTracks(() -> {
            ArrayList<Song> songs = songService.getSongs();
            for (int i = 0; i < 3; i++) {
                Log.d(TAG, "adding recently played songs");
                Song currentSong = songs.get(i);
                String description = "A song called " + currentSong.getName() + " by "
                        + LikedSong.formatArtistNames(currentSong.getArtists()) + " in the album "
                        + currentSong.getAlbum().getName();
                likedSongs.add(new LikedSong(currentSong.getId(), currentSong.getName(),
                        currentSong.getArtists(), currentSong.getAlbum(), "POP", description,
                        4, currentSong.getAlbum().getImages().get(1).getURL()));
            }
            Log.d(TAG, "addRecentlyPlayedSongs() number of liked songs: " + String.valueOf(likedSongs.size()));

        });
    }

    private void showToast(String message) {
        Toast toast= Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}