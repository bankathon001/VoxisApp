package com.bankathon.voxisapp;

import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bankathon.voxisapp.R;
import com.bankathon.voxisapp.util.AudioUtils;

import androidx.appcompat.app.AppCompatActivity;

public class SwipeActivity extends AppCompatActivity {
    OnSwipeTouchListener onSwipeTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button_act);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SwipeActivity.this, VoiceActivity.class);
                startActivity(myIntent);
            }
        });
        onSwipeTouchListener = new OnSwipeTouchListener(SwipeActivity.this, this, findViewById(R.id.relativeLayout));
    }

    public static class OnSwipeTouchListener implements View.OnTouchListener {
        private final GestureDetector gestureDetector;
        Context context;
        SwipeActivity mainActivity;

        OnSwipeTouchListener(SwipeActivity mainActivity, Context ctx, View mainView) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
            mainView.setOnTouchListener(this);
            context = ctx;
            mainActivity = mainActivity;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        public class GestureListener extends
                GestureDetector.SimpleOnGestureListener {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
            long lastTapTimestamp;
            int tapCount;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Toast.makeText(context, "on Long Press", Toast.LENGTH_SHORT).show();
                super.onLongPress(e);
            }

            /*@Override
            public boolean onDoubleTap(MotionEvent e) {
                Toast.makeText(context, "on Double Tap", Toast.LENGTH_SHORT).show();
                return super.onDoubleTap(e);
            }
            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                Toast.makeText(context, "on Double Tap Event", Toast.LENGTH_SHORT).show();
                return super.onDoubleTapEvent(e);
            }*/

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                //Toast.makeText(context, "on Single Tap Confirmed", Toast.LENGTH_SHORT).show();
                if (lastTapTimestamp == 0) {
                    lastTapTimestamp = System.currentTimeMillis();
                    tapCount = 1;
                } else {
                    long tapDiffTime = ((System.currentTimeMillis() - lastTapTimestamp) / 1000);
                    System.out.println("diff in sec: " + tapDiffTime);
                    if (tapDiffTime > 1) {
                        Toast.makeText(context, "last tapped count is: " + tapCount, Toast.LENGTH_SHORT).show();
                        lastTapTimestamp = 0;
                    } else {
                        lastTapTimestamp = System.currentTimeMillis();
                        tapCount++;
                    }
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        void onSwipeRight() {
            Toast.makeText(context, "Swiped Right", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeRight();
        }

        void onSwipeLeft() {
            Toast.makeText(context, "Swiped Left", Toast.LENGTH_SHORT).show();
            //AudioUtils.convertBytesToFile(new byte[10], context);
            this.onSwipe.swipeLeft();
        }

        void onSwipeTop() {
            Toast.makeText(context, "Swiped Up", Toast.LENGTH_SHORT).show();

            this.onSwipe.swipeTop();
        }

        void onSwipeBottom() {
            Toast.makeText(context, "Swiped Down", Toast.LENGTH_SHORT).show();

            this.onSwipe.swipeBottom();
        }

        interface onSwipeListener {
            void swipeRight();

            void swipeTop();

            void swipeBottom();

            void swipeLeft();
        }

        onSwipeListener onSwipe;
    }
}