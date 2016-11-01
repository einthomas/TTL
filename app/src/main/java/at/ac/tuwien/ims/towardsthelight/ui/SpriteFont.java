package at.ac.tuwien.ims.towardsthelight.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Felix on 01.11.2016.
 */

public class SpriteFont {

    private Bitmap bitmap;
    private Map<Character, Rect> glyphMap;

    private int height;

    public SpriteFont(Bitmap bitmap, String characters, int[] widths) {
        this.bitmap = bitmap;
        height = bitmap.getHeight();

        glyphMap = new HashMap<>();

        int x = 0;
        for (int i = 0; i < characters.length(); i++) {
            Rect source = new Rect(x, 0, x + widths[i], height);
            glyphMap.put(characters.charAt(i), source);
            x = source.right;
        }
    }

    public void drawText(Canvas canvas, Paint paint, String text, float x, float y) {
        RectF destination = new RectF(x, y, 0, y + height);

        for (int i = 0; i < text.length(); i++) {

            Rect source = glyphMap.get(text.charAt(i));
            if (source != null) {

                destination.right = destination.left + source.width();
                canvas.drawBitmap(bitmap, source, destination, paint);
                destination.left += source.width() + 1;
            }
        }
    }
}
