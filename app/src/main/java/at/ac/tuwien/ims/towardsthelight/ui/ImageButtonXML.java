package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import at.ac.tuwien.ims.towardsthelight.R;

public class ImageButtonXML extends ButtonXML {

    public String text;
    public float x, y;
    protected Bitmap pressedBitmap, releasedBitmap;
    public SpriteFont font;
    private Paint paint;

    public ImageButtonXML(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        paint = new Paint();

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.ImageButtonXML, 0, 0);
        try {
            text = typedArray.getString(R.styleable.ImageButtonXML_text);
            x = typedArray.getFloat(R.styleable.ImageButtonXML_x, 0.0f);
            y = typedArray.getFloat(R.styleable.ImageButtonXML_y, 0.0f);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        invalidate();
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        if (pressed) {
            canvas.drawBitmap(pressedBitmap, position.left, position.top, paint);
        } else {
            canvas.drawBitmap(releasedBitmap, position.left, position.top, paint);
        }

        if (font != null) {
            font.drawCentered(canvas, paint, text, position.centerX(), position.centerY());
        }
    }
}
