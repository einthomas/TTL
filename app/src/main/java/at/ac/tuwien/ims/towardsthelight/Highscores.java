package at.ac.tuwien.ims.towardsthelight;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.tech.MifareUltralight;

import java.io.Serializable;

/**
 * Handles reading and writing scores from and to the database.
 * @author Felix Kugler
 */
public class Highscores {

    /**
     * Stores score and time for a level.
     */
    public static class Score implements Serializable {

        /**
         * Score achieved in a level.
         */
        public int score;

        /**
         * Time spent in a level in milliseconds.
         */
        public int time;

        /**
         * Creates a new score.
         *
         * @param time Time spent in a level.
         * @param score Score achieved in a level.
         */
        public Score(int time, int score) {
            this.time = time;
            this.score = score;
        }
    }

    /**
     * Database used for storing scores.
     */
    private SQLiteDatabase database;

    /**
     * Create a new object for reading and writing scores.
     * This will create a new database if it doesn't already exist.
     *
     * @param path Path to the SQLite database file.
     */
    public Highscores(String path) {
        database = SQLiteDatabase.openOrCreateDatabase(path, null);

        database.execSQL(
            "CREATE TABLE IF NOT EXISTS highscore (\n" +
            "    level INTEGER PRIMARY KEY, time INTEGER, score INTEGER\n" +
            ");"
        );
    }

    /**
     * Access the score for a given level.
     *
     * @param level The level index.
     * @return The score for that level.
     */
    public Score getHighscore(int level) {
        Cursor cursor = database.rawQuery(
            "SELECT time, score FROM highscore WHERE level = ?;", new String[] {
                Integer.toString(level)
            }
        );

        Score score = null;
        if (cursor.moveToNext()) {
            score = new Score(cursor.getInt(0), cursor.getInt(1));
        }

        cursor.close();

        return score;
    }

    /**
     * Store a new score.
     *
     * @param level The level index on which the score was achieved.
     * @param score The score that was achieved on the level.
     */
    public void putHighscore(int level, Score score) {
        if (getHighscore(level).score < score.score) {
            ContentValues record = new ContentValues();
            record.put("level", level);
            record.put("time", score.time);
            record.put("score", score.score);

            database.replace("highscore", null, record);
        }
    }

    public void closeConnection() {
        database.close();
    }
}
