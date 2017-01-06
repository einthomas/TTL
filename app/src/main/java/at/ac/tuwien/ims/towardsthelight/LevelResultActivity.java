package at.ac.tuwien.ims.towardsthelight;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

import java.util.Locale;

import at.ac.tuwien.ims.towardsthelight.ui.PixelText;

/**
 * Fullscreen activity displayed after finishing a level. <tt>activity_level_result</tt> is the
 * corresponding layout.
 *
 * @author Thomas Koch
 */
public class LevelResultActivity extends AppCompatActivity {

    /**
     * Sets the activity to fullscreen and sets the content view. Sets the visibility of the medals
     * according to the points specified in the resource file <tt>levelx_medal_points</tt>, where
     * <tt>x</tt> is the number of the level.
     * Writes the new highscore to the database if the score is higher.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getSupportActionBar().hide();

        int levelScore = getIntent().getIntExtra("LevelScore", 0);
        int levelTime = getIntent().getIntExtra("LevelTime", 0);
        int levelNumber = getIntent().getIntExtra("LevelNumber", 0);

        // add bonus points for time:
        levelScore += 10 * 60 * 1000 / levelTime;


        int arrayId = getResources().getIdentifier("level" + levelNumber + "_medal_points", "array", getPackageName());
        TypedArray pointsArray = getResources().obtainTypedArray(arrayId);

        // set medals
        if (levelScore >= pointsArray.getInt(0, 0)) {
            findViewById(R.id.level_result_bronzemedal).setVisibility(View.VISIBLE);
            // noinspection ResourceType
            if (levelScore >= pointsArray.getInt(1, 0)) {
                findViewById(R.id.level_result_silvermedal).setVisibility(View.VISIBLE);
                // noinspection ResourceType
                if (levelScore >= pointsArray.getInt(2, 0)) {
                    findViewById(R.id.level_result_goldmedal).setVisibility(View.VISIBLE);
                }
            }
        }
        pointsArray.recycle();

        setContentView(R.layout.activity_level_result);

        if (levelNumber < 10) {
            ((PixelText)findViewById(R.id.level_result_passed)).text += " 0" + levelNumber;
        } else {
            ((PixelText)findViewById(R.id.level_result_passed)).text += " " + levelNumber;
        }

        ((PixelText)findViewById(R.id.level_result_highscore)).text += "" + levelScore;

        String time = levelTime / 1000 / 60 + ":"
                + String.format(Locale.US, "%02d", levelTime / 1000 % 60) + "."
                + levelTime / 100 % 10;
        ((PixelText)findViewById(R.id.level_result_time)).text = time;

        Highscores highscores = new Highscores(getFilesDir().getAbsolutePath() + "/highscore.db");
        Highscores.Score score = new Highscores.Score(levelTime, levelScore);
        highscores.putHighscore(levelNumber, score);
        highscores.closeConnection();
    }

    @Override
    protected void onStart() {
        super.onStart();

        findViewById(R.id.level_result_passed).startAnimation(AnimationUtils.loadAnimation(this, R.anim.passed));
    }

    /**
     * Makes sure the activity is fullscreen (again).
     */
    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getSupportActionBar().hide();
    }

    /**
     * Goes back to {@link LevelSelectionActivity}.
     *
     * @param keyCode The key code of the event.
     * @param event The touch event to react to.
     * @return Whether the event was handled.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, LevelSelectionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
