package au.edu.unsw.infs3634.musicrecommender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;

public class SongDetailActivity extends AppCompatActivity {

    public static LikedSong selectedSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        Bundle bundle = getIntent().getExtras();
        String selectedSongId = bundle.getString("SONG_ID");
        selectedSong = LikedSong.getLikedSong(selectedSongId);
        Log.i("name of song clicked in detail activity", selectedSong.getName());

        TextView songName, artistNames, genre, description;
        songName = findViewById(R.id.tvSongNameDetail);
        artistNames = findViewById(R.id.tvArtistNames);
        genre = findViewById(R.id.tvGenreName);
        description = findViewById(R.id.tvDescription);
        ImageView songImage = findViewById(R.id.ivSong);

        Glide.with(songImage.getContext())
                .load(selectedSong.getImageURL())
                .into(songImage);
        songName.setText(selectedSong.getName());
        artistNames.setText(LikedSong.formatArtistNames(selectedSong.getArtists()));
        genre.setText(selectedSong.getGenre());
        description.setText(selectedSong.getDescription());


    }
}