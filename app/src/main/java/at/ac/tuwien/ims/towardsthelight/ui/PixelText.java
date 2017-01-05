package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import at.ac.tuwien.ims.towardsthelight.R;

/**
 * A custom TextView intended to be used in XML. Uses either {@link SpriteFont#mainFont(Resources)}
 * or {@link SpriteFont#hudFont(Resources)} to draw the text.
 *
 * @author Thomas Koch
 */
public class PixelText extends View {

    /**
     * The font the text is drawn with.
     */
    private SpriteFont font;

    /**
     * Used to draw the text.
     */
    private Paint paint;

    /**
     * The text that's set in XML which is drawn.
     */
    public String text;

    /**
     * @param context The current context.
     * @param attrs The attribute set.
     */
    public PixelText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Sets the {@link #paint}, the {@link #text} (to the value specified in XML) and the {@link #font}
     * (to the value specified in XML).
     *
     * @param context The current context.
     * @param attrs The attribute set.
     * @param defStyleAttr Attribute in the current theme that contains a reference to a style
     *                     resource that supplies default values for the StyledAttributes.
     */
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

    /**
     * Calls {@link #setMeasuredDimension(int, int)} to set the required by the text. If the
     * <tt>layout_width</tt> or <tt>layout_height</tt> is set to <tt>wrap_content</tt>, the width
     * or height is set to the dimensions calculated by {@link SpriteFont#getDimensions(String)}
     * otherwise the width or height is set to the value specified.
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
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

        setMeasuredDimension(width, height);
    }

    /**
     * Draws the {@link #text} using {@link #font} and {@link #paint} centered within the bounding
     * box.
     *
     * @param canvas The canvas to be drawn on.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        font.drawCentered(canvas, paint, text, getWidth() / 2, getHeight() / 2 - 1);
    }

    /**
     * @return The {@link #font}.
     */
    public SpriteFont getFont() {
        return font;
    }
}
