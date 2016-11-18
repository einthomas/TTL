package at.ac.tuwien.ims.towardsthelight;

/**
 * Stores information about the player character.
 * @author Felix Kugler
 */
public class Player {

    /**
     * Width of collision rectangle in virtual pixel.
     */
    public static final int SIZE_X = 4;
    /**
     * Height of collision rectangle in virtual pixel.
     */
    public static final int SIZE_Y = 4;
    /**
     * Multiplier for all movement.
     */
    public static final float SPEED = 8.0f;
    /**
     * Velocity right after a collision.
     */
    public static final float SLOWED_VELOCITY_Y = 4.0f;
    /**
     * Velocity without boost.
     */
    public static final float BASE_VELOCITY_Y = 8.0f;
    /**
     * Acceleration without boost. This defines how quickly the player reaches
     * {@link #BASE_VELOCITY_Y} after the start of the game or a collision.
     */
    public static final float BASE_ACCELERATION = 8.0f;
    /**
     * Acceleration when using boost.
     */
    public static final float BOOST_ACCELERATION = 4.0f;
    /**
     * Deceleration when not using boost.
     */
    public static final float DECELERATION = 16.0f;
    /**
     * Time the player is invincible after a collision.
     */
    public static final float INVINCIBILITY_TIME = 1.5f;

    /**
     * X position of the player.
     */
    public float x;
    /**
     * Y position of the player.
     * Since the vertical position of the player is fixed this is the inverse Y position
     * of the level.
     */
    public float y;
    /**
     * Player velocity on Y axis.
     */
    public float velocityY;

    /**
     * Number of collectibles that have been collected.
     */
    public int score = 0;
    /**
     * Number of lives the player has left.
     */
    public int lives = 3;
}
