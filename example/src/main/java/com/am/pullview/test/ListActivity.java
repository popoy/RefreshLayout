package com.am.pullview.test;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.am.pullview.adapter.AdapterItem;
import com.am.pullview.adapter.AdapterModel;
import com.am.pullview.adapter.ViewHolder;
import com.am.pullview.recycleview.layoutmanager.NestLinearLayoutManager;
import com.am.pullview.recycleview.recycler.ExCommonRcvAdapter;
import com.am.pullview.recycleview.recycler.ExRecyclerView;
import com.am.pullview.recycleview.recycler.OnRecyclerViewScrollListener;
import com.am.pullview.swiperefresh.SwipeRefreshLayout;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends ActionBarActivity {
    private static final String imageURL = "http://img1.imgtn.bdimg.com/it/u=46636934,3749739269&fm=21&gp=0.jpg";
    private final static String TAG = "ListActivity";
    SwipeRefreshLayout mSwipeRefreshWidget;
    ProgressBar mProgressBar;
    ExRecyclerView mRecyclerView;
    SampleAdapter adapter;
    private ArrayList<Item> values;
    View footerView;
    private volatile boolean isLoadingMore = false;

   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        footerView = LayoutInflater.from(this).inflate(R.layout.list_item_footer, null);
        mProgressBar = (ProgressBar) footerView.findViewById(R.id.browse_view_bottom_progressbar);
        mRecyclerView = (ExRecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.addFooterView(footerView);
        RecyclerView.LayoutManager layoutManager = new NestLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        values = new ArrayList<Item>();
        for (int i = 0; i < 15; i++) {
            values.add(new Item(imageURL, "Item " + i));
        }
        adapter = new SampleAdapter(this, values);
        mRecyclerView.setAdapter(adapter);

        mSwipeRefreshWidget.setColorScheme(R.color.google_red, R.color.google_red,
                R.color.google_red, R.color.google_red);
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
        mSwipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        });
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
        TextView textView;
        Context context;

        public ItemView(Context context) {
            this.context = context;
        }

        @Override
        public int getLayoutResId() {
            return R.layout.list_item;
        }

        @Override
        public void initViews(ViewHolder vh, Item model, int position) {
            picCountTextView = vh.getView(R.id.imageview);
            textView = vh.getView(R.id.title);
            textView.setText(values.get(position).getTitle());
            Glide.with(context).load(values.get(position).getPic()).fitCenter().into(picCountTextView);
        }
    }
}
