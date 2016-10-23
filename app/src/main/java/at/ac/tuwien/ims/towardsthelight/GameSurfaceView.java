package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by Felix on 22.10.2016.
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    Paint paint;

    public GameSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);

        // TODO: initialize assets

        paint = new Paint();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
