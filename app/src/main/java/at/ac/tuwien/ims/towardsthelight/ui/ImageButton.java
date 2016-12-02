package at.ac.tuwien.ims.towardsthelight.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Felix on 02.12.2016.
 */

public abstract  class ImageButton extends Button {
    public SpriteFont font;
    public String text;
    public Bitmap releasedBitmap, pressedBitmap;

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

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (pressed) {
            canvas.drawBitmap(pressedBitmap, position.left, position.top, paint);
        } else {
            canvas.drawBitmap(releasedBitmap, position.left, position.top, paint);
        }

        font.drawCentered(canvas, paint, text, position.centerX(), position.centerY());
    }
}

