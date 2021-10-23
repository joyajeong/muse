package au.edu.unsw.infs3634.musicrecommender;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.palette.graphics.Palette;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class SongDetailActivity extends AppCompatActivity {

    public static LikedSong selectedSong;
    private AddToLibraryService addToLibraryService;
    private static final String TAG = "SongDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        addToLibraryService = new AddToLibraryService(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        String selectedSongId = bundle.getString("SONG_ID");
        selectedSong = LikedSong.getLikedSong(selectedSongId);
        Log.i("name of song clicked in detail activity", selectedSong.getName());

        //Set title of activity
        getSupportActionBar().setTitle("");

        TextView songName = findViewById(R.id.tvSongNameDetail);
        TextView artistNames = findViewById(R.id.tvArtistNames);
        TextView genre = findViewById(R.id.tvGenreName);
        TextView description = findViewById(R.id.tvDescription);
        RatingBar rating = findViewById(R.id.songRating);
        ImageView songImage = findViewById(R.id.ivSong);
        Button btnAddLib = findViewById(R.id.btnAddLib);

        Glide.with(songImage.getContext())
                .load(selectedSong.getImageURL())
                .into(songImage);
        rating.setRating(selectedSong.getRating());
        songName.setText(selectedSong.getName());
        songName.setSelected(true);
        artistNames.setText(LikedSong.formatArtistNames(selectedSong.getArtists()));
        artistNames.setSelected(true);
        genre.setText(selectedSong.getGenre());
        description.setText(selectedSong.getDescription());

        btnAddLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToLibraryService.addSongToLibrary(selectedSong);
            }
        });


    }



}