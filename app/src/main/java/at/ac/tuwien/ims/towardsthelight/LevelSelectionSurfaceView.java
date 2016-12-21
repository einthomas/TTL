package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Locale;

import at.ac.tuwien.ims.towardsthelight.level.LevelInfo;
import at.ac.tuwien.ims.towardsthelight.ui.SpriteFont;

/**
 * Draws the level selection screen. For each level either a "normal" or a "diamond" border, the
 * number of the level, the highscore, the best achieved time and the achieved medals (bronze, medal,
 * gold) are displayed.
 * <br />
 * If a touch ACTION_UP event on a level entry is registered, the activity {@link GameActivity} is
 * started. The intent is parameterized by the corresponding {@link LevelInfo} object.
 *
 * @author Thomas Koch
 */
public class LevelSelectionSurfaceView extends TTLSurfaceView {

    /**
     * Holds the {@link LevelInfoPosition#number} of the Level and the top and left position of the
     * level entry.
     */
    private class LevelInfoPosition {

        /**
         * The number of the level.
         */
        public int number;

        /**
         * The top and left position of the level entry.
         */
        public float top, left;

        /**
         * @param number {@link #number}
         * @param top    {@link #top}
         * @param left   {@link #left}
         */
        public LevelInfoPosition(int number, float top, float left) {
            this.number = number;
            this.top = top;
            this.left = left;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private float previousDistanceY = 0.0f;

        @Override
        public boolean onDown(MotionEvent event) {
            fingerUp = false;
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
            yOffset -= distanceY / 17;
            if (Math.signum(distanceY) != Math.signum(previousDistanceY)) {
                previousDistanceY = 0;
            }
            velocity = (distanceY - previousDistanceY) / 17;
            scrollUp = velocity < 0;
            if (scrollUp) {
                velocity = Math.max(velocity, -5);
            } else {
                velocity = Math.min(velocity, 5);
            }
            scrollDeceleration = velocity * 0.05f;
            previousDistanceY = distanceY;

            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
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
            return true;
        }
    }

    private float scrollDeceleration;
    private GestureDetectorCompat gestureDetector;
    private boolean fingerUp = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            fingerUp = true;
        }
        return gestureDetector.onTouchEvent(event);
    }

    /**
     * The number of levels.
     */
    private final int LEVEL_COUNT = 3;

    /**
     * Contains a {@link LevelInfo} object for each level.
     */
    private ArrayList<LevelInfo> levels;

    /**
     * Contains a {@link LevelInfoPosition} object for each level.
     */
    private ArrayList<LevelInfoPosition> levelInfoPositions;

    /**
     * The loaded font which is used to draw the text.
     */
    private SpriteFont uiFont;

    /**
     * The loaded border and diamond border images.
     */
    private Bitmap normalBorder, diamondBorder;

    /**
     * Contains the loaded medal images.
     */
    private Bitmap[] medalBitmaps;

    private float yOffset;
    private float velocity;
    private boolean scrollUp;

    /**
     * Creates a surface view.
     */
    public LevelSelectionSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);

        gestureDetector = new GestureDetectorCompat(getContext(), new GestureListener());

        yOffset = 0.0f;
        levels = new ArrayList<>();
        levelInfoPositions = new ArrayList<>();
        medalBitmaps = new Bitmap[3];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        // load ui font
        uiFont = SpriteFont.hudFont(getResources());

        // load border bitmaps
        normalBorder = BitmapFactory.decodeResource(getResources(), R.drawable.normal_border, options);
        diamondBorder = BitmapFactory.decodeResource(getResources(), R.drawable.diamond_border, options);

        // load medal bitmaps
        medalBitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bronze_medal, options);
        medalBitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.silver_medal, options);
        medalBitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.gold_medal, options);

        loadLevels();
    }

    public void pause() {
        if (gameLoop != null) {
            gameLoop.setRunning(false);
        }
    }

    /**
     * Iterates through the image resources <tt>levelx</tt> and <tt>levelxcollision</tt>, where
     * <tt>x</tt> is <tt>{@link LevelInfo#number} - 1</tt>, queries the score (best time and highscore) from the database and
     * adds a {@link LevelInfo} object containing these variables for each level to the levels
     * list {@link #levels}.
     */
    private void loadLevels() {
        Highscores highscores = new Highscores(getContext().getFilesDir().getAbsolutePath() + "/highscore.db");

        int imageResource;
        int collisionResource;
        for (int i = 1; i <= LEVEL_COUNT; i++) {
            imageResource = getResources().getIdentifier("level" + i, "drawable", getContext().getPackageName());
            collisionResource = getResources().getIdentifier("level" + i + "collision", "drawable", getContext().getPackageName());

            Highscores.Score score = highscores.getHighscore(i);
            if (score == null) {
                score = new Highscores.Score(0, 0);
            }

            levels.add(new LevelInfo(i, score, imageResource, collisionResource));
        }

        highscores.closeConnection();
    }

    @Override
    public synchronized void updateGame(float delta) {
        if (fingerUp && ((!scrollUp && velocity > 0.0f) || (scrollUp && velocity < 0.0f))) {
            yOffset += velocity;
            velocity -= scrollDeceleration;
        }
    }

    /**
     * Iterates through the levels according to {@link #LEVEL_COUNT}. Calculates the top and left
     * position for each level and adds a {@link LevelInfoPosition} object containing these values
     * to {@link #levelInfoPositions}.
     *
     * For each level a border, the achieved medals, the number of the level, the best time and the
     * highscore is drawn. A diamond border is drawn if the highscore is greater than the largest
     * score in the resource file <tt>levelx_medal_points</tt>, where <tt>x</tt> is
     * <tt>{@link LevelInfo#number} - 1</tt>. The resource file also decides which medals are displayed.
     *
     * @param canvas the canvas which is drawn on
     */
    @Override
    public synchronized void drawGame(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.setMatrix(gameMatrix);
        levelInfoPositions.clear();

        float yOffsetTemp = yOffset;
        String text = "";
        int[] textDimensions;
        for (int i = 0; i < LEVEL_COUNT; i++) {
            float borderTop = yOffsetTemp + GAME_HEIGHT - (normalBorder.getHeight() + 2) * (i + 1);
            float borderHeight = normalBorder.getHeight();
            if (borderTop >= -borderHeight && borderTop <= GAME_HEIGHT) {
                float borderLeft = (GAME_WIDTH - normalBorder.getWidth()) / 2.0f;
                float borderWidth = normalBorder.getWidth();

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

    /**
     * Loads {@link #uiFont}, the {@link #normalBorder normal} and {@link #diamondBorder diamond border}
     * images and the {@link #medalBitmaps medal images} and calls {@link #loadLevels()}.
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        super.surfaceCreated(surfaceHolder);

        yOffset = 0.0f;
        startGameLoop(surfaceHolder);
    }

    /**
     * Calls <tt>super.surfaceDestroyed(SurfaceHolder)</tt>.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        super.surfaceDestroyed(surfaceHolder);
    }
}
