package me.skiloop.game.movingball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import me.skiloop.game.movingball.model.Ball;
import me.skiloop.game.movingball.model.BallField;

/**
 * Created by skiloop on 14-2-7.
 */
public class BoardView extends View {
    public static final int NO_COLOR = Color.WHITE;
    public static final int DEFAULT_BOARD_WIDTH = 240;
    public static final int DEFAULT_BOARD_HEIGHT = 300;

    private int mSizeX;
    private int mSizeY;
    private int mBackgroundColor;
    private BallField mBallField;
    private Paint mOuterBackground;
    private Paint mInnerBackground;

    public BoardView(Context context) {
        this(context, null);

    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBackgroundColor = NO_COLOR;
        mOuterBackground = new Paint();
        mInnerBackground = new Paint();
        mInnerBackground.setColor(Color.BLUE);
    }

    public void setBallField(BallField ballField) {
        mBallField = ballField;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int height, width;
        if (MeasureSpec.EXACTLY == widthMode) {
            width = widthSize;
        } else {
            width = DEFAULT_BOARD_WIDTH;
            if (MeasureSpec.AT_MOST == widthMode && width > widthSize) {
                width = widthSize;
            }
        }
        if (MeasureSpec.EXACTLY == heightMode) {
            height = heightSize;
        } else {
            height = DEFAULT_BOARD_HEIGHT;
            if (MeasureSpec.AT_MOST == heightMode && height > heightSize) {
                height = heightSize;
            }
        }

        setSizeX(width - getPaddingLeft() - getPaddingRight());
        setSizeY(height - getPaddingBottom() - getPaddingTop());

        setMeasuredDimension(width, height);

        if (null != mBallField) {
            mBallField.setmSizeX(width);
            mBallField.setmSizeY(height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        int paddingLeft = getPaddingLeft();
        int paddingBottom = getPaddingBottom();

        // draw background
        if (mOuterBackground.getColor() != NO_COLOR) {
            canvas.drawRect(0.0f, getSizeY(), getSizeX(), 0.0f, mOuterBackground);
        }
        if (mInnerBackground.getColor() != NO_COLOR) {
            canvas.drawRect(paddingLeft, paddingBottom + getSizeY(), paddingLeft + getSizeX(), paddingBottom, mInnerBackground);
        }
        // draw sample circle
        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);
        canvas.drawCircle(100, 160, 80, paint);

        // draw ball
        if (null != mBallField) {
            Paint tempPaint = new Paint();
//            for (Ball ball : mBallField.getmBalls()) {
//                tempPaint.setColor(ball.getColor());
//                canvas.drawCircle(ball.getX(), ball.getY(), ball.getRadius(), tempPaint);
//            }
            Ball ball = mBallField.getmBall();
            tempPaint.setColor(ball.getColor());
            canvas.drawCircle(ball.getX(), ball.getY(), ball.getRadius(), tempPaint);
        }
    }

    public void setOuterBackgroundColor(int color) {
        mOuterBackground.setColor(color);
    }

    public void setInnerBackgroundColor(int color) {
        mInnerBackground.setColor(color);
    }

    public Point getSize() {

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        measure(w, h);

        int x = getMeasuredWidth();
        int y = getMeasuredHeight();
        return new Point(x, y);
    }

    public int getSizeX() {
        return mSizeX;
    }

    private void setSizeX(int mSizeX) {
        this.mSizeX = mSizeX;
    }

    public int getSizeY() {
        return mSizeY;
    }

    private void setSizeY(int mSizeY) {
        this.mSizeY = mSizeY;
    }
}
