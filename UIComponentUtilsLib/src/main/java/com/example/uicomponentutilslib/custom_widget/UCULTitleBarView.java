package com.example.uicomponentutilslib.custom_widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uicomponentutilslib.R;

/**
 * 标题栏控件
 */
public class UCULTitleBarView extends LinearLayout
{
    private Context mContext;

    private View root;
    private LinearLayout ll_left, ll_right;
    private TextView tv_left, tv_title, tv_right;
    private ImageView iv_left, iv_right;

    private onViewClick mClick;

    public UCULTitleBarView(Context context)
    {
        this(context, null);     //调用同名构造方法
    }

    public UCULTitleBarView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);     //调用同名构造方法
    }

    public UCULTitleBarView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);    //通过上面的传参，实现无论系统调用，哪个构造方法，最终调用的是具有样式的构造方法
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = inflater.inflate(R.layout.ucul_title_bar_view, this, true);

        initView();
        initStyle(context, attrs, defStyleAttr);

        ll_left.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mClick != null)
                    mClick.leftClick();
            }
        });

        ll_right.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mClick != null)
                    mClick.rightClick();
            }
        });
    }

    /**
     * 初始化界面
     */
    private void initView()
    {
        ll_left = root.findViewById(R.id.ll_ucul_title_bar_left);
        ll_right = root.findViewById(R.id.ll_ucul_title_bar_right);
        tv_left = root.findViewById(R.id.tv_ucul_title_bar_left);
        tv_title = root.findViewById(R.id.tv_ucul_title_bar_title);
        tv_right = root.findViewById(R.id.tv_ucul_title_bar_right);
        iv_left = root.findViewById(R.id.iv_ucul_title_bar_left);
        iv_right = root.findViewById(R.id.iv_ucul_title_bar_right);
    }

    /**
     * 初始化相关自定义属性
     */
    private void initStyle(Context context, AttributeSet attrs, int defStyleAttr)
    {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ucul_title_bar_view, defStyleAttr, 0);

        int leftImage = array.getResourceId(R.styleable.ucul_title_bar_view_tbv_left_icon, 0);                  //左按钮图标
        int rightImage = array.getResourceId(R.styleable.ucul_title_bar_view_tbv_right_icon, 0);                //右按钮图标

        String leftText = array.getString(R.styleable.ucul_title_bar_view_tbv_left_text);                       //左按钮文本
        String titleText = array.getString(R.styleable.ucul_title_bar_view_tbv_title_text);                     //标题文本
        String rightText = array.getString(R.styleable.ucul_title_bar_view_tbv_right_text);                     //右按钮文本

        int leftTextColor = array.getColor(R.styleable.ucul_title_bar_view_tbv_left_text_color, getResources().getColor(R.color.ucu_colorFont_black));      //左按钮文本颜色
        int titleTextColor = array.getColor(R.styleable.ucul_title_bar_view_tbv_title_text_color, getResources().getColor(R.color.ucu_colorFont_black));    //标题文本颜色
        int rightTextColor = array.getColor(R.styleable.ucul_title_bar_view_tbv_right_text_color, getResources().getColor(R.color.ucu_colorFont_black));    //右按钮文本颜色

        float leftTextSize = array.getDimensionPixelSize(R.styleable.ucul_title_bar_view_tbv_left_text_size, 15);           //左按钮文本大小
        float titleTextSize = array.getDimensionPixelSize(R.styleable.ucul_title_bar_view_tbv_title_text_size, 20);         //标题文本大小
        float rightTextSize = array.getDimensionPixelSize(R.styleable.ucul_title_bar_view_tbv_right_text_size, 15);         //右按钮文本大小

        iv_left.setImageResource(leftImage);
        iv_right.setImageResource(rightImage);

        tv_left.setText(leftText);
        tv_title.setText(titleText);
        tv_right.setText(rightText);

        tv_left.setTextColor(leftTextColor);
        tv_title.setTextColor(titleTextColor);
        tv_right.setTextColor(rightTextColor);

        tv_left.getPaint().setTextSize(leftTextSize);
        tv_title.getPaint().setTextSize(titleTextSize);
        tv_right.getPaint().setTextSize(rightTextSize);

        array.recycle();
    }

    /**
     * 设置左图标
     * @param res
     */
    public void setLeftIcon(int res)
    {
        iv_left.setImageResource(res);
    }

    /**
     * 设置右图标
     * @param res
     */
    public void setRightIcon(int res)
    {
        iv_right.setImageResource(res);
    }

    /**
     * 设置左按钮文本
     * @param text
     */
    public void setLeftText(String text)
    {
        if (text != null)
        {
            tv_left.setText(text);
        }
    }

    /**
     * 设置标题文本
     * @param text
     */
    public void setTitle(String text)
    {
        if (text != null)
        {
            tv_title.setText(text);
        }
    }

    /**
     * 设置右按钮文本
     * @param text
     */
    public void setRightText(String text)
    {
        if (text != null)
        {
            tv_right.setText(text);
        }
    }

    /**
     * 设置左按钮文本颜色
     *
     * @param color
     */
    public void setLeftTextColor(int color)
    {
        tv_left.setTextColor(getResources().getColor(color));
    }

    /**
     * 设置标题文本颜色
     *
     * @param color
     */
    public void setTitleTextColor(int color)
    {
        tv_title.setTextColor(getResources().getColor(color));
    }

    /**
     * 设置右按钮文本颜色
     *
     * @param color
     */
    public void setRightTextColor(int color)
    {
        tv_right.setTextColor(getResources().getColor(color));
    }

    /**
     * 设置左按钮文本大小
     *
     * @param size
     */
    public void setLeftTextSize(int size)
    {
        tv_left.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置标题文本大小
     *
     * @param size
     */
    public void setTitleTextSize(int size)
    {
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置右按钮文本大小
     *
     * @param size
     */
    public void setRightTextSize(int size)
    {
        tv_right.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 左右按钮点击事件接口（因为按钮反馈点击范围太小）
     */
    public interface onViewClick
    {
        void leftClick();
        void rightClick();
    }

    public void setOnViewClick(onViewClick click)
    {
        this.mClick = click;
    }
}
