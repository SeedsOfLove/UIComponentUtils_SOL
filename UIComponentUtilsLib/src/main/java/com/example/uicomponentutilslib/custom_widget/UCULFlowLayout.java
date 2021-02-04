package com.example.uicomponentutilslib.custom_widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uicomponentutilslib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 文字流式布局
 */
public class UCULFlowLayout extends RelativeLayout
{
    private int horizontalSpacing = dp2px(15);                          // 关键字之间的水平间距，单位为dp（默认15dp）
    private int verticalSpacing = dp2px(15);                            // 关键字之间的垂直间距，单位为dp

    private List<Line> linesList = new ArrayList<>();                   // 行的集合
    private Line line;                                                  // 当前的行
    private int lineWidthSize = 0;                                      // 当前行使用的宽度空间

    private int textSize = sp2px(12);                                   // 关键字大小，单位为sp
    private int textColor = Color.BLACK;                                // 关键字颜色（默认黑色）
    private int backgroundResource = R.drawable.ucul_flowlayout_item_bg;// 关键字背景
    private int textPaddingH = dp2px(15);                               // 关键字水平padding，单位为dp
    private int textPaddingV = dp2px(8);                                // 关键字垂直padding，单位为dp

    public UCULFlowLayout(Context context)
    {
        this(context, null);
    }

