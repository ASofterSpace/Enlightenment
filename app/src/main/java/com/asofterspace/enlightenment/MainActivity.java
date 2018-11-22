package com.asofterspace.enlightenment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.asofterspace.toolbox.web.WebAccessor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private BackendThread backendThread;

    private Set<BackendTarget> currentTargets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTargets = new HashSet<>();

        startupBackend();

        arrangeButtons();
    }

    private void startupBackend() {

        backendThread = new BackendThread();

        Thread actualThread = new Thread(backendThread);

        actualThread.start();
    }

    private void arrangeButtons() {

        final ToggleButton tHue1 = findViewById(R.id.tHue1);
        tHue1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (tHue1.isChecked()) {
                    currentTargets.add(BackendTarget.H1);
                } else {
                    currentTargets.remove(BackendTarget.H1);
                }
            }
        });

        final ToggleButton tHue2 = findViewById(R.id.tHue2);
        tHue1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (tHue2.isChecked()) {
                    currentTargets.add(BackendTarget.H2);
                } else {
                    currentTargets.remove(BackendTarget.H2);
                }
            }
        });

        final ToggleButton tHue3 = findViewById(R.id.tHue3);
        tHue3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (tHue3.isChecked()) {
                    currentTargets.add(BackendTarget.H3);
                } else {
                    currentTargets.remove(BackendTarget.H3);
                }
            }
        });

        final ToggleButton tBoulderwand = findViewById(R.id.tBoulderwand);
        tBoulderwand.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (tBoulderwand.isChecked()) {
                    currentTargets.add(BackendTarget.BW);
                } else {
                    currentTargets.remove(BackendTarget.BW);
                }
            }
        });

        Button bOff = findViewById(R.id.bOff);
        bOff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, 0, 0, 0));
            }
        });

        Button bDim = findViewById(R.id.bDim);
        bDim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, "dim"));
            }
        });

        Button bMedium = findViewById(R.id.bMedium);
        bMedium.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, "medium"));
            }
        });

        Button bMax = findViewById(R.id.bMax);
        bMax.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, 255, 255, 255));
            }
        });


        Button bRed = findViewById(R.id.bRed);
        bRed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, 255, 0, 0));
            }
        });

        Button bOrange = findViewById(R.id.bOrange);
        bOrange.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, 255, 128, 0));
            }
        });

        Button bYellow = findViewById(R.id.bYellow);
        bYellow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, 255, 255, 0));
            }
        });


        Button bGreen = findViewById(R.id.bGreen);
        bGreen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, 0, 255, 0));
            }
        });

        Button bTurquoise = findViewById(R.id.bTurquoise);
        bTurquoise.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, 0, 255, 255));
            }
        });

        Button bBlue = findViewById(R.id.bBlue);
        bBlue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, 0, 0, 255));
            }
        });


        Button bPurple = findViewById(R.id.bPurple);
        bPurple.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, 179, 0, 255));
            }
        });

        Button bBrown = findViewById(R.id.bBrown);
        bBrown.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, 128, 64, 0));
            }
        });

        Button bRainbow = findViewById(R.id.bRainbow);
        bRainbow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, "rainbow"));
            }
        });

        Button bPulse = findViewById(R.id.bPulse);
        bPulse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, "pulsred"));
            }
        });

        Button bTwinkle = findViewById(R.id.bTwinkle);
        bTwinkle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, "twinkle"));
            }
        });

        Button bStrobeRed = findViewById(R.id.bStrobeRed);
        bStrobeRed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(currentTargets, "strobored"));
            }
        });
    }
}
