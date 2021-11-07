package com.example.musicplayer.activities;

import android.content.Context;
import android.media.AudioManager;
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

public class PlayerActivity extends AppCompatActivity implements
        MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {

    private ActivityPlayerBinding binding;
    private int position;
    private ArrayList<AudioModel> audioList;
    private final Handler handler = new Handler();
    private boolean isPlayButtonPause = false;
    private PreferenceManager preferenceManager;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        setAudioFocus();
        loadAudio();
    }

    private void setAudioFocus() {
        audioManager.requestAudioFocus(this,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);
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
                if (Constants.mediaPlayer != null && fromUser) {
                    Constants.mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (Constants.mediaPlayer.isPlaying() && !isPlayButtonPause) {
                    Constants.mediaPlayer.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!Constants.mediaPlayer.isPlaying() && !isPlayButtonPause) {
                    Constants.mediaPlayer.start();
                }
            }
        });
        binding.imagePlayMode.setOnClickListener(view -> setPlayMode());
        Constants.mediaPlayer.setOnCompletionListener(this);
    }

    private void setPlayMode() {
        switch (preferenceManager.getString(Constants.PLAY_MODE)) {
            case Constants.PLAY_MODE_LOOP:
                binding.imagePlayMode.setImageResource(R.drawable.ic_repeat);
                preferenceManager.putString(Constants.PLAY_MODE, Constants.PLAY_MODE_REPEAT);
                Constants.mediaPlayer.setLooping(true);
                break;
            case Constants.PLAY_MODE_REPEAT:
                binding.imagePlayMode.setImageResource(R.drawable.ic_shuffle);
                preferenceManager.putString(Constants.PLAY_MODE, Constants.PLAY_MODE_SHUFFLE);
                if (Constants.mediaPlayer.isLooping())
                    Constants.mediaPlayer.setLooping(false);
                break;
            case Constants.PLAY_MODE_SHUFFLE:
                binding.imagePlayMode.setImageResource(R.drawable.ic_loop);
                preferenceManager.putString(Constants.PLAY_MODE, Constants.PLAY_MODE_LOOP);
                if (Constants.mediaPlayer.isLooping())
                    Constants.mediaPlayer.setLooping(false);
                break;
        }
    }

    private void playPrevious() {
        Constants.mediaPlayer.stop();
        Constants.mediaPlayer.release();
        if (preferenceManager.getString(Constants.PLAY_MODE).equals(Constants.PLAY_MODE_SHUFFLE))
            position = getRandomNumber();
        else
            position = (0 > --position) ? audioList.size() - 1 : position;
        Constants.mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(audioList.get(position).getPath()));
        binding.seekbarPlay.setMax(Constants.mediaPlayer.getDuration() / 1000);
        Constants.mediaPlayer.start();
        Constants.mediaPlayer.setOnCompletionListener(this);
        checkLoopIfTrue();
        loadAudioDetails();
        setAudioFocus();
    }

    private void playNext() {
        Constants.mediaPlayer.stop();
        Constants.mediaPlayer.release();
        if (preferenceManager.getString(Constants.PLAY_MODE).equals(Constants.PLAY_MODE_SHUFFLE))
            position = getRandomNumber();
        else
            position = ((audioList.size() - 1) < ++position) ? 0 : position;
        Constants.mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(audioList.get(position).getPath()));
        binding.seekbarPlay.setMax(Constants.mediaPlayer.getDuration() / 1000);
        Constants.mediaPlayer.start();
        Constants.mediaPlayer.setOnCompletionListener(this);
        checkLoopIfTrue();
        loadAudioDetails();
        setAudioFocus();
    }

    private void checkLoopIfTrue() {
        Constants.mediaPlayer.setLooping(preferenceManager.getString(Constants.PLAY_MODE).equals(Constants.PLAY_MODE_REPEAT));
    }

    private void playPause() {
        if (Constants.mediaPlayer != null) {
            if (Constants.mediaPlayer.isPlaying()) {
                Constants.mediaPlayer.pause();
                binding.imagePlayPause.setImageResource(R.drawable.ic_play);
                isPlayButtonPause = true;
            } else {
                Constants.mediaPlayer.start();
                binding.imagePlayPause.setImageResource(R.drawable.ic_pause);
                isPlayButtonPause = false;
            }
        }
        setAudioFocus();
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
        position = getIntent().getIntExtra(Constants.KEY_POSITION, -1);
        if (audioList != null && position != -1) {
            Uri uri = Uri.parse(audioList.get(position).getPath());
            if (Constants.mediaPlayer != null) {
                Constants.mediaPlayer.stop();
                Constants.mediaPlayer.release();
            }
            Constants.mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            Constants.mediaPlayer.start();
            binding.seekbarPlay.setMax(Constants.mediaPlayer.getDuration() / 1000);
            loadAudioDetails();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Constants.mediaPlayer != null) {
                    int currentPosition = Constants.mediaPlayer.getCurrentPosition() / 1000;
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
                Constants.mediaPlayer.setLooping(true);
                break;
            case Constants.PLAY_MODE_SHUFFLE:
                binding.imagePlayMode.setImageResource(R.drawable.ic_shuffle);
                break;
        }
    }

    private void loadAudioDetails() {
        if (audioList.get(position).getAlbumArtUri() != null) {
            Glide.with(getApplicationContext())
                    .load(audioList.get(position).getAlbumArtUri())
                    .placeholder(R.drawable.image_holder)
                    .into(binding.image);
        } else {
            Glide.with(getApplicationContext())
                    .load(R.drawable.image_holder)
                    .into(binding.image);
        }
        binding.textName.setText(Constants.removeMP3FormString(audioList.get(position).getName()));
        binding.textArtist.setText(audioList.get(position).getArtist());
        binding.textDuration.setText(getFormattedTime(audioList.get(position).getDuration() / 1000));
        if (Constants.mediaPlayer != null) {
            if (Constants.mediaPlayer.isPlaying())
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
            Constants.mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(audioList.get(position).getPath()));
            binding.seekbarPlay.setMax(Constants.mediaPlayer.getDuration() / 1000);
            Constants.mediaPlayer.start();
            loadAudioDetails();
            Constants.mediaPlayer.setOnCompletionListener(this);
        }
    }

    private int getRandomNumber() {
        int random = (new Random()).nextInt(audioList.size() - 1);
        if (position == random)
            return ++random;
        else
            return random;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange)
        {
            case AudioManager.AUDIOFOCUS_GAIN:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Constants.mediaPlayer.start();
                binding.imagePlayPause.setImageResource(R.drawable.ic_pause);
                isPlayButtonPause = false; // Resume your media player here
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Constants.mediaPlayer.pause();
                binding.imagePlayPause.setImageResource(R.drawable.ic_play);
                isPlayButtonPause = true;// Pause your media player here
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioManager.abandonAudioFocus(this);
    }
}