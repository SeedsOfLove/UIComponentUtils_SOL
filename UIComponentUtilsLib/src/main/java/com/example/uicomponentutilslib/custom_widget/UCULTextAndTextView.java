package com.example.uicomponentutilslib.custom_widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uicomponentutilslib.R;

/**
 * 文本框配文本框控件
 */
public class UCULTextAndTextView extends LinearLayout
{
    private Context mContext;

    private View root;
    private TextView txt_title;         //标题
    private TextView txt_division;      //分割线
    private TextView txt_content;       //内容

    public UCULTextAndTextView(Context context)
    {
        this(context, null);     //调用同名构造方法
    }

    public UCULTextAndTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);     //调用同名构造方法
    }

    public UCULTextAndTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);    //通过上面的传参，实现无论系统调用，哪个构造方法，最终调用的是具有样式的构造方法
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = inflater.inflate(R.layout.ucul_text_and_text_view, this, true);

        initView();
        initStyle(context, attrs);
    }

    /**
     * 初始化界面
     */
    private void initView()
    {
        txt_title = root.findViewById(R.id.tv_tatv_title);
        txt_division = root.findViewById(R.id.tv_tatv_division);
        txt_content = root.findViewById(R.id.tv_tatv_content);
    }

    /**
     * 初始化相关自定义属性
     */
    private void initStyle(Context context, AttributeSet attrs)
    {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ucul_text_and_text_view);

        String titleText = array.getString(R.styleable.ucul_text_and_text_view_title_text);                                                                     //标题文本
        float titleTextSize = array.getDimensionPixelSize(R.styleable.ucul_text_and_text_view_title_text_size, 15);                                             //标题文本大小
        int titleTextColor = array.getColor(R.styleable.ucul_text_and_text_view_title_text_color,  getResources().getColor(R.color.ucu_colorFont_black));       //标题文本颜色
        Drawable titleTextBg = array.getDrawable(R.styleable.ucul_text_and_text_view_title_text_bg);                                                            //标题文本背景
        int titleTextWidth = array.getInteger(R.styleable.ucul_text_and_text_view_title_text_width, 300);                                                       //标题文本宽度
        int divisionBgColor = array.getColor(R.styleable.ucul_text_and_text_view_division_bg,  getResources().getColor(R.color.ucu_colorFont_gray));            //分割线颜色
        int divisionVisibility = array.getInteger(R.styleable.ucul_text_and_text_view_division_visibility, 0);                                                  //分割线显示
        String contentText = array.getString(R.styleable.ucul_text_and_text_view_content_text);                                                                 //内容文本
        float contentTextSize = array.getDimensionPixelSize(R.styleable.ucul_text_and_text_view_content_text_size, 15);                                         //内容文本大小
        int contentTextColor = array.getColor(R.styleable.ucul_text_and_text_view_content_text_color,  getResources().getColor(R.color.ucu_colorFont_black));   //内容文本颜色
        Drawable contentTextBg = array.getDrawable(R.styleable.ucul_text_and_text_view_content_text_bg);                                                        //内容文本背景

        txt_title.setText(titleText);
        txt_title.getPaint().setTextSize(titleTextSize);
        txt_title.setTextColor(titleTextColor);
        txt_title.setBackground(titleTextBg);

        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) txt_title.getLayoutParams();
        linearParams.width = titleTextWidth;
        txt_title.setLayoutParams(linearParams);

        txt_division.setBackgroundColor(divisionBgColor);
        txt_division.setVisibility(divisionVisibility);

        txt_content.setText(contentText);
        txt_content.getPaint().setTextSize(contentTextSize);
        txt_content.setTextColor(contentTextColor);
        txt_content.setBackground(contentTextBg);

        array.recycle();
    }

    /**
     * 设置标题文本
     * @param titleText
     */
    public void setTitleText(String titleText)
    {
        if (!TextUtils.isEmpty(titleText))
        {
            txt_title.setText(titleText);
        }
    }

    /**
     * 设置标题文本大小
     * @param size
     */
    public void setTitleTextSize(int size)
    {
        if (txt_title != null)
        {
            txt_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
    }

    /**
     * 设置标题文本颜色
     * @param color
     */
    public void setTitleTextColor(int color)
    {
        if (txt_title != null)
        {
            txt_title.setTextColor(getResources().getColor(color));
        }
    }

    /**
     * 设置标题文本背景
     * @param res
     */
    public void setTitleTextBackground(int res)
    {
        if (txt_title != null)
        {
            txt_title.setBackground(getResources().getDrawable(res));
        }
    }

    /**
     * 设置标题文本宽度
     * @param width
     */
    public void setTitleTextWidth(int width)
    {
        if (txt_title != null)
        {
            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) txt_title.getLayoutParams();
            linearParams.width = width;
            txt_title.setLayoutParams(linearParams);
        }
    }


    /**
     * 设置分割线背景颜色
     * @param color
     */
    public void setDivisionBackgroundColor(int color)
    {
        if (txt_division != null)
        {
            txt_division.setBackgroundColor(getResources().getColor(color));
        }
    }

    /**
     * 设置分割线显示
     * @param visibility
     */
    public void setDivisionVisibility(int visibility)
    {
        if (txt_division != null)
        {
            txt_division.setVisibility(visibility);
        }
    }

    /**
     * 设置内容文本
     * @param contentText
     */
    public void setContentText(String contentText)
    {
        if (!TextUtils.isEmpty(contentText))
        {
            txt_content.setText(contentText);
        }
    }

    /**
     * 设置内容文本大小
     * @param size
     */
    public void setContentTextSize(int size)
    {
        if (txt_content != null)
        {
            txt_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
    }

    /**
     * 设置内容文本颜色
     * @param color
     */
    public void setContentTextColor(int color)
    {
        if (txt_content != null)
        {
            txt_content.setTextColor(getResources().getColor(color));
        }
    }

    /**
     * 设置内容文本背景
     * @param res
     */
    public void setContentTextBackground(int res)
    {
        if (txt_content != null)
        {
            txt_content.setBackground(getResources().getDrawable(res));
        }
    }
}
