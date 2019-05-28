package com.example.mifans.shadolayoutdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;



public class ShadowLayout extends FrameLayout {
    //需要绘制阴影的边
    public static int SHADOW_TOP = 1;
    public static int SHADOW_RIGHT = 2;
    public static int SHADOW_BOTTOM = 4;
    public static int SHADOW_LEFT = 8;
    public static int SHADOW_ALL = 15;

    //默认阴影颜色
    public static int SHADOW_COLOR = Color.GRAY;
    //默认的阴影宽度
    public static float SHADOW_WIDTH = 0f;
    //默认边框颜色
    public static int BORDER_COLOR = Color.BLACK;
    //默认边框宽度
    public static float BORDER_WIDTH = 0f;
    //默认边框圆角
    public static float BORDER_RADIUS = 0;
    //默认x，y轴阴影偏移量
    public static float OFFSET_X = 0;
    public static float OFFSET_Y = 0;

    private int shadowColor;
    private float shadowWidth;
    private int borderColor;
    private float borderWidth;
    private float borderRadius;
    private float offsetX;
    private float offsetY;
    private int shadowSides;

    //layout的宽高
    private int width;
    private int height;

    //画笔
    private Paint borderPaint;

    //边框矩形
    private RectF borderRecf;

    //图像混合模式

    private Xfermode xfermode;
    private Context context;
    public ShadowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context,attrs);
        processPadding();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速


    }

    //初始化属性及画笔
    public void init(Context context,AttributeSet attrs){

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.ShadoLayout);
        shadowColor = typedArray.getColor(R.styleable.ShadoLayout_shadow_color,SHADOW_COLOR);
        shadowWidth = typedArray.getDimension(R.styleable.ShadoLayout_shadow_width,SHADOW_WIDTH);
        borderColor = typedArray.getColor(R.styleable.ShadoLayout_border_color,BORDER_COLOR);
        borderWidth = typedArray.getDimension(R.styleable.ShadoLayout_border_width,BORDER_WIDTH);
        borderRadius = typedArray.getDimension(R.styleable.ShadoLayout_border_Radius,BORDER_RADIUS);
        offsetX = typedArray.getDimension(R.styleable.ShadoLayout_offset_X,OFFSET_X);
        offsetY = typedArray.getDimension(R.styleable.ShadoLayout_offset_Y,OFFSET_Y);
        shadowSides = typedArray.getInt(R.styleable.ShadoLayout_shadow_sides,SHADOW_ALL);
        typedArray.recycle();

        borderPaint = new Paint();
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setShadowLayer(shadowWidth,offsetX,offsetY,shadowColor);




    }

    public void processPadding(){
        int paddingx = (int)(shadowWidth+Math.abs(offsetX));
        int paddingy = (int) (shadowWidth+Math.abs(offsetY));
        setPadding(judgSide(SHADOW_LEFT)?paddingx:0
                ,judgSide(SHADOW_TOP)?paddingy:0
                ,judgSide(SHADOW_RIGHT)?paddingx:0
                ,judgSide(SHADOW_BOTTOM)?paddingy:0);


    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        //矩形四个角的位置
        float left = judgSide(SHADOW_LEFT)?getPaddingLeft():0f;
        float top = judgSide(SHADOW_TOP)?getPaddingTop():0f;
        float right = judgSide(SHADOW_RIGHT)?(w-getPaddingRight()):0f;
        float bottom = judgSide(SHADOW_BOTTOM)?(h-getPaddingBottom()):0f;
        borderRecf = new RectF(left,top,right,bottom);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.saveLayer(0,0,width,height,borderPaint);
        }
        super.dispatchDraw(canvas);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        borderPaint.setXfermode(xfermode);
        borderPaint.setStrokeWidth(1);
        borderPaint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.addRoundRect(borderRecf,50,50,Path.Direction.CCW);
        canvas.drawPath(path,borderPaint);
        canvas.restore();
        drawshadow(canvas);





    }



    //绘制阴影
    public void drawshadow(Canvas canvas){
        canvas.drawRoundRect(borderRecf,borderRadius,borderRadius,borderPaint);

    }
    //判断某一边是否绘制阴影
    public boolean judgSide(int side){
        return (shadowSides|side) == shadowSides;
    }

    //dp转px
    public float dpTopx(float dp){
        if (dp == 0){
            return 0f;
        }else {
            float scale = context.getResources().getDisplayMetrics().density;
            return dp*scale+0.5f;
        }
    }
}