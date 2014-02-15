package me.skiloop.game.movingball;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.*;

import me.skiloop.game.movingball.model.Ball;
import me.skiloop.game.movingball.model.BallField;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = "MainActivity";
    public static final int REQUEST_SETTING = -1;
    private static final int BALL_MOVE_MESSAGE = 0x123;
    private static final float DEFAULT_TIME_DELTA_FLOAT = 0.2F;

    protected static final String DELTA_TIME_STRING = "delta_time";
    protected static final String DELTA_T_FLOAT = "delta_t_float";
    protected static final String BALL_FIELD = "ball_field";
    protected static final String USE_GRAVITY = "use_gravity";

    private BallField mBallField;
    private BoardView mBoardView;

    private float mDt = DEFAULT_TIME_DELTA_FLOAT;

    private int mDeltaTime = 150;
    private int mHalfDeltaTime;

    private boolean mUseGravity = true;
    private boolean mUseRandomAccelerometer = false;

    private float mGravityCoefficients = 1.0f;

    private SensorManager mSensorManager;

    private Timer mTimer;

    private SensorEventListener mSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (Sensor.TYPE_ACCELEROMETER == event.sensor.getType()) {
                for (Ball ball : mBallField.getmBalls()) {
                    ball.setXAccelerometer(event.values[0] * mGravityCoefficients);
                    ball.setYAccelerometer(event.values[1] * mGravityCoefficients);
                }
                Ball ball = mBallField.getmBall();
                ball.setXAccelerometer(-event.values[0] * mGravityCoefficients);
                ball.setYAccelerometer(event.values[1] * mGravityCoefficients);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DummyFragment())
                    .commit();
        }

        mBallField = new BallField(mDt);

        mBoardView = (BoardView) findViewById(R.id.ball_board);

//        ViewTreeObserver viewTreeObserver=mBoardView.getViewTreeObserver();
//
//        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                if(!hasMeasured){
//                    int width=mBoardView.getMeasuredWidth();
//                    int height=mBoardView.getMeasuredHeight();
//
//                    mBallField.setmSizeX(width);
//                    mBallField.setmSizeY(height);
//                    mBoardView.setY(height);
//                    mBoardView.setX(width);
//                    hasMeasured=true;
//                }
//                return true;
//            }
//        });

        mHalfDeltaTime = mDeltaTime / 2;
        mBoardView.setBallField(mBallField);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == BALL_MOVE_MESSAGE) {
                    mBoardView.invalidate();
                }
            }
        };
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                mBallField.moveBalls();

//                mBoardView.invalidate();
                Message msg = new Message();
                msg.what = BALL_MOVE_MESSAGE;
                handler.sendMessageAtTime(msg, mHalfDeltaTime);
            }
        }, 0, mDeltaTime);

        // sensors
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        if (mUseGravity) {
            mSensorManager.unregisterListener(mSensorEventListener);
        }
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        if (mUseGravity) {
            mSensorManager.unregisterListener(mSensorEventListener);
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        SharedPreferences gameSettings =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mBallField.disableRandomMove();
        mUseGravity = gameSettings.getBoolean(getResources().getString(R.string.use_gravity), true);
        if (!mUseGravity) {
            mUseRandomAccelerometer = gameSettings.getBoolean(getResources().getString(R.string.random_accelerometer), false);
        }
        mBoardView.setInnerBackgroundColor(
                gameSettings.getInt(getString(R.string.theme_inner_bg_color), BoardView.NO_COLOR));
        mBoardView.setOuterBackgroundColor(
                gameSettings.getInt(getString(R.string.theme_outer_bg_color), BoardView.NO_COLOR));
        mDeltaTime = Integer.parseInt(gameSettings.getString(
                getString(R.string.update_time), "150"));

        Ball ball = mBallField.getmBall();

        ball.setX(Float.parseFloat(gameSettings.getString(
                getResources().getString(R.string.ball_x), "100f")));
        ball.setY(Float.parseFloat(gameSettings.getString(
                getResources().getString(R.string.ball_y), "110")));

        ball.setXVelocity(Float.parseFloat(gameSettings.getString(
                getResources().getString(R.string.ball_v_x), "10")));
        ball.setYVelocity(Float.parseFloat(gameSettings.getString(
                getResources().getString(R.string.ball_v_y), "10")));
        ball.setCOR(Float.parseFloat(gameSettings.getString(
                getString(R.string.coefficients_of_restitution), "1.0")));
        ball.setRadius(Float.parseFloat(gameSettings.getString(
                getString(R.string.ball_radius), "30")));
        ball.setColor(gameSettings.getInt(getString(R.string.ball_color_setting), Color.RED));


        if (mUseGravity) {
            mGravityCoefficients = Float.parseFloat(gameSettings.getString(
                    getString(R.string.gravity_coefficients), "1.0"));
            ball.setXAccelerometer(0.0f);
            ball.setYAccelerometer(0.0f);
            mSensorManager.registerListener(mSensorEventListener,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_GAME);
        } else if (mUseRandomAccelerometer) {
            mBallField.enableRandomMove();
            mBallField.setMaxRandomXAccelerometer(Float.parseFloat(
                    gameSettings.getString(getString(R.string.max_x_accelerometer), "30.0")));
            mBallField.setMaxRandomYAccelerometer(Float.parseFloat(
                    gameSettings.getString(getString(R.string.max_y_accelerometer), "30.0")));
        } else {
            ball.setXAccelerometer(Float.parseFloat(
                    gameSettings.getString(getResources().getString(R.string.accelerometer_x), "0")));
            ball.setYAccelerometer(Float.parseFloat(
                    gameSettings.getString(getResources().getString(R.string.accelerometer_y), "0")));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_SETTING == requestCode) {
            startActivity(getIntent());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, REQUEST_SETTING);
                return true;
//            case R.id.obg_color:
//                Toast.makeText(this, getResources().getString(R.string.obg_color)
//                        , Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.ibg_color:
//                Toast.makeText(this, getResources().getString(R.string.ibg_color)
//                        , Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.ball_color:
//                Toast.makeText(this, getResources().getString(R.string.ball_color)
//                        , Toast.LENGTH_SHORT).show();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A dummy fragment containing a simple view.
     */
    public static class DummyFragment extends Fragment {

        public DummyFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
