package com.example.uicomponentutilslib.custom_widget.search_bar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uicomponentutilslib.R;

/**
 * 自定义搜索栏（模糊查询、搜索历史）
 * 模糊查询结果格式（以逗号分隔，如：xxx,xxx,xxx,xxx...）
 */
public class UCULSearchBarView extends LinearLayout
{
    private static final int REQUEST_UCUL_SEARCH_BAR = 1312;   //查询请求码，用于查询完成后信息的回调

    private LinearLayout ll_main;
    private ImageView imageView;
    private TextView textView;

    private String fileName;        //存储数据的本地文件名(SharedPreferences)
    private String url;             //模糊查询地址

    private String text;

    public UCULSearchBarView(Context context)
    {
        this(context, null);     //调用同名构造方法
    }

    public UCULSearchBarView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);     //调用同名构造方法
    }

    public UCULSearchBarView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);    //通过上面的传参，实现无论系统调用，哪个构造方法，最终调用的是具有样式的构造方法
        LayoutInflater.from(context).inflate(R.layout.ucul_search_bar_view, this);

        initView();
        initEvent(context);
    }

    /**
     * 初始化界面
     */
    private void initView()
    {
        ll_main = findViewById(R.id.ll_ucul_search_bar_view);
        imageView = findViewById(R.id.iv_ucul_search_bar_view);
        textView = findViewById(R.id.tv_ucul_search_bar_view);
    }

    /**
     * 初始化点击事件
     */
    private void initEvent(final Context context)
    {
        ll_main.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, UCULSearchBarDetilActivity.class);
                intent.putExtra("filename", getFileName());
                intent.putExtra("url", getUrl());

                Activity activity = (Activity)context;
                activity.startActivityForResult(intent, REQUEST_UCUL_SEARCH_BAR);   //第二个参数是请求码（只要是唯一值就可以），用于在之后的回调中判断数据的来源。
            }
        });
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getText()
    {
        this.text = textView.getText().toString();
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
        textView.setText(this.text);
    }

    public void setHintText(String hintText)
    {
        textView.setHint(hintText);
    }
}
