package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.ims.towardsthelight.R;

/**
 * Describes a font as an image containing all characters.
 * @author Felix Kugler
 * @author Thomas Koch
 */
public class SpriteFont {

    public enum FontType {
        mainFont(0), hudFont(1);
        int id;

        FontType(int id) {
            this.id = id;
        }

        static FontType getFromId(int id) {
            for (FontType fontType : values()) {
                if (fontType.id == id) {
                    return fontType;
                }
            }
            return null;
        }
    }

    /**
     * Load main font
     * @param context Context to load from
     * @return Main SpriteFont
     */
    public static SpriteFont mainFont(Resources context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        return new SpriteFont(
            BitmapFactory.decodeResource(context, R.drawable.main_font, options),
            "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ! 0123456789",
            new int[]{
                7, 7, 7, 7, 7, 7, 7, 7, 5,
                7, 7, 7, 7, 7, 7, 7, 7, 7,
                7, 7, 7, 7, 7, 7, 7, 7, 7,
                7, 7, 3, 4, 7, 5, 7, 7, 7,
                7, 7, 7, 7, 7
            }
        );
    }

    /**
     * Load font for hud
     * @param context Context to load from
     * @return SpriteFont for hud
     */
    public static SpriteFont hudFont(Resources context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        return new SpriteFont(
                BitmapFactory.decodeResource(context, R.drawable.hud_font, options),
                "0123456789:.♥♡",
                new int[]{5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 3, 3, 7, 7}
        );
    }

    /**
     * Load font for countdown
     * @param context Context to load from
     * @return SpriteFont for countdown
     */
    public static SpriteFont countdownFont(Resources context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        return new SpriteFont(
                BitmapFactory.decodeResource(context, R.drawable.countdown, options),
                "321GO!",
                new int[]{11, 11, 8, 13, 13, 5}
        );
    }

    /**
     * Returns the {@link SpriteFont} specified by spriteFont.
     *
     * @param spriteFont The {@link FontType}.
     * @param context The current context.
     * @return The {@link SpriteFont} according to spriteFont.
     */
    public static SpriteFont getFont(FontType spriteFont, Resources context) {
        if (spriteFont == FontType.mainFont) {
            return mainFont(context);
        } else if (spriteFont == FontType.hudFont) {
            return hudFont(context);
        }

        return null;
    }

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
     * <br />Author: Felix Kugler
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
     * <br />Author: Felix Kugler
     * @param canvas Canvas used to draw.
     * @param paint Paint used to draw.
     * @param text Text to draw.
     * @param x X position of text.
     * @param y Y position of text.
     */
    public void drawText(Canvas canvas, Paint paint, String text, float x, float y) {
        if (text == null) {
            return;
        }

        RectF destination = new RectF(x - 1, y - 1, 0, y + height - 1); // -1 to remove outline

        for (int i = 0; i < text.length(); i++) {

            Rect source = glyphMap.get(text.charAt(i));
            if (source != null) {

                destination.right = destination.left + source.width();
                canvas.drawBitmap(bitmap, source, destination, paint);
                destination.left += source.width() - 1;
            }
        }
    }

    /**
     * Draw text with this font centered at given position.
     * <br />Author: Felix Kugler
     * @param canvas Canvas used to draw.
     * @param paint Paint used to draw.
     * @param text Text to draw.
     * @param x X position of text.
     * @param y Y position of text.
     */
    public void drawCentered(Canvas canvas, Paint paint, String text, float x, float y) {
        int[] dimension = getDimensions(text);
        drawText(canvas, paint, text, Math.round(x - dimension[0] * 0.5), Math.round(y - dimension[1] * 0.5));
    }

    /**
     * Calculates the dimensions (width, height) of <tt>text</tt>.
     * <br />Author: Thomas Koch
     * @param text the string which dimensions are calculated
     * @return the dimensions of <tt>text</tt> as an integer array. Index 0 contains the width,
     *         index 1 the height.
     */
    public int[] getDimensions(String text) {
        if (text == null) {
            return new int[] {0, 0};
        }

        int[] dimensions = new int[] {-1, height - 2};  // [0] -> width, [1] -> height
        for (int i = 0; i < text.length(); i++) {

            Rect source = glyphMap.get(text.charAt(i));
            if (source != null) {

                dimensions[0] += source.width();
                if (i < text.length() - 1) {
                    dimensions[0] -= 1; // remove outline
                }
            }
        }

        return dimensions;
    }
}
