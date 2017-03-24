package com.weiguowang.schoolmate.utils;

import android.text.TextUtils;

/**
 * function:
 * Created by 韦国旺 on 2017/3/10 0010.
 * Copyright (c) 2017 All Rights Reserved.
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    public static boolean isMobile(String number) {
        return false;

    }

    public static boolean isEmail(String email) {
        return false;
    }

    public static boolean isNotEmpty(CharSequence str) {

        return !isEmpty(str);

    }

    public static boolean isEmpty(CharSequence str) {

        return str == null || str.length() == 0;

    }
}
