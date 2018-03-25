package com.example.xzper.server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.xzper.android.server.Server;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String address = Server.start(this);
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(address);
    }
}
