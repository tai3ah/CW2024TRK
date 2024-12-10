package com.example.demo.managers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

    private static SoundManager instance;

    private MediaPlayer backgroundSoundPlayer;

    private static final String BACKGROUND_SOUND_PATH = "/com/example/demo/audio/gamebgSound.mp3";
    private static final String WIN_SOUND_PATH = "/com/example/demo/audio/winGameSound.mp3";
    private static final String LOSE_SOUND_PATH = "/com/example/demo/audio/loseGameSound.mp3";

    private SoundManager() {
        setupBackgroundSound();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    // Setup the persistent background sound
    private void setupBackgroundSound() {
        backgroundSoundPlayer = createMediaPlayer(BACKGROUND_SOUND_PATH, true);
    }

    private MediaPlayer createMediaPlayer(String filePath, boolean loop) {
        Media media = new Media(getClass().getResource(filePath).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        if (loop) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        return mediaPlayer;
    }

    public void playBackgroundSound() {
        if (backgroundSoundPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            backgroundSoundPlayer.play();
        }
    }

    public void stopBackgroundSound() {
        backgroundSoundPlayer.stop();
    }

    public void playWinSound() {
        MediaPlayer winSoundPlayer = createMediaPlayer(WIN_SOUND_PATH, false);
        winSoundPlayer.play();
    }

    public void playLoseSound() {
        MediaPlayer loseSoundPlayer = createMediaPlayer(LOSE_SOUND_PATH, false);
        loseSoundPlayer.play();
    }
}
