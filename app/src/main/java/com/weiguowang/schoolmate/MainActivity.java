package com.weiguowang.schoolmate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements RoundSpinView.onRoundSpinViewListener {

    private RoundSpinView rsv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        rsv_test = (RoundSpinView)this.findViewById(R.id.rsv_test);
        rsv_test.setOnRoundSpinViewListener(this);
    }

    @Override
    public void onSingleTapUp(int position) {
        Toast.makeText(MainActivity.this, "place:"+position, Toast.LENGTH_SHORT).show();
    }

}
