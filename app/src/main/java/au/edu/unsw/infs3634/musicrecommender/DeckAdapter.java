package au.edu.unsw.infs3634.musicrecommender;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//Source code from https://github.com/aaronbond/Swipe-Deck

public class DeckAdapter extends BaseAdapter {

    private ArrayList<Song> songData;
    public static int numStars;
    private Context context;
    private static final String TAG = "DeckAdapter";

    //Constructor for the deck variables.
    public DeckAdapter(ArrayList<Song> songData, Context context) {
        this.songData = songData;
        this.context = context;
    }

    @Override
    public int getCount() {
        //Returns the size of the song array list.
        return songData.size();
    }

    @Override
    public Object getItem(int position) {
        //Returns the item from the song array list.
        return songData.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Returns the id of the song
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_card_item, parent, false);
        }

        //Initializing variables and setting the data
        TextView songName = v.findViewById(R.id.tvSongNameCard);
        songName.setText(songData.get(position).getName());
        songName.setSelected(true);

        TextView artistName = v.findViewById(R.id.tvSongArtistNamesCard);
        artistName.setText(LikedSong.formatArtistNames(songData.get(position).getArtists()));
        artistName.setSelected(true);

        ImageView songImage = v.findViewById(R.id.ivSongCard);
        Glide.with(songImage.getContext())
                .load(songData.get(position).getAlbum().getImages().get(1).getURL())
                .into(songImage);

        RatingBar ratingBar = v.findViewById(R.id.ratingBarCard);
        ratingBar.setRating(2);
        //Make the default rating 2
        numStars = 2;
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            //Listens to changes to the rating and updates the value
            @Override
            public void onRatingChanged(RatingBar arg0, float rateValue, boolean arg2) {
                Log.d(TAG, "Your selected rating is :" + rateValue);
                numStars = Math.round(rateValue);
            }
        });
        return v;
    }
}