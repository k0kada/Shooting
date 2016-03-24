package io.kokada.shooting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by kokada on 16/03/24.
 */
public class GameView extends View {
    private Doroid doroid;

    public GameView(Context context) {
        super(context);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        //自機の描画
        if (doroid == null) {
            Bitmap doridBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.droid);
            doroid = new Doroid(doridBitmap, width, height);
        }
        doroid.draw(canvas);
    }
}
