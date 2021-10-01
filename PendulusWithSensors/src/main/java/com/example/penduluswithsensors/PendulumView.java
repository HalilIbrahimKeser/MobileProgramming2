package com.example.penduluswithsensors;

//Halil Ibrahim Keser

//Start kode er hentet fra denne siden. Så har jeg bygget på den med sensorer, endret endel på mainfragment som har sensorer fra forrige oppgave.
//https://steemit.com/utopian-io/@ideba/demonstrate-simple-pendulum-with-android-studio


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

public class PendulumView extends View {

    private static boolean draw;
    private static boolean drop;
    private static int counter;

    Paint paintCircle, paintThread;
    Path pathThread,pathHolder;
    float circlePositionX = 0;
    float threadPositionX = 0;
    float x_dir;
    float y_dir;
    int xCenter;
    int yHeight;
    float addValue;

    double thread_x_dir;
    float bigCircle_x;
    private static float bigCircle_y;

    double thread_x;
    float boundryRight;
    float boundryLeft;
    float boundryRightOriginal;
    float boundryLeftOriginal;

    //Sensor Data
    private static float mMagneticField_North;
    private static float mMagneticField_East;
    private static float mMagneticField_Up;

    private static float mAccelerometer_X;
    private static float mAccelerometer_Y;
    private static float mAccelerometer_Z;

    private static float mGyroscope_X;
    private static float mGyroscope_Y;
    private static float mGyroscope_Z;

    private static float mOrientation_Azimuth;
    private static float mOrientation_Pitch;
    private static float mOrientation_Roll;

