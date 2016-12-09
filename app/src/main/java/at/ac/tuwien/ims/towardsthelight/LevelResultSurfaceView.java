package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.Locale;
import java.util.Random;

import at.ac.tuwien.ims.towardsthelight.ui.SpriteFont;

public class LevelResultSurfaceView extends TTLSurfaceView {

    private Bitmap[] medalBitmaps;

    private Bitmap background;

    /**
     * The loaded font which is used to draw the text.
     */
    private SpriteFont uiFont, mainFont;

    private Sprite sparkleSprite;

    public int levelScore;
    public int levelTime;
    public int levelNumber;

    private Random random;

    /**
     * Creates a surface view.
     */
    public LevelResultSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);

        medalBitmaps = new Bitmap[3];
        random = new Random();
    }

    @Override
    public synchronized void updateGame(float delta) {
        sparkleSprite.update(delta);
    }

    @Override
    public synchronized void drawGame(Canvas canvas) {
        levelScore = 9999;  // TODO: remove

        canvas.setMatrix(gameMatrix);
        String text;
        int[] textDimensions;


        // draw background
        canvas.drawBitmap(background, 0, 0, null);


        // draw title
        text = "PASSED ";
        if (levelNumber < 10) {
            text += "0";
        }
        text += levelNumber;
        textDimensions = mainFont.getDimensions(text);
        int titleTop = 10;
        int titleBottom = titleTop + textDimensions[1];
        mainFont.drawText(canvas, null, text, GAME_WIDTH / 2 - textDimensions[0] / 2, titleTop);


        // draw medals
        int medalLeft = Math.round(GAME_WIDTH / 2 - medalBitmaps[0].getWidth() * 1.5f) - 3;
        int medalTop = titleBottom + 5;
        int medalBottom = medalTop + medalBitmaps[0].getHeight();
        int arrayId = getResources().getIdentifier("level" + (levelNumber) + "_medal_points", "array", getContext().getPackageName());
        TypedArray pointsArray = getResources().obtainTypedArray(arrayId);
        int randomMedal = random.nextInt(medalBitmaps.length);
        for (int k = 0; k < pointsArray.length() - 1; k++) {
            if (levelScore >= pointsArray.getInt(k, 0)) {
                canvas.drawBitmap(medalBitmaps[k], medalLeft, medalTop, null);
                medalLeft += medalBitmaps[k].getWidth() + 3;
                if (k == randomMedal && sparkleSprite.done && random.nextInt(100) > 97) {
                    sparkleSprite.setPosition(
                        medalLeft + random.nextInt(medalBitmaps[k].getWidth()) - medalBitmaps[k].getWidth() - sparkleSprite.frameWidth / 2,
                        medalTop + random.nextInt(medalBitmaps[k].getHeight()) - sparkleSprite.frameHeight / 2
                    );
                    sparkleSprite.reset();
                }
            } else {
                break;
            }
        }
        sparkleSprite.draw(canvas);
        pointsArray.recycle();


        // draw score
        int scoreTop = medalBottom + 5;
        text = "" + levelScore;
        textDimensions = uiFont.getDimensions(text);
        int scoreBottom = scoreTop + textDimensions[1];
        uiFont.drawText(canvas, null, text, GAME_WIDTH / 2 - textDimensions[0] / 2, medalTop + medalBitmaps[0].getHeight() + 6);


        // draw time
        text = levelTime / 1000 / 60 + ":"
                + String.format(Locale.US, "%02d", levelTime / 1000 % 60) + "."
                + levelTime / 100 % 10;
        textDimensions = uiFont.getDimensions(text);
        uiFont.drawText(canvas, null, text, GAME_WIDTH / 2 - textDimensions[0] / 2, scoreBottom + 5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        super.surfaceCreated(surfaceHolder);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;


        // load ui font
        uiFont = SpriteFont.hudFont(getResources());
        mainFont = SpriteFont.mainFont(getResources());


        // load medal bitmaps
        medalBitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bronze_medal_big, options);
        medalBitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.silver_medal_big, options);
        medalBitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.gold_medal_big, options);


        // load background bitmap
        background = BitmapFactory.decodeResource(getResources(), R.drawable.level_result_background, options);


        // load sparkle sprite
        Bitmap sparkleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sparkles, options);
        sparkleSprite = new Sprite(sparkleBitmap, 10, 10, 7, 7, 3, 0.20f);


        startGameLoop(surfaceHolder);
    }
}
