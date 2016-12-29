package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import at.ac.tuwien.ims.towardsthelight.R;

/**
 * @author Thomas Koch
 */
public class PixelText extends View {

    private SpriteFont font;
    private Paint paint;
    public float x, y;
    public String text;

    public PixelText(Context context) {
        super(context);
    }

    public PixelText(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PixelText);

        // get text attribute
        String textAttribute = attributes.getString(R.styleable.PixelText_text);
        if (textAttribute != null) {
            text = textAttribute;
        }

        // get x, y attributes
        x = attributes.getFloat(R.styleable.PixelText_x, 0);
        y = attributes.getFloat(R.styleable.PixelText_y, 0);

        // get fontType attribute
        SpriteFont.FontType fontType = SpriteFont.FontType.getFromId(attributes.getInt(R.styleable.PixelText_fontType, 0));
        font = SpriteFont.getFont(fontType, getResources());
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

        // get x, y attributes
        x = attributes.getInt(R.styleable.PixelText_x, 0);
        y = attributes.getInt(R.styleable.PixelText_y, 0);

        // get fontType attribute
        SpriteFont.FontType fontType = SpriteFont.FontType.getFromId(attributes.getInt(R.styleable.PixelText_fontType, 0));
        font = SpriteFont.getFont(fontType, getResources());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] dimensions = font.getDimensions(text);
        setMeasuredDimension(dimensions[0] + 2, dimensions[1] + 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        font.drawText(canvas, paint, text, x, y);
    }

    public SpriteFont getFont() {
        return font;
    }
}
