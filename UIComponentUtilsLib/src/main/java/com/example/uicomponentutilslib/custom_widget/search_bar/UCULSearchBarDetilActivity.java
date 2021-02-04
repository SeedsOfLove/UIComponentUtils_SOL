package com.example.uicomponentutilslib.custom_widget.search_bar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluewater.commonpopuplib.CommonPopup;
import com.bluewater.commonpopuplib.CommonPopupImpl;
import com.bluewater.toolutilslib.SharedPreferencesUtils;
import com.example.uicomponentutilslib.R;
import com.example.uicomponentutilslib.custom_widget.UCULFlowLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UCULSearchBarDetilActivity extends AppCompatActivity
{
    private final static int FUZZY_QUARY = 0;                   //模糊查询
    private final static int GET_FUZZY_QUARY_FAIL = 1;          //模糊查询失败
    private final static int GET_FUZZY_QUARY_SUCCESS = 2;       //模糊查询成功

    private Context mContext;
    private Activity mActivity;

    private LinearLayout ll_Back;           //返回键
    private EditText et_Search;             //搜索框
    private ImageView iv_Del;               //删除搜索框内容按钮
    private TextView tv_Ok;                 //确定按钮

    private ImageView iv_ClearHistory;      //清空历史记录
    private UCULFlowLayout fl_history;      //历史记录标签容器

    private String strEditText;             //模糊查询实时文本
    private ListView lv_FuzzyQuery;         //模糊查询列表

    private String mFileName;               //存储数据的本地文件名(SharedPreferences)
    private List<String> listHistory;       //存储本地所有的历史搜索记录

    private String mUrl;                    //模糊查询地址
    private List<String> listFuzzy;         //存储模糊查询的数据
    private UCULSearchBarDetilAdapter mFuzzyAdapter;

    private String mStrResult;              //服务器返回的模糊查询结果

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ucul_search_bar_detil);

        this.mContext = this;
        this.mActivity = this;

        Intent intent = getIntent();
        this.mFileName = intent.getStringExtra("filename");
        this.mUrl = intent.getStringExtra("url");

        initSearchHistory();
        initView();
        initFuzzyQuery();
        initEvent();
    }

    /**
     * 初始化搜索历史
     */
    private void initSearchHistory()
    {
        listHistory = new ArrayList<>();

        SharedPreferencesUtils sharedPre = new SharedPreferencesUtils(this, mFileName);
        boolean first = sharedPre.getBoolean("first", true);   //若第一次创建，找不到"first"的值，则默认使用第二个参数的值，即true
        if (first)      //是否第一次创建
        {
            //设置不是第一次创建，搜索历史信息为空
            sharedPre.putValues(new SharedPreferencesUtils.ContentValue("first", false)
                    , new SharedPreferencesUtils.ContentValue("history", ""));
        }

        String strHistory = getLocalHistory();
        if (!strHistory.equals(""))     //是否有历史数据
        {
            for(Object o : strHistory.split(","))
            {
                listHistory.add((String) o);        //添加数据到list中
            }
        }
    }

    /**
     * 初始化视图
     */
    private void initView()
    {
        ll_Back = findViewById(R.id.ll_ucul_sbd_back);
        et_Search = findViewById(R.id.et_ucul_sbd);
        iv_Del = findViewById(R.id.iv_ucul_sbd_del);
        tv_Ok = findViewById(R.id.tv_ucul_sbd_ok);
        iv_ClearHistory = findViewById(R.id.iv_ucul_sbd_clear_history);
        fl_history = findViewById(R.id.fl_ucul_sbd);
        lv_FuzzyQuery = findViewById(R.id.lv_ucul_sbd);
    }

    /**
     * 初始化模糊查询
     */
    private void initFuzzyQuery()
    {
        listFuzzy = new ArrayList<>();
        mFuzzyAdapter = new UCULSearchBarDetilAdapter(mContext, listFuzzy);
        lv_FuzzyQuery.setAdapter(mFuzzyAdapter);

        //ListView item点击事件
        lv_FuzzyQuery.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                et_Search.setText(listFuzzy.get(position));             //点击ListView中的item，搜索框显示点击的item文本
                lv_FuzzyQuery.setVisibility(View.INVISIBLE);            //点击后模糊查询列表就不用显示了

                saveHistory();  //保存到本地

                //将数据返回给上级Activity
                Intent intent = new Intent();
                intent.putExtra("search_data_return", listFuzzy.get(position));
                setResult(RESULT_OK, intent);

                // 隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                finish();
            }
        });
    }

    /**
     * 初始化事件
     */
    private void initEvent()
    {
        //返回键点击事件
        ll_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.putExtra("search_data_return", "");      //返回给上个Activity的数据为空字符串
                setResult(RESULT_OK, intent);

                // 隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                finish();
            }
        });

        //搜索栏删除图标显示事件
        et_Search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                //如果有输入内容长度大于0那么显示清空按钮和模糊查询listview
                if (s.length() > 0)
                {
                    iv_Del.setVisibility(View.VISIBLE);
                    lv_FuzzyQuery.setVisibility(View.VISIBLE);

                    //每次数据改变的时候都会调用afterTextChanged，短时间内输入很多的字母时，由于每改变一个字母就要去搜索,搜索速度会大大下降
                    //解决方案：用户输入完成时再去执行搜索，比如500ms内文字没有改变，则判断为用户输入完成，否则取消执行搜索。
                    strEditText = s.toString().replace(" ","");  //去空格
                    if (handler.hasMessages(FUZZY_QUARY))
                    {
                        handler.removeMessages(FUZZY_QUARY);
                    }
                    Message msg = new Message();
                    msg.what = FUZZY_QUARY;
                    handler.sendMessageDelayed(msg, 500);
                }
                else
                {
                    iv_Del.setVisibility(View.INVISIBLE);
                    lv_FuzzyQuery.setVisibility(View.INVISIBLE);
                }
            }
        });

        //清空搜索栏
        iv_Del.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                et_Search.setText("");
            }
        });

        //确定键点击事件
        tv_Ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (et_Search.getText().toString().equals(""))
                {
                    Toast.makeText(mContext, "请输入查询内容", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    saveHistory();  //保存到本地

                    //将数据返回给上级Activity
                    Intent intent = new Intent();
                    intent.putExtra("search_data_return", et_Search.getText().toString());
                    setResult(RESULT_OK, intent);

                    // 隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                    finish();
                }
            }
        });

        //清空历史标签
        iv_ClearHistory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final CommonPopup popup = new CommonPopupImpl(mContext, false);
                popup.showConfirmDialog(null, "确认清空吗？", new CommonPopupImpl.OnConfirmDialogClickListener()
                {
                    @Override
                    public void onConfirmDialogOkButtonClick()
                    {
                        popup.dialogDismiss();

                        cleanHistory();                         //清空本地数据
                        listHistory.clear();                    //清空ListView
                        fl_history.removeAllViews();            //清空标签容器
                        Toast.makeText(mContext, "清除成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onConfirmDialogCancelButtonClick()
                    {
                        popup.dialogDismiss();
                    }
                });
            }
        });

        //历史标签点击事件
        fl_history.setViews(listHistory, new UCULFlowLayout.OnItemClickListener()
        {
            @Override
            public void onItemClick(String content)
            {
                et_Search.setText(content);                     //搜索框显示点击的标签文本
            }
        });
    }

    /**
     * 获得保存在本地的查询历史
     *
     * @return 返回含有搜索历史的字符串，格式为“XX,XX,XX...”
     */
    public String getLocalHistory()
    {
        SharedPreferencesUtils sharedPref = new SharedPreferencesUtils(this, mFileName);
        String history = sharedPref.getString("history");
        return history;
    }

    /**
     * 保存搜索历史到本地文件
     */
    private void saveHistory()
    {
        String text = et_Search.getText().toString();
        String oldText = getLocalHistory();
        if (!TextUtils.isEmpty(text) && !oldText.contains(text))    //若搜索栏不为空，并且本地历史记录里不包含要该搜索栏的内容
        {
            String history;
            if (oldText.equals(""))
            {
                history = text;
            }
            else
            {
                history = text + "," + oldText;         //拼接字符串
            }

            SharedPreferencesUtils sharedPref = new SharedPreferencesUtils(this, mFileName);
            sharedPref.putValues(new SharedPreferencesUtils.ContentValue("history", history));      //存入本地
        }
    }

    /**
     * 清空本地文件里的搜索历史数据
     */
    private void cleanHistory()
    {
        SharedPreferencesUtils sharedPref = new SharedPreferencesUtils(mContext, mFileName);
        sharedPref.putValues(new SharedPreferencesUtils.ContentValue("history", ""));           //将本地文件里的搜索历史置空
    }

    /**
     * 模糊查询
     */
    private void doFuzzyQuery(final String content)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String url = mUrl + content;

                mStrResult = getUrlContent(url, 8000);
                if (mStrResult == null)
                {
                    handler.sendEmptyMessage(GET_FUZZY_QUARY_FAIL);      //查询失败
                }
                else
                {
                    handler.sendEmptyMessage(GET_FUZZY_QUARY_SUCCESS);   //查询成功
                }
            }
        }).start();
    }

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case FUZZY_QUARY:
                    doFuzzyQuery(strEditText);     //进行模糊查询
                    break;
                case GET_FUZZY_QUARY_FAIL:
                    break;
                case GET_FUZZY_QUARY_SUCCESS:
                    showResult();       //显示模糊查询结果
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 显示模糊查询结果
     */
    private void showResult()
    {
        if (!mStrResult.equals(""))
        {
            listFuzzy.clear();
            String[] array = mStrResult.split(",");
            listFuzzy.addAll(Arrays.asList(array));
        }
        else
        {
            listFuzzy.clear();
        }

        mFuzzyAdapter.notifyDataSetChanged();     //刷新ListView
    }

    /**
     * 重写返回按钮
     */
    @Override
    public void onBackPressed()
    {
        //将数据返回给上级Activity
        Intent intent = new Intent();
        intent.putExtra("search_data_return", "");
        setResult(RESULT_OK, intent);

        finish();
    }

    /**
     * 获取URL内容
     * @param url           接口地址
     * @param timeOut       超时时间
     * @return
     */
    private String getUrlContent(String url, int timeOut)
    {
        String strResponse = null;
        HttpURLConnection conn = null;
        BufferedReader reader = null;

        //不加这句，低版本的Android无法自行对url里的中文转码，造成服务器返回出错
        url = Uri.encode(url, "-![.:/,%?&=]");      //解决url中文字符串问题，"-![.:/,%?&=]"表示不对花括号中的字符进行编码

        try
        {
            URL aURL = new URL(url);                                //使用该地址创建一个 URL 对象
            conn = (HttpURLConnection) aURL.openConnection();       //建立连接
            conn.setRequestMethod("GET");                           //设置请求方式
            conn.setConnectTimeout(timeOut);                        //设置请求超时时间
            conn.setReadTimeout(timeOut);                           //设置读取超时时间
            conn.connect();                                         //连接

            InputStream in = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            //读取获取到的输入流
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
            strResponse = sb.toString();

        } catch (Exception e)
        {
            Log.e("UCUL搜索栏", e.toString());
            e.printStackTrace();
        }finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (conn != null)
            {
                conn.disconnect();
            }
        }
        return strResponse;
    }
}