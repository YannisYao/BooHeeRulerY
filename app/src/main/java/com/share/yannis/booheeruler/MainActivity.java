package com.share.yannis.booheeruler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private RulerValueDisplayView displayView;
    private BooHeeRulerLayout rulerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayView = (RulerValueDisplayView) findViewById(R.id.value_view);
        rulerLayout = (BooHeeRulerLayout) findViewById(R.id.ruler_latout);
        displayView.setRulerLayout(rulerLayout);
    }
}
