package io.kokada.shooting.Object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import io.kokada.shooting.Object.BaseObject;

/**
 * Created by kokada on 16/03/24.
 */

/**
 * 自機クラス
 */
public class Doroid extends BaseObject {

    private final Paint paint = new Paint();

    public final Bitmap bitmap;
    public final Rect rect;

    /**
     * 自機の表示
     * @param bitmap
     * @param width
     * @param height
     */
    public Doroid(Bitmap bitmap, int width, int height) {

        this.bitmap = bitmap;

        //画面の中央に位置させる
        int left = (width - bitmap.getWidth()) / 2;
        int top = height - bitmap.getHeight();
        int right = left + bitmap.getWidth();
        int bottom = top + bitmap.getHeight();
        rect = new Rect(left, top, right, bottom);

        //描画位置の中心にxyPositionを設定
        yPosotion = rect.centerY();
        xPosition = rect.centerX();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, rect.left, rect.top, paint);
    }

    @Override
    public void move() {

    }
}