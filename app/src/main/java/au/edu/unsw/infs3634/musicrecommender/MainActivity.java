package au.edu.unsw.infs3634.musicrecommender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daprlabs.cardstack.SwipeDeck;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

//Swiping feature source code from https://github.com/aaronbond/Swipe-Deck

public class MainActivity extends AppCompatActivity {

    private SwipeDeck cardStack;
    private ArrayList<Song> recommendedTracks;
    private RecommendationService recommendationService;
    private ArtistService artistService;
    private static final String TAG = "MainActivity";
    private ArrayList<LikedSong> likedSongs = SongListActivity.likedSongs;

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

        //When user wants to go to their list of songs
        Button btnToSongList = findViewById(R.id.btnToList);
        btnToSongList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SongListActivity.class);
                startActivity(intent);
            }
        });

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
                    likedSongs.add(new LikedSong(currentSong.getId(), currentSong.getName(),
                            currentSong.getArtists(), "POP", description, DeckAdapter.numStars,
                            currentSong.getAlbum().getImages().get(1).getURL()));
                    showToast("Song added");
//                    artistService = new ArtistService(getApplicationContext());
//                    getArtist(currentSong.getArtists());
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
//    private void getArtist(ArrayList<Artist> artists) {
//        artistService.getArtist(artists.get(0) -> {
//            Artist a = artistService.getArtists();
//        });
//    }
    private void showToast(String message) {
        Toast toast= Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.person:
//                Log.i("TAG", "person");
//                return true;
//            case R.id.home:
//                Log.i("TAG", "home");
//                Intent intent = new Intent(MainActivity.this, SongListActivity.class);
//                startActivity(intent);
//                return true;
//        }
//        return false;
//    }
}