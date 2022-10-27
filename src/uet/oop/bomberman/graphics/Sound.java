package uet.oop.bomberman.graphics;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.net.URL;

import static uet.oop.bomberman.graphics.Menu.muteSound;

public class Sound {
    public static Clip play;
    public static Sound sound = new Sound();

    public static boolean isPlaying = false;


    public void playSound(String name) {
        try {
            URL url = this.getClass().getClassLoader().getResource("sounds/" + name + ".wav");
            assert url != null;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            play = AudioSystem.getClip();
            play.open(audioIn);
            play.loop(0);
            play.start();
            isPlaying = true;
            if (muteSound) {
                play.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
