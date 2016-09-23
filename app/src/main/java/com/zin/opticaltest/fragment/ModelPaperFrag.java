package com.zin.opticaltest.fragment;

/**
 * Created by ZIN on 2016/4/16.
 */
public class ModelPaperFrag extends  MyBaseFragement{
    @Override
    public String setURL() {
        return "http://192.168.1.122:8080/otweb/testBank/tlist.action";
    }

    @Override
    public String setId() {
        return null;
    }
}
