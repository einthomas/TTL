package at.ac.tuwien.ims.towardsthelight.level;

import java.io.Serializable;

import at.ac.tuwien.ims.towardsthelight.Highscores;

/**
 * Holds general information about the level.
 *
 * @author Thomas Koch
 */
public class LevelInfo implements Serializable {

    public int number;
    public Highscores.Score score;
    public int imageResource;
    public int collisionResource;

    public LevelInfo(int number, Highscores.Score score, int imageResource, int collisionResource) {
        this.score = score;
        this.number = number;
        this.imageResource = imageResource;
        this.collisionResource = collisionResource;
    }
}
