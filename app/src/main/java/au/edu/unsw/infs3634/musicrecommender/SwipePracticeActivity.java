package au.edu.unsw.infs3634.musicrecommender;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.daprlabs.cardstack.SwipeDeck;

import java.util.ArrayList;

//Source code from https://github.com/aaronbond/Swipe-Deck

public class SwipePracticeActivity extends AppCompatActivity {
    // on below line we are creating variable
    // for our array list and swipe deck.
    private SwipeDeck cardStack;
    private ArrayList<Song> recommendedTracks;
    private RecommendationService recommendationService;
    private static final String TAG = "SwipeActivityPractice";
    private ArrayList<LikedSong> likedSongs = SongListActivity.likedSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_practice);
        Log.d(TAG, "onCreate: Starting launch");
        // on below line we are initializing our array list and swipe deck.
        recommendationService = new RecommendationService(getApplicationContext());
        getRecommendedTracks();
        cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);

        Button btnToSongList = findViewById(R.id.btnToList);
        btnToSongList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SwipePracticeActivity.this, SongListActivity.class);
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
                Toast.makeText(SwipePracticeActivity.this, "No more songs present", Toast.LENGTH_SHORT).show();
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
}