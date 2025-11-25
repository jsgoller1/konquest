package game;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Random;

// Simple MusicPlayer using Java Sound. Plays WAV/PCM audio files and supports looping.
public class MusicPlayer {
    private Clip clip;
    private FloatControl volumeControl;

    public MusicPlayer() {
        this.clip = null;
        this.volumeControl = null;
    }

    public void start() {
        Random rand = new Random();
        int pick = rand.nextInt(3);
        switch (pick) {
            case 0:
                playTrack("assets/Music/fgo-between-light-dark.wav", true);
                break;
            case 1:
                playTrack("assets/Music/granblue-armageddon.wav", true);
                break;
            case 2:
                playTrack("assets/Music/zelda-totk-main.wav", true);
                break;
        }
    }

    /**
     * Play a WAV file. If loop=true the track will loop continuously. Example path:
     * "assets/Music/FGO_BetweenLight.wav"
     */
    public void playTrack(String filePath, boolean loop) {
        stopTrack();
        try {
            File audioFile = new File(filePath);
            AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);

            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
                    baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);

            AudioInputStream dais = AudioSystem.getAudioInputStream(decodedFormat, ais);
            clip = AudioSystem.getClip();
            clip.open(dais);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Failed to play audio: " + e.getMessage());
        }
    }

    // Stop playback and release resources
    public void stopTrack() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }

    /**
     * Set volume where 0.0 is silent and 1.0 is max. If hardware doesn't support gain control this
     * is a no-op.
     */
    public void setVolume(float level) {
        if (volumeControl == null)
            return;
        float clamped = Math.max(0f, Math.min(1f, level));
        float dB = (float) (20f * Math.log10(clamped <= 0f ? 0.0001f : clamped));
        volumeControl.setValue(dB);
    }

    public boolean isPlaying() {
        return clip != null && clip.isActive();
    }
}
