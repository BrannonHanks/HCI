package com.example.brannon.ia08;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;

import java.util.Stack;

/**
 * Created by Brannon on 11/2/2016.
 */

public class DoodleView extends View {
    private int opaC;
    private int redC;
    private int greenC;
    private int blueC;
    private int wid;
    private Paint _paintDoodle = new Paint();
    private Path _path = new Path();
    private Stack _art;

    public DoodleView (Context context) {
        super(context);
        init(null, 0);
    }

    public DoodleView (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DoodleView (Context context, AttributeSet attrs, int styleDef) {
        super(context, attrs, styleDef);
        init(attrs, styleDef);
    }

    private void init (AttributeSet attrs, int styleDef) {
        _art = new Stack();
        opaC = 100;
        redC = 123;
        blueC = 165;
        greenC = 170;
        wid = 7;
        _paintDoodle.setColor(android.graphics.Color.argb(opaC, redC, greenC, blueC));
        _paintDoodle.setAntiAlias(true);
        _paintDoodle.setStyle(Paint.Style.STROKE);
        _paintDoodle.setStrokeWidth(wid);

        //_paintDoodle.se
    }
    public void setAlpha (int r, Space temp) {
        opaC = r;
        _paintDoodle.setColor(android.graphics.Color.argb(r, redC, greenC, blueC));
        temp.setBackgroundColor(android.graphics.Color.argb(opaC, redC, greenC, blueC));
    }

    public void setRed (int r, Space temp1, Space temp) {
        redC = r;
        _paintDoodle.setColor(android.graphics.Color.argb(opaC, r, greenC, blueC));
        temp1.setBackgroundColor(android.graphics.Color.argb(opaC, redC, 0, 0));
        temp.setBackgroundColor(android.graphics.Color.argb(opaC, redC, greenC, blueC));
    }

    public void setGreen (int r, Space temp1, Space temp) {
        greenC = r;
        _paintDoodle.setColor(android.graphics.Color.argb(opaC, redC, r, blueC));
        temp1.setBackgroundColor(android.graphics.Color.argb(opaC, 0, greenC, 0));
        temp.setBackgroundColor(android.graphics.Color.argb(opaC, redC, greenC, blueC));
    }

    public void setBlue (int r, Space temp1, Space temp) {
        blueC = r;
        _paintDoodle.setColor(android.graphics.Color.argb(opaC, redC, greenC, r));
        temp1.setBackgroundColor(android.graphics.Color.argb(opaC, 0, 0, blueC));
        temp.setBackgroundColor(android.graphics.Color.argb(opaC, redC, greenC, blueC));
    }


    public void changeBrushSize(int progress) {
        _paintDoodle.setStrokeWidth(progress);
        wid = progress;
    }

    public void undo () {_art.pop(); invalidate();}

    public void clear () {
        _art = new Stack();
        invalidate();
    }

    public void onDraw (Canvas canvas) {
        super.onDraw(canvas);
        Stack temp = (Stack)_art.clone();
        while(!temp.isEmpty()) {
            MyPaint current = (MyPaint) temp.pop();
            canvas.drawPath(current.getPath(), current.getPaint());
        }
    }

    public boolean onTouchEvent (MotionEvent motionEvent) {
        float touchX = motionEvent.getX();
        float touchY = motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                _path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                _path.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                _art.push(new MyPaint(_paintDoodle, _path));
                _paintDoodle = new Paint();
                _path = new Path();
                _paintDoodle.setColor(android.graphics.Color.argb(opaC, redC, greenC, blueC));
                _paintDoodle.setAntiAlias(true);
                _paintDoodle.setStyle(Paint.Style.STROKE);
                _paintDoodle.setStrokeWidth(wid);
                break;
        }

        invalidate();
        return true;
    }
}

class MyPaint {
    private Paint _paintDoodle;
    private Path _path;

    MyPaint (Paint in, Path alsoIn) {
        _paintDoodle = in;
        _path = alsoIn;
    }

    public Paint getPaint () {
        return _paintDoodle;
    }

    public Path getPath () {
        return _path;
    }
}
