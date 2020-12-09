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
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.uicomponentutilslib.R;

/**
 * 编辑框配按钮控件
 */
public class UCULEditAndBtnView extends LinearLayout
{
    private Context mContext;

    private View root;
    private EditText editText;          //文本
    private ImageButton ibtn;           //按钮

    private onIBtnClick mClick;

    public UCULEditAndBtnView(Context context)
    {
        this(context, null);     //调用同名构造方法
    }

    public UCULEditAndBtnView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);     //调用同名构造方法
    }

    public UCULEditAndBtnView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);    //通过上面的传参，实现无论系统调用，哪个构造方法，最终调用的是具有样式的构造方法
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = inflater.inflate(R.layout.ucul_edit_and_btn_view, this, true);

        initView();
        initStyle(context, attrs, defStyleAttr);

        ibtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mClick != null)
                    mClick.ibtnClick();
            }
        });
    }

    /**
     * 初始化界面
     */
    private void initView()
    {
        editText = root.findViewById(R.id.ev_seabv);
        ibtn = root.findViewById(R.id.ibtn_seabv);
    }

    /**
     * 初始化相关自定义属性
     */
    private void initStyle(Context context, AttributeSet attrs, int defStyleAttr)
    {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ucul_edit_and_btn_view, defStyleAttr, 0);

        String hintText = array.getString(R.styleable.ucul_edit_and_btn_view_eab_hint_text);                                                                //提示文本
        int editTextHintColor = array.getColor(R.styleable.ucul_edit_and_btn_view_eab_hint_color, getResources().getColor(R.color.ucu_colorFont_gray));     //编辑框文本颜色
        float editTextSize = array.getDimensionPixelSize(R.styleable.ucul_edit_and_btn_view_eab_text_size, 15);                                             //编辑框文本大小
        int editTextColor = array.getColor(R.styleable.ucul_edit_and_btn_view_eab_text_color, getResources().getColor(R.color.ucu_colorFont_black));        //编辑框文本颜色
        Drawable editBg = array.getDrawable(R.styleable.ucul_edit_and_btn_view_eab_edit_bg);                                                                //编辑框背景
        int btnImage = array.getResourceId(R.styleable.ucul_edit_and_btn_view_eab_btn_icon, 0);                                                             //按钮图标

        editText.setHint(hintText);
        editText.setHintTextColor(editTextHintColor);
        editText.getPaint().setTextSize(editTextSize);
        editText.setTextColor(editTextColor);
        editText.setBackground(editBg);
        ibtn.setImageResource(btnImage);

        array.recycle();
    }

    /**
     * 设置输入框的提示文本
     *
     * @param textHint
     */
    public void setTextHint(String textHint)
    {
        if (textHint != null)
        {
            editText.setHint(textHint);
        }
    }

    /**
     * 设置编辑框提示文本颜色
     *
     * @param color
     */
    public void setEditTextHintColor(int color)
    {
        editText.setHintTextColor(getResources().getColor(color));
    }

    /**
     * 设置编辑框文本大小
     *
     * @param size
     */
    public void setEditTextSize(int size)
    {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置编辑框文本颜色
     *
     * @param color
     */
    public void setEditTextColor(int color)
    {
        editText.setTextColor(getResources().getColor(color));
    }

    /**
     * 设置编辑框背景
     *
     * @param res
     */
    public void setEditTextBackground(int res)
    {
        editText.setBackground(getResources().getDrawable(res));
    }

    /**
     * 设置按钮图标
     *
     * @param res
     */
    public void setDrawable(int res)
    {
        ibtn.setImageResource(res);
    }

    /**
     * 设置文本内容
     *
     * @return
     */
    public void setEditText(String content)
    {
        editText.setText(content);
    }

    /**
     * 获取文本内容
     *
     * @return
     */
    public String getEditText()
    {
        return editText.getText().toString();
    }

    /**
     * 按钮点击事件接口
     */
    public interface onIBtnClick
    {
        void ibtnClick();
    }

    public void setOnViewClick(onIBtnClick click)
    {
        this.mClick = click;
    }

    public EditText getEditTextView()
    {
        return editText;
    }
}

