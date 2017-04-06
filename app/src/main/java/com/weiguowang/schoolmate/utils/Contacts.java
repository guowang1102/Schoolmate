package com.weiguowang.schoolmate.utils;

import android.net.Uri;

/**
 * function:
 * Created by 韦国旺 on 2017/4/5 0005.
 * Copyright (c) 2017 北京联龙博通 All Rights Reserved.
 */
public class Contacts {
    // 查询所有会话的uri
    public static final Uri SMS_CONVERSATION = Uri.parse("content://sms/conversations");

    // 操作sms表的uri
    public static final Uri SMS_URI = Uri.parse("content://sms");

    // 收件箱的uri
    public static final Uri INBOX_URI = Uri.parse("content://sms/inbox");
    // 发件箱的uri
    public static final Uri OUTBOX_URI = Uri.parse("content://sms/outbox");
    // 已发送的uri
    public static final Uri SENT_URI = Uri.parse("content://sms/sent");
    // 草稿箱的uri
    public static final Uri DRAFT_URI = Uri.parse("content://sms/draft");

    // 添加到群组表的操作
    public static final Uri GROUPS_INSERT_URI = Uri.parse("content://com.itheima30.smsmanager.provider.GroupContentProvider/groups/insert");

    // 查询所有群组的操作
    public static final Uri GROUPS_QUERY_ALL_URI = Uri.parse("content://com.itheima30.smsmanager.provider.GroupContentProvider/groups");

    // 修改群组的操作
    public static final Uri GROUPS_UPDATE_URI = Uri.parse("content://com.itheima30.smsmanager.provider.GroupContentProvider/groups/update");

    // 删除一条群组的操作
    public static final Uri GROUPS_DELETE_ITEM_URI = Uri.parse("content://com.itheima30.smsmanager.provider.GroupContentProvider/groups/delete/#");

    // 查询所有关联关系表数据的操作
    public static final Uri THREAD_GROUP_QUERY_ALL_URI = Uri.parse("content://com.itheima30.smsmanager.provider.GroupContentProvider/thread_group");

    // 添加到关联关系表的操作
    public static final Uri THREAD_GROUP_INSERT_URI = Uri.parse("content://com.itheima30.smsmanager.provider.GroupContentProvider/thread_group/insert");

    public static final int RECEIVE_TYPE = 1;	// 短信类型: 接收类型
    public static final int SEND_TYPE = 2;	// 短信类型: 发送类型
}
