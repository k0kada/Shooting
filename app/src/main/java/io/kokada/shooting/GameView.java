package io.kokada.shooting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.kokada.shooting.Object.BaseObject;
import io.kokada.shooting.Object.Doroid;
import io.kokada.shooting.Object.Missile;

/**
 * Created by kokada on 16/03/24.
 */
public class GameView extends View {

    //ミサイル発射の間隔
    private static final int MISSILE_LAUNCH_WEIGHT = 50;

    private Doroid doroid;
    private final List<BaseObject> missileList = new ArrayList<>();

    private final Random rand = new Random(System.currentTimeMillis());

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
            //自機画像のリサイズ
            Bitmap resizeDoridBitmap = Bitmap.createScaledBitmap(doridBitmap, 150, 150, false);
            doroid = new Doroid(resizeDoridBitmap, width, height);
        }

        //ランダムで0が出たらミサイル追加
        if (rand.nextInt(MISSILE_LAUNCH_WEIGHT) == 0) {
            Missile missile = launchMissile(width, height);
            missileList.add(missile);
        }
        drawObjectList(canvas, missileList, width, height);

        doroid.draw(canvas);

        invalidate();
    }

    /**
     * オブジェクト描画
     * @param canvas
     * @param objectList
     * @param width
     * @param height
     */
    private static void drawObjectList(Canvas canvas, List<BaseObject> objectList, int width, int height) {
        for (int i = 0; i < objectList.size(); i++) {
            BaseObject object = objectList.get(i);
            if (object.isAvailable(width, height)) {
                object.move();
                object.draw(canvas);
            } else {
                //画面外に行ったらオブジェクトをリストから削除
                objectList.remove(object);
                i--;
            }
        }
    }

    /**
     *ミサイルの発射位置
     * @param width
     * @param height
     * @return
     */
    private Missile launchMissile(int width, int height) {
        int fromX = rand.nextInt(width);
        int toX = rand.nextInt(width);

        //画面サイズの比率から傾きを調整
        float alignX = (toX - fromX) / (float) height;
        return new Missile(fromX, alignX);
    }
}
