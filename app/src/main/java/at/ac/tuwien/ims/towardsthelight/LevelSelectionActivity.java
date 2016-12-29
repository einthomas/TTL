package at.ac.tuwien.ims.towardsthelight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import at.ac.tuwien.ims.towardsthelight.level.LevelInfo;
import at.ac.tuwien.ims.towardsthelight.ui.LevelInfoAdapter;
import at.ac.tuwien.ims.towardsthelight.ui.PixelImageView;

/**
 * Fullscreen activity for level selection. <tt>activity_level_selection</tt> is the corresponding
 * layout.
 *
 * @author Thomas Koch
 */
public class LevelSelectionActivity extends AppCompatActivity {

    /**
     * The number of levels.
     */
    private final int LEVEL_COUNT = 3;

    /**
     * Contains a {@link LevelInfo} object for each level.
     */
    private ArrayList<LevelInfo> levels;

    /**
     * Sets the activity to fullscreen and sets the content view.
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
        setContentView(R.layout.activity_level_selection);

        levels = new ArrayList<>();
        loadLevels();

        ListView listView = ((ListView) findViewById(R.id.activity_level_selection_listview));
        LevelInfoAdapter levelInfoAdapter = new LevelInfoAdapter(this, 0, levels);
        listView.setAdapter(levelInfoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("LevelInfo", levels.get(position));
                getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

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
     * Iterates through the image resources <tt>levelx</tt> and <tt>levelxcollision</tt>, where
     * <tt>x</tt> is <tt>{@link LevelInfo#number} - 1</tt>, queries the score (best time and highscore) from the database and
     * adds a {@link LevelInfo} object containing these variables for each level to the levels
     * list {@link #levels}.
     */
    private void loadLevels() {
        Highscores highscores = new Highscores(getFilesDir().getAbsolutePath() + "/highscore.db");

        int imageResource;
        int collisionResource;
        for (int i = 1; i <= LEVEL_COUNT; i++) {
            imageResource = getResources().getIdentifier("level" + i, "drawable", getPackageName());
            collisionResource = getResources().getIdentifier("level" + i + "collision", "drawable", getPackageName());

            Highscores.Score score = highscores.getHighscore(i);
            if (score == null) {
                score = new Highscores.Score(0, 0);
            }

            levels.add(new LevelInfo(i, score, imageResource, collisionResource));
        }

        highscores.closeConnection();
    }
}