    public UCULFlowLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public UCULFlowLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ucul_flow_layout, defStyleAttr, 0);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++)
        {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.ucul_flow_layout_ucul_fl_horizontalSpacing)
            {                         //水平间距
                horizontalSpacing = typedArray.getDimensionPixelSize(attr, dp2px(10));
            }
            else if (attr == R.styleable.ucul_flow_layout_ucul_fl_verticalSpacing)
            {                           //垂直间距
                verticalSpacing = typedArray.getDimensionPixelSize(attr, dp2px(10));
            }
            else if (attr == R.styleable.ucul_flow_layout_ucul_fl_itemSize)
            {                                  //关键字大小
                textSize = typedArray.getDimensionPixelSize(attr, sp2px(12));
            }
            else if (attr == R.styleable.ucul_flow_layout_ucul_fl_itemColor)
            {                                 //关键字颜色
                textColor = typedArray.getColor(attr, Color.BLACK);
            }
            else if (attr == R.styleable.ucul_flow_layout_ucul_fl_backgroundResource)
            {                        //关键字背景
                backgroundResource = typedArray.getResourceId(attr, R.drawable.ucul_flowlayout_item_bg);
            }
            else if (attr == R.styleable.ucul_flow_layout_ucul_fl_textPaddingH)
            {                              //关键字水平padding
                textPaddingH = typedArray.getDimensionPixelSize(attr, dp2px(7));
            }
            else if (attr == R.styleable.ucul_flow_layout_ucul_fl_textPaddingV)
            {                              //关键字垂直padding
                textPaddingV = typedArray.getDimensionPixelSize(attr, dp2px(4));
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)       //onMeasure作用是父视图问子视图想要多大的空间
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 实际可以用的宽和高（FlowLayout）
        int FLwidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int FLheight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingTop();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        restoreLine();  // Line初始化

        for (int i = 0; i < getChildCount(); i++)
        {
            View child = getChildAt(i);
            // 测量所有的childView
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(FLwidth, widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(FLheight, heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            if (line == null)
            {
                line = new Line();  // 创建新一行
            }

            int measuredChildWidth = child.getMeasuredWidth();      //关键字标签宽度
            lineWidthSize += measuredChildWidth;                    //计算当前行已使用的宽度（标签宽度的和）

            if (lineWidthSize <= FLwidth)      // 如果使用的宽度小于可用的宽度，这时候childView能够添加到当前的行上
            {
                line.addChild(child);
                lineWidthSize += horizontalSpacing;     //加上关键字之间的水平间距
            }
            else    //否则换行
            {
                newLine();
                line.addChild(child);
                lineWidthSize += child.getMeasuredWidth();
                lineWidthSize += horizontalSpacing;     //加上关键字之间的水平间距
            }
        }

        if (line != null && !linesList.contains(line))  // 把最后一行记录到集合中
        {
            linesList.add(line);
        }

        int totalHeight = 0;    //FlowLayout的高度
        for (int i = 0; i < linesList.size(); i++)  //把所有行的高度加上
        {
            totalHeight += linesList.get(i).getHeight();
        }
        totalHeight += verticalSpacing * (linesList.size() - 1);    // 加上行的竖直间距
        totalHeight += getPaddingBottom();   // 加上上下padding
        totalHeight += getPaddingTop();

        // 设置布局的宽高
        // 宽度 直接采用父view传递过来的最大宽度，而不用考虑子view是否填满宽度，因为该布局的特性就是填满一行后，再换行
        // 高度 根据设置的模式来决定采用所有子View的高度之和还是采用父view传递过来的高度
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), resolveSize(totalHeight, heightMeasureSpec));   //setMeasuredDimension作用是子视图告诉父视图具体大小
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);

        int left = getPaddingLeft();
        int top = getPaddingTop();
        for (int i = 0; i < linesList.size(); i++)
        {
            Line line = linesList.get(i);
            line.layout(left, top);         //该行在父容器中的位置

            top = top + line.getHeight() + verticalSpacing;     // 计算下一行的起点y轴坐标
        }
    }

    /**
     * Line初始化
     */
    private void restoreLine()
    {
        linesList.clear();
        line = new Line();
        lineWidthSize = 0;
    }

    /**
     * 创建新的一行
     */
    private void newLine()
    {
        if (line != null)    // 把之前的行记录下来
        {
            linesList.add(line);
        }

        line = new Line();      // 创建新的一行
        lineWidthSize = 0;
    }

    /*管理每行上的View对象，封装了每行上的View对象，提供添加与绘制childView的方法。*/
    class Line
    {
        private List<View> children = new ArrayList<>();    // 子控件集合
        private int height;     //行高

        /**
         * 添加childView
         * @param childView 子控件
         */
        public void addChild(View childView)
        {
            children.add(childView);

            if (height < childView.getMeasuredHeight())     // 让当前的行高是最高的一个childView的高度
            {
                height = childView.getMeasuredHeight();
            }
        }

        /**
         * 指定childView的绘制区域
         * @param left 左上角x轴坐标
         * @param top  左上角y轴坐标
         */
        public void layout(int left, int top)
        {
            int totalWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            int currentLeft = left;      // 当前childView的左上角x轴坐标

            for (int i = 0; i < children.size(); i++)
            {
                View view = children.get(i);
                view.layout(currentLeft, top, currentLeft + view.getMeasuredWidth(), top + view.getMeasuredHeight());   // 指定childView的绘制区域
                currentLeft = currentLeft + view.getMeasuredWidth() + horizontalSpacing;    // 计算下一个childView的位置
            }
        }

        public int getHeight()
        {
            return height;
        }

        public int getChildCount()
        {
            return children.size();
        }
    }

    /**
     * 设置FlowLayout中的内容，并提供点击事件处理。
     * @param list                关键字
     * @param onItemClickListener 点击监听
     */
    @Deprecated //(@Deprecated 表示此方法或类不再建议使用，调用时也会出现删除线，但并不代表不能用，只是说，不推荐使用，因为还有更好的方法可以调用)
    public void setFlowLayout(List<String> list, final OnItemClickListener onItemClickListener)
    {
        removeAllViews();
        addViews(list, onItemClickListener);
    }

    /**
     * 设置单个关键字标签
     * @param str                 关键字
     * @param onItemClickListener 点击监听
     */
    public void setView(String str, final OnItemClickListener onItemClickListener)
    {
        List<String> list = new ArrayList<>();
        list.add(str);
        setViews(list, onItemClickListener);
    }

    /**
     * 设置多个关键字标签
     * @param list                关键字
     * @param onItemClickListener 点击监听
     */
    public void setViews(List<String> list, final OnItemClickListener onItemClickListener)
    {
        removeAllViews();
        addViews(list, onItemClickListener);
    }

    /**
     * 增加单个关键字标签
     * @param str                 关键字
     * @param onItemClickListener 点击监听
     */
    public void addView(String str, final OnItemClickListener onItemClickListener)
    {
        List<String> list = new ArrayList<>();
        list.add(str);
        addViews(list, onItemClickListener);
    }

    /**
     * 增加多个关键字标签
     * @param list                关键字
     * @param onItemClickListener 点击监听
     */
    public void addViews(List<String> list, final OnItemClickListener onItemClickListener)
    {
        for (int i = 0; i < list.size(); i++)
        {
            final TextView tv = new TextView(getContext());

            /*设置TextView属性（标签属性）*/
            tv.setText(list.get(i));                                                //文本
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);                   //字体大小
            tv.setTextColor(textColor);                                             //字体颜色
            tv.setGravity(Gravity.CENTER);                                          //排版
            tv.setPadding(textPaddingH, textPaddingV, textPaddingH, textPaddingV);  //内边距
            tv.setClickable(true);                                                  //可点击
            tv.setBackgroundResource(backgroundResource);                           //背景样式

            addView(tv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            if (onItemClickListener != null)
            {
                tv.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        onItemClickListener.onItemClick(tv.getText().toString());
                    }
                });
            }
        }
    }

    /**
     * 点击事件监听接口
     */
    public interface OnItemClickListener
    {
        void onItemClick(String content);
    }

    /**
     * 设置文字水平间距
     * @param horizontalSpacing 间距/dp
     */
    public void setHorizontalSpacing(int horizontalSpacing)
    {
        this.horizontalSpacing = dp2px(horizontalSpacing);
    }

    /**
     * 设置文字垂直间距
     * @param verticalSpacing 间距/dp
     */
    public void setVerticalSpacing(int verticalSpacing)
    {
        this.verticalSpacing = dp2px(verticalSpacing);
    }

    /**
     * 设置文字大小
     * @param textSize 文字大小/sp
     */
    public void setTextSize(int textSize)
    {
        this.textSize = sp2px(textSize);
    }

    /**
     * 设置文字颜色
     * @param textColor 文字颜色
     */
    public void setTextColor(int textColor)
    {
        this.textColor = textColor;
    }

    /**
     * 设置文字背景
     * @param backgroundResource 文字背景
     */
    public void setBackgroundResource(int backgroundResource)
    {
        this.backgroundResource = backgroundResource;
    }

    /**
     * 设置文字水平padding
     * @param textPaddingH padding/dp
     */
    public void setTextPaddingH(int textPaddingH)
    {
        this.textPaddingH = dp2px(textPaddingH);
    }

    /**
     * 设置文字垂直padding
     * @param textPaddingV padding/dp
     */
    public void setTextPaddingV(int textPaddingV)
    {
        this.textPaddingV = dp2px(textPaddingV);
    }

    /**
     * dp转px
     * @param dp dp值
     * @return px值
     */
    private int dp2px(float dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     * @param sp sp值
     * @return px值
     */
    private int sp2px(float sp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }
}
