package com.share.yannis.booheeruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.OverScroller;

import java.text.DecimalFormat;

/**
 * Created by yannis on 2017/10/30.
 * 仿写薄荷健康的惯性滑动尺子
 * 防止滑动滑出边界，需要重写scrollTo方法（滑动过程中是通过scrollBy完成，scrollBy实质是调用了scrollTo）
 * 低版本api可能需要关闭硬件加速
 */

public class BooHeeRulerView extends View {
    private final static String TAG = "BooHeeRulerView";
    private final static float UNIT_HEIGHT = 70f;
    private final static float TOP_LINW_WID = 3;
    private final static float PADDING_LEFT = 0;
    private final static float DEF_TEXTSIZE = 70;
    //最小刻度的宽度
    private float minUnitWid = 5f;
    //大刻度的宽度
    private float maxUnitWid = 7f;
    //刻度相邻间距
    private int unitSpacing = 30;
    //一个大刻度的间隔数
    private int unitNums = 10;
    //最小刻度的颜色
    private int minUnitColor = 0xFFE4E6E3;
    //大刻度颜色
    private int maxUnitColor = 0xFFDEE2DE;
    //背景色
    private int backgroundColor = 0xFFF5F8F5;
    //大刻度文字颜色
    private int unitTextColor = 0xFF414440;
    //起始刻度
    private float startNum = 0.0f;
    //刻度间隔总数
    private int unitTotal = 100;
    //最小刻度的值
    private float unitValue = 0.1f;
    //刻度画笔
    private Paint unitPaint;
    //基准线画笔
    private Paint topLinePaint;
    //大刻度文字画笔
    private Paint unitTextPaint;
    //计算滑动速度
    private VelocityTracker vTracker;
    //手势滑动的最大速度
    private int maxVelocity;
    //手势滑动的最小速度
    private int minVelocity;
    //滑动结束落点
    private float lastestX;
    //所有刻度的总长
    private int totalUnitLength;
    //X轴左边界
    private int leftSideX;
    //X轴的右边界
    private int  rightSideX;
    //当前刻度值
    private float currentValue;
    //滑动的精确值，用于和当前实际值比较，是否回滚到最近刻度
    private float currValueExact;
    //控制滑动
    private OverScroller overScroller;

