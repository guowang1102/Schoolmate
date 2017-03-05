package com.weiguowang.schoolmate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity implements RoundSpinView.onRoundSpinViewListener {

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
        // TODO Auto-generated method stub
        switch (position) {
            case 0:
                Toast.makeText(MainActivity.this, "place:0", 0).show();
                break;
            case 1:
                Toast.makeText(MainActivity.this, "place:1", 0).show();
                break;
            case 2:
                Toast.makeText(MainActivity.this, "place:2", 0).show();
                break;
            default:
                break;
        }
    }

}
