package com.share.yannis.booheeruler;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by yanniszone on 2017/11/8.
 * 尺子数值显示UI
 */

public class RulerValueDisplayView extends RelativeLayout implements RulerDataCallBack {
    private TextView valueTv;
    private TextView unitTv;
    private BooHeeRulerLayout booHeeRulerLayout;

    public RulerValueDisplayView(Context context) {
        super(context);
    }

    public RulerValueDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RulerValueDisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        valueTv = (TextView) findViewById(R.id.value_tv);
        unitTv = (TextView) findViewById(R.id.unit_tv);
        unitTv.setText(R.string.ruler_unit);
    }

    @Override
    public void onDataChanged(String value) {
        valueTv.setText(value);
    }

    public void setRulerLayout(BooHeeRulerLayout booHeeRulerLayout){
        this.booHeeRulerLayout = booHeeRulerLayout;
        booHeeRulerLayout.setOnRulerValueChangedListener(this);
    }
}
