package com.example.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.databinding.ItemContainerArtistBinding;
import com.example.musicplayer.listener.ArtistListener;
import com.example.musicplayer.listener.AudioListener;
import com.example.musicplayer.model.ArtistModel;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private final List<ArtistModel> artistList;
    private LayoutInflater layoutInflater;
    private final ArtistListener artistListener;
    private final Context context;

    public ArtistAdapter(Context context, List<ArtistModel> artistList, ArtistListener artistListener) {
        this.artistList = artistList;
        this.artistListener = artistListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ArtistAdapter.ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerArtistBinding binding = ItemContainerArtistBinding.inflate(
                layoutInflater,
                parent,
                false
        );
        return new ArtistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.ArtistViewHolder holder, int position) {
        holder.bindData(artistList.get(position));
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerArtistBinding binding;

        public ArtistViewHolder(ItemContainerArtistBinding itemContainerArtistBinding) {
            super(itemContainerArtistBinding.getRoot());
            binding = itemContainerArtistBinding;
        }

        void bindData(ArtistModel artist) {
            binding.textArtist.setText(artist.getArtist());
            binding.textTotalSongs.setText(artist.getTotalSongs());
            Glide.with(context)
                    .load(R.drawable.artist_placeholder)
                    .into(binding.image);
            binding.getRoot().setOnClickListener(view ->
                    artistListener.onArtistClicked(artist,getAdapterPosition()));
        }
    }

}
