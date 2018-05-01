package com.asofterspace.enlightenment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.asofterspace.toolbox.web.WebAccessor;

public class MainActivity extends AppCompatActivity {

    public static AppCompatActivity debugParent;

    private BackendThread backendThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        debugParent = this;

        backendThread = new BackendThread();
        Thread actualThread = new Thread(backendThread);
        actualThread.start();

        Button bWzOff = findViewById(R.id.bWzOff);
        bWzOff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(BackendTarget.WZ, 0, 0, 0));
            }
        });

        Button bWzDim = findViewById(R.id.bWzDim);
        bWzDim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(BackendTarget.WZ, 50, 50, 50));
            }
        });

        Button bWzMax = findViewById(R.id.bWzMax);
        bWzMax.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(BackendTarget.WZ, 255, 255, 255));
            }
        });


        Button bBwOff = findViewById(R.id.bBwOff);
        bBwOff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(BackendTarget.BW, 0, 0, 0));
            }
        });

        Button bBwDim = findViewById(R.id.bBwDim);
        bBwDim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(BackendTarget.BW, 50, 50, 50));
            }
        });

        Button bBwMax = findViewById(R.id.bBwMax);
        bBwMax.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(BackendTarget.BW, 255, 255, 255));
            }
        });


        Button bAllOff = findViewById(R.id.bAllOff);
        bAllOff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(BackendTarget.ALL, 0, 0, 0));
            }
        });

        Button bAllDim = findViewById(R.id.bAllDim);
        bAllDim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(BackendTarget.ALL, 50, 50, 50));
            }
        });

        Button bAllMax = findViewById(R.id.bAllMax);
        bAllMax.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                backendThread.performTask(new BackendTask(BackendTarget.ALL, 255, 255, 255));
            }
        });
    }
}
