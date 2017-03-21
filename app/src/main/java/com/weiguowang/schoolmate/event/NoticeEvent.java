package com.weiguowang.schoolmate.event;

/**
 * function : 通知
 * Created by 韦国旺 on 2017/3/20.
 * Copyright (c) 2017 All Rights Reserved.
 */


public class NoticeEvent {

    public final int what;
    public static final int WHAT_UPDATE_HEAD = 100;

    public NoticeEvent(int what) {
        this.what = what;
    }

}
