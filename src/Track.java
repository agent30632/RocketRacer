import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Track implements Comparable<Track> {
    static final int MAX_GRID_X = 32;
    static final int MAX_GRID_Y = 32;
    
    private String trackID;

    private Time bronzeTime;
    private Time silverTime;
    private Time goldTime;
    private Time authorTime;

    private TrackBlock startBlock;
    private TrackBlock finishBlock;
    private HashSet<TrackBlock> blockList;

    private int checkpointCount = 0;

    /**
     * Constructs a new Track object from a file
     * @param filepath the path of the track file
     * @throws FileNotFoundException if the provide filepath does not point to a file
     * @throws IOException if an I/O exception of some sort occurs
     * @throws ParseException if the reader encounters an error when parsing the file (e.g. invalid formatting)
     */
    public Track(String filepath) throws FileNotFoundException, IOException, IllegalArgumentException {
        BufferedReader bufIn = new BufferedReader(new FileReader(filepath));

        this.trackID = bufIn.readLine();

        this.bronzeTime = new Time(bufIn.readLine());
        this.silverTime = new Time(bufIn.readLine());
        this.goldTime = new Time(bufIn.readLine());
        this.authorTime = new Time(bufIn.readLine());

        this.startBlock = TrackBlock.parseBlock(bufIn.readLine());
        if (this.startBlock.getType() != BlockType.START) {
            bufIn.close();
            throw new IllegalArgumentException();
        }
        this.finishBlock = TrackBlock.parseBlock(bufIn.readLine());
        if (this.finishBlock.getType() != BlockType.FINISH) {
            bufIn.close();
            throw new IllegalArgumentException();
        }

        Pattern blockFormat = Pattern.compile("^(\\([0-9]+, [0-9]+\\) ){1}(START|CHECKPOINT|FINISH|BOOST|NOCONTROL|RESET|WALL){1} (UP|RIGHT|DOWN|LEFT){1}$");
        Pattern blockFormatAlt = Pattern.compile("^(\\([0-9]+, [0-9]+\\) ){2}(BOOST|NOCONTROL|RESET|WALL){1} (UP|RIGHT|DOWN|LEFT){1}$");

        HashSet<TrackBlock> trackBlockBuffer = new HashSet<>();
        String trackData = bufIn.readLine();
        while (trackData != null) {
            Matcher format1Matcher = blockFormat.matcher(trackData);
            Matcher format2Matcher = blockFormatAlt.matcher(trackData);

            if (format1Matcher.matches()) {
                TrackBlock block = TrackBlock.parseBlock(trackData);
                BlockType blockType = block.getType();
                if (blockType == BlockType.CHECKPOINT) {
                    checkpointCount++;
                }
                trackBlockBuffer.add(block);
            } else if (format2Matcher.matches()) {
                // TODO: rectangles of blocks
                trackBlockBuffer.addAll(TrackBlock.parseBlockRange(trackData));
            } else {
                bufIn.close();
                throw new NumberFormatException();
            }
            trackData = bufIn.readLine();
        }

        this.blockList = trackBlockBuffer;

        bufIn.close();
    }

    @Override
    public int compareTo(Track track2) {
        // TODO Auto-generated method stub
        return this.trackID.compareTo(track2.getTrackID());
    }

    public String getTrackID() {
        return trackID;
    }

    public Time getBronzeTime() {
        return bronzeTime;
    }
    public Time getSilverTime() {
        return silverTime;
    }
    public Time getGoldTime() {
        return goldTime;
    }
    public Time getAuthorTime() {
        return authorTime;
    }

    public TrackBlock getStartBlock() {
        return startBlock;
    }
    public TrackBlock getFinishBlock() {
        return finishBlock;
    }

    public HashSet<TrackBlock> getBlockSet() {
        return blockList;
    }

    public int getCheckpointCount() {
        return checkpointCount;
    }
}
