package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import at.ac.tuwien.ims.towardsthelight.R;

/**
 * @author Thomas Koch
 */
public class PixelText extends View {

    private SpriteFont font;
    private Paint paint;
    public String text;

    public PixelText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PixelText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PixelText);

        // get text attribute
        String textAttribute = attributes.getString(R.styleable.PixelText_text);
        if (textAttribute != null) {
            text = textAttribute;
        }

        // get fontType attribute
        SpriteFont.FontType fontType = SpriteFont.FontType.getFromId(attributes.getInt(R.styleable.PixelText_fontType, 0));
        font = SpriteFont.getFont(fontType, getResources());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] dimensions = font.getDimensions(text);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) {
            width = dimensions[0] + 2;
        }

        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) {
            height = dimensions[1] + 2;
        }

        Log.i("PixelText", "onMeasure: " + width + "x" + height);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        font.drawCentered(canvas, paint, text, getWidth() / 2, getHeight() / 2 - 1);
    }

    public SpriteFont getFont() {
        return font;
    }
}
