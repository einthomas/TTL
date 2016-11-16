package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Locale;

import at.ac.tuwien.ims.towardsthelight.level.LevelInfo;
import at.ac.tuwien.ims.towardsthelight.ui.SpriteFont;

/**
 * @author Thomas Koch
 */
public class LevelSelectionSurfaceView extends TTLSurfaceView {

    private class LevelInfoPosition {

        public int number;
        public float top, left;

        public LevelInfoPosition(int number, float top, float left) {
            this.number = number;
            this.top = top;
            this.left = left;
        }
    }

    private final int LEVEL_COUNT = 3;

    private ArrayList<LevelInfo> levels;
    private ArrayList<LevelInfoPosition> levelInfoPositions;
    private SpriteFont uiFont;
    private Bitmap normalBorder, diamondBorder;
    private Bitmap[] medalBitmaps;

    public LevelSelectionSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);

        levels = new ArrayList<>();
        levelInfoPositions = new ArrayList<>();
        medalBitmaps = new Bitmap[3];
    }

    /**
     * Iterates through the level resource images (levelx and levelxcollision, where top is a number),
     * queries the score (best time and highscore) from the database and adds a LevelInfo object
     * for each level containing these variables to the levels ArrayList.
     */
    private void loadLevels() {
        Highscores highscores = new Highscores(getContext().getFilesDir().getAbsolutePath() + "/highscore.db");

        int imageResource;
        int collisionResource;
        for (int i = 1; i <= LEVEL_COUNT; i++) {
            //imageResource = getResources().getIdentifier("level" + i, "drawable", getContext().getPackageName());
            collisionResource = getResources().getIdentifier("level" + i + "collision", "drawable", getContext().getPackageName());
            imageResource = collisionResource;

            Highscores.Score score = highscores.getHighscore(i);
            if (score == null) {
                score = new Highscores.Score(0, 0);
            }

            levels.add(new LevelInfo(i, score, imageResource, collisionResource));
        }
    }

    private void drawLevelInfos(Canvas canvas) {
        levelInfoPositions.clear();

        canvas.setMatrix(gameMatrix);

        String text = "";
        int[] textDimensions;
        for (int i = 0; i < LEVEL_COUNT; i++) {
            float borderTop = GAME_HEIGHT - (normalBorder.getHeight() + 2) * (i + 1);
            if (borderTop >= 0 && borderTop <= GAME_HEIGHT) {
                float borderLeft = (GAME_WIDTH - normalBorder.getWidth()) / 2.0f;
                float borderWidth = normalBorder.getWidth();
                float borderHeight = normalBorder.getHeight();

                levelInfoPositions.add(new LevelInfoPosition(levels.get(i).number, borderTop, borderLeft));


                // draw border
                int arrayId = getResources().getIdentifier("level" + (i + 1) + "_medal_points", "array", getContext().getPackageName());
                TypedArray pointsArray = getResources().obtainTypedArray(arrayId);
                if (levels.get(i).score.score >= pointsArray.getInt(pointsArray.length() - 1, 0)) {
                    canvas.drawBitmap(diamondBorder, borderLeft, borderTop, null);
                } else {
                    canvas.drawBitmap(normalBorder, borderLeft, borderTop, null);
                }


                // draw medals
                int medalX = 14;
                for (int k = 0; k < pointsArray.length() - 1; k++) {
                    if (levels.get(i).score.score >= pointsArray.getInt(k, 0)) {
                        canvas.drawBitmap(medalBitmaps[k], medalX, borderTop + 2, null);
                        medalX += medalBitmaps[k].getWidth() + 1;
                    } else {
                        break;
                    }
                }
                pointsArray.recycle();


                // draw number
                text = "" + levels.get(i).number;
                if (levels.get(i).number < 10) {
                    text = "0 " + text;
                }
                uiFont.drawText(canvas, null, text, borderLeft + 2, borderTop + 2);

                // draw best time
                text = levels.get(i).score.time / 1000 / 60 + ":" +
                        String.format(Locale.US, "%02d", levels.get(i).score.time / 1000 % 60) + "." +
                        levels.get(i).score.time / 100 % 10;
                textDimensions = uiFont.getDimensions(text);
                uiFont.drawText(canvas, null, text, borderLeft + borderWidth - 2 - textDimensions[0], borderTop + 2);

                // draw highscore
                text = "" + levels.get(i).score.score;
                textDimensions = uiFont.getDimensions(text);
                uiFont.drawText(canvas, null, text, borderLeft + borderWidth - 2 - textDimensions[0], borderTop + borderHeight - 2 - textDimensions[1]);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float[] touchPosition = { event.getX(), event.getY() };
            gameMatrixInverse.mapVectors(touchPosition);

            for (int i = 0; i < levelInfoPositions.size(); i++) {
                if (touchPosition[0] >= levelInfoPositions.get(i).left
                        && touchPosition[0] <= levelInfoPositions.get(i).left + normalBorder.getWidth()
                        && touchPosition[1] >= levelInfoPositions.get(i).top
                        && touchPosition[1] <= levelInfoPositions.get(i).top + normalBorder.getHeight()) {
                    Intent intent = new Intent(getContext(), GameActivity.class);
                    intent.putExtra("LevelInfo", levels.get(levelInfoPositions.get(i).number - 1));
                    getContext().startActivity(intent);
                    break;
                }
            }
        }

        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        super.surfaceCreated(surfaceHolder);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;


        // load ui font
        uiFont = new SpriteFont(
                BitmapFactory.decodeResource(getResources(), R.drawable.hud_font, options),
                "0123456789:.♥♡",
                new int[] {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 5, 5}
        );


        // load border bitmaps
        normalBorder = BitmapFactory.decodeResource(getResources(), R.drawable.normal_border, options);
        diamondBorder = BitmapFactory.decodeResource(getResources(), R.drawable.diamond_border, options);


        // load medal bitmaps
        medalBitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bronze_medal, options);
        medalBitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.silver_medal, options);
        medalBitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.gold_medal, options);


        loadLevels();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        super.surfaceChanged(surfaceHolder, format, width, height);

        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas(null);
            if (canvas != null) {
                drawLevelInfos(canvas);
            }
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        super.surfaceDestroyed(surfaceHolder);
    }
}
