package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Locale;

import at.ac.tuwien.ims.towardsthelight.R;

/**
 * Draws score, lives and FPS.
 * @author Felix Kugler
 */
public class InGameUI {

    /**
     * FPS averaged over multiple frames.
     */
    private float softFps;

    /**
     * Font used for UI.
     */
    private SpriteFont uiFont;

    /**
     * Constructs a new ui.
     * @param context Resources used for loading resources.
     */
    public InGameUI(Resources context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        uiFont = SpriteFont.hudFont(context);
    }

    /**
     * Draws the UI.
     * @param canvas Canvas used for drawing.
     * @param fps FPS the game is running at.
     * @param score The Players score.
     * @param time Time since level begin in milliseconds.
     * @param lives Lives the player has left.
     */
    public void draw(Canvas canvas, int fps, int score, int time, int lives) {
        Paint paint = new Paint();

        String health = "";
        int hearts = 0;
        for (; hearts < lives; hearts++) {
            health += '♥';
        }
        for (; hearts < 3; hearts++) {
            health += '♡';
        }
        uiFont.drawText(canvas, paint, health, 1, 106);

        uiFont.drawText(canvas, paint, score + "", 1, 99);
        uiFont.drawText(canvas, paint,
            time / 1000 / 60 + ":" +
            String.format(Locale.US, "%02d", time / 1000 % 60) + "." +
            time / 100 % 10,
            1, 92
        );

        // display softFps
        uiFont.drawText(canvas, paint, Math.round(this.softFps) + "", 1, 85);

        this.softFps = (this.softFps * 9 + fps) / 10;
    }
}
