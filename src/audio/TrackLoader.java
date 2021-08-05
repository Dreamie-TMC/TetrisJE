package audio;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public final class TrackLoader {
    private TrackLoader() { }

    public static String loadTrack(String trackList, int songId) {
        Scanner tlistReader = new Scanner(TrackLoader.class.getResourceAsStream(trackList));
        while (songId > 0 && tlistReader.hasNextLine()) {
            tlistReader.nextLine();
            --songId;
        }
        return tlistReader.hasNextLine() ?
                trackList.substring(0, trackList.lastIndexOf('/') + 1) +
                        trimTrack(tlistReader.nextLine()) :
                null;
    }

    public static String loadTrackAsExternalRef(String trackList, int songId) {
        Scanner tlistReader = new Scanner(TrackLoader.class.getResourceAsStream(trackList));
        while (songId > 0 && tlistReader.hasNextLine()) {
            tlistReader.nextLine();
            --songId;
        }
        return tlistReader.hasNextLine() ?
                TrackLoader.class.getResource(
                        trackList.substring(0,
                                trackList.lastIndexOf('/') + 1) +
                                trimTrack(tlistReader.nextLine())).toExternalForm() :
                null;
    }

    public static String loadTrackAsExternalRef(String trackList, String trackName) {
        String track = null;
        Scanner tlistReader = new Scanner(TrackLoader.class.getResourceAsStream(trackList));
        while (tlistReader.hasNextLine()) {
            String temp = tlistReader.nextLine();
            if (temp.toLowerCase(Locale.ROOT).contains(trackName.toLowerCase(Locale.ROOT))) {
                track = temp;
                break;
            }
        }
        return TrackLoader.class.getResource(
                trackList.substring(0,
                        trackList.lastIndexOf('/') + 1) +
                        trimTrack(track)).toExternalForm();
    }

    public static int resolveTrackIdFromName(String trackList, String trackName) {
        Scanner tlistReader = new Scanner(TrackLoader.class.getResourceAsStream(trackList));
        int id = 0;
        while (tlistReader.hasNextLine()) {
            if (tlistReader.nextLine().toLowerCase(Locale.ROOT).contains(trackName.toLowerCase(Locale.ROOT)))
                break;
            ++id;
        }
        return id;
    }

    public static List<String> loadTrackList(String trackList) {
        Scanner tlistReader = new Scanner(TrackLoader.class.getResourceAsStream(trackList));
        List<String> tracks = new ArrayList<>();
        while (tlistReader.hasNextLine()) {
            tracks.add(trimTrack(tlistReader.nextLine()));
        }
        return tracks;
    }

    public static int loadNumberOfTracksInList(String trackList) {
        Scanner tlistReader = new Scanner(TrackLoader.class.getResourceAsStream(trackList));
        int tracks = 0;
        while (tlistReader.hasNextLine()) {
            ++tracks;
            tlistReader.nextLine();
        }
        return tracks;
    }

    public static int[] getLoopSampleMarkers(String trackList, int songId) {
        Scanner tlistReader = new Scanner(TrackLoader.class.getResourceAsStream(trackList));
        while (songId > 0 && tlistReader.hasNextLine()) {
            tlistReader.nextLine();
            --songId;
        }

        int[] ret = new int[2];
        if (tlistReader.hasNextLine()) {
            String line = tlistReader.nextLine();
            int index = line.lastIndexOf(' ');
            ret[1] = Integer.parseInt(line.substring(index + 1));
            line = line.substring(0, index);
            index = line.lastIndexOf(' ');
            ret[0] = Integer.parseInt(line.substring(index + 1));
        }
        return ret;
    }

    public static String findTrackName(String trackList, int songId) {
        Scanner tlistReader = new Scanner(TrackLoader.class.getResourceAsStream(trackList));
        while (songId > 0 && tlistReader.hasNextLine()) {
            tlistReader.nextLine();
            --songId;
        }
        String ret = "NONE";
        if (tlistReader.hasNextLine()) {
            ret = trimTrack(tlistReader.nextLine());
            ret = ret.substring(ret.lastIndexOf('/') + 1, ret.lastIndexOf('.'));
        }
        return ret;
    }

    private static String trimTrack(String track) {
        try {
            //Keep trimming until we hit an error, this means that SFX, which shouldn't loop, don't need loop values to work
            while (track.contains(" ")) {
                int index = track.lastIndexOf(' ');
                Float.parseFloat(track.substring(index + 1));
                track = track.substring(0, index);
            }
        } catch (Exception ignored) {

        }

        return track;
    }
}
