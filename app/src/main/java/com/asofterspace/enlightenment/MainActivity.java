package com.asofterspace.enlightenment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.asofterspace.toolbox.Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public final static String PROGRAM_TITLE = "Enlightenment";
    public final static String VERSION_NUMBER = "0.0.1.0(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
    public final static String VERSION_DATE = "29. April 2018 - 25. November 2018";

    private BackendThread backendThread;

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

    private void startupBackend() {

        backendThread = new BackendThread();

        Thread actualThread = new Thread(backendThread);

        actualThread.start();
    }

    private void arrangeButtons() {

        Button bOff = findViewById(R.id.bOff);
        bOff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), 0, 0, 0));
            }
        });

        Button bDim = findViewById(R.id.bDim);
        bDim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), "dim"));
            }
        });

        Button bMedium = findViewById(R.id.bMedium);
        bMedium.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), "medium"));
            }
        });

        Button bMax = findViewById(R.id.bMax);
        bMax.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), 255, 255, 255));
            }
        });


        Button bRed = findViewById(R.id.bRed);
        bRed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), 255, 0, 0));
            }
        });

        Button bOrange = findViewById(R.id.bOrange);
        bOrange.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), 255, 128, 0));
            }
        });

        Button bYellow = findViewById(R.id.bYellow);
        bYellow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), 255, 255, 0));
            }
        });


        Button bGreen = findViewById(R.id.bGreen);
        bGreen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), 0, 255, 0));
            }
        });

        Button bTurquoise = findViewById(R.id.bTurquoise);
        bTurquoise.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), 0, 255, 255));
            }
        });

        Button bBlue = findViewById(R.id.bBlue);
        bBlue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), 0, 0, 255));
            }
        });


        Button bPurple = findViewById(R.id.bPurple);
        bPurple.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), 179, 0, 255));
            }
        });

        Button bBrown = findViewById(R.id.bBrown);
        bBrown.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), 128, 64, 0));
            }
        });

        Button bRainbow = findViewById(R.id.bRainbow);
        bRainbow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), "rainbow"));
            }
        });

        Button bPulse = findViewById(R.id.bPulse);
        bPulse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), "pulsred"));
            }
        });

        Button bTwinkle = findViewById(R.id.bTwinkle);
        bTwinkle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), "twinkle"));
            }
        });

        Button bStrobeRed = findViewById(R.id.bStrobeRed);
        bStrobeRed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(getCurrentTargets(), "strobored"));
            }
        });
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

        ToggleButton tBoulderwand = findViewById(R.id.tBoulderwand);
        if (tBoulderwand.isChecked()) {
            result.add(BackendTarget.BW);
        }

        return result;
    };

}
