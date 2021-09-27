package com.example.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.databinding.ItemContainerAlbumsBinding;
import com.example.musicplayer.listener.AlbumListener;
import com.example.musicplayer.model.AlbumModel;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>{

    private final ArrayList<AlbumModel> albumList;
    private final Context context;
    private final AlbumListener listener;

    public AlbumAdapter(Context context, ArrayList<AlbumModel> albumList, AlbumListener listener) {
        this.context = context;
        this.albumList = albumList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerAlbumsBinding view = ItemContainerAlbumsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.setBinding(albumList.get(position));
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerAlbumsBinding binding;

        public AlbumViewHolder(@NonNull ItemContainerAlbumsBinding itemContainerAlbumsBinding) {
            super(itemContainerAlbumsBinding.getRoot());
            binding = itemContainerAlbumsBinding;
        }

        void setBinding(AlbumModel album){
            binding.textAlbumName.setText(album.getAlbum());
            binding.textTotalSongs.setText(String.format("%s Songs",album.getTotalSongs()));
            String art = album.getAlbumArt();
            if(art != null){
                Glide.with(context)
                        .load(art)
                        .placeholder(R.drawable.image_holder)
                        .into(binding.image);
            }
            else {
                Glide.with(context)
                        .load(R.drawable.image_holder)
                        .into(binding.image);
            }
            binding.getRoot().setOnClickListener(view -> {
                listener.onAlbumClicked(album,getAdapterPosition());
            });
        }
    }
}
