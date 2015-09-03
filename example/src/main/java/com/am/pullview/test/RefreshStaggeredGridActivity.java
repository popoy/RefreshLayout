package com.am.pullview.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.am.pullview.adapter.AdapterItem;
import com.am.pullview.adapter.ViewHolder;
import com.am.pullview.recycleview.layoutmanager.ExStaggeredGridLayoutManager;
import com.am.pullview.recycleview.recycler.ExCommonRcvAdapter;
import com.am.pullview.recycleview.recycler.ExRecyclerView;
import com.am.pullview.recycleview.recycler.OnRecyclerViewScrollListener;
import com.am.pullview.swiperefresh.SwipeRefreshLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class RefreshStaggeredGridActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String imageURL = "http://img.ivsky.com/img/bizhi/slides/201507/19/monkey_king_hero_is_back-011.jpg";
    private static final String imageURL2 = "http://creatim.qtmojo.cn/imedia/csdn/20150720/15_29_29_0F4F8D2A.gif";
    private final static String TAG = "RefreshStaggeredGridActivity";
    private ArrayList<Item> values;
    SwipeRefreshLayout mSwipeRefreshWidget;
    ExRecyclerView mRecyclerView;
    SampleAdapter adapter;
    ExStaggeredGridLayoutManager mgr;
    ProgressBar mProgressBar;
    View footerView;
    private volatile boolean isLoadingMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered_grid);
        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        footerView = LayoutInflater.from(this).inflate(R.layout.list_item_footer, null);
        mProgressBar = (ProgressBar) footerView.findViewById(R.id.browse_view_bottom_progressbar);
        mRecyclerView = (ExRecyclerView) findViewById(android.R.id.list);
        mSwipeRefreshWidget.setOnRefreshListener(this);
        mRecyclerView.setOnScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onBottom() {
                footerView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                if (!isLoadingMore && !mSwipeRefreshWidget.isRefreshing()) {
                    loadMore();
                }
            }

            @Override
            public void onMoved(int i, int i1) {

            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mgr = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mSwipeRefreshWidget.setColorScheme(R.color.google_red, R.color.google_red,
                R.color.google_red, R.color.google_red);
        mgr.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(mgr);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        values = new ArrayList<Item>();
        for (int i = 0; i < 15; i++) {
            values.add(new Item(imageURL, "Item " + i));
        }
        adapter = new SampleAdapter(this, values);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void loadMore() {
        isLoadingMore = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                values.add(new Item(imageURL, "Swipe Down to Refresh " + values.size()));
                values.add(new Item(imageURL, "Swipe Down to Refresh " + values.size()));
                values.add(new Item(imageURL, "Swipe Down to Refresh " + values.size()));
                values.add(new Item(imageURL, "Swipe Down to Refresh " + values.size()));
                values.add(new Item(imageURL, "Swipe Down to Refresh " + values.size()));
                values.add(new Item(imageURL, "Swipe Down to Refresh " + values.size()));
                values.add(new Item(imageURL, "Swipe Down to Refresh " + values.size()));
                adapter.notifyDataSetChanged();
                isLoadingMore = false;
            }
        }, 1000);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mSwipeRefreshWidget.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        values.add(0, new Item(imageURL, "Swipe up to Refresh " + values.size()));
                        adapter.notifyDataSetChanged();
                        mSwipeRefreshWidget.setRefreshing(false);
                    }
                }, 1000);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                values.add(0, new Item(imageURL, "Swipe up to Refresh " + values.size()));
                adapter.notifyDataSetChanged();
                mSwipeRefreshWidget.setRefreshing(false);
            }
        }, 1000);
    }


    public class SampleAdapter extends ExCommonRcvAdapter<Item> {
        ArrayList<Item> values;
        Context context;
        private static final int TYPE_ITEM = 1;
        private static final int TYPE_FOOTER = 2;


        public SampleAdapter(Context context, ArrayList<Item> values) {
            super(values);
            this.values = values;
            this.context = context;
        }

        @NonNull
        @Override
        protected AdapterItem<Item> initItemView(Object type) {
            switch (Integer.valueOf(type.toString())) {
                case TYPE_ITEM:
                    return new ItemView(context);
                case TYPE_FOOTER:
                default:
                    return null;
            }
        }
    }

    class ItemView implements AdapterItem<Item> {
        ImageView picCountTextView;
        Context context;

        public ItemView(Context context) {
            this.context = context;
        }

        @Override
        public int getLayoutResId() {
            return R.layout.staggergrid_item;
        }

        @Override
        public void initViews(ViewHolder vh, Item model, int position) {
            picCountTextView = vh.getView(R.id.imageview);
            Glide.with(context).load(values.get(position).getPic()).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).into(picCountTextView);
        }
    }

}
