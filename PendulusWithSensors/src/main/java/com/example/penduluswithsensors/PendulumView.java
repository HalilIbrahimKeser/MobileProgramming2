package com.example.penduluswithsensors;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class PendulumView  extends View {

    Paint paintCircle, paintThread;
    Path pathThread,pathHolder;
    float circlePositionX = 0;
    float threadPositionX = 0;
    int x_dir;
    int y_dir;
    double thread_x_dir;
    int circle_x;
    double thread_x;

    public PendulumView(Context context) {
        super(context);
        init();
    }
    public PendulumView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PendulumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int xCenter = getWidth() / 2;           //540

        int height = getHeight();
        //canvas.he(height/2);


        float boundryRight = xCenter + 200;     //740

        float boundryLeft = xCenter - 200;      //340

        Log.i("Thread: Width ", String.valueOf(getWidth()));

        //Log.i("boundry ", String.valueOf(boundryRight));

        paintCircle = new Paint();
        paintThread = new Paint();

        pathThread = new Path();
        pathHolder = new Path();

        float threadNewLine = xCenter + threadPositionX;


        //build paint for the circle
        paintCircle.setStyle(Paint.Style.FILL);
        paintCircle.setColor(Color.BLACK);
        //paintCircle.setStrokeWidth(5);


        //build paint for the thread
        paintThread.setColor(Color.BLACK);
        paintThread.setStyle(Paint.Style.STROKE);
        paintThread.setStrokeWidth(3);

        pathHolder.moveTo(xCenter,200);
        pathHolder.lineTo(400,200);


        pathHolder.moveTo(400,200);
        pathHolder.lineTo(400,50);

        pathHolder.moveTo(400,50);
        pathHolder.lineTo(700,50);

        pathHolder.moveTo(700,50);
        pathHolder.lineTo(700,200);

        pathHolder.moveTo(700,200);
        pathHolder.lineTo(xCenter,200);

        //build path for the thread
        pathThread.moveTo(xCenter, 200);            //540
        pathThread.lineTo((float) thread_x, 700);   //360

        canvas.drawPath(pathThread, paintThread);
        canvas.drawPath(pathHolder,paintThread);
        canvas.drawCircle(circle_x, 700, 30, paintCircle);
        canvas.drawCircle(xCenter, 200, 5, paintCircle);    //540

        if (circle_x >= boundryRight) {             //740
            x_dir -= 5;
        }

        if (circle_x <= boundryLeft) {              //340
            x_dir += 5;
        }

        circle_x = circle_x + x_dir;
        thread_x = thread_x + x_dir;
        Log.i("Thread: Thread Line", String.valueOf(threadNewLine));

        invalidate();
    }

    public void init() {
        x_dir = 5;
        thread_x_dir = 9.5;

        circle_x = 360;
        thread_x = 360;

    }
}
