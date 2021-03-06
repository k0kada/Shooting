package io.kokada.shooting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import io.kokada.shooting.Object.BaseObject;
import io.kokada.shooting.Object.Bullet;
import io.kokada.shooting.Object.Doroid;
import io.kokada.shooting.Object.Missile;

/**
 * Created by kokada on 16/03/24.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static final long DRAW_INTERVAL = 1000 / 60;

    //ミサイル発射の間隔
    private static final int MISSILE_LAUNCH_WEIGHT = 50;

    private static final float SCORE_TEXT_SIZE = 60.f;

    private Doroid doroid;
    //敵オブジェクト配列
    private final List<BaseObject> missileList = new ArrayList<>();
    //自機オブジェクト配列
    private final List<BaseObject> bulletList = new ArrayList<>();

    private final Random rand = new Random(System.currentTimeMillis());

    private long score;
    private final Paint panitScore = new Paint();

    private DrawThread drawThread;

    private class DrawThread extends Thread {
        private final AtomicBoolean isFinished = new AtomicBoolean();

        public void finish() {
            isFinished.set(true);
        }

        @Override
        public void run() {
            SurfaceHolder holder = getHolder();

            while (!isFinished.get()) {
                if (holder.isCreating()) {
                    continue;
                }

                //描画用のCanvasオブジェクト取得
                Canvas canvas = holder.lockCanvas();
                if (canvas == null) {
                    continue;
                }

                //画面描画
                drawGame(canvas);
                //画面反映
                holder.unlockCanvasAndPost(canvas);

                synchronized (this) {
                    try {
                        wait(DRAW_INTERVAL);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }
    }

    public void startDrawThread() {
        stopDrawThread();

        drawThread = new DrawThread();
        drawThread.start();
    }

    public boolean stopDrawThread() {
        if (drawThread == null) {
            return false;
        }

        drawThread.finish();
        drawThread = null;
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startDrawThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        startDrawThread();
    }


    public interface EventCallback {
        void onGameOver(long score);
    }

    private EventCallback eventCallback;

    public void setEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    private Handler handler = new Handler();

    public GameView(Context context) {
        super(context);

        //スコア
        panitScore.setColor(Color.BLACK);
        panitScore.setTextSize(SCORE_TEXT_SIZE);
        //アンチエイリアス
        panitScore.setAntiAlias(true);

        getHolder().addCallback(this);
    }

    private void drawGame(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

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
        //敵オブジェクト描画
        drawObjectList(canvas, missileList, width, height);
        //自機弾オブジェクト描画
        drawObjectList(canvas, bulletList, width, height);


        //自機とミサイルが当たったかを判定
        for (int i = 0; i < missileList.size(); i++) {
            BaseObject missile = missileList.get(i);


            if (doroid.isHit(missile)) {
                missile.hit();
                doroid.hit();

                //UIスレッドへgameoverを伝える
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        eventCallback.onGameOver(score);
                    }
                });

                break;
            }

            //弾とミサイルが当たったかの判定
            for (int j = 0; j < bulletList.size(); j++) {
                BaseObject bullet = bulletList.get(j);

                if (bullet.isHit(missile)) {
                    missile.hit();
                    bullet.hit();

                    score += 10;
                }
            }
        }

        doroid.draw(canvas);

        canvas.drawText("Score:" + score, 0, SCORE_TEXT_SIZE, panitScore);

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
     * 画面タッチ
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //自機弾発射
                fire(event.getX(), event.getY());
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * xy座標を元に傾きを計算して自機弾発射
     * @param x
     * @param y
     */
    private void fire(float x, float y) {
        float alignX = (x - doroid.rect.centerX()) / (doroid.rect.centerY() - y);

        //自機オブジェクトより下をタッチした場合
        if (doroid.rect.centerY() - y <= 0) {
            alignX = -alignX;
        }

        Bullet bullet = new Bullet(doroid.rect, alignX);
        bulletList.add(0, bullet);
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
