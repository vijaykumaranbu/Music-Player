package com.example.musicplayer.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.databinding.ItemContainerFoldersBinding;
import com.example.musicplayer.listener.FolderListener;
import com.example.musicplayer.model.FolderModel;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder>{

    private ArrayList<FolderModel> folders;
    private FolderListener listener;

    public FolderAdapter(ArrayList<FolderModel> folders, FolderListener listener) {
        this.folders = folders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerFoldersBinding binding = ItemContainerFoldersBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new FolderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        holder.setBinding(folders.get(position));
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    class FolderViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerFoldersBinding binding;

        public FolderViewHolder(@NonNull ItemContainerFoldersBinding itemContainerFoldersBinding) {
            super(itemContainerFoldersBinding.getRoot());
            binding = itemContainerFoldersBinding;
        }

        void setBinding(FolderModel folder){
            binding.textName.setText(folder.getName());
            binding.textTotalSongs.setText(folder.getTotalSongs());
            binding.getRoot().setOnClickListener(view -> {
                listener.onFolderClicked(folder);
            });
        }
    }
}
