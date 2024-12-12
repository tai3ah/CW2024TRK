package com.example.demo.managers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * The SoundManager class handles the playback of background, win, and lose sounds in the game.
 * It uses the singleton pattern to ensure only one instance of the sound manager exists.
 */
public class SoundManager {

    private static SoundManager instance;

    private MediaPlayer backgroundSoundPlayer;

    private static final String BACKGROUND_SOUND_PATH = "/com/example/demo/audio/gamebgSound.mp3";
    private static final String WIN_SOUND_PATH = "/com/example/demo/audio/winGameSound.mp3";
    private static final String LOSE_SOUND_PATH = "/com/example/demo/audio/loseGameSound.mp3";

    /**
     * Private constructor to prevent instantiation.
     * Initializes the background sound.
     */
    private SoundManager() {
        setupBackgroundSound();
    }

    /**
     * Returns the single instance of the SoundManager.
     *
     * @return the SoundManager instance
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Sets up the persistent background sound.
     */
    private void setupBackgroundSound() {
        backgroundSoundPlayer = createMediaPlayer(BACKGROUND_SOUND_PATH, true);
    }

    /**
     * Creates a MediaPlayer for the specified file path.
     *
     * @param filePath the path to the media file
     * @param loop whether the media should loop
     * @return the created MediaPlayer
     */
    private MediaPlayer createMediaPlayer(String filePath, boolean loop) {
        Media media = new Media(getClass().getResource(filePath).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        if (loop) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        return mediaPlayer;
    }

    /**
     * Plays the background sound if it is not already playing.
     */
    public void playBackgroundSound() {
        if (backgroundSoundPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            backgroundSoundPlayer.play();
        }
    }

    /**
     * Stops the background sound.
     */
    public void stopBackgroundSound() {
        backgroundSoundPlayer.stop();
    }

    /**
     * Plays the win sound.
     */
    public void playWinSound() {
        MediaPlayer winSoundPlayer = createMediaPlayer(WIN_SOUND_PATH, false);
        winSoundPlayer.play();
    }

    /**
     * Plays the lose sound.
     */
    public void playLoseSound() {
        MediaPlayer loseSoundPlayer = createMediaPlayer(LOSE_SOUND_PATH, false);
        loseSoundPlayer.play();
    }
}