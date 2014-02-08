package me.skiloop.game.movingball.model;

import android.graphics.Color;

/**
 * Created by skiloop on 14-2-6.
 */
public class Ball {
    public static final int DEFAULT_COLOR = Color.RED;
    public static final float DEFAULT_RADIUS = 30.0F;
    public static final float DEFAULT_X = 0.0F;
    public static final float DEFAULT_Y = 0.0F;
    public static final float DEFAULT_X_VELOCITY = 20.0F;
    public static final float DEFAULT_Y_VELOCITY = 20.0F;
    public static final float DEFAULT_X_ACCELEROMETER = 20.0F;
    public static final float DEFAULT_Y_ACCELEROMETER = 0.0F;
    public static final float DEFAULT_COEFFICIENT_OF_RESTITUTION=1.0F;

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
                DEFAULT_RADIUS, DEFAULT_COLOR,DEFAULT_COEFFICIENT_OF_RESTITUTION);
    }

    public Ball(Ball ball){
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

    public Ball(float x, float y, float x_velocity, float y_velocity, float x_accelerometer, float y_accelerometer, float radius, int color,float cor) {
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

    public void setCOR(float cor){
        mCOR=cor;
    }
    public float getCOR(){
        return  mCOR;
    }

    public void nextPos(float dt,float x_size,float y_size){
        float nextVx=getXVelocity()+getXAccelerometer()*dt;
        float nextVy=getYVelocity()+getYAccelerometer()*dt;
        float nextX=getX()+nextVx*dt;
        float nextY=getY()+nextVy*dt;

        // reach left side
        if(nextX-mRadius<0.0f){
            nextVx = -mCOR*getXVelocity();
            if(nextX<0)nextX=-nextX;
        }
        // reach right size
        else if(nextX+mRadius>x_size){
            nextVx = -mCOR*getXVelocity();
            if(nextX>x_size)nextX=2*x_size-nextX;
        }
        // reach bottom
        if(nextY-mRadius<0.0f){
            nextVy = -mCOR*getYVelocity();
            if(nextY<0)nextY=-nextY;
        }
        // reach top
        else if(nextY+mRadius>y_size){
            nextVy = -mCOR*getYVelocity();
            if(nextY>y_size)nextY=2*y_size-nextY;
        }

        setX(nextX);
        setY(nextY);
        setXVelocity(nextVx);
        setYVelocity(nextVy);
    }

}
