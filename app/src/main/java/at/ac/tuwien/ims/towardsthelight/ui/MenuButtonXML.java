package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import at.ac.tuwien.ims.towardsthelight.R;

/**
 * A menu button intended to be used in XML. Uses the bitmaps for {@link #releasedBitmap} and
 * {@link #pressedBitmap} associated with R.drawable.menu_button and R.drawable.menu_button_pressed.
 *
 * @author Thomas Koch
 */
public class MenuButtonXML extends ImageButtonXML {

    /**
     * Sets {@link #releasedBitmap} to the bitmap associated with R.drawable.menu_button and
     * {@link #releasedBitmap} to the bitmap associated with R.drawable.menu_button_pressed.
     * Also sets the {@link #font} to {@link SpriteFont#mainFont(Resources)} and the {@link #position}
     * according to the size of the bitmaps.
     *
     * @param context The current context.
     * @param attributeSet Th attribute set.
     */
    public MenuButtonXML(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        releasedBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu_button, options);
        pressedBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu_button_pressed, options);

        position = new RectF(x, y, x + releasedBitmap.getWidth(), y + releasedBitmap.getHeight());

        font = SpriteFont.mainFont(context.getResources());
    }
}
