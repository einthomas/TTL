package at.ac.tuwien.ims.towardsthelight.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.HashMap;
import java.util.Map;

/**
 * Describes a font as an image containing all characters.
 * @author Felix Kugler
 */
public class SpriteFont {

    /**
     * Image containing all the characters.
     */
    private Bitmap bitmap;

    /**
     * Maps a Character to a rectangular area on {@link #bitmap}.
     */
    private Map<Character, Rect> glyphMap;

    /**
     * Height of the characters.
     */
    private int height;

    /**
     * Creates a new font from the given image.
     * @param bitmap Image containing all the characters densely packed.
     * @param characters Characters in the order they appear in the bitmap.
     * @param widths Width of each character.
     */
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

    /**
     * Draw text with the font.
     * @param canvas Canvas used to draw.
     * @param paint Paint used to draw.
     * @param text Text to draw.
     * @param x X position of text.
     * @param y Y position of text.
     */
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

    /**
     * Calculates the dimensions (width, height) of "text"
     *
     * @param text the string which dimensions are calculated
     * @return the dimensions of "text" as an integer array. Index 0 contains the width, index 1 the
     *         height.
     * @author Thomas Koch
     */
    public int[] getDimensions(String text) {
        int[] dimensions = new int[2];  // [0] -> width, [1] -> height
        for (int i = 0; i < text.length(); i++) {

            Rect source = glyphMap.get(text.charAt(i));
            if (source != null) {

                dimensions[0] += source.width();
                if (i < text.length() - 1) {
                    dimensions[0] += 1;
                }

                if (source.height() > dimensions[1]) {
                    dimensions[1] = source.height();
                }
            }
        }

        return dimensions;
    }
}
