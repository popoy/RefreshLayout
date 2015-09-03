package com.am.pullview.recycleview.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.am.pullview.adapter.AdapterModel;
import com.am.pullview.adapter.recycler.CommonRcvAdapter;

import java.util.List;


/**
 * @author Jack Tony
 * @date 2015/6/2
 */
public abstract class ExCommonRcvAdapter<T extends AdapterModel> extends CommonRcvAdapter<T> {

    //protected final String TAG = getClass().getSimpleName();
    
    protected View customHeaderView = null;

    protected View customFooterView = null;

    protected AdapterView.OnItemClickListener mOnItemClickListener;

    protected AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    protected ExCommonRcvAdapter(List<T> data) {
        super(data);
    }

    /**
     * view的基本类型，这里只有头/底部/普通，在子类中可以扩展
     */
    class VIEW_TYPES {

        public static final int HEADER = 7;

        public static final int FOOTER = 8;
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPES.HEADER && customHeaderView != null) {
            return new SimpleViewHolder(customHeaderView);
        } else if (viewType == VIEW_TYPES.FOOTER && customFooterView != null) {
            return new SimpleViewHolder(customFooterView);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    /**
     * 返回adapter中总共的item数目，包括头部和底部
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        int headerOrFooter = 0;
        if (customHeaderView != null) {
            headerOrFooter++;
        }
        if (customFooterView != null) {
            headerOrFooter++;
        }
        return super.getItemCount() + headerOrFooter;
    }

    @Override
    public int getItemViewType(int position) {
        if (customFooterView != null && position == getItemCount() - 1) {
            return VIEW_TYPES.FOOTER;
        } else if (customHeaderView != null && position == 0) {
            return VIEW_TYPES.HEADER;
        } else {
            if (customHeaderView != null) {
                return super.getItemViewType(position - 1);
            }
            return super.getItemViewType(position);
        }
    }
    
    /**
     * 载入ViewHolder，这里仅仅处理header和footer视图的逻辑
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        if ((customHeaderView != null && position == 0) || (customFooterView != null && position == getItemCount() - 1)) {
            // 如果是header或者是footer则不处理
        } else {
            if (customHeaderView != null) {
                position--;
            }
            super.onBindViewHolder(viewHolder, position);

            final int pos = position;
            // 设置点击事件  
            if (mOnItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(null, viewHolder.itemView, pos, pos);
                    }
                });
            }
            // 设置长按事件
            if (mOnItemLongClickListener != null) {
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return mOnItemLongClickListener.onItemLongClick(null, viewHolder.itemView, pos, pos);
                    }
                });
            }
        }
    }

}
