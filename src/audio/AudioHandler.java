package audio;

import java.io.File;
import java.io.InputStream;

public final class AudioHandler {
    public InputStream createInput(String filename) {
        return getClass().getResourceAsStream(filename);
    }

    public String sketchPath(String filename) {
        return new File(filename).getAbsolutePath();
    }
}
