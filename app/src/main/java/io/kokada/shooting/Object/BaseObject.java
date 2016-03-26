package io.kokada.shooting.Object;

import android.graphics.Canvas;

/**
 * Created by kokada on 16/03/24.
 */

//抽象クラス
public abstract class BaseObject {
    //当たり判定の判断定数
    static final int STATE_NORMAL = -1;
    static final int STATE_DESTROYED = 0;

    int state = STATE_NORMAL;

    //オブジェクトの列挙型
    enum Type {
        Doroid,
        Bullet,
        Missile,
    }

    public abstract Type getType();

    float yPosotion;
    float xPosition;

    public abstract void draw(Canvas canvas);

    /**
     *　オブジェクトが画面の外にはみ出しているかを判定
     * @param width
     * @param height
     * @return
     */
    public boolean isAvailable(int width, int height) {
        //オブジェクトが画面の外にはみ出していたら消す
        if (yPosotion < 0 || xPosition < 0 || yPosotion > height || xPosition > width) {
            return false;
        }

        //当たったら消す
        if (state == STATE_DESTROYED) {
            return false;
        }

        return true;
    }

    public abstract void move();

    public abstract boolean isHit(BaseObject object);

    public void hit() {
        state = STATE_DESTROYED;
    }

    /**
     * 2つのオブジェクトの距離を返す
     * @param obj1
     * @param obj2
     * @return
     */
    static double calcDistance(BaseObject obj1, BaseObject obj2) {
        float distX = obj1.xPosition - obj2.xPosition;
        float distY = obj1.yPosotion - obj2.yPosotion;
        //三平方の定理を使い、オブジェクトの距離を計算させる
        return Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
    }
}
