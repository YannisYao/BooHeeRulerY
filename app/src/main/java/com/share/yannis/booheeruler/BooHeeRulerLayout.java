package com.share.yannis.booheeruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Yannis on 2017/10/30.
 */

public class BooHeeRulerLayout extends RelativeLayout {
    private BooHeeRulerView rulerView;
    private int midLineColor = 0xFF72BC49;
    private float midLineHeight = 140f;
    private float midLineWidth = 7f;
    private int layoutWidth = LayoutParams.WRAP_CONTENT;
    private int layoutHeight = LayoutParams.WRAP_CONTENT;


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
        TypedArray  b = context.obtainStyledAttributes(attrs,com.android.internal.R.styleable.ViewGroup);
        layoutWidth = b.getLayoutDimension(com.android.internal.R.styleable.ViewGroup_Layout_layout_width,"layout_width");
        layoutHeight = b.getLayoutDimension(com.android.internal.R.styleable.ViewGroup_Layout_layout_height,"layout_height");
        b.recycle();
    }

    private void initView(Context context) {
        rulerView = (BooHeeRulerView) this.inflate(context,R.layout.booheeruler_view,this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(layoutWidth,layoutHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        this.addView(rulerView,params);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

    }
}
