package au.edu.unsw.infs3634.musicrecommender;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.palette.graphics.Palette;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
    View layout;
    private static final String TAG = "SongDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        //Create new services to access the APIs
        addToLibraryService = new AddToLibraryService(getApplicationContext());
        artistService = new ArtistService(getApplicationContext());

        //Receive the selected song ID from SongListActivity
        Bundle bundle = getIntent().getExtras();
        String selectedSongId = bundle.getString("SONG_ID");
        //Set the LikedSong as the song selected
        selectedSong = LikedSong.getLikedSong(selectedSongId);
        Log.d(TAG, "Song clicked: " + selectedSong.getName());

        //Hide the top action bar
        getSupportActionBar().hide();

        //Find all the resources in the SongDetailActivity layout & initialize the variables
        songName = findViewById(R.id.tvSongNameDetail);
        artistNames = findViewById(R.id.tvArtistNames);
        genre = findViewById(R.id.tvGenreName);
        description = findViewById(R.id.tvDescription);
        rating = findViewById(R.id.songRating);
        songImage = findViewById(R.id.ivSong);
        btnAddLib = findViewById(R.id.btnAddLib);
        btnBack = findViewById(R.id.btnBack);
        btnFollowArtists = findViewById(R.id.btnFollowArtists);
        layout = findViewById(R.id.detailScreen);

        //Set data to the variables
        rating.setRating(selectedSong.getRating());
        songName.setText(selectedSong.getName());
        songName.setSelected(true);
        artistNames.setText(LikedSong.formatArtistNames(selectedSong.getArtists()));
        artistNames.setSelected(true);
        genre.setText(selectedSong.getGenre());
        description.setText(selectedSong.getDescription());
        Glide.with(songImage.getContext())
                .load(selectedSong.getImageURL())
                .into(songImage);

        //Change the background & button colours to match the song image
        adaptColours();

        //Handles changes to the rating
        rating.setOnRatingBarChangeListener(changeListener);

        //When user clicks the 'Add to Spotify Library' button
        btnAddLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToLibraryService.addSongToLibrary(selectedSong);
                btnAddLib.setText("Song added to Spotify Library");
            }
        });

        //When user clicks the 'Follow Artists' button
        btnFollowArtists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFollowArtists.setText("Artists Followed");
                for (Artist a : selectedSong.getArtists()) {
                    artistService.followArtist(a);
                }
            }
        });

        //When user clicks the back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Makes sure the song has a rating greater than 0 to prevent accidental deletion
                //of the song
                if (selectedSong.getRating() != 0) {
                    Intent intent = new Intent(SongDetailActivity.this, SongListActivity.class);
                    startActivity(intent);
                } else {
                    showToast("Please give the song a rating");
                }
            }
        });
    }

    private void adaptColours() {
        //Create the image as a Bitmap so its colours can be analysed
        Glide.with(this)
                .asBitmap()
                .load(selectedSong.getImageURL())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        //Once the Bitmap is ready, use a colour API to select a colour
                        selectColor(bitmap);
                    }
                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                    }
                });
    }

    private void selectColor(Bitmap bitmap) {
        //Use the API Palette to select a tone of colour
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {

                //Set the different swatches
                vibrantSwatch = palette.getVibrantSwatch();
                darkVibrantSwatch = palette.getDarkVibrantSwatch();
                darkMutedSwatch = palette.getDarkMutedSwatch();
                lightMutedSwatch = palette.getLightMutedSwatch();

                //Initialise the final swatch that will be used to change the background and button colours
                Palette.Swatch finalSwatch = null;

                //Since images don't have all the swatches (e.g. an image may have a vibrantSwatch
                //but no darkMutedSwatch), the if-else statement is a hierarchy of preferences for
                //the swatches. The most preferential swatch is darkVibrantSwatch
                if (darkVibrantSwatch != null) {
                    finalSwatch = darkVibrantSwatch;
                } else if (darkMutedSwatch != null) {
                    finalSwatch = darkMutedSwatch;
                } else if (vibrantSwatch != null) {
                    finalSwatch = vibrantSwatch;
                } else if (lightMutedSwatch != null) {
                    finalSwatch = lightMutedSwatch;
                }

                if (finalSwatch != null) {
                    //If the image has one of the swatches, set this colour to the background and buttons
                    setColours(finalSwatch.getRgb());
                } else {
                    //If the image has none of the swatches, the default colour is navy
                    setColours(0xFF15144A);
                }

            }
        });
    }

    private void setColours(int finalColour) {
        //Change the colour of buttons
        btnAddLib.setBackgroundColor(finalColour);
        //Set Follow Artist button as a transparent button with an outline
        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius(8);
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setStroke(5, finalColour);
        shape.setColor(Color.TRANSPARENT);
        btnFollowArtists.setBackgroundDrawable(shape);

        //Change the colour of the background
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {finalColour,0xFF131313});
        gd.setCornerRadius(0f);
        layout.setBackgroundDrawable(gd);

        //Change the colour of the status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(finalColour);
        }
    }

    //Handles changes to the rating
    RatingBar.OnRatingBarChangeListener changeListener = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar arg0, float rateValue, boolean arg2) {
            Log.d(TAG, "Your selected rating is :" + rateValue);

            //Gets the selected rating and updates it
            numStars = Math.round(rateValue);
            selectedSong.setRating(numStars);

            //If the user gives the song a 0 rating, it will prompt a message to confirm the
            //removal of the song from their list
            if (numStars == 0) {
                showRemoveConfirmationDialog();
            }
        }
    };

    private void showRemoveConfirmationDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //If YES button clicked, remove the song from the liked songs list
                        //and go back to SongListActivity
                        SongListActivity.likedSongs.remove(selectedSong);
                        Log.d(TAG, "Removed song:" + selectedSong.getName());

                        Intent intent = new Intent(SongDetailActivity.this, SongListActivity.class);
                        startActivity(intent);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //NO button clicked
                        break;
                }
            }
        };

        //Building the alert dialog and setting the message
        AlertDialog.Builder builder = new AlertDialog.Builder(SongDetailActivity.this, R.style.MyDialogTheme);
        builder.setMessage("Remove " + selectedSong.getName() + " from your Liked Songs?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void showToast(String message) {
        Toast toast= Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}