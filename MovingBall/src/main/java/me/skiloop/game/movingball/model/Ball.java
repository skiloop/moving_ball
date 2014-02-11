package me.skiloop.game.movingball.model;

import android.graphics.Color;
import android.util.Log;

/**
 * Created by skiloop on 14-2-6.
 */
public class Ball {
    public static final String TAG = "me.skiloop.game.movingball.model.ball";
    public static final int DEFAULT_COLOR = Color.RED;
    public static final float DEFAULT_RADIUS = 30.0F;
    public static final float DEFAULT_X = 0.0F;
    public static final float DEFAULT_Y = 0.0F;
    public static final float DEFAULT_X_VELOCITY = 20.0F;
    public static final float DEFAULT_Y_VELOCITY = 20.0F;
    public static final float DEFAULT_X_ACCELEROMETER = 20.0F;
    public static final float DEFAULT_Y_ACCELEROMETER = 0.0F;
    public static final float DEFAULT_COEFFICIENT_OF_RESTITUTION = 1.0F;

    private float mXVelocity;              // velocity in x direction
    private float mYVelocity;              // velocity in y direction
    private float mX;                       // position in x direction
    private float mY;                       // position in y direction
    private float mXAccelerometer;        // accelerometer in x direction
    private float mYAccelerometer;        // accelerometer in y direction

    private float mRadius;                 // radius
    private int mColor;                     // color
    private float mCOR;                     // coefficient of restitution against wall

    public Ball() {
        this(DEFAULT_X, DEFAULT_Y, DEFAULT_X_VELOCITY, DEFAULT_Y_VELOCITY,
                DEFAULT_X_ACCELEROMETER, DEFAULT_Y_ACCELEROMETER,
                DEFAULT_RADIUS, DEFAULT_COLOR, DEFAULT_COEFFICIENT_OF_RESTITUTION);
    }

    public Ball(Ball ball) {
        setX(ball.getX());
        setY(ball.getY());
        setYVelocity(ball.getYVelocity());
        setXVelocity(ball.getXVelocity());
        setColor(ball.getColor());
        setRadius(ball.getRadius());
        setXAccelerometer(ball.getXAccelerometer());
        setYAccelerometer(ball.getYAccelerometer());
        setCOR(ball.getCOR());
    }

    public Ball(float x, float y, float x_velocity,
                float y_velocity, float x_accelerometer,
                float y_accelerometer, float radius,
                int color, float cor) {
        setX(x);
        setY(y);
        setXVelocity(x_velocity);
        setYVelocity(y_velocity);
        setXAccelerometer(x_accelerometer);
        setYAccelerometer(y_accelerometer);
        setRadius(radius);
        setColor(color);
        setCOR(cor);
    }

    public float getXVelocity() {
        return mXVelocity;
    }

    public void setXVelocity(float mXVelocity) {
        this.mXVelocity = mXVelocity;
    }

    public float getYVelocity() {
        return mYVelocity;
    }

    public void setYVelocity(float mYVelocity) {
        this.mYVelocity = mYVelocity;
    }

    public float getX() {
        return mX;
    }

    public void setX(float mX) {
        this.mX = mX;
    }

    public float getY() {
        return mY;
    }

    public void setY(float mY) {
        this.mY = mY;
    }

    public float getXAccelerometer() {
        return mXAccelerometer;
    }

    public void setXAccelerometer(float mXAccelerometer) {
        this.mXAccelerometer = mXAccelerometer;
    }

    public float getYAccelerometer() {
        return mYAccelerometer;
    }

    public void setYAccelerometer(float mYAccelerometer) {
        this.mYAccelerometer = mYAccelerometer;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float mRadius) {
        this.mRadius = mRadius;
    }

    public void setCOR(float cor) {
        mCOR = cor;
    }

    public float getCOR() {
        return mCOR;
    }

    /**
     * get travel time when something move ahead s with accelerometer a and initial velocity v0
     *
     * @param a
     * @param v0
     * @param s
     * @return
     */
    private float travelTime(float a, float v0, float s) {
//        if (s < 0) {
//            Log.d(TAG, "s < 0");
//            return Float.MAX_VALUE;
//        }
        if (0.0f != a) {
            float delta = v0 * v0 + 2.0f * a * s;
            if (delta < 0) {
                return Float.MAX_VALUE;
            }
            float abs = (float) Math.sqrt(delta);
            float b1 = (-v0 - abs) / a;
            float b2 = (-v0 + abs) / a;

//            float v1=nextVelocity(a,v0,b1);
//            float v2=nextVelocity(a,v0,b2);
            if (b1 < 0) {
                return b2;
            } else if (b2 < 0) {
                return b1;
            } else {
                return (b1 > b2 ? b2 : b1);
            }
        } else {
            return s / v0;
        }
    }

