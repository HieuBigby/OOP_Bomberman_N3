package uet.oop.bomberman.graphics;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.net.URL;

public class Sound extends JFrame{
    public static Clip main;
    public static Sound sound = new Sound();
    public void playSound(String name){
        try{
                URL url = this.getClass().getClassLoader().getResource("sounds/"+name+".wav");
                assert url != null;
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                main = AudioSystem.getClip();
                main.open(audioIn);
                main.start();
                System.out.println(main.getFrameLength());
//            if (name.equals("main")) {
//                main.loop(20);
//            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
