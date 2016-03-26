package io.kokada.shooting;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GameView.EventCallback {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ActivityのContextからインスタンス
        gameView = new GameView(this);

        gameView.setEventCallback(this);

        setContentView(gameView);
    }

    @Override
    public void onGameOver(long score) {
        gameView.startDrawThread();

        Toast.makeText(this, "Game Over スコア" + score, Toast.LENGTH_LONG).show();
    }

}
