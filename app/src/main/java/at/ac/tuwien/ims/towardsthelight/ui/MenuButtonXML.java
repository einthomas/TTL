package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import at.ac.tuwien.ims.towardsthelight.R;

public class MenuButtonXML extends ImageButtonXML {

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
