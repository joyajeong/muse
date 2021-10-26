package au.edu.unsw.infs3634.musicrecommender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

//Card swiping feature source code from https://github.com/aaronbond/Swipe-Deck

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

        //Get recommended songs from Spotify API
        recommendationService = new RecommendationService(getApplicationContext());
        getRecommendedSongs();

        //Get recently played songs (from Spotify) to load into SongListActivity to ensure a minimum
        //of 10 songs are displayed
        if (likedSongs.size() < 3) {
            songService = new SongService(getApplicationContext());
            addRecentlyPlayedSongs();
        }

        //Display personalised title
        displayTitle();

        //Handles card swiping
        cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);
        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {

            //When user swipes right (likes a song)
            @Override
            public void cardSwipedRight(int position) {
                Song currentSong = recommendedTracks.get(position);
                Log.d(TAG, "The rating of song being added is: " + DeckAdapter.numStars);

                //Check that a rating has been chosen before adding to Liked Songs list
                //AND that the song doesn't already exist in their Liked Song list
                if (DeckAdapter.numStars > 0 && noDuplicates(currentSong.getId())) {
                    //Generate song description
                    String description = Song.createSongDescription(currentSong);
                    //Add song as a liked song
                    addLikedSongs(currentSong, description);
                    showToast("Song added");
                } else {
                    showToast("Please give the song a rating or you may already liked this song");
                }
            }

            @Override
            public void cardSwipedLeft(int position) {
            }

            @Override
            public void cardsDepleted() {
                //When there are no cards left, create new recommendations based on the songs that
                //the user recently liked
                showToast("No more songs present");
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

        //Set up bottom navigation bar
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.recommendations);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //If the activity starts, ensuring the bottom navigation menu is selected to Recommendations
        bottomNavigationView.setSelectedItemId(R.id.recommendations);
    }

    private void getRecommendedSongs() {
        recommendationService.getRecommendedSongs(() -> {
            recommendedTracks = recommendationService.getSongs();
            //Setting the recommended songs to the card/deck adapter
            final DeckAdapter adapter = new DeckAdapter(recommendedTracks, getApplicationContext());
            cardStack.setAdapter(adapter);
        });
    }

    //Displays a personalised message based on the time of day
    private void displayTitle() {
        //Getting and formatting current time
        Date currentLocalTime = Calendar.getInstance().getTime();
        DateFormat date = new SimpleDateFormat("HHmm");
        date.setTimeZone(TimeZone.getTimeZone("GMT+11"));
        String localTime = date.format(currentLocalTime);
        int currentTime = Integer.parseInt(localTime);
        Log.d(TAG,"Current time: " + localTime);

        String greeting = "Hello";
        //Select greeting depending on the time
        if (currentTime >= 1200 && currentTime < 1800) {
            greeting = "Good Afternoon";
        } else if (currentTime >= 1800) {
            greeting = "Good Evening";
        } else if (currentTime >= 0 && currentTime < 1200) {
            greeting = "Good Morning";
        }

        //Gets the display name of the currently logged in user through SharedPreferences
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        //Set the action bar title
        getSupportActionBar().setTitle(greeting + ", " + sharedPreferences.getString("display_name", "No User"));
        getSupportActionBar().setElevation(0);
    }

    private boolean noDuplicates(String id) {
        //Checks if the song ID already exists in the Liked Song ID list
        return !LikedSong.getLikedSongIds().contains(id);
    }

    private void addLikedSongs(Song song, String description) {
        Log.d(TAG, "The song being added is: " + song.getName());
        likedSongs.add(new LikedSong(song.getId(), song.getName(),
                song.getArtists(), song.getAlbum(), "POP", description, DeckAdapter.numStars,
                song.getAlbum().getImages().get(1).getURL()));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recommendations:
                Log.d(TAG, "Recommendations selected");
                return true;
            case R.id.yourSongs:
                Log.i("TAG", "Going to your songs");
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
            Log.d(TAG, "Adding recently played songs");
            int sizeOfResult = songs.size();

            //Pre-populating liked song list with 10 songs (assignment criteria)
            for (int i = 0; i < sizeOfResult; i++) {
                if (likedSongs.size() < 10) {
                    Song currentSong = songs.get(i);
                    String description = Song.createSongDescription(currentSong);

                    if (noDuplicates(currentSong.getId())) {
                        addLikedSongs(currentSong, description);
                    }
                }

            }
        });
    }

    private void showToast(String message) {
        Toast toast= Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}