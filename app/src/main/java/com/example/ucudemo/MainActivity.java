package com.example.ucudemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bluewaterlib.toastutilslib.ToastUtils;
import com.example.uicomponentutilslib.custom_widget.UCULEditAndBtnView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{
    @BindView(R.id.eabv)
    UCULEditAndBtnView eabv;    //输入框配按钮视图控件

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ToastUtils.initToast(this);

        initEvents();
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





















    }

}