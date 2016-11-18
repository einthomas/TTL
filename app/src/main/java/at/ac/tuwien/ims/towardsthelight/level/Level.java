package at.ac.tuwien.ims.towardsthelight.level;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

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
     * Loads the level image and the collision image according to {@link #levelInfo} and the
     * collectable graphic. Also processes the collision image and fills the {@link #collisionData}
     * array.
     *
     * @param context used to load the level's images.
     * @param levelInfo general information about the level to be loaded.
     */
    public Level(Context context, LevelInfo levelInfo) {
        this.levelInfo = levelInfo;

        collectables = new ArrayList<>();

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
                if (Color.red(color) == 0 && Color.green(color) == 0 && Color.blue(color) == 0) {
                    collisionData[arrayPos] = BACKGROUND;
                } else if (Color.red(color) == 255 && Color.green(color) == 255 && Color.blue(color) == 255) {
                    collisionData[arrayPos] = OBSTACLE;
                } else if (Color.red(color) == 0 && Color.green(color) == 0 && Color.blue(color) == 255) {
                    collectables.add(new Collectable(x, collisionImage.getHeight() - y, collectibleBitmap));
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