    /**
     * get velocity when something travel in t with initial velocity v0 and accelerometer a
     *
     * @param a
     * @param v0
     * @param t
     * @return
     */
    private float nextVelocity(float a, float v0, float t) {
        return v0 + a * t;
    }

    public void nextPos(float dt, float x_size, float y_size) {
        float nextVx = getXVelocity() + getXAccelerometer() * dt;
        float nextVy = getYVelocity() + getYAccelerometer() * dt;
        float nextX = getX() + (getXVelocity() + getXAccelerometer() * dt / 2) * dt;
        float nextY = getY() + (getYVelocity() + getYAccelerometer() * dt / 2) * dt;

        if (nextY < 0 || nextX < 0) {
            Log.d(TAG, "next x,y < 0");
        }
        // reach left side
        if (nextX - getRadius() < 0.0f && nextVx < 0) {
            float t1;
            float s = getX() - getRadius();
            t1 = travelTime(getXAccelerometer(), getXVelocity(), -s);
            if (getXAccelerometer() != 0.0f) {
                if (nextVelocity(getXAccelerometer(), getXVelocity(), t1) * getXVelocity() > 0) {
                    float tempVelocity = getXVelocity() + getXAccelerometer() * t1;
                    nextVx = -mCOR * tempVelocity + getXAccelerometer() * (dt - t1);
                    nextX = getRadius() + (-mCOR * tempVelocity + getXAccelerometer() * (dt - t1) / 2) * (dt - t1);
                }
            } else if (Float.MAX_VALUE != t1) {
                nextVx = -getCOR() * getXVelocity();
                t1 = (getX() - getRadius()) / -getXVelocity();
                nextX = nextVx * (dt - t1) + getRadius();
            }
        }
        // reach right size
        else if (nextX + mRadius > x_size && nextVx > 0) {
            float t1;
            float s = x_size - getX() - getRadius();
            t1 = travelTime(getXAccelerometer(), getXVelocity(), s);
            if (t1 != Float.MAX_VALUE && getXAccelerometer() != 0.0f) {
                if (nextVelocity(getXAccelerometer(), getXVelocity(), t1) * getXVelocity() > 0) {
                    float tempVelocity = getXVelocity() + getXAccelerometer() * t1;
                    nextVx = -mCOR * tempVelocity + getXAccelerometer() * (dt - t1);
                    nextX = x_size - getRadius()
                            - (-mCOR * tempVelocity
                            + getXAccelerometer() * (dt - t1) / 2) * (dt - t1);
                }
            } else if (Float.MAX_VALUE != t1) {
                nextVx = -getCOR() * getXVelocity();
                t1 = (x_size - getX() - getRadius()) / -getXVelocity();
                nextX = x_size + nextVx * (dt - t1) - getRadius();
            }
        }
        // reach bottom
        if (nextY - mRadius < 0.0f && nextVy < 0) {
            float t1;
            float s = getY() - getRadius();
            t1 = travelTime(getYAccelerometer(), getYVelocity(), -s);
            if (Float.MAX_VALUE != t1 && getYAccelerometer() != 0.0f) {
                if (nextVelocity(getYAccelerometer(), getYVelocity(), t1) * getYVelocity() > 0) {
                    float tempVelocity = getYVelocity() + getYAccelerometer() * t1;
                    nextVy = -mCOR * tempVelocity + getYAccelerometer() * (dt - t1);
                    nextY = getRadius() + (-mCOR * tempVelocity + getYAccelerometer() * (dt - t1) / 2) * (dt - t1);
                }
            } else if (Float.MAX_VALUE != t1) {
                nextVy = -getCOR() * getYVelocity();
                t1 = (getY() - getRadius()) / -getYVelocity();
                nextY = nextVy * (dt - t1) + getRadius();
            }
        }
        // reach top
        else if (nextY + mRadius > y_size && nextVy > 0) {
            float t1;
            float s = y_size - getY() - getRadius();
            t1 = travelTime(getYAccelerometer(), getYVelocity(), s);
            if (Float.MAX_VALUE != t1 && getYAccelerometer() != 0f) {
                if (nextVelocity(getYAccelerometer(), getYVelocity(), t1) * getYVelocity() > 0) {
                    float tempVelocity = getYVelocity() + getYAccelerometer() * t1;
                    nextVy = -mCOR * tempVelocity + getYAccelerometer() * (dt - t1);
                    nextY = y_size - getRadius() + (-mCOR * tempVelocity + getYAccelerometer() * (dt - t1) / 2) * (dt - t1);
                }
            } else if (Float.MAX_VALUE != t1) {
                nextVy = -getCOR() * getYVelocity();
                t1 = (y_size - getY() - getRadius()) / getYVelocity();
                nextY = y_size + nextVy * (dt - t1) - getRadius();
            }
        }

        if (nextY < 0 || nextX < 0) {
            Log.d(TAG, "next x,y < 0");
        }
        setX(nextX);
        setY(nextY);
        setXVelocity(nextVx);
        setYVelocity(nextVy);
    }

}
