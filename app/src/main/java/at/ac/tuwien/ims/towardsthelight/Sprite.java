package at.ac.tuwien.ims.towardsthelight;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class Sprite {

    private Bitmap bitmap;
    private int x;
    private int y;
    public int frameWidth;
    public int frameHeight;
    private int totalFrames;
    private int drawnFrames;
    private float frameDisplayDuration;
    private float elapsedTime;
    private Rect sourceRect;
    private RectF targetRect;
    public boolean done;

    public Sprite(Bitmap bitmap, int x, int y, int frameWidth, int frameHeight, int totalFrames, float frameDisplayDuration) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.totalFrames = totalFrames;
        this.frameDisplayDuration = frameDisplayDuration;
        drawnFrames = 0;
        sourceRect = new Rect(0, 0, frameWidth, frameHeight);
        targetRect = new RectF(x, y, x + frameWidth, y + frameHeight);
        done = false;
    }

    public void update(float delta) {
        elapsedTime += delta;
        if (elapsedTime >= frameDisplayDuration) {
            elapsedTime = 0;
            if (sourceRect.left + frameWidth >= bitmap.getWidth()) {
                sourceRect.left = 0;
                sourceRect.right = frameWidth;
                done = true;
            } else {
                sourceRect.left += frameWidth;
                sourceRect.right += frameWidth;
                if (totalFrames > 0) {
                    drawnFrames++;
                }
            }
        }
    }

    public void draw(Canvas canvas) {
        if (drawnFrames < totalFrames && !done) {
            canvas.drawBitmap(bitmap, sourceRect, targetRect, null);
        }
    }

    public void setPosition(int x, int y) {
        targetRect.set(x, y, x + frameWidth, y + frameHeight);
    }

    public void reset() {
        drawnFrames = 0;
        done = false;
        sourceRect.left = 0;
        sourceRect.right = frameWidth;
    }
}
