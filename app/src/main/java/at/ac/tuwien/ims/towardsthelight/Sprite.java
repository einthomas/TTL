package at.ac.tuwien.ims.towardsthelight;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Stores a Bitmap, possibly with multiple frames, how to animate it and where to draw it.
 * @author Thomas Koch
 * @author Felix Kugler
 */
public class Sprite {

    /**
     * The Bitmap containing the frames of the Sprite.
     */
    private Bitmap bitmap;

    /**
     * Position on the screen.
     */
    private int x, y;

    /**
     * Dimensions of a single frame.
     */
    public int frameWidth, frameHeight;

    /**
     * Number of frames along the width of the bitmap.
     */
    private int framesPerLine;

    /**
     * Number of seconds to display each frame.
     */
    private float frameDisplayDuration;

    /**
     * Number of seconds the animation is playing.
     */
    private float elapsedTime;

    /**
     * Rect within the bitmap to draw.
     */
    private Rect sourceRect;

    /**
     * RectF the Sprite is drawn in.
     */
    private RectF targetRect;

    /**
     * Index of the first frame of the animation.
     */
    public int startFrame;

    /**
     * Index of the last frame of the animation.
     */
    public int endFrame;

    /**
     * Whether to loop the animation.
     */
    public boolean loop = false;

    /**
     * Whether the animation has finished if it's not looping.
     */
    private boolean done = false;

    /**
     * Create a new Sprite from a bitmap and frame information.
     * @param bitmap The bitmap holding the frames of the Sprite.
     * @param x X position the Sprite is drawn at.
     * @param y Y position the Sprite is drawn at.
     * @param frameWidth Width of an individual frame.
     * @param frameHeight Height of an individual frame.
     * @param totalFrames Total number of frames.
     * @param frameDisplayDuration Seconds each frame is being displayed.
     */
    public Sprite(Bitmap bitmap, int x, int y, int frameWidth, int frameHeight, int totalFrames, float frameDisplayDuration) {
        this.bitmap = bitmap;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameDisplayDuration = frameDisplayDuration;
        this.framesPerLine = bitmap.getWidth() / frameWidth;

        elapsedTime = 0;
        this.startFrame = 0;
        this.endFrame = totalFrames;

        sourceRect = new Rect(0, 0, frameWidth, frameHeight);
        targetRect = new RectF(x, y, x + frameWidth, y + frameHeight);
    }

    /**
     * Advance animation by given number of seconds.
     * @param delta Number of seconds the animation should be advanced.
     */
    public void update(float delta) {
        elapsedTime += delta;

        int frameNumber = (int)(elapsedTime / frameDisplayDuration);

        if (loop || frameNumber < endFrame - startFrame) {
            int frameIndex = frameNumber % (endFrame - startFrame) + startFrame;

            int frameIndexX = frameIndex % framesPerLine;
            int frameIndexY = frameIndex / framesPerLine;

            sourceRect.left = frameIndexX * frameWidth;
            sourceRect.right = sourceRect.left + frameWidth;

            sourceRect.top = frameIndexY * frameHeight;
            sourceRect.bottom = sourceRect.top + frameHeight;
        } else {
            // end of animation
            done = true;
        }
    }

    /**
     * Draw the Sprite onto the given Canvas.
     * @param canvas Canvas to draw onto.
     */
    public void draw(Canvas canvas) {
        if (!done) {
            canvas.drawBitmap(bitmap, sourceRect, targetRect, null);
        }
    }

    /**
     * Set the position of the Sprite on screen.
     * @param x X position the Sprite is drawn at.
     * @param y Y position the Sprite is drawn at.
     */
    public void setPosition(int x, int y) {
        targetRect.set(x, y, x + frameWidth, y + frameHeight);
    }

    /**
     * Set animation back to the start.
     */
    public void reset() {
        elapsedTime = 0;
        done = false;
        sourceRect.left = 0;
        sourceRect.right = frameWidth;
    }

    /**
     * Test whether the animation has finished.
     * @return True if the animation has reached its last frame and is not playing anymore.
     */
    public boolean isDone() {
        return done;
    }
}
