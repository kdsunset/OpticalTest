package com.zin.opticaltest.fragment;

import com.zin.opticaltest.entity.OpticalTestURL;

/**
 * Created by ZIN on 2016/4/16.
 */
public class RealPaperFrag extends MyBaseFragement{
    @Override
    public String setURL() {
        return OpticalTestURL.URL_RealPaper;
    }

    @Override
    public String setId() {
        return null;
    }
}
