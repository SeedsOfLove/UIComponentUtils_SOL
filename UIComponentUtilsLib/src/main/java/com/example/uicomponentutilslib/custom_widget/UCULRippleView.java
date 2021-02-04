package com.example.uicomponentutilslib.custom_widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.example.uicomponentutilslib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 圆形涟漪效果控件
 * https://www.jb51.net/article/137509.htm
 */
public class UCULRippleView extends View
{
    private Context mContext;

    // 画笔对象
    private Paint mPaint;

    // View宽
    private float mWidth;

    // View高
    private float mHeight;

    // 声波的圆圈集合
    private List<Circle> mRipples;

    private int sqrtNumber;

    // 圆圈扩散的速度
    private int mSpeed;

    // 圆圈之间的密度
    private int mDensity;

    // 圆圈的颜色
    private int mColor;

    // 圆圈是否为填充模式
    private boolean mIsFill;

    // 圆圈是否为渐变模式
    private boolean mIsAlpha;

    public UCULRippleView(Context context)
    {
        this(context, null);
    }

    public UCULRippleView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public UCULRippleView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        // 获取用户配置属性
        TypedArray tya = context.obtainStyledAttributes(attrs, R.styleable.ucul_ripple_view);
        mColor = tya.getColor(R.styleable.ucul_ripple_view_ucul_rv_color, Color.BLUE);
        mSpeed = tya.getInt(R.styleable.ucul_ripple_view_ucul_rv_speed, 1);
        mDensity = tya.getInt(R.styleable.ucul_ripple_view_ucul_rv_density, 10);
        mIsFill = tya.getBoolean(R.styleable.ucul_ripple_view_ucul_rv_isFill, false);
        mIsAlpha = tya.getBoolean(R.styleable.ucul_ripple_view_ucul_rv_IsAlpha, false);
        tya.recycle();

        init();
    }

    private void init()
    {
        mContext = getContext();

        // 设置画笔样式
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(getPixelsFromDp(mContext, 1));
        if (mIsFill)
        {
            mPaint.setStyle(Paint.Style.FILL);
        }
        else
        {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);

        // 添加第一个圆圈
        mRipples = new ArrayList<>();
        Circle c = new Circle(0, 255);
        mRipples.add(c);

        mDensity = getPixelsFromDp(mContext, mDensity);

        // 设置View的圆为半透明
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        // 内切正方形
        drawInCircle(canvas);

        // 外切正方形
//         drawOutCircle(canvas);
    }

    /**
     * 圆到宽度
     *
     * @param canvas
     */
    private void drawInCircle(Canvas canvas)
    {
        canvas.save();

        // 处理每个圆的宽度和透明度
        for (int i = 0; i < mRipples.size(); i++)
        {
            Circle c = mRipples.get(i);

            mPaint.setAlpha(c.alpha);// （透明）0~255（不透明）
            canvas.drawCircle(mWidth / 2, mHeight / 2, c.width - mPaint.getStrokeWidth(), mPaint);

            // 当圆的半径超出View的宽度后删除
            if (c.width > mWidth / 2)
            {
                mRipples.remove(i);
            }
            else
            {
                // 计算不透明的数值，这里有个小知识，就是如果不加上double的话，255除以一个任意比它大的数都将是0
                if (mIsAlpha)
                {
                    double alpha = 255 - c.width * (255 / ((double) mWidth / 2));   //透明度 = 255 -  255 * （圆的宽度 / View宽度）
                    c.alpha = (int) alpha;
                }
                // 修改这个值控制速度
                c.width += mSpeed;
            }
        }

        // 里面添加圆
        if (mRipples.size() > 0)
        {
            // 控制第二个圆出来的间距
            if (mRipples.get(mRipples.size() - 1).width > getPixelsFromDp(mContext, mDensity))
            {
                mRipples.add(new Circle(0, 255));
            }
        }
        invalidate();
        canvas.restore();

    }


    /**
     * 圆到对角线
     *
     * @param canvas
     */
    private void drawOutCircle(Canvas canvas)
    {
        canvas.save();

        // 使用勾股定律求得一个外切正方形中心点离角的距离
        sqrtNumber = (int) (Math.sqrt(mWidth * mWidth + mHeight * mHeight) / 2);

        // 变大
        for (int i = 0; i < mRipples.size(); i++)
        {

            // 启动圆圈
            Circle c = mRipples.get(i);
            mPaint.setAlpha(c.alpha);// （透明）0~255（不透明）
            canvas.drawCircle(mWidth / 2, mHeight / 2, c.width - mPaint.getStrokeWidth(), mPaint);

            // 当圆超出对角线后删掉
            if (c.width > sqrtNumber)
            {
                mRipples.remove(i);
            }
            else
            {
                // 计算不透明的度数
                double degree = 255 - c.width * (255 / (double) sqrtNumber);
                c.alpha = (int) degree;
                c.width += 1;
            }
        }

        // 里面添加圆
        if (mRipples.size() > 0)
        {
            // 控制第二个圆出来的间距
            if (mRipples.get(mRipples.size() - 1).width == 250)
            {
                mRipples.add(new Circle(0, 255));
            }
        }

        invalidate();
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int myWidthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int myWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int myHeightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int myHeightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        // 获取宽度
        if (myWidthSpecMode == MeasureSpec.EXACTLY)
        {
            // match_parent
            mWidth = myWidthSpecSize;
        }
        else
        {
            // wrap_content
            mWidth = getPixelsFromDp(mContext, 120);
        }

        // 获取高度
        if (myHeightSpecMode == MeasureSpec.EXACTLY)
        {
            mHeight = myHeightSpecSize;
        }
        else
        {
            // wrap_content
            mHeight = getPixelsFromDp(mContext, 120);
        }

        // 设置该view的宽高
        setMeasuredDimension((int) mWidth, (int) mHeight);
    }

    /**
     * dp转pixels
     *
     * @param dpSize
     * @return
     */
    private int getPixelsFromDp(Context context, int dpSize)
    {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return (dpSize * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }


    class Circle
    {
        Circle(int width, int alpha)
        {
            this.width = width;             //直径
            this.alpha = alpha;             //透明度
        }

        int width;

        int alpha;
    }
}
