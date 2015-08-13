package com.example.administrator.sendingprogressview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.sendingprogressview.R;

/**
 * Created by Administrator on 15-8-13.
 */
public class SendingProgressView extends View {
    private static final Xfermode MASK_XFERMODE;
    private int startAngle = 270;
    private int drawAngle = 0;
    private ValueAnimator arcAnimator, roundAnimator, textAnimator;
    private int defaultWidth = 300;
    private float diameter;
    private Paint paint, roundPaint;
    private float centerX, centerY;
    private RectF rectF;
    private float textDrawY = 0;

    static {
        PorterDuff.Mode localMode = PorterDuff.Mode.DST_ATOP;
        MASK_XFERMODE = new PorterDuffXfermode(localMode);
    }

    public SendingProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.white));
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(50);
        arcAnimator = ValueAnimator.ofInt(0, 361);
        arcAnimator.setDuration(6000);
        roundPaint = new Paint();
        roundPaint.setColor(getResources().getColor(R.color.round));
        roundPaint.setStrokeWidth(1);
        roundPaint.setStyle(Paint.Style.FILL);

        arcAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (drawAngle >= 359) {
                    roundAnimator.start();
                }
                drawAngle = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getWidth() == 0) {
            setMeasuredDimension(defaultWidth, defaultWidth);
        }
        if (getWidth() > getHeight()) {
            diameter = getHeight() - 20;
        } else {
            diameter = getWidth() - 20;
        }
        rectF = new RectF();
        rectF.top = 0;
        rectF.bottom = getHeight();
        rectF.left = 0;
        rectF.right = getWidth();
        centerX = getWidth() / 2;
        centerY = getHeight() + diameter / 2;
        roundAnimator = ValueAnimator.ofFloat(getHeight() + diameter / 2, getHeight() / 2);
        roundAnimator.setDuration(500);
        roundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                centerY = (float) animation.getAnimatedValue();
                if(centerY<=getHeight() / 2+1){
                    textAnimator.start();
                }
                invalidate();
            }
        });
        textDrawY =getHeight()/2+200;
        textAnimator = ValueAnimator.ofFloat(getHeight()/2 + 200, -100);
        textAnimator.setDuration(500);
        textAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textDrawY = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, diameter / 2, roundPaint);
        canvas.drawArc(rectF, startAngle, drawAngle, false, paint);
        if(textDrawY>0) {
            canvas.drawText("√", getWidth() / 2, getHeight()/2-100 + textDrawY, paint);
        }else{
            canvas.drawText("√", getWidth() / 2, getHeight()/2-100 - textDrawY, paint);
        }

    }

    public void show() {
        arcAnimator.start();
    }

    public void setProgress(int progress) {
        drawAngle = (int) (3.6 * progress);
        invalidate();
    }

}
