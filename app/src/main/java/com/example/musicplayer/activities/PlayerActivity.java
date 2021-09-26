package com.example.musicplayer.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.database.PreferenceManager;
import com.example.musicplayer.databinding.ActivityPlayerBinding;
import com.example.musicplayer.fragment.TracksFragment;
import com.example.musicplayer.model.AudioModel;
import com.example.musicplayer.utilities.Constants;

import java.util.ArrayList;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private ActivityPlayerBinding binding;
    private int position;
    private ArrayList<AudioModel> audioList;
    public static MediaPlayer mediaPlayer;
    private final Handler handler = new Handler();
    private boolean isPlayButtonPause = false;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadAudio();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setListeners();
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(view -> onBackPressed());
        Thread threadPrevPlay = new Thread(() -> binding.imagePlayPrevious.setOnClickListener(view -> playPrevious()));
        threadPrevPlay.start();
        Thread threadNextPlay = new Thread(() -> binding.imagePlayNext.setOnClickListener(view -> playNext()));
        threadNextPlay.start();
        Thread threadPlayPause = new Thread(() -> binding.imagePlayPause.setOnClickListener(view -> playPause()));
        threadPlayPause.start();
        binding.seekbarPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer.isPlaying() && !isPlayButtonPause) {
                    mediaPlayer.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!mediaPlayer.isPlaying() && !isPlayButtonPause) {
                    mediaPlayer.start();
                }
            }
        });
        binding.imagePlayMode.setOnClickListener(view -> setPlayMode());
        mediaPlayer.setOnCompletionListener(this);
    }

    private void setPlayMode() {
        switch (preferenceManager.getString(Constants.PLAY_MODE)) {
            case Constants.PLAY_MODE_LOOP:
                binding.imagePlayMode.setImageResource(R.drawable.ic_repeat);
                preferenceManager.putString(Constants.PLAY_MODE, Constants.PLAY_MODE_REPEAT);
                mediaPlayer.setLooping(true);
                break;
            case Constants.PLAY_MODE_REPEAT:
                binding.imagePlayMode.setImageResource(R.drawable.ic_shuffle);
                preferenceManager.putString(Constants.PLAY_MODE, Constants.PLAY_MODE_SHUFFLE);
                if (mediaPlayer.isLooping())
                    mediaPlayer.setLooping(false);
                break;
            case Constants.PLAY_MODE_SHUFFLE:
                binding.imagePlayMode.setImageResource(R.drawable.ic_loop);
                preferenceManager.putString(Constants.PLAY_MODE, Constants.PLAY_MODE_LOOP);
                if (mediaPlayer.isLooping())
                    mediaPlayer.setLooping(false);
                break;
        }
    }

    private void playPrevious() {
        mediaPlayer.stop();
        mediaPlayer.release();
        if (preferenceManager.getString(Constants.PLAY_MODE).equals(Constants.PLAY_MODE_SHUFFLE))
            position = getRandomNumber();
        else
            position = (0 > --position) ? audioList.size() - 1 : position;
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(audioList.get(position).getPath()));
        binding.seekbarPlay.setMax(mediaPlayer.getDuration() / 1000);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(this);
        checkLoopIfTrue();
        loadAudioDetails();
    }

    private void playNext() {
        mediaPlayer.stop();
        mediaPlayer.release();
        if (preferenceManager.getString(Constants.PLAY_MODE).equals(Constants.PLAY_MODE_SHUFFLE))
            position = getRandomNumber();
        else
            position = ((audioList.size() - 1) < ++position) ? 0 : position;
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(audioList.get(position).getPath()));
        binding.seekbarPlay.setMax(mediaPlayer.getDuration() / 1000);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(this);
        checkLoopIfTrue();
        loadAudioDetails();
    }

    private void checkLoopIfTrue() {
        mediaPlayer.setLooping(preferenceManager.getString(Constants.PLAY_MODE).equals(Constants.PLAY_MODE_REPEAT));
    }

    private void playPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                binding.imagePlayPause.setImageResource(R.drawable.ic_play);
                isPlayButtonPause = true;
            } else {
                mediaPlayer.start();
                binding.imagePlayPause.setImageResource(R.drawable.ic_pause);
                isPlayButtonPause = false;
            }
        }
    }


    private void loadAudio() {
        switch (getIntent().getStringExtra(Constants.KEY_FRAGMENT)) {
            case Constants.KEY_TRACK:
                audioList = TracksFragment.audioList;
                break;
            case Constants.KEY_ALBUM:
                audioList = AlbumSongsActivity.audioList;
                break;
            case Constants.KEY_FOLDER:
                audioList = FolderSongsActivity.audioList;
                break;
        }
        position = getIntent().getIntExtra(Constants.KEY_POSITION,-1);
        if (audioList != null && position != -1) {
            Uri uri = Uri.parse(audioList.get(position).getPath());
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
            binding.seekbarPlay.setMax(mediaPlayer.getDuration() / 1000);
            loadAudioDetails();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    binding.seekbarPlay.setProgress(currentPosition);
                    binding.textCurrentDuration.setText(getFormattedTime(currentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });
        switch (preferenceManager.getString(Constants.PLAY_MODE)) {
            case Constants.PLAY_MODE_LOOP:
                binding.imagePlayMode.setImageResource(R.drawable.ic_loop);
                break;
            case Constants.PLAY_MODE_REPEAT:
                binding.imagePlayMode.setImageResource(R.drawable.ic_repeat);
                mediaPlayer.setLooping(true);
                break;
            case Constants.PLAY_MODE_SHUFFLE:
                binding.imagePlayMode.setImageResource(R.drawable.ic_shuffle);
                break;
        }
    }

    private void loadAudioDetails() {
        if(audioList.get(position).getAlbumArtUri() != null){
            Glide.with(getApplicationContext())
                    .load(audioList.get(position).getAlbumArtUri())
                    .placeholder(R.drawable.image_holder)
                    .into(binding.image);
        }
        else {
            Glide.with(getApplicationContext())
                    .load(R.drawable.image_holder)
                    .into(binding.image);
        }
        binding.textName.setText(Constants.removeMP3FormString(audioList.get(position).getName()));
        binding.textArtist.setText(audioList.get(position).getArtist());
        binding.textDuration.setText(getFormattedTime(audioList.get(position).getDuration() / 1000));
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying())
                binding.imagePlayPause.setImageResource(R.drawable.ic_pause);
            else
                binding.imagePlayPause.setImageResource(R.drawable.ic_play);
        }
    }

    private String getFormattedTime(int currentPosition) {
        String seconds = String.valueOf(currentPosition % 60);
        String minutes = String.valueOf(currentPosition / 60);
        if (seconds.length() == 1)
            return minutes + ":" + "0" + seconds;
        else
            return minutes + ":" + seconds;
    }

    @Override
    public void onCompletion(MediaPlayer mm) {
        if (Constants.PLAY_MODE_LOOP.equals(preferenceManager.getString(Constants.PLAY_MODE))) {
            playNext();
        } else if (Constants.PLAY_MODE_SHUFFLE.equals(preferenceManager.getString(Constants.PLAY_MODE))) {
            position = getRandomNumber();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(audioList.get(position).getPath()));
            binding.seekbarPlay.setMax(mediaPlayer.getDuration() / 1000);
            mediaPlayer.start();
            loadAudioDetails();
            mediaPlayer.setOnCompletionListener(this);
        }
    }

    private int getRandomNumber() {
        int random = (new Random()).nextInt(audioList.size() - 1);
        if (position == random)
            return ++random;
        else
            return random;
    }

}