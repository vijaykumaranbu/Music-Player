package com.example.musicplayer.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.databinding.ActivityPlayerBinding;
import com.example.musicplayer.model.AudioModel;
import com.example.musicplayer.utilities.Constants;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    private ActivityPlayerBinding binding;
    private int position;
    private ArrayList<AudioModel> audioList;
    private static MediaPlayer mediaPlayer;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadAudio();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    binding.seekbarPlay.setProgress(currentPosition);
                    binding.textCurrentDuration.setText(getFormattedTime(currentPosition));
                }
                handler.postDelayed(this,1000);
            }
        });
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(view -> onBackPressed());
        binding.imagePlayPause.setOnClickListener(view -> playSong());
        binding.imagePlayNext.setOnClickListener(view -> playNext());
        binding.imagePlayPrevious.setOnClickListener(view -> playPrevious());
        binding.seekbarPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private static final String TAG = "playerActivity";

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStartTrackingTouch: onStartTrack");
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStopTrackingTouch: onStopTrack");
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
            }
        });
    }

    private String getFormattedTime(int currentPosition){
        String seconds = String.valueOf(currentPosition % 60);
        String minutes = String.valueOf(currentPosition / 60);
        if(seconds.length() == 1)
            return minutes + ":" + "0" + seconds;
        else
            return minutes + ":" + seconds;
    }

    private void playPrevious() {
        mediaPlayer.stop();
        mediaPlayer.release();
        position = (0 > --position) ? audioList.size() - 1 : position;
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(audioList.get(position).getPath()));
        binding.seekbarPlay.setMax(mediaPlayer.getDuration() / 1000);
        mediaPlayer.start();
        loadAudioDetails();
    }

    private void playNext() {
        mediaPlayer.stop();
        mediaPlayer.release();
        position = ((audioList.size() - 1) < ++position) ? 0 : position;
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(audioList.get(position).getPath()));
        binding.seekbarPlay.setMax(mediaPlayer.getDuration() / 1000);
        mediaPlayer.start();
        loadAudioDetails();
    }

    private void playSong() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                binding.imagePlayPause.setImageResource(R.drawable.ic_play);
            }else{
                mediaPlayer.start();
                binding.imagePlayPause.setImageResource(R.drawable.ic_pause);
            }
        }
    }


    private void loadAudio() {
        audioList = Constants.getAllAudios(getApplicationContext());
        position = getIntent().getIntExtra("position", -1);
        if (audioList != null && position != -1) {
            Uri uri = Uri.parse(audioList.get(position).getPath());
            if(mediaPlayer != null){
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
            binding.seekbarPlay.setMax(mediaPlayer.getDuration() / 1000);
            loadAudioDetails();
        }
    }

    private void loadImage(String path) {
        byte[] image = Constants.getAlbumArt(path);
        if(image != null){
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(image)
                    .into(binding.image);
        }
        else {
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(R.drawable.image_holder)
                    .into(binding.image);
        }
    }

    private void loadAudioDetails() {
        loadImage(audioList.get(position).getPath());
        binding.textName.setText(Constants.removeMP3FormString(audioList.get(position).getName()));
        binding.textArtist.setText(audioList.get(position).getArtist());
        binding.textDuration.setText(getFormattedTime(Integer.parseInt(audioList.get(position).getDuration()) / 1000));
        if(mediaPlayer != null){
            if(mediaPlayer.isPlaying())
                binding.imagePlayPause.setImageResource(R.drawable.ic_pause);
            else
                binding.imagePlayPause.setImageResource(R.drawable.ic_play);
        }
    }
}