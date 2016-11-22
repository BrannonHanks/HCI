package edu.umd.hcil.impressionistpainter434;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.text.MessageFormat;
import java.util.Random;
import java.util.Stack;

import static edu.umd.hcil.impressionistpainter434.BrushType.Circle;

/**
 * Created by jon on 3/20/2016.
 */
public class ImpressionistView extends View {

    private ImageView _imageView;
    private Bitmap _bitMap;

    private Canvas _offScreenCanvas = null;
    private Bitmap _offScreenBitmap = null;
    private Paint _paint = new Paint();
    private Path _path = new Path();
    private Stack _art;

    private int _alpha = 150;
    private int _defaultRadius = 25;
    private int _rad = 0;
    private Point _lastPoint = null;
    private long _lastPointTime = 1000000000;
    private boolean _useMotionSpeedForBrushStrokeSize = true;
    private Paint _paintBorder = new Paint();
    private BrushType _brushType = BrushType.Square;
    private float _minBrushRadius = 5;

    public ImpressionistView(Context context) {
        super(context);
        init(null, 0);
    }

    public ImpressionistView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ImpressionistView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * Because we have more than one constructor (i.e., overloaded constructors), we use
     * a separate initialization method
     * @param attrs
     * @param defStyle
     */
    private void init(AttributeSet attrs, int defStyle){

        // Set setDrawingCacheEnabled to true to support generating a bitmap copy of the view (for saving)
        // See: http://developer.android.com/reference/android/view/View.html#setDrawingCacheEnabled(boolean)
        //      http://developer.android.com/reference/android/view/View.html#getDrawingCache()
        this.setDrawingCacheEnabled(true);

        _paint.setColor(Color.RED);
        _paint.setAlpha(_alpha);
        _paint.setAntiAlias(true);
        _paint.setStyle(Paint.Style.FILL);
        _paint.setStrokeWidth(4);
        _brushType = Circle;

        _art = new Stack();

        _paintBorder.setColor(Color.BLACK);
        _paintBorder.setStrokeWidth(3);
        _paintBorder.setStyle(Paint.Style.STROKE);
        _paintBorder.setAlpha(50);

        //_paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh){

        Bitmap bitmap = getDrawingCache();
        Log.v("onSizeChanged", MessageFormat.format("bitmap={0}, w={1}, h={2}, oldw={3}, oldh={4}", bitmap, w, h, oldw, oldh));
        if(bitmap != null) {
            _offScreenBitmap = getDrawingCache().copy(Bitmap.Config.ARGB_8888, true);
            _offScreenCanvas = new Canvas(_offScreenBitmap);
        }
    }

    /**
     * Sets the ImageView, which hosts the image that we will paint in this view
     * @param imageView
     */
    public void setImageView(ImageView imageView){
        _imageView = imageView;

    }

    /**
     * Sets the brush type. Feel free to make your own and completely change my BrushType enum
     * @param brushType
     */
    public void setBrushType(BrushType brushType){
        _brushType = brushType;

    }

    /**
     * Clears the painting
     */
    public void clearPainting(){
        //TODO
        Bitmap imageViewBitmap = _imageView.getDrawingCache();
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        _offScreenBitmap = Bitmap.createBitmap(imageViewBitmap.getWidth(), imageViewBitmap.getHeight(), conf);
        _offScreenCanvas = new Canvas(_offScreenBitmap);
        invalidate();
    }

