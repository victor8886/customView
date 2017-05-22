package com.zhen.victor.custemview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.zhen.victor.custemview.view.RemoteControlView;

public class MainActivity extends AppCompatActivity implements RemoteControlView.MenuListener {

    private RemoteControlView controlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controlView = (RemoteControlView) findViewById(R.id.remote);
        controlView.setListener(this);
    }

    @Override
    public void onCenterCliched() {
        Toast.makeText(this, "中", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpCliched() {
        Toast.makeText(this, "上", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRightCliched() {
        Toast.makeText(this, "右", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownCliched() {
        Toast.makeText(this, "下", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLeftCliched() {
        Toast.makeText(this, "左", Toast.LENGTH_SHORT).show();
    }
}
