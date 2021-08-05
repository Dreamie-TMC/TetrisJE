package audio;

import javafx.scene.media.AudioClip;

public final class EffectsHandler {
    /**
     * We use singleton since there should only be 1 music player for any class that needs it. This makes ie
     * expandable moving forward if more stuff is added
     * @return  The reference to the music player
     */
    public static EffectsHandler getInstance() {
        if (handler == null) {
            handler = new EffectsHandler();
        }
        return handler;
    }

    private static EffectsHandler handler;

    private EffectsHandler() { }

    public void playClipFromName(String name) {
        AudioClip clip = new AudioClip(TrackLoader.loadTrackAsExternalRef("/audio/sfx/songs.tlist", name));
        clip.play();
    }

    public void playClipFromId(int id) {
        AudioClip clip = new AudioClip(TrackLoader.loadTrackAsExternalRef("/audio/sfx/songs.tlist", id));
        clip.play();
    }
}
