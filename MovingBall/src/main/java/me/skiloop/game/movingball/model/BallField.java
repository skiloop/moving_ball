package me.skiloop.game.movingball.model;

import java.util.*;

/**
 * Created by skiloop on 14-2-7.
 */
public class BallField {
    public static final int DEFAULT_BALL_COUNT=1;
    public static final float DEFAULT_SIZE_X=1000.0F;
    public static final float DEFAULT_SIZE_Y=1100.0F;
    public static final float DEFAULT_DELTA_T=1.0f;
    private float mSizeX;
    private float mSizeY;

    private Set<Ball> mBalls;

    private float mDeltaT;

    private Ball mBall;

    public BallField(float dt){
        this(DEFAULT_SIZE_X,DEFAULT_SIZE_Y,dt);
    }

    public BallField(){
        this(DEFAULT_SIZE_X,DEFAULT_SIZE_Y,DEFAULT_DELTA_T);
    }
    public BallField(int ballCount,float size_x,float size_y,float deltaT){
        setmSizeX(size_x);
        setmSizeY(size_y);
        setmDeltaT(deltaT);
        setmBalls(new HashSet<Ball>(ballCount));

        setmBall(new Ball());

        for(int i=1;i<=ballCount;i++){
            Ball ball=new Ball();
            //ball.setYAccelerometer(0.0f);
            //ball.setYVelocity(0.0f);
            getmBalls().add(new Ball(ball));
        }
        //initBalls();
    }

    public BallField(float size_x,float size_y,float deltaT){
        this(DEFAULT_BALL_COUNT,size_x,size_y,deltaT);
    }



    private void initBalls(){
        Random rnd=new Random();
        for(Ball ball: getmBalls()){
            ball.setRadius(rnd.nextFloat()*30f);
            ball.setX(rnd.nextFloat()* 300f);
            ball.setY(rnd.nextFloat()* 400f);
        }
    }

    public void moveBalls(){
//        for(Ball ball: getmBalls()){
//            ball.nextPos(getmDeltaT(), getmSizeX(), getmSizeY());
//        }
        mBall.nextPos(getmDeltaT(),getmSizeX(),getmSizeY());
    }

    public Set<Ball> getmBalls() {
        return mBalls;
    }

    public void setmBalls(Set<Ball> mBalls) {
        this.mBalls = mBalls;
    }

    public float getmSizeX() {
        return mSizeX;
    }

    public void setmSizeX(float mSizeX) {
        this.mSizeX = mSizeX;
    }

    public float getmSizeY() {
        return mSizeY;
    }

    public void setmSizeY(float mSizeY) {
        this.mSizeY = mSizeY;
    }

    public float getmDeltaT() {
        return mDeltaT;
    }

    public void setmDeltaT(float mDeltaT) {
        this.mDeltaT = mDeltaT;
    }

    public Ball getmBall() {
        return mBall;
    }

    public void setmBall(Ball mBall) {
        this.mBall = mBall;
    }
}
