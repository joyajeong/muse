package au.edu.unsw.infs3634.musicrecommender;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView userView;
    private TextView tvRecSongName, tvRecArtistName;
    private ImageView ivSongImage;
    private Button addBtn;
    private Song song;
    private int numStars;

    private ArtistService artistService;
    private RecommendationService recommendationService;
    private ArrayList<Song> recentlyPlayedTracks, recommendedTracks;
    private ArrayList<Artist> artists;
    private ArrayList<Song> songs;
    private ArrayList<LikedSong> likedSongs = SongListActivity.likedSongs;

//    private String recSongId, recSongName, recSongArtist, recSongGenre,
//            recSongDesc, recSongRating, recSongImageURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        artistService = new ArtistService(getApplicationContext());
        recommendationService = new RecommendationService(getApplicationContext());
        tvRecSongName = (TextView) findViewById(R.id.tvRecSongName);
        tvRecSongName.setSelected(true);
        tvRecArtistName = (TextView) findViewById(R.id.tvRecArtistName);
        tvRecArtistName.setSelected(true);
        ivSongImage = findViewById(R.id.ivRecSongImage);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        getRecommendedTracks();

        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        numStars = 0;
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar arg0, float rateValue, boolean arg2) {
                Log.d("Rating", "your selected value is :"+rateValue);
                numStars = Math.round(rateValue);
            }
        });

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // i should make these recommendedTracks.get(0)...into variables so i can use it more easily
                if (numStars > 0) {
                    likedSongs.add(new LikedSong(recommendedTracks.get(0).getId(),
                            recommendedTracks.get(0).getName(), recommendedTracks.get(0).getArtists(),
                            "POP", "sample dec", numStars,
                            recommendedTracks.get(0).getAlbum().getImages().get(1).getURL()));
                    Intent intent = new Intent(MainActivity.this, SongListActivity.class);
                    startActivity(intent);
                } else {
                    showToast("Please give the song a rating");
                }
            }
        });

        Button btnAnotherSong = findViewById(R.id.btnAnotherSong);
        btnAnotherSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recommendationService = new RecommendationService(getApplicationContext());
                getRecommendedTracks();
            }
        });

        Button btnSongs = findViewById(R.id.btnSongs);
        btnSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SongListActivity.class);
                startActivity(intent);
            }
        });

    }

//    private void getTracks1() {
//        artistService.getArtistTopTracks(() -> {
//            recentlyPlayedTracks = artistService.getSongs();
//            songView.setText(recentlyPlayedTracks.get(0).getName());
//            songView1.setText(recentlyPlayedTracks.get(1).getName());
//            songView2.setText(recentlyPlayedTracks.get(2).getName());
//            Log.i("artists", recentlyPlayedTracks.get(0).getArtists().get(0).getName());
//
//        });
//    }

    private void getRecommendedTracks() {
        recommendationService.getRecommendedSong(() -> {
            recommendedTracks = recommendationService.getSongs();
            Glide.with(ivSongImage.getContext())
                    .load(recommendedTracks.get(0).getAlbum().getImages().get(1).getURL())
                    .into(ivSongImage);
            tvRecSongName.setText(recommendedTracks.get(0).getName());
            tvRecArtistName.setText(LikedSong.formatArtistNames(recommendedTracks.get(0).getArtists()));
            Log.i("artists", recommendedTracks.get(0).getArtists().get(0).getName());
        });
    }

//    private void updateSong() {
//        if (recentlyPlayedTracks.size() > 0) {
//            songView.setText(recentlyPlayedTracks.get(0).getName());
//            song = recentlyPlayedTracks.get(0);
//        }
//    }

    private void showToast(String message) {
        Toast toast= Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}