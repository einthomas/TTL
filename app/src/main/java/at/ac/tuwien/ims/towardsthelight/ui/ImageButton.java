package at.ac.tuwien.ims.towardsthelight.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Button for SurfaceView with images.
 * Currently only used for the pause button.
 *
 * @author Felix Kugler
 */
public abstract  class ImageButton extends Button {
    /**
     * Font used for caption.
     */
    public SpriteFont font;

    /**
     * Text used for caption.
     */
    public String text;

    /**
     * Bitmaps used for released and pressed state.
     */
    public Bitmap releasedBitmap, pressedBitmap;

    /**
     * Constructs a new ImageButton at the given position with the given Bitmaps.
     * @param releasedBitmap Image used for released state.
     * @param pressedBitmap Image used for pressed state.
     * @param x X coordinate of the center of the button.
     * @param y Y coordinate of the center of the button.
     */
    public ImageButton(Bitmap releasedBitmap, Bitmap pressedBitmap, float x, float y) {
        super(new RectF(
                x - (releasedBitmap.getWidth() + 1) / 2, // ensure proper rounding
                y - (releasedBitmap.getHeight() + 1) / 2,
                x + releasedBitmap.getWidth() / 2,
                y + releasedBitmap.getHeight() / 2
        ));
        this.releasedBitmap = releasedBitmap;
        this.pressedBitmap = pressedBitmap;
    }

    /**
     * Constructs a new ImageButton at the given position with the given Bitmaps and the given
     * caption.
     * @param font Font used for caption.
     * @param text Caption on the button.
     * @param releasedBitmap Image used for released state.
     * @param pressedBitmap Image used for pressed state.
     * @param x X coordinate of the center of the button.
     * @param y Y coordinate of the center of the button.
     */
    public ImageButton(SpriteFont font, String text, Bitmap releasedBitmap, Bitmap pressedBitmap, float x, float y) {
        super(new RectF(
            x - (releasedBitmap.getWidth() + 1) / 2, // ensure proper rounding
            y - (releasedBitmap.getHeight() + 1) / 2,
            x + releasedBitmap.getWidth() / 2,
            y + releasedBitmap.getHeight() / 2
        ));
        this.font = font;
        this.text = text;
        this.releasedBitmap = releasedBitmap;
        this.pressedBitmap = pressedBitmap;
    }

    /**
     * Draw the button onto the given canvas.
     * @param canvas The canvas to draw onto.
     * @param paint The paint to use for drawing.
     */
    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (pressed) {
            canvas.drawBitmap(pressedBitmap, position.left, position.top, paint);
        } else {
            canvas.drawBitmap(releasedBitmap, position.left, position.top, paint);
        }

        if (font != null) {
            font.drawCentered(canvas, paint, text, position.centerX(), position.centerY());
        }
    }

    /**
     * Release images.
     */
    public void recycle() {
        releasedBitmap.recycle();
        pressedBitmap.recycle();
    }
}

