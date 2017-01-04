package at.ac.tuwien.ims.towardsthelight.level;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import java.nio.IntBuffer;
import java.util.ArrayList;

import at.ac.tuwien.ims.towardsthelight.R;

/**
 * Holds the loaded level image ({@link #bitmap}) which is intended to be drawn, a {@link LevelInfo}
 * object ({@link #levelInfo}) and an array of collectables ({@link #collectables}) which are read
 * from the collision image ({@link LevelInfo#collisionResource}).
 *
 * @author Thomas Koch
 */
public class Level {

    public static final byte BACKGROUND = 0;
    public static final byte OBSTACLE = 1;

    public LevelInfo levelInfo;

    /**
     * The loaded level image which is displayed. ({@link LevelInfo#imageResource})
     */
    public Bitmap bitmap;

    /**
     * Contains the data of the parsed ({@link LevelInfo#collisionResource collision image}). For
     * each white pixel the array contains the value of {@link #OBSTACLE}, for each black pixel the
     * value of {@link #BACKGROUND}.
     */
    private byte[] collisionData;

    /**
     * Contains collectables ordered by their position within the level ascending.
     */
    public ArrayList<Collectable> collectables;

    /**
     * Contains (danger) signs ordered by their position within the level ascending.
     */
    public ArrayList<Sign> signs;

    /**
     * Loads the level image and the collision image according to {@link #levelInfo}. A white pixel
     * within the collision image represents an obstacle, black represents background, blue
     * represents a collectable and green represents a sign. Fills the {@link #collisionData} array
     * with this information.
     *
     * @param context used to load the level's images.
     * @param levelInfo general information about the level to be loaded.
     */
    public Level(Context context, LevelInfo levelInfo) {
        this.levelInfo = levelInfo;

        collectables = new ArrayList<>();
        signs = new ArrayList<>();

        // load images
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        bitmap = BitmapFactory.decodeResource(context.getResources(), levelInfo.imageResource, options);
        Bitmap collectibleBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.collectable, options);
        Bitmap collisionImage = BitmapFactory.decodeResource(context.getResources(), levelInfo.collisionResource, options);

        // process collision image
        collisionData = new byte[collisionImage.getWidth() * collisionImage.getHeight()];

        IntBuffer pixelBuffer = IntBuffer.allocate(collisionImage.getWidth() * collisionImage.getHeight());
        collisionImage.copyPixelsToBuffer(pixelBuffer);

        for (int y = collisionImage.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < collisionImage.getWidth(); x++) {
                int arrayPos = y * collisionImage.getWidth() + x;
                int color = pixelBuffer.get(arrayPos);
                arrayPos = collisionImage.getWidth() * (collisionImage.getHeight() - 1) - y * collisionImage.getWidth() + x;
                if (color == 0xFF000000) {
                    collisionData[arrayPos] = BACKGROUND;
                } else if (color == 0xFFFFFFFF) {
                    collisionData[arrayPos] = OBSTACLE;
                } else if (color == 0xFF0000FF) {
                    collectables.add(new Collectable(x, collisionImage.getHeight() - y, collectibleBitmap));
                } else if (color == 0xFF00FF00) {
                    signs.add(new Sign(x, collisionImage.getHeight() - y));
                } else {
                    Log.d("Level", "unknown collision color " + color);
                }
            }
        }
    }

    /**
     * Checks if x and y are within the image bounds/dimensions.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return <tt>true</tt> if within the bounds, <tt>false</tt> otherwise
     */
    public boolean withinBounds(int x, int y) {
        if (x < 0 || y < 0 || x >= bitmap.getWidth() || y >= bitmap.getHeight()) {
            return false;
        }

        return true;
    }

    /**
     * Returns the value of the {@link #collisionData} array at position (x, y) calculated by
     *
     * @param x the x position
     * @param y the y position
     * @return the value of the {@link #collisionData} array at position (x, y).
     */
    public byte getCollisionData(int x, int y) {
        return collisionData[y * bitmap.getWidth() + x];
    }
}
