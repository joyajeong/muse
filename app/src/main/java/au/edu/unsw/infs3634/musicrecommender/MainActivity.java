package au.edu.unsw.infs3634.musicrecommender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daprlabs.cardstack.SwipeDeck;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

//Swiping feature source code from https://github.com/aaronbond/Swipe-Deck

public class MainActivity extends AppCompatActivity {
    // on below line we are creating variable
    // for our array list and swipe deck.
    private SwipeDeck cardStack;
    private ArrayList<Song> recommendedTracks;
    private RecommendationService recommendationService;
    private static final String TAG = "MainActivity";
    private ArrayList<LikedSong> likedSongs = SongListActivity.likedSongs;
//    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting launch");
        // on below line we are initializing our array list and swipe deck.
        recommendationService = new RecommendationService(getApplicationContext());
        getRecommendedTracks();
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
        if (currentTime >= 1200) {
            greeting = "Good Afternoon";
        } else if (currentTime > 1759) {
            greeting = "Good Night";
        } else if (currentTime >= 0 && currentTime < 1200) {
            greeting = "Good Morning";
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        getSupportActionBar().setTitle(greeting + ", " + sharedPreferences.getString("display_name", "No User"));
        getSupportActionBar().setElevation(0);

        //Bottom navigation
//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setOnNavigationItemSelectedListener(this);
//        bottomNavigationView.setSelectedItemId(R.id.person);

        //When user wants to go to their list of songs
        Button btnToSongList = findViewById(R.id.btnToList);
        btnToSongList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SongListActivity.class);
                startActivity(intent);
            }
        });

        // on below line we are setting event callback to our card stack.
        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                // on card swipe left we are displaying a toast message.
//                Toast.makeText(SwipePracticeActivity.this, "Card Swiped Left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardSwipedRight(int position) {
                // on card swiped to right we are displaying a toast message.
                if (DeckAdapter.numStars > 0) {
                    likedSongs.add(new LikedSong(recommendedTracks.get(position).getId(),
                            recommendedTracks.get(position).getName(), recommendedTracks.get(position).getArtists(),
                            "POP", "sample dec", DeckAdapter.numStars,
                            recommendedTracks.get(position).getAlbum().getImages().get(1).getURL()));
                    showToast("Song added");
                } else {
                    showToast("Please give the song a rating");
                }
            }

            @Override
            public void cardsDepleted() {
                // this method is called when no card is present
                Toast.makeText(MainActivity.this, "No more songs present", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardActionDown() {
                // this method is called when card is swipped down.
                Log.i("TAG", "CARDS MOVED DOWN");
            }

            @Override
            public void cardActionUp() {
                // this method is called when card is moved up.
                Log.i("TAG", "CARDS MOVED UP");
            }
        });
    }

    private void getRecommendedTracks() {
        recommendationService.getRecommendedSong(() -> {
            recommendedTracks = recommendationService.getSongs();

            // on below line we are creating a variable for our adapter class and passing array list to it.
            final DeckAdapter adapter = new DeckAdapter(recommendedTracks, getApplicationContext());

            // on below line we are setting adapter to our card stack.
            cardStack.setAdapter(adapter);
        });
    }

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