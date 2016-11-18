package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Locale;

import at.ac.tuwien.ims.towardsthelight.R;

public class InGameUI {

    private float softFps;

    private SpriteFont uiFont;

    public InGameUI(Resources context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        uiFont = new SpriteFont(
            BitmapFactory.decodeResource(context, R.drawable.hud_font, options),
            "0123456789:.♥♡",
            new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 5, 5}
        );

    }

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
