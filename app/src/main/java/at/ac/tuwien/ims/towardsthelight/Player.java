package at.ac.tuwien.ims.towardsthelight;

public class Player {

    public static final int SIZE_X = 4;
    public static final int SIZE_Y = 4;
    public static final float SPEED = 8.0f;
    public static final float SLOWED_VELOCITY_Y = 4.0f;
    public static final float BASE_VELOCITY_Y = 8.0f;
    public static final float BASE_ACCELERATION = 8.0f;
    public static final float BOOST_ACCELERATION = 4.0f;
    public static final float DECELERATION = 16.0f;
    public static final float INVINCIBILITY_TIME = 1.5f;
    public float x, y;
    public float velocityX, velocityY;

    public int score = 0;
}
