package at.ac.tuwien.ims.towardsthelight.level;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Holds the level's obstacles in an ArrayList. Loads the level when the constructor is called.
 *
 * @author Thomas Koch
 */
public class Level {

    public LevelInfo levelInfo;
    public ArrayList<Obstacle> obstacles;
    public int width, height;
    private Context context;

    /**
     * Calls loadLevel to load the level according to the file name which is stored in levelInfo.
     *
     * @param levelInfo the level to be loaded.
     */
    public Level(Context context, LevelInfo levelInfo) {
        this.context = context;
        this.levelInfo = levelInfo;
        obstacles = new ArrayList<>();
        if (!loadLevel()) {
            Log.e(getClass().getName(), "Couldn't load level!");
        }
    }

    /**
     * Reads the level file. The header of these files contain information about the width and height.
     * A <tt>1</tt> represents an obstacle which is added to the <tt>obstacles</tt> list ordered by
     * x and y coordinates.
     *
     * @return <tt>true</tt> if the level has been loaded successfully.
     */
    private boolean loadLevel() {
        try {
            InputStream inputStream = context.getAssets().open(levelInfo.fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String line;

                // read header
                line = reader.readLine();
                if (line != null) {
                    String[] header = line.split(";");
                    if (header.length == 2) {
                        width = Integer.parseInt(header[0]);
                        height = Integer.parseInt(header[1]);
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }

                // read level
                int y = 0;
                while ((line = reader.readLine()) != null) {
                    for (int x = 0; x < line.length(); x++) {
                        if (line.charAt(x) == '1') {    // obstacle
                            obstacles.add(new Obstacle(x, height - y));
                        }
                    }
                    y++;
                }
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            Log.e(getClass().getName(), "exception", e);
            return false;
        }

        return true;
    }
}
