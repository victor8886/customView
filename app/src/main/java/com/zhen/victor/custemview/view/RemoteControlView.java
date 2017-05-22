package com.zhen.victor.custemview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by victor on 17-5-22.
 */

public class RemoteControlView extends View {
    Path up_p, down_p, left_p, right_p, center_p;
    Region up, down, left, right, center;
    Matrix matrix = null;
    Paint paint = new Paint();
    int mWidth;
    int mHeight;

    final int CENTER = 0;
    final int UP = 1;
    final int RIGHT = 2;
    final int DOWN = 3;
    final int LEFT = 4;
    int touchFlag = -1;
    int currentFlag = -1;
    int mDefauColor = 0xFF4E5268;
    int mTouchedColor = 0xFFDF9C81;

    MenuListener listener = null;

    public RemoteControlView(Context context) {
        this(context, null);
    }

    public RemoteControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RemoteControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        up_p = new Path();
        down_p = new Path();
        left_p = new Path();
        right_p = new Path();
        center_p = new Path();

        up = new Region();
        down = new Region();
        left = new Region();
        right = new Region();
        center = new Region();

        paint.setColor(mDefauColor);
        paint.setAntiAlias(true);

        matrix = new Matrix();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        matrix.reset();
        Region globalRegion = new Region(-w, -h, w, h);
        int minWidth = w > h ? h : w;
        minWidth *= 0.8;
        int br = minWidth / 2;
        int sr = minWidth / 4;
        RectF bigCircle = new RectF(-br, -br, br, br);
        RectF smallCircle = new RectF(-sr, -sr, sr, sr);
        float bigSweepAngle = 84;
        float smallSweepAngle = -80;

        center_p.addCircle(0, 0, 0.2f * minWidth, Path.Direction.CW);
        center.setPath(center_p, globalRegion);

        right_p.addArc(bigCircle, -40, bigSweepAngle);
        right_p.arcTo(smallCircle, 40, smallSweepAngle);
        right_p.close();
        right.setPath(right_p, globalRegion);

        down_p.addArc(bigCircle, 50, bigSweepAngle);
        down_p.arcTo(smallCircle, 130, smallSweepAngle);
        down_p.close();
        down.setPath(down_p, globalRegion);

        left_p.addArc(bigCircle, 140, bigSweepAngle);
        left_p.arcTo(smallCircle, 220, smallSweepAngle);
        left_p.close();
        left.setPath(left_p, globalRegion);

        up_p.addArc(bigCircle, 230, bigSweepAngle);
        up_p.arcTo(smallCircle, 310, smallSweepAngle);
        up_p.close();
        up.setPath(up_p, globalRegion);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] pts = new float[2];
        pts[0] = event.getX();
        pts[1] = event.getY();
        matrix.mapPoints(pts);
        int x = (int) pts[0];
        int y = (int) pts[1];
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchFlag = getTouchedPath(x, y);
                currentFlag = touchFlag;
                break;
            case MotionEvent.ACTION_MOVE:
                currentFlag = getTouchedPath(x,y);
                break;
            case MotionEvent.ACTION_UP:
                currentFlag = getTouchedPath(x,y);
                if (currentFlag == touchFlag && currentFlag != -1 && listener != null) {
                    switch (currentFlag) {
                        case CENTER:
                           listener.onCenterCliched();
                            break;
                        case UP:
                            listener.onUpCliched();
                            break;
                        case RIGHT:
                            listener.onRightCliched();
                            break;
                        case DOWN:
                            listener.onDownCliched();
                            break;
                        case LEFT:
                            listener.onLeftCliched();
                            break;
                    }
                }
                touchFlag = currentFlag = -1;
                break;
            case MotionEvent.ACTION_CANCEL:
                touchFlag = currentFlag = -1;
                break;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth/2,mHeight/2);
        if (matrix.isIdentity()) {
            canvas.getMatrix().invert(matrix);
        }

        canvas.drawPath(center_p, paint);
        canvas.drawPath(up_p, paint);
        canvas.drawPath(right_p, paint);
        canvas.drawPath(down_p, paint);
        canvas.drawPath(left_p, paint);

        paint.setColor(mTouchedColor);
        switch (currentFlag) {
            case CENTER:
                canvas.drawPath(center_p,paint);
                break;
            case UP:
                canvas.drawPath(up_p,paint);
                break;
            case RIGHT:
                canvas.drawPath(right_p,paint);
                break;
            case DOWN:
                canvas.drawPath(down_p,paint);
                break;
            case LEFT:
                canvas.drawPath(left_p,paint);
                break;
        }
        paint.setColor(mDefauColor);
    }

    private int getTouchedPath(int x, int y) {
        if (center.contains(x, y)) {
            return CENTER;
        }
        if (up.contains(x, y)) {
            return UP;
        }
        if (right.contains(x, y)) {
            return RIGHT;
        }
        if (down.contains(x, y)) {
            return DOWN;
        }
        if (left.contains(x, y)) {
            return LEFT;
        }

        return -1;
    }

    public void setListener(MenuListener listener) {
        this.listener = listener;
    }

    public interface MenuListener {
        void onCenterCliched();

        void onUpCliched();

        void onRightCliched();

        void onDownCliched();

        void onLeftCliched();
    }
}
