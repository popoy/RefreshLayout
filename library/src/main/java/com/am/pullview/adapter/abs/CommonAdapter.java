package com.am.pullview.adapter.abs;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.am.pullview.adapter.AdapterItem;
import com.am.pullview.adapter.AdapterModel;
import com.am.pullview.adapter.ViewHolder;

import java.util.HashMap;
import java.util.List;


/**
 * @author Jack Tony
 * @date 2015/5/15
 */
public abstract class CommonAdapter<T extends AdapterModel> extends BaseAdapter {

    private List<T> mData;

    protected CommonAdapter(List<T> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * instead by getData().get(position).getDataType()
     */
    @Override
    @Deprecated
    public int getItemViewType(int position) {
        return -1;
    }

    @Override
    public int getViewTypeCount() {
        return mData.get(0).getDataTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterItem<T> item = getItemByType(mData.get(position).getDataType());
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(item.getLayoutResId(), null);
        }
        item.initViews(ViewHolder.getInstance(convertView), mData.get(position), position);
        return convertView;
    }

    protected abstract
    @NonNull
    AdapterItem<T> initItemView(Object type);

    /**
     * 可以被复写用于单条刷新等
     */
    public void updateData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }


    // (type - item) = (key - value)
    private HashMap<Object, AdapterItem<T>> mItemMap = new HashMap<Object, AdapterItem<T>>();

    /**
     * 根据相应的类型得到item对象
     *
     * @param type item的类型
     */
    private AdapterItem<T> getItemByType(Object type) {
        AdapterItem<T> item = mItemMap.get(type);
        if (item == null) {
            item = initItemView(type);
            mItemMap.put(type, item);
        }
        return item;
    }
}
