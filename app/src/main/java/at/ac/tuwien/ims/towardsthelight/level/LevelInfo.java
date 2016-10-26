package at.ac.tuwien.ims.towardsthelight.level;

/**
 * Holds general information about the level.
 *
 * @author Thomas Koch
 */
public class LevelInfo {

    public int number;
    public int bestTime;
    public String fileName;

    public LevelInfo(String fileName, int bestTime, int number) {
        this.fileName = fileName;
        this.bestTime = bestTime;
        this.number = number;
    }
}
