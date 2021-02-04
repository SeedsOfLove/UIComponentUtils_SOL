package com.example.uicomponentutilslib.custom_widget.spinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.uicomponentutilslib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义下拉框
 */
public class UCULSpinnerView extends LinearLayout
{
    private Context mContext;

    private LinearLayout linearLayout;
    private TextView textView;
    private ImageView imageView;
    private PopupWindow popupWindow = null;
    private onItemSpinnerClick mClick;

    private List<String> dataList;

    public UCULSpinnerView(Context context)
    {
        this(context, null);     //调用同名构造方法
    }

    public UCULSpinnerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);     //调用同名构造方法
    }

    public UCULSpinnerView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);    //通过上面的传参，实现无论系统调用，哪个构造方法，最终调用的是具有样式的构造方法
        this.mContext = context;
        dataList = new ArrayList<>();

        initView();
        initStyle(context, attrs);
    }

    /**
     * 初始化视图
     */
    private void initView()
    {
        LayoutInflater.from(mContext).inflate(R.layout.ucul_spinner_view, this);  //加载布局
        linearLayout = findViewById(R.id.ll_normal_spinner_util);
        textView = findViewById(R.id.tv_ucul_spinner_view);
        imageView = findViewById(R.id.iv_ucul_spinner_view);

        linearLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (dataList.size() == 0)
                {
                    return;
                }

                if (popupWindow == null)
                {
                    showPopWindow();
                }
                else
                {
                    closePopWindow();
                }
            }
        });
    }

    /**
     * 初始化相关自定义属性
     */
    private void initStyle(Context context, AttributeSet attrs)
    {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ucul_spinner_view);

        int iconImage = array.getResourceId(R.styleable.ucul_spinner_view_sv_arrow_icon, R.mipmap.ucul_arrow_down); //箭头图标
        Drawable spinnerBg = array.getDrawable(R.styleable.ucul_spinner_view_sv_bg);                                //下拉框背景

        imageView.setImageResource(iconImage);
        linearLayout.setBackground(spinnerBg);

        array.recycle();
    }

    /**
     * 打开下拉列表弹窗
     */
    private void showPopWindow()
    {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.ucul_spinner_popupwindow, null);   //加载布局

        int spinnerWidth = this.getWidth();             //下拉框控件宽
        popupWindow = new PopupWindow(contentView, spinnerWidth, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(this);       //显示PopupWindow

        ListView listView = contentView.findViewById(R.id.lv_ucul_spinner_view);
        UCULSpinnerViewListAdapter adapter = new UCULSpinnerViewListAdapter(mContext, dataList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                textView.setText(dataList.get(position));
                mClick.itemSpinnerClick(dataList.get(position));
                closePopWindow();
            }
        });
    }

    /**
     * 关闭下拉列表弹窗
     */
    private void closePopWindow()
    {
        popupWindow.dismiss();
        popupWindow = null;
    }

    /**
     * 设置数据
     * @param list
     */
    public void setItemsData(List<String> list)
    {
        dataList = list;
        textView.setText(dataList.get(0));
    }

    /**
     * 添加数据
     * @param content
     */
    public void addItemData(String content)
    {
        dataList.add(content);
        textView.setText(content);
    }

    /**
     * 清空数据
     */
    public void clearItems()
    {
        dataList.clear();
        textView.setText("");
    }

    /**
     * 获取当前显示的值
     * @return
     */
    public String getCurText()
    {
        return textView.getText().toString();
    }

    /**
     * 设置点击事件
     * @param click
     */
    public void setOnViewClick(onItemSpinnerClick click)
    {
        this.mClick = click;
    }

    /**
     * item点击事件接口
     */
    public interface onItemSpinnerClick
    {
        void itemSpinnerClick(String itemContent);
    }

    public List<String> getDataList()
    {
        return dataList;
    }
}