    //Just return the image that was drawn.
    public Bitmap save () {
        return _offScreenBitmap;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(_offScreenBitmap != null) {
            canvas.drawBitmap(_offScreenBitmap, 0, 0, _paint);


        }

        /*
        Stack temp = (Stack)_art.clone();
        while(!temp.isEmpty()) {
            MyPaint current = (MyPaint) temp.pop();
            canvas.drawPath(current.getPath(), current.getPaint());
        }*/

        // Draw the border. Helpful to see the size of the bitmap in the ImageView
        canvas.drawRect(getBitmapPositionInsideImageView(_imageView), _paintBorder);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){

        //TODO
        //Basically, the way this works is to liste for Touch Down and Touch Move events and determine where those
        //touch locations correspond to the bitmap in the ImageView. You can then grab info about the bitmap--like the pixel color--
        //at that location

        Bitmap imageViewBitmap = _imageView.getDrawingCache();

        if (_offScreenBitmap == null) {
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            _offScreenBitmap = Bitmap.createBitmap(imageViewBitmap.getWidth(), imageViewBitmap.getHeight(), conf);
            _offScreenBitmap.eraseColor(Color.WHITE);
            _offScreenCanvas = new Canvas(_offScreenBitmap);
        }

        Rect rect = getBitmapPositionInsideImageView(_imageView);
        float touchX = motionEvent.getX();
        float touchY = motionEvent.getY();

        if (touchX < 0 || touchX > _offScreenBitmap.getWidth()) {
            return true;
        }

        if (touchY < 0 || touchY > _offScreenBitmap.getHeight()) {
            return true;
        }

        int myColor = 0;

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:

                myColor = imageViewBitmap.getPixel((int)(touchX), (int)(touchY));
                //_offScreenBitmap.setPixel((int)(touchX), (int)(touchY), myColor);
                _paint.setColor(myColor);
                brushToCanvas(touchX, touchY);
                /*_paint.setColor(myColor);
                _path.moveTo(touchX, touchY);
                _art.push(new MyPaint(_paint, _path));
                _path = new Path();*/
                break;
            case MotionEvent.ACTION_MOVE:
                myColor = imageViewBitmap.getPixel((int)(touchX), (int)(touchY));
                //_offScreenBitmap.setPixel((int)(touchX), (int)(touchY), myColor);
                _paint.setColor(myColor);
                brushToCanvas(touchX, touchY);
                /*_paint.setColor(myColor);
                _path.moveTo(touchX, touchY);
                _art.push(new MyPaint(_paint, _path));
                _path = new Path();*/
                break;
            case MotionEvent.ACTION_UP:
                //_art.push(new MyPaint(_paint, _path));
                //_path = new Path();
                _rad = 0;
                break;
        }

        _lastPointTime = System.currentTimeMillis();
        invalidate();


        return true;
    }

    //This method is what actually draws to the canvas. It takes the brush type and draws accordingly
    private void brushToCanvas (float touchX, float touchY) {
        long time = System.currentTimeMillis();
        switch (_brushType) {
            case Circle:
                _offScreenCanvas.drawCircle(touchX, touchY, _defaultRadius, _paint);
                break;
            case Square:
                _offScreenCanvas.drawRect(touchX, touchY, touchX+40, touchY-40, _paint);
                break;
            case Line:
                Rect rec = getBitmapPositionInsideImageView(_imageView);
                _offScreenCanvas.drawLine(rec.left, touchY, rec.right, touchY, _paint);
                _offScreenCanvas.drawLine(touchX, rec.top, touchX, rec.bottom, _paint);
                break;
            case CircleVelocity:
                if (time - _lastPointTime < 70 && _rad < 75) {
                    _rad+=2;
                } else {
                    _rad-= 40;
                    if (_rad < 0){
                        _rad = 0;
                    }
                }
                _offScreenCanvas.drawCircle(touchX, touchY, _defaultRadius+_rad, _paint);
                break;
            case SquareVelocity:
                if (time - _lastPointTime < 70 && _rad < 75) {
                    _rad+=2;
                } else {
                    _rad-= 40;
                    if (_rad < 0){
                        _rad = 0;
                    }
                }
                _offScreenCanvas.drawRect(touchX, touchY, (touchX+40)+_rad, (touchY-40)-_rad, _paint);
                break;
        }
    }


    /**
     * This method is useful to determine the bitmap position within the Image View. It's not needed for anything else
     * Modified from:
     *  - http://stackoverflow.com/a/15538856
     *  - http://stackoverflow.com/a/26930938
     * @param imageView
     * @return
     */
    private static Rect getBitmapPositionInsideImageView(ImageView imageView){
        Rect rect = new Rect();

        if (imageView == null || imageView.getDrawable() == null) {
            return rect;
        }

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int widthActual = Math.round(origW * scaleX);
        final int heightActual = Math.round(origH * scaleY);

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - heightActual)/2;
        int left = (int) (imgViewW - widthActual)/2;

        rect.set(left, top, left + widthActual, top + heightActual);
        return rect;
    }
}