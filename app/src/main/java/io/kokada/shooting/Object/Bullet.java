package io.kokada.shooting.Object;

/**
 * Created by kokada on 16/03/25.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 自機弾
 */
public class Bullet extends BaseObject {
    //弾の速度
    private static final float MOVE_WEIGHT = 3.0f;

    private final Paint paint = new Paint();

    private static final float SIZE = 15f;

    public final float alignX;

    public Bullet(Rect doroidRect, float alignXValue) {
        //発射位置は自機の真ん中から
        yPosotion = doroidRect.centerY();
        xPosition = doroidRect.centerX();
        alignX = alignXValue;

        paint.setColor(Color.RED);
    }

    @Override
    public void move() {
        yPosotion -= 1 * MOVE_WEIGHT;
        xPosition += alignX * MOVE_WEIGHT;
    }

    @Override
    public boolean isHit(BaseObject object) {
        if (object.getType() != Type.Missile) {
            return false;
        }

        //ミサイルとの距離が自身のサイズ未満だったら当たったと判定
        return (calcDistance(this, object) < SIZE);
    }

    @Override
    public Type getType() {
        return Type.Bullet;
    }



    @Override
    public void draw(Canvas canvas) {
        if (state != STATE_NORMAL) {
            return;
        }
        canvas.drawCircle(xPosition, yPosotion, SIZE, paint);
    }
}
