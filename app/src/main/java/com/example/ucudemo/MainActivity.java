package com.example.ucudemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bluewaterlib.toastutilslib.ToastUtils;
import com.example.uicomponentutilslib.custom_widget.UCULEditAndBtnView;
import com.example.uicomponentutilslib.custom_widget.UCULQPopupWindow;
import com.example.uicomponentutilslib.custom_widget.UCULTextAndTextView;
import com.example.uicomponentutilslib.custom_widget.UCULTitleBarView;
import com.example.uicomponentutilslib.custom_widget.search_bar.UCULSearchBarView;
import com.example.uicomponentutilslib.custom_widget.spinner.UCULSpinnerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{
    @BindView(R.id.eabv)
    UCULEditAndBtnView eabv;    //输入框配按钮视图控件

    @BindView(R.id.tatv)
    UCULTextAndTextView tatv;    //文本框配文本框控件

    @BindView(R.id.tbv)
    UCULTitleBarView tbv;       //标题栏控件

    @BindView(R.id.spinner_view)
    UCULSpinnerView spinnerView;    //下拉框

    @BindView(R.id.sbv)
    UCULSearchBarView sbv;      //搜索栏

    @BindView(R.id.tv_long_menu)
    TextView tvLongMenu;        //长按弹出仿QQ菜单

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ToastUtils.initToast(this);

        mContext = this;

        initData();
        initEvents();
    }

    private void initData()
    {
        //下拉框
        List<String> listSpinner = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            listSpinner.add("选项" + i);
        }
        spinnerView.setItemsData(listSpinner);

        //搜索栏
        sbv.setHintText("请输入施工号");
        sbv.setFileName("ucul_search_bar_test");    //本地存储历史记录文件名
        sbv.setUrl("http://10.44.66.189:8117/AndroidServlet_war/AndroidServletDemo?action=app_get_order_num_fuzzy&fuzzy="); //模糊查询地址
    }

    private void initEvents()
    {
        eabv.setOnViewClick(new UCULEditAndBtnView.onIBtnClick()
        {
            @Override
            public void ibtnClick()
            {
                ToastUtils.onSuccessShowToast(eabv.getEditText());
            }
        });

        tatv.setOnViewClick(new UCULTextAndTextView.onContentTextClick()
        {
            @Override
            public void contentTextClick()
            {
                ToastUtils.onSuccessShowToast(tatv.getContentText());
            }
        });

        tbv.setOnViewClick(new UCULTitleBarView.onViewClick()
        {
            @Override
            public void leftClick()
            {
                ToastUtils.onSuccessShowToast("左");
            }

            @Override
            public void rightClick()
            {
                ToastUtils.onWarnShowToast("右");
            }
        });

        spinnerView.setOnViewClick(new UCULSpinnerView.onItemSpinnerClick()
        {
            @Override
            public void itemSpinnerClick(String itemContent)
            {
                ToastUtils.onInfoShowToast(itemContent);
            }
        });

        tvLongMenu.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                //获得控件在屏幕中的绝对坐标
                int[] location = new int[2];
                tvLongMenu.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
                tvLongMenu.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标

                UCULQPopupWindow.getInstance(mContext).builder
                        .bindView(view, 100)
                        .setPopupItemList(new String[]{"菜单1", "菜单2", "菜单3"})
                        .setNormalBackgroundColor(mContext.getResources().getColor(R.color.ucu_orange))
                        .setPressedBackgroundColor(mContext.getResources().getColor(R.color.ucu_blue))
                        .setPointers(location[0], location[1])
                        .setOnPopupListItemClickListener(new UCULQPopupWindow.OnPopupListItemClickListener()
                        {
                            @Override
                            public void onPopupListItemClick(View anchorView, int anchorViewPosition, int position)
                            {
                                switch (position)
                                {
                                    case 0:
                                        ToastUtils.onInfoShowToast("菜单1");
                                        break;
                                    case 1:
                                        ToastUtils.onInfoShowToast("菜单2");
                                        break;
                                    case 2:
                                        ToastUtils.onInfoShowToast("菜单3");
                                        break;
                                }
                            }
                        })
                        .show();

                return true;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)    //模糊查询请求码111
        {
            if (requestCode == 1312)//返回的模糊查询施工号
            {
                String returnData = data.getStringExtra("search_data_return");
                if (!returnData.equals(""))
                {
                    sbv.setText(returnData);
                    ToastUtils.onInfoShowToast(returnData);
                }

            }
        }
    }

    @OnClick(R.id.btn_refresh)
    public void onViewRefreshClicked()
    {
        Intent intent = new Intent(mContext, RefreshActivity.class);
        startActivity(intent);
    }
}