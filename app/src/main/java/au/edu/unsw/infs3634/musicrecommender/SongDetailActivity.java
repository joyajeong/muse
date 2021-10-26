package au.edu.unsw.infs3634.musicrecommender;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.palette.graphics.Palette;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    private Button btnAddLib, btnFollowArtists, btnBack;
    private RatingBar rating;
    private ImageView songImage;
    private Palette.Swatch vibrantSwatch, darkVibrantSwatch, darkMutedSwatch,lightMutedSwatch;
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
        Log.d(TAG, "Song clicked: " + selectedSong.getName());

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
        btnFollowArtists = findViewById(R.id.btnFollowArtists);

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
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    SongListActivity.likedSongs.remove(selectedSong);
                                    Intent intent = new Intent(SongDetailActivity.this, SongListActivity.class);
                                    startActivity(intent);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(SongDetailActivity.this, R.style.MyDialogTheme);
                    builder.setMessage("Remove " + selectedSong.getName() + " from your Liked Songs?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
            }
        });


        btnAddLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToLibraryService.addSongToLibrary(selectedSong);
                btnAddLib.setText("Song added to Spotify Library");
//                showToast(selectedSong.getName() + " added to Spotify library");
            }
        });

        btnFollowArtists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFollowArtists.setText("Artists Followed");
                for (Artist a : selectedSong.getArtists()) {
                    artistService.followArtist(a);
                }
//                showToast("Followed " + LikedSong.formatArtistNames(selectedSong.getArtists()) + " on Spotify");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedSong.getRating() != 0) {
                    Intent intent = new Intent(SongDetailActivity.this, SongListActivity.class);
                    startActivity(intent);
                } else {
                    showToast("Please give the song a rating");
                }
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
                Palette.Swatch bgSwatch = null;
                Palette.Swatch btnSwatch = null;

                //should this be elif or if statements
                if (darkVibrantSwatch != null) {
                    bgSwatch = darkVibrantSwatch;
                } else if (darkMutedSwatch != null) {
                    bgSwatch = darkMutedSwatch;
                } else if (vibrantSwatch != null) {
                    bgSwatch = vibrantSwatch;
                } else if (lightMutedSwatch != null) {
                    bgSwatch = lightMutedSwatch;
                }

                if (bgSwatch != null) {
                    setColours(bgSwatch.getRgb());
                } else {
                    setColours(0xFF15144A);
                }

            }
        });
    }

    private void setColours(int bgColour) {
        //Changing the colour of buttons
        btnAddLib.setBackgroundColor(bgColour);

        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius(8);
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setStroke(5, bgColour);
        shape.setColor(Color.TRANSPARENT);
        btnFollowArtists.setBackgroundDrawable(shape);


        //Changing the colour of the background
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {bgColour,0xFF131313});
        gd.setCornerRadius(0f);
        layout.setBackgroundDrawable(gd);

        //Changing the colour of the status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(bgColour);
        }
    }

    private void showToast(String message) {
        Toast toast= Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}