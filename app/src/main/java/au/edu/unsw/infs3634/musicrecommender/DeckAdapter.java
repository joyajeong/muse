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

    // on below line we have created variables
    // for our array list and context.
    private ArrayList<Song> songData;
    public static int numStars;

    private Context context;
    private static final String TAG = "DeckAdapter";

    // on below line we have created constructor for our variables.
    public DeckAdapter(ArrayList<Song> songData, Context context) {
        this.songData = songData;
        this.context = context;
    }

    @Override
    public int getCount() {
        // in get count method we are returning the size of our array list.
        return songData.size();
    }

    @Override
    public Object getItem(int position) {
        // in get item method we are returning the item from our array list.
        return songData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // in get item id we are returning the position.
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "in getView");
        // in get view method we are inflating our layout on below line.
        View v = convertView;
        if (v == null) {
            // on below line we are inflating our layout.
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_card_item, parent, false);
        }

        // on below line we are initializing our variables and setting data to our variables.
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

        RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBarCard);
        ratingBar.setRating(2);
        numStars = 2;
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar arg0, float rateValue, boolean arg2) {
                Log.d(TAG, "Your selected rating is :" + rateValue);
                numStars = Math.round(rateValue);
            }
        });
        return v;
    }
}