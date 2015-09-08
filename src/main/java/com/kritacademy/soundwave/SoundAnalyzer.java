package com.kritacademy.soundwave;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

/**
 * Created by Chertpong on 7/9/2558.
 */
public class SoundAnalyzer {
    private AudioInputStream media;
    private Vector lines;
    private int[] audioData;
    private byte[] audioBytes;

    public SoundAnalyzer() {
        lines = new Vector();
    }

    public SoundAnalyzer(String fileLocation) throws IOException,UnsupportedAudioFileException{
        lines = new Vector();
        setMedia(fileLocation);
    }

    public void setMedia(String fileLocation) throws IOException,UnsupportedAudioFileException{
        this.media = AudioSystem.getAudioInputStream(new File(fileLocation));
    }
    private void calcAudioData(){
        lines.removeAllElements();
        audioBytes = new byte[(int)media.getFrameLength()*media.getFormat().getFrameSize()];
        try {
            media.read(audioBytes);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (media.getFormat().getSampleSizeInBits() == 16) {
            int nlengthInSamples = audioBytes.length / 2;
            audioData = new int[nlengthInSamples];
            if (media.getFormat().isBigEndian()) {
                for (int i = 0; i < nlengthInSamples; i++) {
                         /* First byte is MSB (high order) */
                    int MSB = (int) audioBytes[2*i];
                         /* Second byte is LSB (low order) */
                    int LSB = (int) audioBytes[2*i+1];
                    audioData[i] = MSB << 8 | (255 & LSB);
                }
            } else {
                for (int i = 0; i < nlengthInSamples; i++) {
                         /* First byte is LSB (low order) */
                    int LSB = (int) audioBytes[2*i];
                         /* Second byte is MSB (high order) */
                    int MSB = (int) audioBytes[2*i+1];
                    audioData[i] = MSB << 8 | (255 & LSB);
                }
            }
        } else if (media.getFormat().getSampleSizeInBits() == 8) {
            int nlengthInSamples = audioBytes.length;
            audioData = new int[nlengthInSamples];
            if (media.getFormat().getEncoding().toString().startsWith("PCM_SIGN")) {
                for (int i = 0; i < audioBytes.length; i++) {
                    audioData[i] = audioBytes[i];
                }
            } else {
                for (int i = 0; i < audioBytes.length; i++) {
                    audioData[i] = audioBytes[i] - 128;
                }
            }
        }
    }
    public int getLength() {
        if (media != null){
            return (int)(media.getFrameLength()/media.getFormat().getFrameRate());
        }
        return -1;
    }
    /*
     * @param time the time which unit is second
     */
//    public double getAmplitude(int time) {
//        return media.getFormat().getFrameSize()*media.getFrameLength();
//    }
    public Vector getAmplitudeVector(int screenWidth, int screenHeight) throws RuntimeException{
        if(media == null) throw new RuntimeException("No media found");

        calcAudioData();
        int frames_per_pixel = audioBytes.length / media.getFormat().getFrameSize()/screenWidth;
        byte my_byte = 0;
        int numChannels = media.getFormat().getChannels();

        for (int x = 0; x < screenWidth; x++) {
            int idx = (int) (frames_per_pixel * numChannels * x);
            if (media.getFormat().getSampleSizeInBits() == 8) {
                my_byte = (byte) audioData[idx];
            } else {
                my_byte = (byte) (128 * audioData[idx] / 32768 );
            }
            double y = (double) (screenHeight * (128 - my_byte) / 256);
            lines.add(Arrays.asList(x,y));
        }
        return lines;
    }
}
