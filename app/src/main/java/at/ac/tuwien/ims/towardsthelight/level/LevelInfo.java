package at.ac.tuwien.ims.towardsthelight.level;

import java.io.Serializable;

import at.ac.tuwien.ims.towardsthelight.Highscores;

/**
 * Holds general information about a level like the number, a score object and a pointer to the
 * level image and collision image.
 *
 * @author Thomas Koch
 */
public class LevelInfo implements Serializable {

    /**
     * The number of the level. The first level has number 1.
     */
    public int number;

    /**
     * Received from the database.
     */
    public Highscores.Score score;

    /**
     * The image to be displayed.
     */
    public int imageResource;

    /**
     * Not to be displayed. Holds collision information. White pixels represent collidable objects,
     * red pixels represent collectables and black pixels represent the background.
     */
    public int collisionResource;

    /**
     * Constructs a LevelInfo object with the given parameters.
     *
     * @param number see {@link #number}
     * @param score see {@link #score}
     * @param imageResource see {@link #imageResource}
     * @param collisionResource see {@link #collisionResource}
     */
    public LevelInfo(int number, Highscores.Score score, int imageResource, int collisionResource) {
        this.score = score;
        this.number = number;
        this.imageResource = imageResource;
        this.collisionResource = collisionResource;
    }
}
