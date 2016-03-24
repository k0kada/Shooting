package io.kokada.shooting.Object;

import android.graphics.Canvas;

/**
 * Created by kokada on 16/03/24.
 */

//抽象クラス
public abstract class BaseObject {

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
        if (yPosotion < 0 || xPosition < 0 || yPosotion > height || xPosition > width) {
            return false;
        }
        return true;
    }

    public abstract void move();
}