    public PendulumView(Context context) {
        super(context);
        draw = true;
        drop = false;
        init();
    }
    public PendulumView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        draw = true;
        drop = false;
        init();
    }

    public PendulumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        draw = true;
        drop = false;
        init();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //PHONE POSITION CHECK
        //Styring av høyde på Circle
        if(mAccelerometer_Y < (-2) && mAccelerometer_Y >= (-6) && mAccelerometer_X >= 0) {
            //Telefonen lent mot venstre
            xCenter = getWidth() / 2;
            if (!drop){
                bigCircle_y = 500;
            }

        } else if (mAccelerometer_Y < (-2) && mAccelerometer_Y >= (-6) && mAccelerometer_X < 0) {
            //Telefonen lent mot høyre
            xCenter = getWidth() / 2;
            if (!drop){
                bigCircle_y = 500;
            }

        } else if (mAccelerometer_Y < (-6) && mAccelerometer_X >= 0) {
            //Telefone er opp ned, bunnen lent mot høyre
            xCenter = getWidth() / 2;
            if (!drop){
                bigCircle_y = -30;
            }

        } else if (mAccelerometer_Y < (-6) && mAccelerometer_X < 0) {
            //Telefone er opp ned, bunnen lent mot venstre
            xCenter = getWidth() / 2;
            if (!drop){
                bigCircle_y = -30;
            }

        } else {
            xCenter = getWidth() / 2;
            if (!drop){
                bigCircle_y = 700;
            }
        }
        yHeight = getHeight();

        boundryRightOriginal =(float) ((getWidth() / 2.0) + 450);     //754
        boundryLeftOriginal = (float) ((getWidth() / 2.0) - 450);      //354
        boundryRight = xCenter + 200;     //754
        boundryLeft = xCenter - 200;      //354

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

        //Build paint for the thread
        paintThread.setColor(Color.BLACK);
        paintThread.setStyle(Paint.Style.STROKE);
        paintThread.setStrokeWidth(3);

        int left_x = 400;
        int right_x = 700;
        int bottom_y = 50;
        int top_y = 200;

        pathHolder.moveTo(xCenter,top_y);
        pathHolder.lineTo(left_x,top_y);

        pathHolder.moveTo(left_x,top_y);
        pathHolder.lineTo(left_x,bottom_y);
        pathHolder.moveTo(left_x,bottom_y);
        pathHolder.lineTo(right_x,bottom_y);
        pathHolder.moveTo(right_x,bottom_y);
        pathHolder.lineTo(right_x,top_y);
        pathHolder.moveTo(right_x,top_y);
        pathHolder.lineTo(xCenter,top_y);

        //Build path for the thread
        pathThread.moveTo(xCenter, top_y);               //540
        pathThread.lineTo((float) thread_x, bigCircle_y);   //360
        canvas.drawPath(pathHolder,paintThread);
        canvas.drawPath(pathThread, paintThread);
        canvas.drawCircle(xCenter, top_y, 10, paintCircle);

        //DROP BUTTON CONTROL
        if(draw){
            canvas.drawCircle(bigCircle_x, bigCircle_y, 30, paintCircle);
        }
        if (drop) {
            canvas.drawCircle(bigCircle_x, bigCircle_y, 30, paintCircle);
            int heigt = getHeight()-90;
            if (bigCircle_y < heigt) {
                bigCircle_y = bigCircle_y + 20;
            }
        }

        calculateBoundaries();

        //BOUNDARY HIT CHECK FOR COUNTER
        if (bigCircle_x <= boundryLeftOriginal) {
            MainFragment.updateCounter();
        } else if (bigCircle_x >= boundryRightOriginal) {
            MainFragment.updateCounter();
        }

        //PHONE POSITION CHECK
        //BOUNDARY HIT CHECK
        if ((mOrientation_Azimuth > 0.2 || mOrientation_Azimuth < (-0.2)) && !drop) {

            if (mAccelerometer_Z >= 9.4 || mAccelerometer_Z <= (-9.4)){
                // mAccelerometer_Z: Telefonen ligger flat
                // Circle beveger seg saktere
                if (bigCircle_x >= boundryRight) {
                    x_dir = -4;
                    bigCircle_y -= 300;
                }
                if (bigCircle_x <= boundryLeft) {
                    x_dir = 4;
                    bigCircle_y -= 300;
                }
            } else {
                if (mAccelerometer_Y >= 0) {
                    // mAccelerometer_Y: Telefonens øvre del går max 90 grader mot begge sider.
                    // mAccelerometer_Y: Toppen av telefonen ligger i den øvre delen av 180 graders vinkelen.
                    // mAccelerometer_Z: Telefonen ligger ikke flat
                    // Circle beveger seg fortere
                    if (bigCircle_x >= boundryRight) {
                        x_dir = -8;
                    }
                    if (bigCircle_x <= boundryLeft) {
                        x_dir = 8;
                    }

                } else if (mAccelerometer_Y < 0) {
                    // Telefonen er opp ned
                    // Toppen av telefonen ligger i den nedre delen av 180 graders vinkelen.
                    // mAccelerometer_Z: Telefonen ligger ikke flat
                    // Circle beveger seg litt fortere
                    if (bigCircle_x >= boundryRight) {
                        x_dir = -6;
                        bigCircle_y -= 300;
                    }
                    if (bigCircle_x <= boundryLeft) {
                        x_dir = 6;
                        bigCircle_y -= 300;
                    }
                }
            }
            bigCircle_x = bigCircle_x + x_dir;
            thread_x = thread_x + x_dir;
            Log.i("PENDULUM: Thread Line", String.valueOf(threadNewLine));
        }
        invalidate();
    }

    public static void DropTheCircleFromSurface() {
        draw = false;
        drop = true;
        MainFragment.updateDropCounter();
    }

    public static void resetValues() {
        draw = true;
        drop = false;
        bigCircle_y = 500;
        MainFragment.reset();
    }

    private void calculateBoundaries() {
        if (mAccelerometer_X == 0) {
            boundryLeft = boundryLeftOriginal;
            boundryRight = boundryRightOriginal;

        } else if (mAccelerometer_X > 0 && mAccelerometer_X < 9.81) {
            boundryLeft = (float) (boundryLeft - (mAccelerometer_X * 36.1));
            boundryRight = (float) (boundryRight - (mAccelerometer_X * 36.1));

        } else if (mAccelerometer_X < 0 && mAccelerometer_X > (-9.81)) {
            boundryLeft = (float) (boundryLeft - (mAccelerometer_X * 36.1));
            boundryRight = (float) (boundryRight - (mAccelerometer_X * 36.1));
        }
    }

    public static void sendMagneticFieldData(float[] values) {
        mMagneticField_North = values[0];
        mMagneticField_East = values[1];
        mMagneticField_Up = values[2];
    }

    public static void sendAccelerometerData(float[] values) {
        mAccelerometer_X = values[0];
        mAccelerometer_Y = values[1];
        mAccelerometer_Z = values[2];
    }

    public static void sendGyroscopeData(float[] values) {
        mGyroscope_X = values[0];
        mGyroscope_Y = values[1];
        mGyroscope_Z = values[2];
    }

    public static void sendOrientationData(float[] values) {
        mOrientation_Azimuth = values[0];
        mOrientation_Pitch = values[1];
        mOrientation_Roll = values[2];
    }

    public void init() {
        x_dir = 5;
        thread_x_dir = 9.5;

        bigCircle_x = 360;
        bigCircle_y = 500;
        thread_x = 360;
    }
}
