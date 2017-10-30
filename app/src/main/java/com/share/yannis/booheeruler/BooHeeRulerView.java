package com.share.yannis.booheeruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yannis on 2017/10/30.
 * 仿写薄荷健康的惯性尺子
 */

public class BooHeeRulerView extends View {
    private final static float UNIT_HEIGHT = 70f;
    private final static float TOP_LINW_WID = 3;
    private final static float PADDING_LEFT = 10;
    private final static float DEF_TEXTSIZE = 50;
    //最小刻度的宽度
    private float minUnitWid = 5f;
    //大刻度的宽度
    private float maxUnitWid = 7f;
    //刻度相邻间距
    private float unitSpacing = 30f;
    //一个大刻度的最小刻度数
    private int unitNums = 10;
    //最小刻度的颜色
    private int minUnitColor = 0xFFE4E6E3;
    //中线的颜色
    private int datumLineColor = 0xFF72BC49;
    //大刻度颜色
    private int maxUnitColor = 0xFFDEE2DE;
    //背景色
    private int backgroundColor = 0xFFF5F8F5;
    //大刻度文字颜色
    private int unitTextColor = 0xFF414440;
    //起始刻度
    private float startNum = 0.0f;
    //终止刻度的值
    private int unitTotal = 100;
    //最小刻度的值
    private float unitValue = 0.1f;
    //刻度画笔
    private Paint unitPaint;
    //基准线画笔
    private Paint topLinePaint;
    //大刻度文字画笔
    private Paint unitTextPaint;

    public BooHeeRulerView(Context context) {
        super(context);
        init(context,null);
    }
    public BooHeeRulerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BooHeeRulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context , AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.BooHeeRulerView);
            minUnitWid = array.getDimension(R.styleable.BooHeeRulerView_minUnitWid,5);
            maxUnitWid = array.getDimension(R.styleable.BooHeeRulerView_maxUnitWid,7);
            unitNums = array.getInteger(R.styleable.BooHeeRulerView_unitNums,10);
            minUnitColor = array.getColor(R.styleable.BooHeeRulerView_minUnitColor,0xFFE4E6E3);
            datumLineColor = array.getColor(R.styleable.BooHeeRulerView_datumLineColor,0xFF72BC49);
            maxUnitColor = array.getColor(R.styleable.BooHeeRulerView_maxUnitColor,0xFFDEE2DE);
            startNum = array.getFloat(R.styleable.BooHeeRulerView_startNum,0.0f);
            unitTotal = array.getInteger(R.styleable.BooHeeRulerView_unitTotal,100);
            unitValue = array.getFloat(R.styleable.BooHeeRulerView_unitValue,0.1f);
            unitSpacing = array.getFloat(R.styleable.BooHeeRulerView_unitSpacing,30f);
            backgroundColor = array.getColor(R.styleable.BooHeeRulerView_backgroundColor,0xFFF5F8F5);
            array.recycle();

            unitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            unitPaint.setColor(minUnitColor);
            unitPaint.setStrokeWidth(minUnitWid);
            unitPaint.setStrokeCap(Paint.Cap.ROUND);

            topLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            topLinePaint.setColor(minUnitColor);
            topLinePaint.setStrokeWidth(TOP_LINW_WID);

            unitTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            unitTextPaint.setColor(unitTextColor);
            unitTextPaint.setTextSize(DEF_TEXTSIZE);

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(backgroundColor);
        drawRuler(canvas);
    }

    /**
     * 画刻度
     * @param canvas
     */
    private void drawRuler(Canvas canvas) {
        int lenth = unitTotal;
        float xOffset = PADDING_LEFT;
        float centerY = UNIT_HEIGHT;
        canvas.drawLine(xOffset,centerY-UNIT_HEIGHT,xOffset+ lenth * unitSpacing + lenth * minUnitWid,centerY-UNIT_HEIGHT,topLinePaint);
        for(int i = 0 ; i < lenth ; i ++){
            int count = i/unitNums;
            if(i % unitNums == 0){
                float startX = xOffset+ i * unitSpacing + (i- count) * minUnitWid + (count) * maxUnitWid;
                float endX = startX;
                unitPaint.setColor(maxUnitColor);
                unitPaint.setStrokeWidth(maxUnitWid);
                canvas.drawLine(startX,centerY-UNIT_HEIGHT,endX,centerY+UNIT_HEIGHT,unitPaint);
                if(count > 0) drawUnitText(startX,canvas,count);//绘制刻度文字
            }else{
                float startX = xOffset+ i * unitSpacing + (i- count - 1) * minUnitWid + (count + 1) * maxUnitWid;
                float endX = startX;
                unitPaint.setColor(minUnitColor);
                unitPaint.setStrokeWidth(minUnitWid);
                canvas.drawLine(startX,centerY-UNIT_HEIGHT,endX,centerY,unitPaint);
            }
        }
    }

    /**
     * 画出刻度数字
     * @param startX 刻度起始坐标
     * @param canvas
     * @param count 大刻度计数
     */
    private void drawUnitText(float startX,Canvas canvas,int count){
        String text = String.valueOf((int) (startNum + count*unitNums*unitValue));
        Paint.FontMetrics metrics =  unitTextPaint.getFontMetrics();
        float textWid = unitTextPaint.measureText(text);
        canvas.drawText(text,startX - textWid/2 ,UNIT_HEIGHT*3 - metrics.top,unitTextPaint);
    }
}
