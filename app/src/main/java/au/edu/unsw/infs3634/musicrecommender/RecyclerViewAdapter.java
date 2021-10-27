package au.edu.unsw.infs3634.musicrecommender;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
    public static final int SORT_SONG_NAME = 1;
    public static final int SORT_ARTIST_NAME = 2;
    public static final int SORT_RATING = 3;

    //Constructor for adapter
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
                if (charString.isEmpty()) {
                    //If there is no filter query, the filtered list is the default list
                    mLikedSongsFiltered = mLikedSongs;
                } else {
                    ArrayList<LikedSong> filteredList = new ArrayList<>();
                    Log.d(TAG, "Search query: " + charString);

                    for (LikedSong s : mLikedSongs) {
                        //Filters through the artists names
                        for (int i = 0; i < s.getArtists().size(); i++) {
                            if (!filteredList.contains(s) &&
                                    s.getArtists().get(i).getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(s);
                            }
                        }
                        //Filters through the song names
                        if (!filteredList.contains(s) && s.getName().toLowerCase().contains(charString.toLowerCase())) {
                            Log.d(TAG, "matches " + s.getName());
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
        void onSongClick(View view, String id);
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_row, parent, false);
        return new ViewHolder(view, mClickListener);
    }

    //Associate the data with the view holder for a given position in the RecyclerView
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        final LikedSong song = mLikedSongsFiltered.get(position);
        holder.songName.setText(song.getName());
        holder.songName.setSelected(true);
        holder.artistName.setText(LikedSong.formatArtistNames(song.getArtists()));
        holder.artistName.setSelected(true);
        holder.ratingBar.setRating(song.getRating());
        holder.itemView.setTag(song.getId());
        Glide.with(holder.image.getContext())
                .load(song.getImageURL())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mLikedSongsFiltered.size();
    }

    //Extend the ViewHolder to implement ClickListener
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image;
        TextView songName, artistName;
        RatingBar ratingBar;
        ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            this.listener = listener;
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.ivImage);
            songName = itemView.findViewById(R.id.tvSongName);
            artistName = itemView.findViewById(R.id.tvArtist);
            ratingBar = itemView.findViewById(R.id.ratingBarList);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onSongClick(view, (String) view.getTag());
        }
    }

    //Sorts the list by either song, artist or ratings
    public void sort(final int sortMethod) {
        if(mLikedSongsFiltered.size() > 0) {
            Collections.sort(mLikedSongsFiltered, new Comparator<LikedSong>() {
                @Override
                public int compare(LikedSong s1, LikedSong s2) {
                    if (sortMethod == SORT_SONG_NAME) {
                        //Sort by song name
                        Log.d(TAG, "Sorting by Songs");
                        return s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase());
                    } else if (sortMethod == SORT_ARTIST_NAME) {
                        //Sort by artist name
                        Log.d(TAG, "Sorting by Artists");
                        return s1.getArtists().get(0).getName().toLowerCase()
                                .compareTo(s2.getArtists().get(0).getName().toLowerCase());
                    } else if (sortMethod == SORT_RATING) {
                        //Sort by rating
                        Log.d(TAG, "Sorting by ratings");
                        return -(String.valueOf(s1.getRating()).compareTo(String.valueOf((s2.getRating()))));
                    }
                    //Default sort by name
                    return s1.getName().compareTo(s2.getName());
                }
            });
        }
        notifyDataSetChanged();
    }

}
