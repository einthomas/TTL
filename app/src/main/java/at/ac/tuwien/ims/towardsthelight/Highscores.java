package at.ac.tuwien.ims.towardsthelight;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Felix on 26.10.2016.
 */

public class Highscores {
    public static class Score {
        public int score;
        public int time; // number of milliseconds

        public Score(int time, int score) {
            this.time = time;
            this.score = score;
        }
    }

    SQLiteDatabase database;

    public Highscores(String path) {
        database = SQLiteDatabase.openOrCreateDatabase(path, null);

        database.execSQL(
            "CREATE TABLE IF NOT EXISTS highscore (\n" +
            "    level INTEGER, time INTEGER, score INTEGER\n" +
            ");"
        );
    }

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

    public void putHighscore(int level, Score score) {
        ContentValues record = new ContentValues();
        record.put("level", level);
        record.put("time", score.time);
        record.put("score", score.score);

        database.insert("highscore", null, record);
    }
}
