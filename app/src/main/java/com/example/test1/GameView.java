package com.example.test1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class GameView extends View {
    private Paint mPaint;

    public GameView(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(0x10000000);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
