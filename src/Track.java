import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

public class Track implements Comparable<Track> {
    private String trackName;
    private int trackID;

    private long bronzeTimeMS;
    private long silverTimeMS;
    private long goldTimeMS;
    private long authorTimeMS;

    private ArrayList<TrackBlock> blockList;

    /**
     * Constructs a new Track object from a file
     * @param filepath the path of the track file
     * @throws FileNotFoundException if the provide filepath does not point to a file
     * @throws IOException if an I/O exception of some sort occurs
     * @throws NumberFormatException if the reader is given a string where a number is expected
     */
    public Track(String filepath) throws FileNotFoundException, IOException, NumberFormatException {
        BufferedReader bufIn = new BufferedReader(new FileReader(filepath));

        int trackID = Integer.parseInt(bufIn.readLine());
    }

    @Override
    public int compareTo(Track track2) {
        // TODO Auto-generated method stub
        return this.trackID - track2.trackID;
    }
}
