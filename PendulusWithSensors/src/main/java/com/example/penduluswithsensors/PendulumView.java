package com.example.penduluswithsensors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PendulumView extends View {

    Paint paintCircle, paintThread;
    Path pathThread,pathHolder;
    float circlePositionX = 0;
    float threadPositionX = 0;
    int x_dir;
    int y_dir;
    int addValue;
    double thread_x_dir;
    float circle_x;
    float circle_y;
    double thread_x;
    float boundryRight;
    float boundryLeft;

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

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int xCenter = getWidth() / 2;           //540
        int yHeight = getHeight();

        boundryRight = xCenter + 200;     //740
        boundryLeft = xCenter - 200;      //340

        Log.i("PENDULUM: Thread Width ", String.valueOf(getWidth()));
        Log.i("PENDULUM: Boundry ", String.valueOf(boundryRight));

        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintThread = new Paint(Paint.ANTI_ALIAS_FLAG);

        pathThread = new Path();
        pathHolder = new Path();

        float threadNewLine = xCenter + threadPositionX;

        //Build paint for the circle
        paintCircle.setStyle(Paint.Style.FILL);
        paintCircle.setColor(Color.BLUE);
        //paintCircle.setStrokeWidth(5);


        //Build paint for the thread
        paintThread.setColor(Color.BLACK);
        paintThread.setStyle(Paint.Style.STROKE);
        paintThread.setStrokeWidth(3);

        int bottom_x = 400;
        int top_x = 700;
        int bottom_y = 50;
        int top_y = 200;

        pathHolder.moveTo(xCenter,top_y);
        pathHolder.lineTo(bottom_x,top_y);

        pathHolder.moveTo(bottom_x,top_y);
        pathHolder.lineTo(bottom_x,bottom_y);
        pathHolder.moveTo(bottom_x,bottom_y);
        pathHolder.lineTo(top_x,bottom_y);
        pathHolder.moveTo(top_x,bottom_y);
        pathHolder.lineTo(top_x,top_y);
        pathHolder.moveTo(top_x,top_y);
        pathHolder.lineTo(xCenter,top_y);

        //Build path for the thread
        circle_y = 800;
        pathThread.moveTo(xCenter, top_y);               //540
        pathThread.lineTo((float) thread_x, circle_y);   //360

        canvas.drawPath(pathThread, paintThread);
        canvas.drawPath(pathHolder,paintThread);
        canvas.drawCircle(circle_x, circle_y, 30, paintCircle);
        canvas.drawCircle(xCenter, top_y, 5, paintCircle);    //540

        addValue = 5;
        if (circle_x >= boundryRight) {             //740
            x_dir -= addValue;
        }
        if (circle_x <= boundryLeft) {              //340
            x_dir += addValue;
        }

        Fragment sensorFragment = new MainFragment();
        //sensorFragment.
        circle_x = circle_x + x_dir;
        thread_x = thread_x + x_dir;
        Log.i("PENDULUM: Thread Line", String.valueOf(threadNewLine));

        invalidate();
    }

    public static void sendData(float[] values) {
        for (int position = 0; position <= values.length; position++ ){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(values[position]);
        }
    }

    public static void sendGyroscopeData(float[] values) {

    }

    public void init() {
        x_dir = 5;
        thread_x_dir = 9.5;

        circle_x = 360;
        thread_x = 360;
    }
}
