

package com.example.mifans.shadolayoutdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class TestView extends View {
    Paint paint;
    int width;
    int height;

    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context,AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }



    public void init(){
        paint = new Paint();
        paint.setStrokeWidth(0);
        paint.setColor(Color.RED);
        paint.setShadowLayer(30,0,40,Color.BLUE);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(width/2,height/2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(-200,-100,200,100,50,50,paint);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }
}
