package at.ac.tuwien.ims.towardsthelight.level;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;

import java.nio.IntBuffer;
import java.util.ArrayList;

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
    public ArrayList<Obstacle> obstacles;
    public int width, height;
    public Rect rect, rectSource;
    public Bitmap bitmap;
    private Context context;
    private byte[] collisionData;

    public float t, b;

    /**
     * Calls loadLevel to load the level according to the file name which is stored in levelInfo.
     *
     * @param levelInfo the level to be loaded.
     */
    public Level(Context context, final int GAME_HEIGHT, final int GAME_WIDTH, LevelInfo levelInfo) {
        this.context = context;
        this.levelInfo = levelInfo;

        obstacles = new ArrayList<>();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), levelInfo.imageResource, options);
        rect = new Rect(0, 0, GAME_HEIGHT, GAME_WIDTH);
        rectSource = new Rect(0, bitmap.getHeight() - 114, bitmap.getWidth(), bitmap.getHeight());

        t = rectSource.top;
        b = rectSource.bottom;


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
                } else {
                    collisionData[arrayPos] = COLLECTABLE;
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
