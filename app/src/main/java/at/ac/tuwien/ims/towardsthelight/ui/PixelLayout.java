package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import at.ac.tuwien.ims.towardsthelight.R;

/**
 * RelativeLayout with a virtual resolution that is scaled to fit the screen resolution.
 * @author Felix Kugler
 */
public class PixelLayout extends RelativeLayout {

    /**
     * Virtual resolution.
     */
    private int virtualWidth, virtualHeight;

    /**
     * This value is subtracted from all positions.
     * Work around Android clipping negative margins and translations.
     */
    private int offsetX, offsetY;

    /**
     * Construct a new PixelLayout with the given context.
     * @param context Context object to pass to RelativeLayout.
     */
    public PixelLayout(Context context) {
        super(context);
    }

    /**
     * Construct a new PixelLayout with the given context.
     * @param context Context object to pass to RelativeLayout.
     * @param attrs Attributes to pass to RelativeLayout.
     */
    public PixelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Construct a new PixelLayout with the given context.
     * @param context Context object to pass to RelativeLayout.
     * @param attrs Attributes to pass to RelativeLayout.
     * @param defStyleAttr Style attribute to pass to RelativeLayout.
     */
    public PixelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PixelLayout);
        setVirtualWidth(attributes.getInt(R.styleable.PixelLayout_virtualWidth, 1));
        setVirtualHeight(attributes.getInt(R.styleable.PixelLayout_virtualHeight, 1));
        setOffsetX(attributes.getInt(R.styleable.PixelLayout_offsetX, 0));
        setOffsetY(attributes.getInt(R.styleable.PixelLayout_offsetY, 0));
    }

    /**
     * Update scaling to make sure the virtual resolution fits onto the screen.
     * @param changed Unused.
     * @param l Left
     * @param t Top
     * @param r Right
     * @param b Bottom
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // virtual resolution
        int resolutionX, resolutionY;

        float screenAspectRatio = (float) getWidth() / getHeight();
        float gameAspectRatio = (float) virtualWidth / virtualHeight;

        float scale;
        if (screenAspectRatio > gameAspectRatio) {
            scale = (float) getHeight() / virtualHeight;   // fit to height
        } else {
            scale = (float) getWidth() / virtualWidth;     // fit to width
        }
        resolutionX = Math.round(getWidth() / scale);
        resolutionY = Math.round(getHeight() / scale);

        setScaleX((float)getWidth() / resolutionX);
        setScaleY((float)getHeight() / resolutionY);

        setPivotX(0);
        setPivotY(0);

        setTranslationX(((resolutionX - virtualWidth) / 2 - offsetX) * scale);
        setTranslationY(((resolutionY - virtualHeight) / 2 - offsetY) * scale);

        super.onLayout(changed, l, t, r, b);
    }

    /**
     * Get virtual width.
     * @return Virtual width.
     */
    public int getVirtualWidth() {
        return virtualWidth;
    }

    /**
     * Set virtual width.
     * @param virtualWidth New virtual width.
     */
    public void setVirtualWidth(int virtualWidth) {
        this.virtualWidth = virtualWidth;
        invalidate();
    }

    /**
     * Get virtual height.
     * @return Virtual height.
     */
    public int getVirtualHeight() {
        return virtualHeight;
    }

    /**
     * Set virtual height.
     * @param virtualHeight New virtual height.
     */
    public void setVirtualHeight(int virtualHeight) {
        this.virtualHeight = virtualHeight;
        invalidate();
    }

    /**
     * Get horizontal offset.
     * @return Horizontal offset.
     */
    public int getOffsetX() {
        return offsetX;
    }

    /**
     * Set horizontal offset.
     * @param offsetX Horizontal offset.
     */
    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    /**
     * Get vertical offset.
     * @return Vertical offset.
     */
    public int getOffsetY() {
        return offsetY;
    }

    /**
     * Set vertical offset.
     * @param offsetY Vertical offset.
     */
    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }
}
