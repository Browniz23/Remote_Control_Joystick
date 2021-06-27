package com.flightgearapp_ms3.myView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Joystick is a touchable view which can be injected into activity and communicate with.
 * contains base circle and smaller hat circle which moves according user's touch.
 */
public class Joystick extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    // center point
    private float centerX;
    private float centerY;
    // radius's of circles
    private float baseRadius;
    private float hatRadius;
    // an instance of OnJoystickChange, determine the function called when joystick changed
    // called from onTouch
    public OnJoystickChange onChange;

    /**
     * makes joystick in center of screen. determines circles sizes (base and hat).
     */
    private void setupDimensions() {
        // center is middle of Joystick screen
        centerX = (float) getWidth() / 2;
        centerY = (float) getHeight() / 2;
        // baseRadius for big circle, hatRadius for little circle
        baseRadius = (float) Math.min(getWidth(), getHeight()) / 3;
        hatRadius = (float) Math.min(getWidth(), getHeight()) / 5;
    }

    /**
     * draw the joystick with newX and newY as center of joystick hat.
     * @param newX - current x touched in screen
     * @param newY - current y touched in screen
     */
    private void drawJoystick(float newX, float newY) {
        // make sure surface already created before draw
        if (getHolder().getSurface().isValid()) {
            Canvas myCanvas = this.getHolder().lockCanvas();
            Paint color = new Paint();
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            // background
            color.setARGB(200, 17, 44, 61);
            myCanvas.drawRect(0, 0, getWidth(), getHeight(), color);
            // dark green base circle
            color.setARGB(255, 18, 92, 64);
            myCanvas.drawCircle(centerX, centerY, baseRadius, color);
            // warm blue hat circle
            color.setARGB(255, 50, 127, 151);
            myCanvas.drawCircle(newX, newY, hatRadius, color);
            // print draw to screen
            getHolder().unlockCanvasAndPost(myCanvas);
        }
    }

    public Joystick(Context context) {
        super(context);
        // at event - this object callback methods are called
        getHolder().addCallback(this);
        // when screen touched - use onTouch method from this class
        setOnTouchListener(this);
        onChange = (x, y) -> {};
    }

    public Joystick(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // at event - this object callback methods are called
        getHolder().addCallback(this);
        // when screen touched - use onTouch method from this class
        setOnTouchListener(this);
        onChange = (x, y) -> {};
    }

    public Joystick(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        onChange = (x, y) -> {};
    }

    public Joystick(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        onChange = (x, y) -> {};
    }

    /**
     * when surface is fully created - setup joystick sizes and draw.
     */
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        setupDimensions();
        drawJoystick(centerX, centerY);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {}

    /**
     * implements onTouch - when Joystick screen touched - refresh joystick draw and calls onChange.
     * @param v - view.
     * @param event - touch event.
     * @return true for enable future touches.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // touch came from this view
        if (v.equals(this)) {
            // when touch wasn't removing finger from joystick
            if (event.getAction() != MotionEvent.ACTION_UP) {
                // displacement - distance from center.
                float disFromCenter = (float) Math.sqrt(Math.pow(event.getX() - centerX, 2) +
                        Math.pow(event.getY() - centerY, 2));
                if (disFromCenter < baseRadius) {
                    // touch inside boundaries - draw and execute onChange
                    drawJoystick(event.getX(), event.getY());
                    // calls onChange with normalized value for x and y.
                    onChange.onJoystickMoved((event.getX() - centerX) / baseRadius,
                            (event.getY() - centerY) / baseRadius);
                }
                else {
                    // touch outside boundaries - fix to be as touched at edge of circle.
                    float ratio = baseRadius / disFromCenter;
                    float fixedX = centerX + (event.getX() - centerX) * ratio;
                    float fixedY = centerY + (event.getY() - centerY) * ratio;
                    drawJoystick(fixedX, fixedY);
                    // calls onChange with normalized value for x and y.
                    onChange.onJoystickMoved((fixedX - centerX) / baseRadius,
                            (fixedY - centerY) / baseRadius);
                }
            } else {
                // when touch was removing finger from joystick - returns joystick to center
                drawJoystick(centerX, centerY);
                onChange.onJoystickMoved(0, 0);
            }
        }
        // true for enabled future touches
        return true;
    }

    /**
     * interface 'OnJoystickChange' contains method 'onJoystickMoved' which gets x and y normalized
     * as is percents of moving range for each axe.
     */
    public interface OnJoystickChange {
        void onJoystickMoved(float xNormalized, float yNormalized);
    }
}
