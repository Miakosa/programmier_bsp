package ws23.Version_Two.Model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Sound {

    private final MediaPlayer mediaPlayer;

    public Sound(String soundFilePath){
        Media sound = new Media(new File(soundFilePath).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
    }

    /**
     * Method to play the sound file once
     */
    public void play() {
        mediaPlayer.stop();
        mediaPlayer.seek(mediaPlayer.getStartTime());
        mediaPlayer.play();
    }

    /**
     * Method to play the sound file in a loop
     */
    public void playLoop() {
        mediaPlayer.stop();
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play();
    }

    /**
     * Method to stop the sound file
     */
    public void stop() {
        mediaPlayer.stop();
        mediaPlayer.setCycleCount(1);
    }
}

