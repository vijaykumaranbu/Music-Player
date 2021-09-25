package com.example.musicplayer.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.databinding.ItemContainerTracksBinding;
import com.example.musicplayer.listener.AudioListener;
import com.example.musicplayer.model.AudioModel;
import com.example.musicplayer.utilities.Constants;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    private final List<AudioModel> audioList;
    private LayoutInflater layoutInflater;
    private final AudioListener audioListener;
    private final Context context;

    public TrackAdapter(Context context, List<AudioModel> audioList, AudioListener audioListener) {
        this.audioList = audioList;
        this.audioListener = audioListener;
        this.context = context;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerTracksBinding binding = ItemContainerTracksBinding.inflate(
                layoutInflater,
                parent,
                false
        );
        return new TrackViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        holder.bindData(audioList.get(position));
    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    class TrackViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerTracksBinding binding;

        public TrackViewHolder(ItemContainerTracksBinding itemContainerTracksBinding) {
            super(itemContainerTracksBinding.getRoot());
            binding = itemContainerTracksBinding;
        }

        void bindData(AudioModel audio) {
            binding.textName.setText(Constants.removeMP3FormString(audio.getName()));
            binding.textArtist.setText(audio.getArtist());
            Uri art = audio.getAlbumArtUri();
            if(art != null){
                Glide.with(context)
                        .load(art)
                        .into(binding.image);
            }
            else{
                Glide.with(context)
                        .load(R.drawable.image_holder)
                        .into(binding.image);
            }
            binding.getRoot().setOnClickListener(view ->
                    audioListener.onAudioClicked(getAdapterPosition()));
        }
    }
}
