package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import at.ac.tuwien.ims.towardsthelight.R;

/**
 * Created by Felix on 26.12.2016.
 */

public class PixelLayout extends RelativeLayout {

    private int virtualWidth, virtualHeight;

    /**
     * Work-around because Android clips negative margins or translations
     */
    private int offsetX, offsetY;

    public PixelLayout(Context context) {
        super(context);
    }

    public PixelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PixelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PixelLayout);
        setVirtualWidth(attributes.getInt(R.styleable.PixelLayout_virtualWidth, 1));
        setVirtualHeight(attributes.getInt(R.styleable.PixelLayout_virtualHeight, 1));
        setOffsetX(attributes.getInt(R.styleable.PixelLayout_offsetX, 0));
        setOffsetY(attributes.getInt(R.styleable.PixelLayout_offsetY, 0));
    }

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

    public int getVirtualWidth() {
        return virtualWidth;
    }

    public void setVirtualWidth(int virtualWidth) {
        this.virtualWidth = virtualWidth;
        invalidate();
    }

    public int getVirtualHeight() {
        return virtualHeight;
    }

    public void setVirtualHeight(int virtualHeight) {
        this.virtualHeight = virtualHeight;
        invalidate();
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }
}
