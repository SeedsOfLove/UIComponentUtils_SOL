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
 * 图片配文本框控件
 */
public class UCULImageAndTextView extends LinearLayout
{
    private Context mContext;

    private View root;
    private ImageView iv_Icon;          //图标
    private TextView txt_content;       //内容

    public UCULImageAndTextView(Context context)
    {
        this(context, null);     //调用同名构造方法
    }

    public UCULImageAndTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);     //调用同名构造方法
    }

    public UCULImageAndTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);    //通过上面的传参，实现无论系统调用，哪个构造方法，最终调用的是具有样式的构造方法
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = inflater.inflate(R.layout.ucul_image_and_text_view, this, true);

        initView();
        initStyle(context, attrs, defStyleAttr);
    }

    /**
     * 初始化界面
     */
    private void initView()
    {
        iv_Icon = root.findViewById(R.id.iv_iatv_icon);
        txt_content = root.findViewById(R.id.tv_iatv_content);
    }

    /**
     * 初始化相关自定义属性
     */
    private void initStyle(Context context, AttributeSet attrs, int defStyleAttr)
    {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ucul_image_and_text_view, defStyleAttr, 0);

        int iconImage = array.getResourceId(R.styleable.ucul_image_and_text_view_iat_icon, 0);                                                                      //图标

        String contentText = array.getString(R.styleable.ucul_image_and_text_view_iat_content_text);                                                                //内容文本
        float contentTextSize = array.getDimensionPixelSize(R.styleable.ucul_image_and_text_view_iat_content_text_size, 15);                                        //内容文本大小
        int contentTextColor = array.getColor(R.styleable.ucul_image_and_text_view_iat_content_text_color, getResources().getColor(R.color.ucu_colorFont_black));   //内容文本颜色
        Drawable contentTextBg = array.getDrawable(R.styleable.ucul_image_and_text_view_iat_content_text_bg);                                                       //内容文本背景

        iv_Icon.setImageResource(iconImage);

        txt_content.setText(contentText);
        txt_content.getPaint().setTextSize(contentTextSize);
        txt_content.setTextColor(contentTextColor);
        txt_content.setBackground(contentTextBg);

        array.recycle();
    }

    /**
     * 设置图标
     * @param res
     */
    public void setIcon(int res)
    {
        iv_Icon.setImageResource(res);
    }

    /**
     * 设置内容文本
     *
     * @param contentText
     */
    public void setContentText(String contentText)
    {
        if (contentText != null)
        {
            txt_content.setText(contentText);
        }
    }

    /**
     * 设置内容文本大小
     *
     * @param size
     */
    public void setContentTextSize(int size)
    {
        txt_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置内容文本颜色
     *
     * @param color
     */
    public void setContentTextColor(int color)
    {
        txt_content.setTextColor(getResources().getColor(color));
    }

    /**
     * 设置内容文本背景
     *
     * @param res
     */
    public void setContentTextBackground(int res)
    {
        txt_content.setBackground(getResources().getDrawable(res));
    }

    /**
     * 获取内容文本内容
     *
     * @return
     */
    public String getContentText()
    {
        return txt_content.getText().toString();
    }

    public TextView getContentTextView()
    {
        return txt_content;
    }
}
