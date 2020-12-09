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
 * 文本框配编辑框控件
 */
public class UCULTextAndEditView extends LinearLayout
{
    private Context mContext;

    private View root;
    private TextView txt_title;         //标题
    private TextView txt_division;      //分割线
    private EditText et_content;        //编辑内容

    public UCULTextAndEditView(Context context)
    {
        this(context, null);     //调用同名构造方法
    }

    public UCULTextAndEditView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);     //调用同名构造方法
    }

    public UCULTextAndEditView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);    //通过上面的传参，实现无论系统调用，哪个构造方法，最终调用的是具有样式的构造方法
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = inflater.inflate(R.layout.ucul_text_and_edit_view, this, true);

        initView();
        initStyle(context, attrs);
    }

    /**
     * 初始化界面
     */
    private void initView()
    {
        txt_title = root.findViewById(R.id.tv_taev_title);
        txt_division = root.findViewById(R.id.tv_taev_division);
        et_content = root.findViewById(R.id.et_taev_content);
    }

    /**
     * 初始化相关自定义属性
     */
    private void initStyle(Context context, AttributeSet attrs)
    {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ucul_text_and_edit_view);

        String titleText = array.getString(R.styleable.ucul_text_and_edit_view_tae_title_text);                                                                             //标题文本
        float titleTextSize = array.getDimensionPixelSize(R.styleable.ucul_text_and_edit_view_tae_title_text_size, 15);                                                     //标题文本大小
        int titleTextColor = array.getColor(R.styleable.ucul_text_and_edit_view_tae_title_text_color, getResources().getColor(R.color.ucu_colorFont_black));                //标题文本颜色
        Drawable titleTextBg = array.getDrawable(R.styleable.ucul_text_and_edit_view_tae_title_text_bg);                                                                    //标题文本背景
        int titleTextWidth = array.getInteger(R.styleable.ucul_text_and_edit_view_tae_title_text_width, 300);                                                               //标题文本宽度
        int divisionBgColor = array.getColor(R.styleable.ucul_text_and_edit_view_tae_division_bg, getResources().getColor(R.color.ucu_colorFont_gray));                 //分割线颜色
        int divisionVisibility = array.getInteger(R.styleable.ucul_text_and_edit_view_tae_division_visibility, 0);                                                      //分割线显示
        String contentEditHintText = array.getString(R.styleable.ucul_text_and_edit_view_tae_content_edit_hint_text);                                                               //内容编辑框提示文本
        int contentEditHintTextColor = array.getColor(R.styleable.ucul_text_and_edit_view_tae_content_edit_hint_text_color, getResources().getColor(R.color.ucu_colorFont_gray));   //内容编辑框提示文本颜色
        String contentEditText = array.getString(R.styleable.ucul_text_and_edit_view_tae_content_edit_text);                                                                        //内容编辑框文本
        float contentEditTextSize = array.getDimensionPixelSize(R.styleable.ucul_text_and_edit_view_tae_content_edit_text_size, 15);                                                //内容编辑框文本大小
        int contentEditTextColor = array.getColor(R.styleable.ucul_text_and_edit_view_tae_content_edit_text_color, getResources().getColor(R.color.ucu_colorFont_black));           //内容编辑框文本颜色
        Drawable contentEditTextBg = array.getDrawable(R.styleable.ucul_text_and_edit_view_tae_content_edit_text_bg);                                                               //内容编辑框文本背景

        txt_title.setText(titleText);
        txt_title.getPaint().setTextSize(titleTextSize);
        txt_title.setTextColor(titleTextColor);
        txt_title.setBackground(titleTextBg);

        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) txt_title.getLayoutParams();
        linearParams.width = titleTextWidth;
        txt_title.setLayoutParams(linearParams);

        txt_division.setBackgroundColor(divisionBgColor);
        txt_division.setVisibility(divisionVisibility);

        et_content.setHint(contentEditHintText);
        et_content.setHintTextColor(contentEditHintTextColor);
        et_content.setText(contentEditText);
        et_content.getPaint().setTextSize(contentEditTextSize);
        et_content.setTextColor(contentEditTextColor);
        et_content.setBackground(contentEditTextBg);

        array.recycle();
    }

    /**
     * 设置标题文本
     *
     * @param titleText
     */
    public void setTitleText(String titleText)
    {
        if (titleText != null)
        {
            txt_title.setText(titleText);
        }
    }

    /**
     * 设置标题文本大小
     *
     * @param size
     */
    public void setTitleTextSize(int size)
    {
        txt_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置标题文本颜色
     *
     * @param color
     */
    public void setTitleTextColor(int color)
    {
        txt_title.setTextColor(getResources().getColor(color));
    }

    /**
     * 设置标题文本背景
     *
     * @param res
     */
    public void setTitleTextBackground(int res)
    {
        txt_title.setBackground(getResources().getDrawable(res));
    }

    /**
     * 设置标题文本宽度
     *
     * @param width
     */
    public void setTitleTextWidth(int width)
    {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) txt_title.getLayoutParams();
        linearParams.width = width;
        txt_title.setLayoutParams(linearParams);
    }


    /**
     * 设置分割线背景颜色
     *
     * @param color
     */
    public void setDivisionBackgroundColor(int color)
    {
        txt_division.setBackgroundColor(getResources().getColor(color));
    }

    /**
     * 设置分割线显示
     *
     * @param visibility
     */
    public void setDivisionVisibility(int visibility)
    {
        txt_division.setVisibility(visibility);
    }

    /**
     * 设置内容编辑提示文本
     *
     * @param textHint
     */
    public void setContentEditTextHint(String textHint)
    {
        if (textHint != null)
        {
            et_content.setHint(textHint);
        }
    }

    /**
     * 设置标题提示文本颜色
     *
     * @param color
     */
    public void setTitleHintTextColor(int color)
    {
        et_content.setHintTextColor(getResources().getColor(color));
    }

    /**
     * 设置内容编辑文本
     *
     * @param contentText
     */
    public void setContentEditText(String contentText)
    {
        if (contentText != null)
        {
            et_content.setText(contentText);
        }
    }

    /**
     * 设置内容编辑文本大小
     *
     * @param size
     */
    public void setContentEditTextSize(int size)
    {
        et_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置内容编辑文本颜色
     *
     * @param color
     */
    public void setContentEditTextColor(int color)
    {
        et_content.setTextColor(getResources().getColor(color));
    }

    /**
     * 设置内容编辑文本背景
     *
     * @param res
     */
    public void setContentEditTextBackground(int res)
    {
        et_content.setBackground(getResources().getDrawable(res));
    }

    /**
     * 获取内容编辑文本内容
     *
     * @return
     */
    public String getContentEditText()
    {
        return et_content.getText().toString();
    }

    public EditText getEditTextView()
    {
        return et_content;
    }
}
