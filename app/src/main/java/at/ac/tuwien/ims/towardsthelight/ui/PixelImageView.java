package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import at.ac.tuwien.ims.towardsthelight.R;

/**
 * Displays a pixelized image.
 *
 * @author Felix Kugler
 */
public class PixelImageView extends View {

    /**
     * The image to display.
     */
    private Bitmap image;

    /**
     * Transformation of the image.
     */
    private Matrix transformation;

    /**
     * Paint used for drawing.
     */
    private Paint paint = new Paint();

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public PixelImageView(Context context) {
        super(context);
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p>
     * <p>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     * @see View(Context, AttributeSet, int)
     */
    public PixelImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute. This constructor of View allows subclasses to use their
     * own base style when they are inflating. For example, a Button class's
     * constructor would call this version of the super class constructor and
     * supply <code>R.attr.buttonStyle</code> for <var>defStyleAttr</var>; this
     * allows the theme's button style to modify all of the base view attributes
     * (in particular its background) as well as the Button class's attributes.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     * @see View(Context, AttributeSet)
     */
    public PixelImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PixelImageView);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        int imageId = attributes.getResourceId(R.styleable.PixelImageView_imageResource, 0);
        setImage(BitmapFactory.decodeResource(context.getResources(), imageId, options));

        transformation = new Matrix();
    }

    /**
     * This is called during layout when the size of this view has changed. If
     * you were just added to the view hierarchy, you're called with the old
     * values of 0.
     *
     * @param w    Current width of this view.
     * @param h    Current height of this view.
     * @param oldw Old width of this view.
     * @param oldh Old height of this view.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (image != null) {
            getLayoutParams().width = image.getWidth();
            super.onSizeChanged(w, h, oldw, oldh);

            float viewAspectRatio = (float) w / h;
            float imageAspectRatio = (float) image.getWidth() / image.getHeight();

            float scale, offset;
            if (viewAspectRatio < imageAspectRatio) {
                scale = (float) h / image.getHeight();   // fit to height
                offset = (w - image.getWidth() * scale) / 2;
                transformation.setScale(scale, scale);
                transformation.postTranslate(offset, 0);
            } else {
                scale = (float) w / image.getWidth();     // fit to width
                offset = (h - image.getHeight() * scale) / 2;
                transformation.setScale(scale, scale);
                transformation.postTranslate(0, offset);
            }
        }
    }

    /**
     * Determine the size of the view to allow using "wrap_content" in XML.
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(image.getWidth(), image.getHeight());
    }

    /**
     * Implement this to do your drawing.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (image != null) {
            canvas.drawBitmap(image, transformation, paint);
        }
    }

    /**
     * Get the image.
     * @return The image.
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Set the image.
     * @param image The image.
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }
}
