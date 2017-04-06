package com.weiguowang.schoolmate.utils;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsManager;

import java.io.InputStream;
import java.util.ArrayList;


/**
 * function:
 * Created by 韦国旺 on 2017/4/5 0005.
 * Copyright (c) 2017 北京联龙博通 All Rights Reserved.
 */
public class Utils {
    /**
     * 输出游标结果集
     *
     * @param cursor
     */
    public static void printCursor(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                int columnCount = cursor.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);

                    System.out.println("第" + cursor.getPosition() + "行: "
                            + columnName + " = " + columnValue);
                }
            }

            cursor.close();
        }
    }

    /**
     * 根据号码获取联系人的姓名
     *
     * @param address
     * @return
     */
    public static String getContactName(ContentResolver resolver, String address) {
        Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(address));

        Cursor cursor = resolver.query(lookupUri, new String[]{"display_name"}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            // 联系人的姓名
            String contactName = cursor.getString(0);
            cursor.close();
            return contactName;
        }
        return null;
    }

    /**
     * 根据号码获取联系人的头像
     *
     * @param contentResolver
     * @param address
     * @return
     */
    public static Bitmap getContactIcon(ContentResolver contentResolver,
                                        String address) {
        // 获取联系人的id
        Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(address));

        Cursor cursor = contentResolver.query(lookupUri, new String[]{"_id"}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            // 联系人的id
            int id = cursor.getInt(0);
            cursor.close();

            // 根据联系人的id获取联系人头像的流信息
            Uri uri = ContentUris.withAppendedId(android.provider.ContactsContract.Contacts.CONTENT_URI, id);
            InputStream is = android.provider.ContactsContract.Contacts
                    .openContactPhotoInputStream(contentResolver, uri);
            return BitmapFactory.decodeStream(is);
        }
        return null;
    }

    /**
     * 发送短信
     *
     * @param address
     * @param content
     */
    public static void sendMessage(Context context, String address, String content) {
        SmsManager smsManager = SmsManager.getDefault();

        ArrayList<String> divideMessage = smsManager.divideMessage(content);

        // intent是传递给系统, 系统会发送此广播. 必须使用隐式的方式
        Intent intent = new Intent("com.itheima30.sendMessage");
        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_ONE_SHOT);

        for (String sms : divideMessage) {
            // 只给你发送短信, 没有向数据库中添加一条信息
            smsManager.sendTextMessage(
                    address,    // 对方的手机号
                    null,    // 短信中心号码
                    sms,    // 短信内容
                    sentIntent, // 延期的广播, 当发送成功时的回执信息.
                    null);    // 延期的广播, 当对方成功时的回执信息.
        }

        // 向数据库中添加一条消息
        writeMessage(context.getContentResolver(), address, content);
    }

    /**
     * 把号码和内容添加到短信数据库中.
     *
     * @param address
     * @param content
     */
    public static void writeMessage(ContentResolver resolver, String address, String content) {
        ContentValues values = new ContentValues();
        values.put("address", address);
        values.put("body", content);
        values.put("type", Contacts.SEND_TYPE);
        resolver.insert(Contacts.SMS_URI, values);
    }

    /**
     * 根据给定的uri获取联系人的id
     *
     * @param uri
     * @return -1  代表当前联系人没有添加过号码
     */
    public static int getContactID(ContentResolver resolver, Uri uri) {
        Cursor cursor = resolver.query(uri, new String[]{"has_phone_number", "_id"}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int hasPhoneNumber = cursor.getInt(0);

            if (hasPhoneNumber == 0) {
                // 当前联系人是没有号码的
                cursor.close();
                return -1;
            }

            int id = cursor.getInt(1);
            cursor.close();
            return id;
        }
        return 0;
    }

    /**
     * 根据联系人的id获取联系人的号码
     *
     * @param contentResolver
     * @param id
     * @return
     */
    public static String getContactAddress(ContentResolver contentResolver,
                                           int id) {
        Cursor cursor = contentResolver.query(Phone.CONTENT_URI, new String[]{"data1"}, "contact_id = " + id, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String address = cursor.getString(0);
            cursor.close();
            return address;
        }
        return null;
    }

    /**
     * 根据索引获取类型的uri
     *
     * @param index
     * @return
     */
    public static Uri getTypeUri(int index) {
        switch (index) {
            case 0:
                return Contacts.INBOX_URI;
            case 1:
                return Contacts.OUTBOX_URI;
            case 2:
                return Contacts.SENT_URI;
            case 3:
                return Contacts.DRAFT_URI;
            default:
                break;
        }
        return null;
    }
}
