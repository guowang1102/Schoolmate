package com.weiguowang.schoolmate.utils;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.CursorAdapter;

/**
 * function:
 * Created by 韦国旺 on 2017/4/5 0005.
 * Copyright (c) 2017 北京联龙博通 All Rights Reserved.
 */
public class CommonAsyncQueryHandler extends AsyncQueryHandler {
    private OnNotifyAdapterListener mOnNotifyAdapterListener;

    public CommonAsyncQueryHandler(ContentResolver cr) {
        super(cr);
    }

    /**
     * 设置当通知adapter更新的回调事件
     * @param listener
     */
    public void setOnNotifyAdapterListener(OnNotifyAdapterListener listener) {
        this.mOnNotifyAdapterListener = listener;
    }

    /**
     * 当异步查询完毕时, 回调此方法. 把查询的结果传递过来 特点: 此方法是执行在主线程中. 更新界面
     * @param cursor 查询的结果
     * @param token startQuery 接收的参数  token
     * @param cookie  startQuery 接收的参数 cookie
     */
    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        // 在更新adapter之前, 调用使用者的回调事件
        if(mOnNotifyAdapterListener != null) {
            mOnNotifyAdapterListener.onPreviousNotify(token, cookie, cursor);
        }

        // 更新adapter
        if(cookie != null && cookie instanceof CursorAdapter) {	// 当前的cookie对象是cursoradapter对象. 说明使用者传递过来, 想让我们这个方法更新数据
            CursorAdapter mAdapter = (CursorAdapter) cookie;
            mAdapter.changeCursor(cursor);	// 把最新查出来的结果, 设置给适配器, 适配器会调用适配器中的方法去绑定数据.
        } else {
            Utils.printCursor(cursor);    // 输出游标
        }

        // 在更新adapter之后, 调用使用者的回调事件
        if(mOnNotifyAdapterListener != null) {
            mOnNotifyAdapterListener.onPostNotify(token, cookie, cursor);
        }
    }

    /**
     * @author andong
     * 当更新adapter时的监听事件
     */
    public interface OnNotifyAdapterListener {

        /**
         * 当更新之前调用
         * @param token
         * @param cookie
         * @param cursor
         */
        void onPreviousNotify(int token, Object cookie, Cursor cursor);

        /**
         * 当更新之后调用
         * @param token
         * @param cookie
         * @param cursor
         */
        void onPostNotify(int token, Object cookie, Cursor cursor);

    }
}
