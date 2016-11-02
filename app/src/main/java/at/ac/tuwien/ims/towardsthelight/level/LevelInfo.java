package at.ac.tuwien.ims.towardsthelight.level;

/**
 * Holds general information about the level.
 *
 * @author Thomas Koch
 */
public class LevelInfo {

    public int number;
    public int bestTime;
    public int imageResource;
    public int collisionResource;

    public LevelInfo(int bestTime, int number, int imageResource, int collisionResource) {
        this.bestTime = bestTime;
        this.number = number;
        this.imageResource = imageResource;
        this.collisionResource = collisionResource;
    }
}
