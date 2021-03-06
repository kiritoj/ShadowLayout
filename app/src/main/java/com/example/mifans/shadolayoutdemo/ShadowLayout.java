package com.example.mifans.shadolayoutdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
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

    //边界path和内容区path
    private Path borderPath;
    private Path contentPath;


    //阴影矩形
    private RectF borderRecf;

    //图像混合模式

    private Xfermode xfermode;
    private Context context;

    public ShadowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
        processPadding();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速


    }

    //初始化属性及画笔
    public void init(Context context, AttributeSet attrs) {

        //获取属性数据
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadoLayout);
        shadowColor = typedArray.getColor(R.styleable.ShadoLayout_shadow_color, SHADOW_COLOR);
        shadowWidth = typedArray.getDimension(R.styleable.ShadoLayout_shadow_width, SHADOW_WIDTH);
        borderColor = typedArray.getColor(R.styleable.ShadoLayout_border_color, BORDER_COLOR);
        borderWidth = typedArray.getDimension(R.styleable.ShadoLayout_border_width, BORDER_WIDTH);
        borderRadius = typedArray.getDimension(R.styleable.ShadoLayout_border_Radius, BORDER_RADIUS);
        offsetX = typedArray.getDimension(R.styleable.ShadoLayout_offset_X, OFFSET_X);
        offsetY = typedArray.getDimension(R.styleable.ShadoLayout_offset_Y, OFFSET_Y);
        shadowSides = typedArray.getInt(R.styleable.ShadoLayout_shadow_sides, SHADOW_ALL);
        typedArray.recycle();

        //初始化画笔
        borderPaint = new Paint();
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setShadowLayer(shadowWidth, offsetX, offsetY, shadowColor);

        borderPath = new Path();
        contentPath = new Path();

        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    }

    //设置padding绘制阴影
    public void processPadding() {
        int paddingx = (int) (shadowWidth + Math.abs(offsetX));
        int paddingy = (int) (shadowWidth + Math.abs(offsetY));
        setPadding(judgSide(SHADOW_LEFT) ? paddingx : 0
                , judgSide(SHADOW_TOP) ? paddingy : 0
                , judgSide(SHADOW_RIGHT) ? paddingx : 0
                , judgSide(SHADOW_BOTTOM) ? paddingy : 0);


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        //边界矩形四个角的位置
        float left = getPaddingLeft();
        float top = getPaddingTop();
        float right = w - getPaddingRight();
        float bottom = h - getPaddingBottom();
        borderRecf = new RectF(left, top, right, bottom);

        //通过画笔的布尔操作，截取出子view四个角
        borderPath.addRect(borderRecf, Path.Direction.CCW);
        contentPath.addRoundRect(borderRecf, borderRadius, borderRadius, Path.Direction.CCW);
        borderPath.op(contentPath, Path.Op.DIFFERENCE);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void dispatchDraw(Canvas canvas) {

        //先绘制阴影，否则阴影会出现在子view部分
        drawshadow(canvas);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.saveLayer(0, 0, width, height, borderPaint);
        }
        //绘制子view，作为xfermode的目标图像
        super.dispatchDraw(canvas);
        //设置合成模式
        borderPaint.setXfermode(xfermode);
        //绘制xfermode的源图像
        canvas.drawPath(borderPath, borderPaint);
        borderPaint.clearShadowLayer();
        borderPaint.setXfermode(null);
        canvas.restore();
        //再绘制一遍边框，绘制阴影时候的边框被子view挡住了
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
        canvas.drawRoundRect(borderRecf, borderRadius, borderRadius, borderPaint);

        //一定要重置画笔状态，button点击后会重新调用本方法，画笔的阴影，粗细，颜色回归第一次调用的时候
        resetPaint();


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    //获取xfermode的源图
    public Bitmap getSrcBitmap() {
        Bitmap bitmap = Bitmap.createBitmap((int) borderRecf.width(), (int) borderRecf.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint p = new Paint();
        p.setColor(Color.BLACK);
<<<<<<< HEAD

=======
>>>>>>> origin/master
        canvas.drawRoundRect(0, 0, borderRecf.width(), borderRecf.height(), borderRadius, borderRadius, p);
        return bitmap;
    }

    //绘制阴影
    public void drawshadow(Canvas canvas) {
        canvas.drawRoundRect(borderRecf, borderRadius, borderRadius, borderPaint);

    }

    //判断某一边是否绘制阴影
    public boolean judgSide(int side) {
        return (shadowSides | side) == shadowSides;
    }

    public void resetPaint() {
        borderPaint.setStyle(Paint.Style.FILL);
        borderPaint.setStrokeWidth(0);
        borderPaint.setShadowLayer(shadowWidth, offsetX, offsetY, shadowColor);
    }

}
