package audio;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.javasound.JSMinim;
import ddf.minim.spi.AudioOut;
import ddf.minim.spi.AudioRecordingStream;

import javax.sound.sampled.AudioFormat;
import java.lang.reflect.Field;
import java.util.Objects;

public final class MusicPlayer {
    /**
     * We use singleton since there should only be 1 music player for any class that needs it. This makes ie
     * expandable moving forward if more stuff is added
     * @return  The reference to the music player
     */
    public static MusicPlayer getInstance() {
        if (player == null) {
            player = new MusicPlayer();
        }
        return player;
    }

    public static String getTrackName() {
        return trackName;
    }

    private static String trackName = "NONE";
    private static MusicPlayer player;
    private static final String tlistPath = "/audio/music/songs.tlist";

    private boolean play = false;
    private final Minim minim;
    private AudioPlayer audioPlayer;
    private int trackId;

    /**
     * Creates a new music reference and loads the first track
     */
    private MusicPlayer() {
        minim = new Minim(new AudioHandler());
        trackId = 0;
        selectTrack(0);
    }

    /**
     * Selects a new track from the list of background audio tracks, will not change if the music
     * was not properly stopped
     * @param trackNumber   The ID number for the specific track
     */
    public void selectTrack(int trackNumber) {
        if (trackNumber == -1)
            trackName = "No Music";
        else if (!play) {
            String track = TrackLoader.loadTrack(tlistPath, trackNumber);
            AudioRecordingStream stream = minim.loadFileStream(track);
            int[] loopPoints = TrackLoader.getLoopSampleMarkers(tlistPath, trackNumber);
            audioPlayer = new AudioPlayer(stream, Objects.requireNonNull(getAudioOut(track)));
            audioPlayer.setLoopPoints((int)(loopPoints[0] / 44.1), (int)(loopPoints[1] / 44.1));
            audioPlayer.play();
            remoteStreamLoopInvocation(stream);
            trackName = TrackLoader.findTrackName(tlistPath, trackNumber);
            play = true;
        }
    }

    public boolean isPlaying() {
        return play && audioPlayer != null && audioPlayer.isPlaying();
    }

    /**
     * Stops the bg music if it is already playing
     */
    public void stop() {
        if (play) {
            play = false;
            audioPlayer.close();
        }
    }

    public void pause() {
        if (audioPlayer == null)
            return;
        if (audioPlayer.isPlaying()) {
            audioPlayer.pause();
        } else {
            audioPlayer.loop();
        }
    }

    public boolean hasPreviousTrack() {
        return trackId != -1;
    }

    public boolean hasNextTrack() {
        return trackId + 1 != TrackLoader.loadNumberOfTracksInList(tlistPath);
    }

    public boolean selectNextTrack() {
        selectTrack(++trackId);
        return trackId + 1 != TrackLoader.loadNumberOfTracksInList(tlistPath);
    }

    public boolean selectPreviousTrack() {
        selectTrack(--trackId);
        return trackId != -1;
    }

    /**
     * A hack in otder to get the audio out stream for a custom audio player. This is needed for the loop invocation
     * stuff below.
     * @param track The name of the track we are getting
     * @return      The audio out stream for the audio player
     */
    private static AudioOut getAudioOut(String track) {
        JSMinim js = new JSMinim(new AudioHandler());
        AudioRecordingStream rec = js.getAudioRecordingStream(track, 1024, false);
        if (rec != null) {
            AudioFormat format = rec.getFormat();
            return js.getAudioOutput(format.getChannels(), 1024, format.getSampleRate(), format.getSampleSizeInBits());
        }
        return null;
    }

    /**
     * Plays with reflections in order to make the audio loop not start at the looping position but instead at the
     * start of the track. This is extremely hacky but I couldn't find a better way to do this that involved
     * a clean loop of the audio.
     * @param stream    The underlying audio stream.
     */
    private static void remoteStreamLoopInvocation(AudioRecordingStream stream) {
        try {
            Field field = stream.getClass().getSuperclass().getDeclaredField("loop");
            field.setAccessible(true);
            field.setBoolean(stream, true);
            field.setAccessible(false);
            field = stream.getClass().getSuperclass().getDeclaredField("numLoops");
            field.setAccessible(true);
            field.setInt(stream, -1);
            field.setAccessible(false);
        } catch (Exception e) {
            System.out.println("Could not properly loop track for audio stream!");
        }
    }
}
