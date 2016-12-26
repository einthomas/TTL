package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;

import at.ac.tuwien.ims.towardsthelight.R;

public class ImageButtonXML extends ButtonXML {

    public String text;
    public int x, y;
    protected Bitmap pressedBitmap, releasedBitmap;
    public SpriteFont font;

    public ImageButtonXML(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.ImageButtonXML, 0, 0);

        try {
            text = typedArray.getString(R.styleable.ImageButtonXML_text);
            x = typedArray.getInteger(R.styleable.ImageButtonXML_x, 0);
            y = typedArray.getInteger(R.styleable.ImageButtonXML_y, 0);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        //canvas.setMatrix(gameMatrix);

        if (pressed) {
            canvas.drawBitmap(pressedBitmap, position.left, position.top, null);
        } else {
            canvas.drawBitmap(releasedBitmap, position.left, position.top, null);
        }

        if (font != null) {
            font.drawCentered(canvas, null, text, position.centerX(), position.centerY());
        }
    }
}
