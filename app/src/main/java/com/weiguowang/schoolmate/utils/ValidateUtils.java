package com.weiguowang.schoolmate.utils;

/**
 * function: 企业组织机构代码验证JavaScript版和Java版 - 修正版V20090214
 * Created by 韦国旺 on 2017/4/5 0005.
 * Copyright (c) 2017 北京联龙博通 All Rights Reserved.
 */
public class ValidateUtils {
    /**
     * 验证企业代码是否正确
     *
     * @param code 企业组织机构代码
     * @return
     */
    public static final boolean isEntperisCode(String code) {
        int[] ws = {3, 7, 9, 10, 5, 8, 4, 2};
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String regex = "^([0-9A-Z]){8}-[0-9|X]$";

        if (!code.matches(regex)) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += str.indexOf(String.valueOf(code.charAt(i))) * ws[i];
        }

        int c9 = 11 - (sum % 11);

        String sc9 = String.valueOf(c9);
        if (11 == c9) {
            sc9 = "0";
        } else if (10 == c9) {
            sc9 = "X";
        }
        return sc9.equals(String.valueOf(code.charAt(9)));
    }
}
