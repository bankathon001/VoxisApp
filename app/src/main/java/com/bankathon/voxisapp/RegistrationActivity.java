package com.bankathon.voxisapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.bankathon.voxisapp.apis.AwsApiClient;
import com.bankathon.voxisapp.apis.request.RegisteredVoiceRequest;
import com.bankathon.voxisapp.apis.request.ValidatePinRequest;
import com.bankathon.voxisapp.apis.response.RegisteredVoiceStatus;
import com.bankathon.voxisapp.apis.response.Response;
import com.bankathon.voxisapp.apis.response.ValidatePinStatus;
import com.bankathon.voxisapp.util.AudioUtils;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;

public class RegistrationActivity extends Activity {
    OnSwipeTouchListener onSwipeTouchListener;
    String tapString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        onSwipeTouchListener = new OnSwipeTouchListener(this, findViewById(R.id.frame_layout), tapString);

        AudioUtils.textToSpeech("Input your debit card pin");
        AudioUtils.textToSpeech("Tap for digit and swipe right to confirm");
        while (true) {
            if (tapString.length() >= 4) {
                break;
            }
        }
        ValidatePinStatus status = validateDebitCardDetail(tapString);

        if (status.equals(ValidatePinStatus.SUCCESS)) {
            Intent i = new Intent(this.getApplicationContext(), BankActivity.class);
            startActivity(i);
            finish();
        } else {
            AudioUtils.textToSpeech("Debit pin is incorrect, Try Again");
            Intent i = new Intent(this.getApplicationContext(), RegistrationActivity.class);
            startActivity(i);
            finish();
        }
    }

    private ValidatePinStatus validateDebitCardDetail(String inputFromUser) {
        AtomicReference<ValidatePinStatus> response = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            ValidatePinRequest request = new ValidatePinRequest();
            request.setMobileNumber(AudioUtils.getMobileNumber());
            request.setDebitPin(inputFromUser);

            Call<Response> generateCaptcha =
                    AwsApiClient.getInstance().getMyApi().validateDebitCardPin(request);
            try {

                response.set(ValidatePinStatus.valueOf((String) generateCaptcha.execute().body().getBody()));
            } catch (IOException e) {
                Log.i(e.toString(), "");
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.i(e.toString(), "");
        }
        return response.get();
    }

    public static class OnSwipeTouchListener implements View.OnTouchListener {
        private final GestureDetector gestureDetector;
        Context context;
        String tapCountString;
        Integer tapCount;

        OnSwipeTouchListener(Context ctx, View mainView, String tapCountString) {
            gestureDetector = new GestureDetector(ctx, new GestureListener(tapCount));
            mainView.setOnTouchListener(this);
            this.context = ctx;
            this.tapCountString = tapCountString;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        public class GestureListener extends
                GestureDetector.SimpleOnGestureListener {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
            Integer tapCount;

            public GestureListener(Integer tapCount) {
                this.tapCount = 0;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                //Toast.makeText(context, "on Long Press", Toast.LENGTH_SHORT).show();
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
                this.tapCount++;
                if (this.tapCount > 9) {
                    AudioUtils.textToSpeech("You tap more than 9 time, tap again");
                    this.tapCount = 0;
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
                            //onSwipeBottom();
                        } else {
                            //onSwipeTop();
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
            //Toast.makeText(context, "Swiped Right", Toast.LENGTH_SHORT).show();
            tapCountString = tapCountString + this.tapCount;
            this.tapCount = 0;
            this.onSwipe.swipeRight();
        }

        void onSwipeLeft() {
            Toast.makeText(context, "Swiped Left", Toast.LENGTH_SHORT).show();
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