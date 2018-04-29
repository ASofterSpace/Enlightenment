package com.asofterspace.enlightenment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.asofterspace.toolbox.web.WebAccessor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WebAccessor webAccessor = new WebAccessor();

        Button bOff = findViewById(R.id.bOff);
        bOff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO :: do not hardcode the IP address, but instead discover it!
                // TODO :: do not hardcode an authorized user, but instead generate an authorization on the device and store it!
                webAccessor.put("http://192.168.178.21/api/L59MkdWe78VkT916JWAhYf4cLAWmNLmS3s3vAzVR/lights/4/state", "{\"on\":false}");
            }
        });

        Button bDim = findViewById(R.id.bDim);
        bDim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                webAccessor.put("http://192.168.178.21/api/L59MkdWe78VkT916JWAhYf4cLAWmNLmS3s3vAzVR/lights/4/state", "{\"on\":true, \"sat\":254, \"bri\":254,\"hue\":49000}");
            }
        });

        // TODO :: add the other buttons
    }
}
