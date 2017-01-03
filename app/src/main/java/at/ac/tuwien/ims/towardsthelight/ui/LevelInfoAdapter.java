package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.Locale;

import at.ac.tuwien.ims.towardsthelight.R;
import at.ac.tuwien.ims.towardsthelight.level.LevelInfo;

public class LevelInfoAdapter extends ArrayAdapter<LevelInfo> {

    private final Context context;

    public LevelInfoAdapter(Context context, int resource, List<LevelInfo> levelInfos) {
        super(context, resource, levelInfos);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LevelInfo levelInfo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.levelinfo_entry, parent, false);
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        // set background
        PixelImageView background = (PixelImageView) convertView.findViewById(R.id.level_info_entry_background);
        int arrayId = getContext().getResources().getIdentifier("level" + (position + 1) + "_medal_points", "array", getContext().getPackageName());
        TypedArray pointsArray = getContext().getResources().obtainTypedArray(arrayId);
        Bitmap backgroundBitmap;
        if (levelInfo.score.score >= pointsArray.getInt(pointsArray.length() - 1, 0)) {
            backgroundBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.diamond_border, options);
        } else {
            backgroundBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.normal_border, options);
        }
        background.setImage(backgroundBitmap);

        // set medals
        if (levelInfo.score.score >= pointsArray.getInt(0, 0)) {
            convertView.findViewById(R.id.level_info_entry_bronzemedal).setVisibility(View.VISIBLE);
            // noinspection ResourceType
            if (levelInfo.score.score >= pointsArray.getInt(1, 0)) {
                convertView.findViewById(R.id.level_info_entry_silvermedal).setVisibility(View.VISIBLE);
                // noinspection ResourceType
                if (levelInfo.score.score >= pointsArray.getInt(2, 0)) {
                    convertView.findViewById(R.id.level_info_entry_goldmedal).setVisibility(View.VISIBLE);
                }
            }
        }
        pointsArray.recycle();

        // set level number
        PixelText levelNumberPixelText = (PixelText) convertView.findViewById(R.id.level_info_entry_levelNumber);
        levelNumberPixelText.text = "" + levelInfo.number;
        if (levelInfo.number < 10) {
            levelNumberPixelText.text = "0" + levelNumberPixelText.text;
        }

        // set time
        PixelText timePixelText = (PixelText) convertView.findViewById(R.id.level_info_entry_time);
        timePixelText.text = levelInfo.score.time / 1000 / 60 + ":" +
                String.format(Locale.US, "%02d", levelInfo.score.time / 1000 % 60) + "." +
                levelInfo.score.time / 100 % 10;
        int[] dimensions = timePixelText.getFont().getDimensions(timePixelText.text);
        timePixelText.setX(background.getImage().getWidth() - 2 - dimensions[0]);

        // set highscore
        PixelText highscorePixelText = (PixelText) convertView.findViewById(R.id.level_info_entry_highscore);

        highscorePixelText.text = "" + levelInfo.score.score;
        dimensions = highscorePixelText.getFont().getDimensions(highscorePixelText.text);
        highscorePixelText.setX(background.getImage().getWidth() - 2 - dimensions[0]);

        return convertView;
    }

    @Override
    public LevelInfo getItem(int position) {
        return super.getItem(super.getCount() - position - 1);
    }
}
