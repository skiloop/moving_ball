package me.skiloop.game.movingball;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.Toast;

import me.skiloop.game.movingball.model.Ball;
import me.skiloop.game.movingball.model.BallField;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

public class MainActivity extends ActionBarActivity {
    public static final int REQUEST_SETTING = -1;
    private BallField mBallField;
    private BoardView mBoardView;
    private static final int BALL_MOVE_MESSAGE = 0x123;
    private float mDt = 0.1f;

    private int mDeltaTime;
    private int mHalfDeltaTime;
    private boolean hasMeasured = false;

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
        mDeltaTime = 300;
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
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mBallField.moveBalls();
//                mBoardView.invalidate();
                Message msg = new Message();
                msg.what = BALL_MOVE_MESSAGE;
                handler.sendMessageAtTime(msg, mHalfDeltaTime);
            }
        }, 0, mDeltaTime);


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
            case R.id.obg_color:
                Toast.makeText(this, getResources().getString(R.string.obg_color)
                        , Toast.LENGTH_SHORT).show();
                return true;
            case R.id.ibg_color:
                Toast.makeText(this, getResources().getString(R.string.ibg_color)
                        , Toast.LENGTH_SHORT).show();
                return true;
            case R.id.ball_color:
                Toast.makeText(this, getResources().getString(R.string.ball_color)
                        , Toast.LENGTH_SHORT).show();
                return true;
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
