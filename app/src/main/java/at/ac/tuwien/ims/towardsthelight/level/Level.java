package at.ac.tuwien.ims.towardsthelight.level;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.nio.IntBuffer;
import java.util.ArrayList;

import at.ac.tuwien.ims.towardsthelight.R;

/**
 * Holds the level's obstacles in an ArrayList. Loads the level when the constructor is called.
 *
 * @author Thomas Koch
 */
public class Level {

    public static final byte BACKGROUND = 0;
    public static final byte OBSTACLE = 1;
    public static final byte COLLECTABLE = 2;

    public LevelInfo levelInfo;
    public Bitmap bitmap;
    private Context context;
    private byte[] collisionData;
    public ArrayList<Collectable> collectables;

    /**
     * Calls loadLevel to load the level according to the file name which is stored in levelInfo.
     *
     * @param levelInfo the level to be loaded.
     */
    public Level(Context context, final int GAME_HEIGHT, final int GAME_WIDTH, LevelInfo levelInfo) {
        this.context = context;
        this.levelInfo = levelInfo;

        collectables = new ArrayList<>();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), levelInfo.imageResource, options);

        Bitmap collectibleBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.collectable, options);


        // process collision image
        Bitmap collisionImage = BitmapFactory.decodeResource(context.getResources(), levelInfo.collisionResource, options);

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

    public boolean withinBounds(int x, int y) {
        if (x < 0 || y < 0 || x >= bitmap.getWidth() || y >= bitmap.getHeight()) {
            return false;
        }

        return true;
    }

    public byte getCollisionData(int x, int y) {
        return collisionData[y * bitmap.getWidth() + x];
    }
}
