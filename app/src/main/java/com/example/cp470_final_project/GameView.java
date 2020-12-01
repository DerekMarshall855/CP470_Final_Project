package com.example.cp470_final_project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private Thread thread;
    private Boolean isPlaying;
    private Background background1, background2;
    private float screenRatioX, screenRatioY;
    private Paint paint;
    private int screenX, screenY;
    public boolean select;
    private SurfaceHolder mSurfaceHolder;

    public GameView(Context ctx, int screenX, int screenY) {
        super(ctx);

        this.select = false;

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        background2.x = screenX;

        paint = new Paint();

    }

    @Override
    public void run() {

        while (isPlaying) {
            update();
            draw();
            sleep();
        }

    }

    private void draw() {
        //Draw images here with new updated values

        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            getHolder().unlockCanvasAndPost(canvas);
        }

    }

    private void sleep(){
        //Determines fps, sleep time of 17 milliseconds = 60fps
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        //update game values
        background1.x -= 5 * screenRatioX;
        background2.x -= 5 * screenRatioX;
        Log.i("GameView", "Update Method");

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }
        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause(){
        isPlaying = false;
        Boolean retry = true;
        while(retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        this.resume();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        //idk
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        this.pause();
    }
}
