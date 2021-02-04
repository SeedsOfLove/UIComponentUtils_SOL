package com.example.uicomponentutilslib.custom_widget.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uicomponentutilslib.R;

import java.util.List;

public class UCULSpinnerViewListAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private List<String> mList;

    //这个ViewHolder只能服务于当前这个特定的adapter，因为ViewHolder里会指定item的控件，不同的ListView，item可能不同，所以ViewHolder写成一个私有的类
    private class ViewHolder
    {
        TextView textContent;
    }

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public UCULSpinnerViewListAdapter(Context context, List<String> list)
    {
        mInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.item_ucul_spinner_view, parent, false); //加载布局

            holder = new ViewHolder();
            holder.textContent = convertView.findViewById(R.id.tv_ucul_spinner_view_item);
            convertView.setTag(holder);
        }
        else    //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textContent.setText(mList.get(position));

        return convertView;
    }

    @Override
    public Object getItem(int position)
    {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getCount()
    {
        return mList.size();
    }
}