    private RulerDataCallBack callBack;

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
            maxUnitColor = array.getColor(R.styleable.BooHeeRulerView_maxUnitColor,0xFFDEE2DE);
            startNum = array.getFloat(R.styleable.BooHeeRulerView_startNum,0.0f);
            unitTotal = array.getInteger(R.styleable.BooHeeRulerView_unitTotal,100);
            unitValue = array.getFloat(R.styleable.BooHeeRulerView_unitValue,0.1f);
            unitSpacing = array.getInteger(R.styleable.BooHeeRulerView_unitSpacing,30);
            backgroundColor = array.getColor(R.styleable.BooHeeRulerView_backgroundColor,0xFFF5F8F5);
            array.recycle();
        }

        initPaint();
        //配置滑动获取参数
        vTracker = VelocityTracker.obtain();
        maxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        minVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        overScroller = new OverScroller(context);
        //初始化默认位置
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else{
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                gotoDefaultLocation(currentValue);
            }
        });
    }


    private void initPaint() {
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
        int lenth = unitTotal + 1;
        float xOffset = PADDING_LEFT;
        float centerY = UNIT_HEIGHT;
        canvas.drawLine(xOffset,centerY-UNIT_HEIGHT,xOffset+ unitTotal * unitSpacing + maxUnitWid,centerY-UNIT_HEIGHT,topLinePaint);
        for(int i = 0 ; i < lenth ; i ++){
            int count = i/unitNums;
            float startX = xOffset+ i * unitSpacing;
            float endX = startX;
            if(i % unitNums == 0){
                unitPaint.setColor(maxUnitColor);
                unitPaint.setStrokeWidth(maxUnitWid);
                canvas.drawLine(startX,centerY-UNIT_HEIGHT,endX,centerY+UNIT_HEIGHT,unitPaint);
                if(count > 0) drawUnitText(startX,canvas,count);//绘制刻度文字
            }else{
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action  = event.getAction();
        float startX = event.getX();
        vTracker.addMovement(event);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(!overScroller.isFinished()){
                    overScroller.abortAnimation();
                }
                if(vTracker != null) vTracker.clear();
                vTracker.addMovement(event);
                lastestX = startX;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = lastestX - startX;
                scrollBy(((int) deltaX),0);//处理尺子滑动（此处注意scroll是反向移动的，与正常坐标系理解不同）
                lastestX = startX;
                break;
            case MotionEvent.ACTION_UP:
                vTracker.computeCurrentVelocity(1000,maxVelocity);
                float xVelocity = vTracker.getXVelocity();
                if(Math.abs(xVelocity) > minVelocity)reverseFling(-(int)xVelocity);//尺子反向滑动
                else scrollToLastestUnit();//滑动到最近刻度
                vTracker.clear();
                lastestX = 0;
                break;
            case MotionEvent.ACTION_CANCEL:
                if(!overScroller.isFinished()) overScroller.abortAnimation();
                break;
        }
        return true;
    }

    /**
     * 把光标移动到最近的刻度
     */
    private void scrollToLastestUnit() {
        float scrollX = scaleToUnitCount(currentValue);
        overScroller.startScroll(getScrollX(),0,(int)(scrollX) - getScrollX(),0,1000);
        invalidate();//重新绘制
    }

    private void reverseFling(int v) {
        overScroller.fling(getScrollX(),0,v,0,leftSideX,rightSideX,0,0);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //计算尺子的边界
        totalUnitLength =unitTotal * unitSpacing;
        //rightSideX - leftsideX == totalUnitLength;这样才能选中所有的刻度
        leftSideX = -getMeasuredWidth()/2;
        rightSideX = totalUnitLength - getMeasuredWidth()/2;
    }

    @Override
    public void scrollTo(@Px int x, @Px int y) {
        if(x < leftSideX) x =  leftSideX;
        if(x > rightSideX) x = rightSideX;
        if(x != getScrollX()) super.scrollTo(x, y);
        currentValue = getScaleXUnitValue(x);
        if(callBack != null) callBack.onDataChanged(decimalFormat(currentValue));//回调数值用于显示
    }

    private float getScaleXUnitValue(int scrollX) {
        float value = startNum;
        float count = (scrollX - leftSideX)/(float)(totalUnitLength) * unitTotal;
        value +=  Math.round(count) * unitValue;
        currValueExact = (scrollX - leftSideX)/ (float)totalUnitLength * unitTotal * unitValue + startNum;
       return value;
    }

    private float scaleToUnitCount(float scale){
        return (scale - startNum) /unitValue * unitSpacing  + leftSideX;
    }

    public void setOnDataChangedListener(RulerDataCallBack callBack){
        this.callBack = callBack;
    }

    @Override
    public void computeScroll() {
        //super.computeScroll();
        if(overScroller.computeScrollOffset()) {
            scrollTo(overScroller.getCurrX(), overScroller.getCurrY());
            //在动画结束后，光标滑动到最近刻度
            if (!overScroller.computeScrollOffset() && currentValue != Math.round(currValueExact)) {
                scrollToLastestUnit();
            }
            invalidate();
        }
    }

    private void gotoDefaultLocation(float currentValue) {
        float scrollX = scaleToUnitCount(currentValue);
        scrollTo((int)scrollX,0);
        if (callBack != null) {
            callBack.onDataChanged(decimalFormat(currentValue));
        }
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = Float.parseFloat(decimalFormat(currentValue));
    }

    public static String  decimalFormat(float value){
        DecimalFormat df = new DecimalFormat("##0.0");
        return df.format(value);
    }
}
