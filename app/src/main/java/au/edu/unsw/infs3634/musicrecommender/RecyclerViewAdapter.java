package au.edu.unsw.infs3634.musicrecommender;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    private ArrayList<LikedSong> mLikedSongs;
    private ArrayList<LikedSong> mLikedSongsFiltered;
    private ClickListener mClickListener;
    private static final String TAG = "RecyclerViewAdapter";

    public RecyclerViewAdapter(ArrayList<LikedSong> likedSongs, ClickListener listener) {
        this.mLikedSongs = likedSongs;
        this.mLikedSongsFiltered = likedSongs;
        this.mClickListener = listener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if(charString.isEmpty()) {
                    mLikedSongsFiltered = mLikedSongs;
                } else {
                    ArrayList<LikedSong> filteredList = new ArrayList<>();
                    for (LikedSong s : mLikedSongs) {
                        if (s.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(s);
                        }
                    }
                    mLikedSongsFiltered = filteredList;
                }
                FilterResults filterResult = new FilterResults();
                filterResult.values = mLikedSongsFiltered;
                return filterResult;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mLikedSongsFiltered = (ArrayList<LikedSong>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    // Allows clicks events to be caught
    public interface ClickListener {
        void onClick(View view, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_row, parent, false);
        return new ViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LikedSong song = mLikedSongsFiltered.get(position);
        holder.songName.setText(song.getName());
        holder.songName.setSelected(true);
        holder.artistName.setText(LikedSong.formatArtistNames(song.getArtists()));
        holder.artistName.setSelected(true);
//        holder.songGenre.setText(song.getGenre());
        holder.ratingBar.setRating(song.getRating());
        Glide.with(holder.image.getContext())
                .load(song.getImageURL())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mLikedSongsFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image;
        TextView songName, artistName, songGenre;
        RatingBar ratingBar;
        ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            image = itemView.findViewById(R.id.ivImage);
            songName = itemView.findViewById(R.id.tvSongName);
            artistName = itemView.findViewById(R.id.tvArtist);
//            songGenre = itemView.findViewById(R.id.tvGenre);
            ratingBar = itemView.findViewById(R.id.ratingBarList);
            itemView.setOnClickListener(this);
            Log.d(TAG, "In View holder");

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onClick(view, getAbsoluteAdapterPosition());
//            listener.onClick(view, (Integer) view.getTag());
        }
    }

    public String formatNumber(int number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    //sort method
    public void sort(final int sortMethod) {
        if(mLikedSongsFiltered.size() > 0) {
            Collections.sort(mLikedSongsFiltered, new Comparator<LikedSong>() {
                @Override
                public int compare(LikedSong s1, LikedSong s2) {
                    //sort by song name
                    if (sortMethod == 1) {
                        return s1.getName().compareTo(s2.getName());
                    } else if (sortMethod == 2) {
                        //sort by artist name
                        return s1.getArtists().get(0).getName().compareTo(s2.getArtists().get(0).getName());
                    }
                    return s1.getName().compareTo(s2.getName());
                }
            });
        }
        notifyDataSetChanged();
    }

}
