package at.ac.tuwien.ims.towardsthelight.level;

/**
 * Represents a warning sign in a level.
 * @author Felix Kugler
 */
public class Sign {
    /**
     * Position of the obstacle this sign is for.
     * The sign will be shown to the player a fixed amount of time before they reach the obstacle.
     */
    private int x, y;

    /**
     * Construct a new sign for the given position.
     * @param x X coordinate of the obstacle this sign is for.
     * @param y Y coordinate of the obstacle this sign is for.
     */
    public Sign(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x coordinate of the obstacle this sign is for.
     * @return The x coordinate of the obstacle.
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y coordinate of the obstacle this sign is for.
     * @return The y coordinate of the obstacle.
     */
    public int getY() {
        return y;
    }
}
