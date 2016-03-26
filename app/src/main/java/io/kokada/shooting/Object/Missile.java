package io.kokada.shooting.Object;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import io.kokada.shooting.Object.BaseObject;

/**
 * Created by kokada on 16/03/24.
 */

/**
 * ミサイル(敵)クラス
 */
public class Missile extends BaseObject {

    private static final float MOVE_WIGHT = 3.0f;

    private static final float SIZE = 10f;

    private final Paint paint = new Paint();

    //ミサイルの傾き
    public final float alignX;

    public Missile(int fromX, float alignX) {
        yPosotion = 0;
        xPosition = fromX;
        this.alignX = alignX;

        paint.setColor(Color.BLUE);
    }

    @Override
    public void move() {
        yPosotion += 1 * MOVE_WIGHT;
        xPosition += alignX * MOVE_WIGHT;
    }

    @Override
    public boolean isHit(BaseObject object) {
        if (object.getType() == Type.Missile) {
            return false;
        }

        //オブジェクトがミサイル以外での距離がサイズ未満だったら当たったと判定
        return (calcDistance(this, object) < SIZE);
    }

    @Override
    public Type getType() {
        return Type.Missile;
    }

    @Override
    public void draw(Canvas canvas) {
        if (state != STATE_NORMAL) {
            return;
        }
        canvas.drawCircle(xPosition, yPosotion, SIZE, paint);
    }
}
