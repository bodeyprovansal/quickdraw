package com.example.quickdraw;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

public class DrawView extends View {
    private Path path;
    private Paint userPaint, boardPaint;
    private Bitmap bitmap;
    private Canvas canvas;
    private int drawColor = 0x000000F;
    private float x, y;

    //Constructor
    public DrawView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        createDrawingBoard();
    }
    //Initialize
    public void createDrawingBoard()
    {
        path = new Path();
        userPaint = new Paint();
        //Setting paint style
        userPaint.setColor(drawColor);
        userPaint.setStyle(Paint.Style.STROKE);
        userPaint.setStrokeJoin(Paint.Join.ROUND);
        userPaint.setStrokeCap(Paint.Cap.SQUARE);
        userPaint.setStrokeWidth(20);
        //Bitmap Paint
        boardPaint = new Paint(Paint.DITHER_FLAG);
    }
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        x = e.getX();
        y = e.getY();
        switch (e.getAction())
        {
            //Press down, move, and lift cursor
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                canvas.drawPath(path, userPaint);
                path.reset();
            default:
                return false;
        }
        invalidate();
        return true;

    }
    //Resizes and creates our DrawView, consists of Canvas and Bitmap
    @Override
    public void onSizeChanged(int w, int h, int ow, int oh)
    {
        super.onSizeChanged(w, h, ow, oh);
        bitmap = Bitmap.createBitmap(w + 1, h + 1, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

    }
    //Draws to bitmap using canvas on every call to onDraw
    @Override
    public void onDraw(Canvas canvas)
    {
        canvas.drawBitmap(bitmap, 0, 0, boardPaint);
        canvas.drawPath(path, userPaint);
    }
    //Replaces everything on canvas with transparent color
    public void clearCanvas()
    {
        path.reset();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        invalidate();
    }
    //Accessor and Mutator
    public void setUserPaint(int newColor)
    {
        invalidate();
        userPaint.setColor(newColor);
    }
    public int getUserPaint()
    {
        return userPaint.getColor();
    }
}
