package com.share.yannis.booheeruler;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Yannis on 2017/10/30.
 */

public class BooHeeRulerLayout extends RelativeLayout {
    private BooHeeRulerView rulerView;
    private int midLineColor = 0xFF72BC49;
    private float midLineHeight = 140f;
    private float midLineWidth = 7f;

    public BooHeeRulerLayout(Context context) {
        super(context);
        initView(context);
    }

    public BooHeeRulerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context,attrs);
        initView(context);
    }


    public BooHeeRulerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context,attrs);
        initView(context);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray  a = context.obtainStyledAttributes(attrs,R.styleable.BooHeeRulerLayout);
        midLineColor = a.getColor(R.styleable.BooHeeRulerLayout_midLineColor,0xFF72BC49);
        midLineHeight = a.getDimension(R.styleable.BooHeeRulerLayout_midLineHeight,140f);
        midLineWidth = a.getDimension(R.styleable.BooHeeRulerLayout_midLineWidth,7f);
        a.recycle();
    }

    private void initView(Context context) {
        rulerView = (BooHeeRulerView) ((Activity) context).getLayoutInflater().inflate(R.layout.booheeruler_view,null);
        setGravity(RelativeLayout.ALIGN_PARENT_TOP|RelativeLayout.CENTER_HORIZONTAL);
        addView(rulerView);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }
}
