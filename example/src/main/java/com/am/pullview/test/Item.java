package com.am.pullview.test;

import com.am.pullview.adapter.AdapterModel;

class Item implements AdapterModel {
    String pic;
    String title;

    public Item(String pic, String title) {
        this.pic = pic;
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getDataTypeCount() {
        return 1;
    }

    @Override
    public Object getDataType() {
        return 1;
    }
}