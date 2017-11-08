package com.share.yannis.booheeruler;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.text.DecimalFormat;

/**
 * Created by Yannis on 2017/10/30.
 * 尺子UI包裹Layout
 */

public class BooHeeRulerLayout extends RelativeLayout {
    private BooHeeRulerView rulerView;
    //中线颜色
    private int midLineColor = 0xFF72BC49;
    //中线高度
    private float midLineHeight = 150f;
    //中线宽度
    private float midLineWidth = 10f;

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
        midLineHeight = a.getDimension(R.styleable.BooHeeRulerLayout_midLineHeight,150f);
        midLineWidth = a.getDimension(R.styleable.BooHeeRulerLayout_midLineWidth,10f);
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
        drawMidLine(canvas);
    }

    /**
     * 画中线
     * @param canvas
     */
    private void drawMidLine(Canvas canvas) {
        Paint midLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        midLinePaint.setColor(midLineColor);
        midLinePaint.setStrokeWidth(midLineWidth);
        midLinePaint.setStrokeCap(Paint.Cap.ROUND);
        float startX = getWidth()/2;
        canvas.drawLine(startX,0,startX,midLineHeight,midLinePaint);
    }

    public void setOnRulerValueChangedListener(RulerDataCallBack callback){
       if(callback != null){
           rulerView.setOnDataChangedListener(callback);
       }
    }

    public void setCurrentValue(float value){
        if (rulerView != null) {
            rulerView.setCurrentValue(value);
        }
    }
}
