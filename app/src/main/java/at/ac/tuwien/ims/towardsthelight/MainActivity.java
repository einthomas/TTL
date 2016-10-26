package at.ac.tuwien.ims.towardsthelight;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LOW_PROFILE |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
        setContentView(R.layout.activity_main);

        Log.d(getClass().getName(), "onCreate: " + getFilesDir().getAbsolutePath());
        Highscores highscores = new Highscores(getFilesDir().getAbsolutePath() + "/highscore.db");

        highscores.putHighscore(1, new Highscores.Score(180000, 9999));
        Highscores.Score score = highscores.getHighscore(1);
        Log.d(getClass().getName(), "onCreate: " + score.score);
    }
}
