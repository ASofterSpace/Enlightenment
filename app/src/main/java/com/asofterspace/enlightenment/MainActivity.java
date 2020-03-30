package com.asofterspace.enlightenment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.asofterspace.toolbox.Utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public final static String PROGRAM_TITLE = "Enlightenment";
    public final static String VERSION_NUMBER = "0.0.1.9(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
    public final static String VERSION_DATE = "29. April 2018 - 30. March 2020";

    // if we are including Bene's light in the app, set this to true, and if not, set it to false
    private boolean BENE_VERSION = false;

    private static BackendThread backendThread;

    // TIMER FUNK :D
    private boolean timerRunning = false;
    private int timerHours;
    private int timerMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // let the Utils know in what program it is being used
        Utils.setProgramTitle(PROGRAM_TITLE);
        Utils.setVersionNumber(VERSION_NUMBER);
        Utils.setVersionDate(VERSION_DATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startupBackend();

        arrangeButtons();
    }

    private static void startupBackend() {

        if (backendThread == null) {

            backendThread = new BackendThread();

            Thread actualThread = new Thread(backendThread);

            actualThread.start();
        }
    }

    public static BackendThread getBackendThread() {

        startupBackend();

        return backendThread;
    }

    private void arrangeButtons() {

        ToggleButton tBene = findViewById(R.id.tBene);
        final EditText tBeneTime = findViewById(R.id.tBeneTime);
        if (BENE_VERSION) {
            tBene.setVisibility(View.VISIBLE);
            tBeneTime.setVisibility(View.VISIBLE);

            tBeneTime.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_ACTION_DONE) {
                        // hide keyboard
                        InputMethodManager imm = (InputMethodManager)textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                        // by default, unset the timer (well, we also don't want it to start doing
                        // stuff while the person is still entering data, etc.)
                        unsetTimer();

                        String toastText = "The timer was un-set (because you did not enter a valid time.)";

                        // we use a while (true) because what we really want to do it jump around
                        // with gotos, but some idiot decided that gotos are a bad idea, so instead
                        // we use a while and when we want to jump to its end we break... yay!
                        while (true) {
                            String timerText = tBeneTime.getText().toString();

                            if (timerText == null) {
                                break; // GOTO toast
                            }

                            if (timerText.equals("")) {
                                break; // GOTO toast
                            }

                            String timerMinutes = "";
                            String timerHour = "";

                            if (timerText.contains(":")) {
                                String[] timerTexts = timerText.split(":");
                                timerHour = timerTexts[0];
                                timerMinutes = timerTexts[1];
                            } else {
                                timerHour = timerText;
                            }

                            timerHour = timerHour.trim();
                            timerMinutes = timerMinutes.trim();

                            // convert both hours and minutes to int, taking 0 if unrecognized
                            int iTimerHour;
                            int iTimerMinutes;

                            try {
                                iTimerHour = Integer.parseInt(timerHour);
                            } catch (NumberFormatException e) {
                                iTimerHour = 0;
                            }

                            try {
                                iTimerMinutes = Integer.parseInt(timerMinutes);
                            } catch (NumberFormatException e) {
                                iTimerMinutes = 0;
                            }

                            // perform some sanity checks
                            if (iTimerHour < 0) {
                                toastText = "The timer was un-set (because hours are not allowed to be negative.)";
                                break; // GOTO toast
                            }
                            if (iTimerHour > 23) {
                                toastText = "The timer was un-set (because hours are not allowed to be above 23.)";
                                break; // GOTO toast
                            }
                            if (iTimerMinutes < 0) {
                                toastText = "The timer was un-set (because minutes are not allowed to be negative.)";
                                break; // GOTO toast
                            }
                            if (iTimerMinutes > 59) {
                                toastText = "The timer was un-set (because minutes are not allowed to be above 60.)";
                                break; // GOTO toast
                            }

                            // convert the time back to a string to display it on toast - nom nom!
                            String timerStr = ""+iTimerMinutes;
                            if (timerStr.length() < 2) {
                                timerStr = "0" + timerStr;
                            }
                            timerStr = iTimerHour + ":" + timerStr;
                            if (timerStr.length() < 5) {
                                timerStr = "0" + timerStr;
                            }

                            int timerResult = setTimer(iTimerHour, iTimerMinutes) / 1000;

                            toastText = "The timer was set to " + timerStr + ",\nwhich is in " + timerResult + " seconds.\nMeow! :)";
                            break; // GOTO toast (well, just fall out of the while...)
                        }

                        Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_LONG).show();

                        return true;
                    }
                    return false;
                }
            });
        } else {
            tBene.setVisibility(View.INVISIBLE);
            tBeneTime.setVisibility(View.INVISIBLE);
        }

        View.OnTouchListener touchToClick = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.callOnClick();
                    return true;
                }
                return false;
            }
        };


        final ImageView bLightPicker = findViewById(R.id.bLightPicker);
        bLightPicker.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                int x = (int)event.getX();
                final Bitmap bLightPickerBitmap = ((BitmapDrawable) bLightPicker.getDrawable()).getBitmap();
                if (x < 0) {
                    x = 0;
                }
                try {
                    int pixel = bLightPickerBitmap.getPixel(x, 0);
                    backendThread.performTask(new BackendTask(getCurrentTargets(), "brightness:" + Color.green(pixel)));
                } catch (Exception e) {
                    // whoops!
                }
                return true;
            }
        });


        Button bOff = findViewById(R.id.bOff);
        bOff.setOnTouchListener(touchToClick);
        bOff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bOff);
                backendThread.performTask(new BackendTask(getCurrentTargets(), "off"));
            }
        });

        Button bDim = findViewById(R.id.bDim);
        bDim.setOnTouchListener(touchToClick);
        bDim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bDim);
                backendThread.performTask(new BackendTask(getCurrentTargets(), "dim"));
            }
        });

        Button bMedium = findViewById(R.id.bMedium);
        bMedium.setOnTouchListener(touchToClick);
        bMedium.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bMedium);
                backendThread.performTask(new BackendTask(getCurrentTargets(), "medium"));
            }
        });

        Button bMax = findViewById(R.id.bMax);
        bMax.setOnTouchListener(touchToClick);
        bMax.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bMax);
                backendThread.performTask(new BackendTask(getCurrentTargets(), "max"));
            }
        });


        final ImageView bColorPicker = findViewById(R.id.bColorPicker);
        bColorPicker.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                int x = (int)event.getX();
                final Bitmap bColorPickerBitmap = ((BitmapDrawable) bColorPicker.getDrawable()).getBitmap();
                if (x < 0) {
                    x = 0;
                }
                try {
                    int pixel = bColorPickerBitmap.getPixel(x, 0);
                    backendThread.performTask(new BackendTask(getCurrentTargets(), Color.red(pixel), Color.green(pixel), Color.blue(pixel)));
                } catch (Exception e) {
                    // whoops!
                }
                return true;
            }
        });


        Button bRed = findViewById(R.id.bRed);
        bRed.setOnTouchListener(touchToClick);
        bRed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bRed);
                backendThread.performTask(new BackendTask(getCurrentTargets(), 255, 0, 0));
            }
        });

        Button bOrange = findViewById(R.id.bOrange);
        bOrange.setOnTouchListener(touchToClick);
        bOrange.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bOrange);
                backendThread.performTask(new BackendTask(getCurrentTargets(), 255, 128, 0));
            }
        });

        Button bYellow = findViewById(R.id.bYellow);
        bYellow.setOnTouchListener(touchToClick);
        bYellow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bYellow);
                backendThread.performTask(new BackendTask(getCurrentTargets(), 255, 255, 0));
            }
        });

        Button bGreen = findViewById(R.id.bGreen);
        bGreen.setOnTouchListener(touchToClick);
        bGreen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bGreen);
                backendThread.performTask(new BackendTask(getCurrentTargets(), 0, 255, 0));
            }
        });

        Button bTurquoise = findViewById(R.id.bTurquoise);
        bTurquoise.setOnTouchListener(touchToClick);
        bTurquoise.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bTurquoise);
                backendThread.performTask(new BackendTask(getCurrentTargets(), 0, 255, 255));
            }
        });

        Button bBlue = findViewById(R.id.bBlue);
        bBlue.setOnTouchListener(touchToClick);
        bBlue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bBlue);
                backendThread.performTask(new BackendTask(getCurrentTargets(), 0, 0, 255));
            }
        });

        Button bPurple = findViewById(R.id.bPurple);
        bPurple.setOnTouchListener(touchToClick);
        bPurple.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bPurple);
                backendThread.performTask(new BackendTask(getCurrentTargets(), 179, 0, 255));
            }
        });

        Button bBrown = findViewById(R.id.bBrown);
        bBrown.setOnTouchListener(touchToClick);
        bBrown.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bBrown);
                backendThread.performTask(new BackendTask(getCurrentTargets(), 128, 64, 0));
            }
        });

        Button bRainbow = findViewById(R.id.bRainbow);
        bRainbow.setOnTouchListener(touchToClick);
        bRainbow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bRainbow);
                backendThread.performTask(new BackendTask(getCurrentTargets(), "rainbow"));
            }
        });

        Button bPulse = findViewById(R.id.bPulse);
        bPulse.setOnTouchListener(touchToClick);
        bPulse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bPulse);
                backendThread.performTask(new BackendTask(getCurrentTargets(), "pulsred"));
            }
        });

        Button bTwinkle = findViewById(R.id.bTwinkle);
        bTwinkle.setOnTouchListener(touchToClick);
        bTwinkle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bTwinkle);
                backendThread.performTask(new BackendTask(getCurrentTargets(), "twinkle"));
            }
        });

        Button bStrobeRed = findViewById(R.id.bStrobeRed);
        bStrobeRed.setOnTouchListener(touchToClick);
        bStrobeRed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClickAnimation(R.id.bStrobeRed);
                backendThread.performTask(new BackendTask(getCurrentTargets(), "strobored"));
            }
        });
    }

    private void showClickAnimation(final int id) {

        findViewById(id).setBackgroundColor(Color.parseColor("#AA00FF"));

        Runnable animationEnder = new Runnable() {
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                findViewById(id).setBackgroundColor(Color.parseColor("#4C0081"));
            }
        };

        Thread actualThread = new Thread(animationEnder);

        actualThread.start();
    }

    private Set<BackendTarget> getCurrentTargets() {

        Set<BackendTarget> result = new HashSet<>();

        ToggleButton tHue1 = findViewById(R.id.tHue1);
        if (tHue1.isChecked()) {
            result.add(BackendTarget.H1);
        }

        ToggleButton tHue2 = findViewById(R.id.tHue2);
        if (tHue2.isChecked()) {
            result.add(BackendTarget.H2);
        }

        ToggleButton tHue3 = findViewById(R.id.tHue3);
        if (tHue3.isChecked()) {
            result.add(BackendTarget.H3);
        }

        ToggleButton tHue4 = findViewById(R.id.tHue4);
        if (tHue4.isChecked()) {
            result.add(BackendTarget.H4);
        }

        ToggleButton tHue5 = findViewById(R.id.tHue5);
        if (tHue5.isChecked()) {
            result.add(BackendTarget.H5);
        }

        ToggleButton tBoulderwand = findViewById(R.id.tBoulderwand);
        if (tBoulderwand.isChecked()) {
            result.add(BackendTarget.BW);
        }

        ToggleButton tWohnzimmer = findViewById(R.id.tWohnzimmer);
        if (tWohnzimmer.isChecked()) {
            result.add(BackendTarget.WZ);
        }

        if (BENE_VERSION) {
            ToggleButton tBene = findViewById(R.id.tBene);
            if (tBene.isChecked()) {
                result.add(BackendTarget.BENE);
            }
        }

        return result;
    }

    private AlarmManager alarmManager;

    /**
     * We set a timer for performing the wakeup sunrise functionality.
     * @param hours .. the hours at which the timer should activate (NOT further sanity checked!)
     * @param minutes .. the minutes at which the timer should activate (NOT further checked!)
     */
    public int setTimer(int hours, int minutes) {

        timerRunning = true;
        timerHours = hours;
        timerMinutes = minutes;

        Calendar now = Calendar.getInstance();
        int timediffInMinutes = (hours * 60 + minutes) - (now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE));
        if (timediffInMinutes <= 0) {
            timediffInMinutes += 24*60;
        }
        int timediffInMSec = timediffInMinutes*60*1000;

        if (alarmManager == null) {
            alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        }

        Intent intent = new Intent(this, TimerCalled.class);
        PendingIntent timerIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + timediffInMSec,
                timerIntent
        );

        // immediately turn off the light, as we are going to sleep :)
        Set<BackendTarget> targets = new HashSet<>();
        targets.add(BackendTarget.BENE);
        backendThread.performTask(new BackendTask(targets, 0, 0, 0));

        return timediffInMSec;
    }

    public void unsetTimer() {
        timerRunning = false;
    }

}
