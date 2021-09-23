package com.example.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayer.R;
import com.example.musicplayer.databinding.ItemContainerSongBinding;
import com.example.musicplayer.listener.AudioListener;
import com.example.musicplayer.model.AudioModel;
import com.example.musicplayer.utilities.Constants;

import java.util.List;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {

    private final List<AudioModel> audioList;
    private LayoutInflater layoutInflater;
    private final AudioListener audioListener;
    private final Context context;

    public AudioListAdapter(Context context, List<AudioModel> audioList, AudioListener audioListener) {
        this.audioList = audioList;
        this.audioListener = audioListener;
        this.context = context;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerSongBinding itemContainerSongBinding = ItemContainerSongBinding.inflate(
                layoutInflater,
                parent,
                false
        );
        return new AudioViewHolder(itemContainerSongBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        holder.bindData(audioList.get(position));
    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    class AudioViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerSongBinding itemContainerSongBinding;

        public AudioViewHolder(ItemContainerSongBinding itemContainerSongBinding) {
            super(itemContainerSongBinding.getRoot());
            this.itemContainerSongBinding = itemContainerSongBinding;
        }

        void bindData(AudioModel audio) {
            itemContainerSongBinding.textName.setText(Constants.removeMP3FormString(audio.getName()));
            itemContainerSongBinding.textArtist.setText(audio.getArtist());
            byte[] image = Constants.getAlbumArt(audio.getPath());
            if (image != null) {
                Glide.with(context)
                        .load(image)
                        .thumbnail(0.2f)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).dontAnimate())
                        .into(itemContainerSongBinding.image);
            } else {
                Glide.with(context)
                        .load(R.drawable.image_holder)
                        .thumbnail(0.2f)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).dontAnimate())
                        .into(itemContainerSongBinding.image);
            }
            itemContainerSongBinding.getRoot().setOnClickListener(view ->
                    audioListener.onAudioClicked(getAdapterPosition()));
        }
    }
}
