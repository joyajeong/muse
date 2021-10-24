package au.edu.unsw.infs3634.musicrecommender;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.palette.graphics.Palette;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;


public class SongDetailActivity extends AppCompatActivity {

    public static LikedSong selectedSong;
    private AddToLibraryService addToLibraryService;
    private ArtistService artistService;
    private TextView songName, artistNames, genre, description;
    private Button btnAddLib, btnBack, btnFollow;
    private RatingBar rating;
    private ImageView songImage;
    private Palette.Swatch vibrantSwatch;
    private Palette.Swatch darkVibrantSwatch;
    private Palette.Swatch darkMutedSwatch;
    private Palette.Swatch lightMutedSwatch;
    private int numStars;
    ConstraintLayout screen;
    View layout;
    private static final String TAG = "SongDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        addToLibraryService = new AddToLibraryService(getApplicationContext());
        artistService = new ArtistService(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        String selectedSongId = bundle.getString("SONG_ID");
        selectedSong = LikedSong.getLikedSong(selectedSongId);
        Log.i("name of song clicked in detail activity", selectedSong.getName());

        //Set title of activity
        getSupportActionBar().hide();

        songName = findViewById(R.id.tvSongNameDetail);
        artistNames = findViewById(R.id.tvArtistNames);
        genre = findViewById(R.id.tvGenreName);
        description = findViewById(R.id.tvDescription);
        rating = findViewById(R.id.songRating);
        songImage = findViewById(R.id.ivSong);
        btnAddLib = findViewById(R.id.btnAddLib);
        btnBack = findViewById(R.id.btnBack);
        btnFollow = findViewById(R.id.btnFollow);

        Glide.with(songImage.getContext())
                .load(selectedSong.getImageURL())
                .into(songImage);

        screen = findViewById(R.id.detailScreen);
        layout = findViewById(R.id.detailScreen);

        Glide.with(this)
                .asBitmap()
                .load(selectedSong.getImageURL())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        changeColor(bitmap);
                    }
                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                    }
                });

        rating.setRating(selectedSong.getRating());
        songName.setText(selectedSong.getName());
        songName.setSelected(true);
        artistNames.setText(LikedSong.formatArtistNames(selectedSong.getArtists()));
        artistNames.setSelected(true);
        genre.setText(selectedSong.getGenre());
        description.setText(selectedSong.getDescription());

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar arg0, float rateValue, boolean arg2) {
                Log.d(TAG, "Your selected rating is :" + rateValue);
                numStars = Math.round(rateValue);
                selectedSong.setRating(numStars);

                if (numStars == 0) {
                    SongListActivity.likedSongs.remove(selectedSong);
                }
            }
        });


        btnAddLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToLibraryService.addSongToLibrary(selectedSong);
                showToast("Song added to Spotify library");
            }
        });

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                for (Artist a : selectedSong.getArtists()) {
//                    artistService.followArtist(a);
//                }
                   artistService.followArtist(selectedSong.getArtists().get(0));

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SongDetailActivity.this, SongListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void changeColor(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                vibrantSwatch = palette.getVibrantSwatch();
                darkVibrantSwatch = palette.getDarkVibrantSwatch();
                darkMutedSwatch = palette.getDarkMutedSwatch();
                lightMutedSwatch = palette.getLightMutedSwatch();
//                songName.setTextColor(darkVibrantSwatch.getRgb());
//                artistNames.setTextColor(lightMutedSwatch.getRgb());
//                genre.setTextColor(lightMutedSwatch.getRgb());
//                description.setTextColor(lightMutedSwatch.getRgb());

                if (vibrantSwatch == null) {
                    vibrantSwatch = darkMutedSwatch;
                }
                if (darkMutedSwatch == null) {
                    vibrantSwatch = darkVibrantSwatch;
                }
//                if (darkMutedSwatch == null) {
//                    vibrantSwatch = lightMutedSwatch;
//                }

                btnAddLib.setBackgroundColor(darkVibrantSwatch.getRgb());

                GradientDrawable gd = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[] {vibrantSwatch.getRgb(),0xFF131313});
                gd.setCornerRadius(0f);
                layout.setBackgroundDrawable(gd);
            }
        });
    }

    private void showToast(String message) {
        Toast toast= Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}