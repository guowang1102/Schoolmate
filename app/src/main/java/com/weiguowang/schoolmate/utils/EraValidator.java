package com.weiguowang.schoolmate.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * function:
 * Created by 韦国旺 on 2017/4/5 0005.
 * Copyright (c) 2017 北京联龙博通 All Rights Reserved.
 */
public class EraValidator {
    // 保存每个月的天数
    private static final int[] DAYS_OF_MONTH = {31, 28, 31, 30, 31, 30, 31,
            31, 30, 31, 30, 31};

    // 日历的起始年份
    public static final int START_YEAR = 1900;

    // 日历的结束年份
    public static final int END_YEAR = 2100;

    private static final String ZERO_STRING = "0";

    /***************************************************************************
     * 匹配英文字母 或者汉字 如"Shenzhen" "深圳"
     *
     * @param str 待匹配字符串
     * @return true 匹配通过 false 匹配失败
     */
    public static boolean isValidEnglishOrChinese(String str) {
        // 1、[A-Za-z]* 英文字母的匹配 一次或者多次
        // 2、[\u4E00-\u9FA5]* 汉字匹配 一次或者多次
        boolean flag = false;
        Pattern p = Pattern.compile("^[A-Za-z]*|[\u4E00-\u9FA5]*$");
        if (str != null) {
            Matcher match = p.matcher(str);
            flag = match.matches();
        }
        return flag;
    }

    /***************************************************************************
     * 匹配英中文姓名 与英文名 英文名格式为：姓与名之间用/隔开 例如Green/Jim King
     *
     * @param str 待匹配字符串
     * @return true 匹配通过 false 匹配失败
     */
    public static boolean isValidName(String str) {
        // 1、[A-Za-z]* 英文字母的匹配 一次或者多次
        // 2、[\u4E00-\u9FA5]* 汉字匹配 一次或者多次
        boolean flag = false;
        Pattern p = Pattern
                .compile("^([A-Za-z]+[\\/][A-Za-z]+)|[\u4E00-\u9FA5]*");
        if (str != null) {
            Matcher match = p.matcher(str);
            flag = match.matches();
        }
        return flag;
    }


    /**
     * 验证字串的长度范围,min为空max不为空,验证是否小于max;min不为空max为空,验证是否大于min
     *
     * @return
     */
    public static boolean isLengthScope(String str, Integer minLenth, Integer maxLength, boolean isConfine) {
        if (null == str || "".equals(str) || (minLenth == null && maxLength == null)) {
            return false;
        }
        if (minLenth != null && maxLength == null) {//验证是否大于min长度
            if (isConfine && str.length() >= minLenth) {//大于等于min
                return true;
            } else if (!isConfine && str.length() > minLenth) {//大于min
                return true;
            }
        } else if (minLenth == null && maxLength != null) {
            if (isConfine && str.length() <= maxLength) {//小于等于max
                return true;
            } else if (!isConfine && str.length() < maxLength) {//小于max
                return true;
            }
        } else {
            if (isConfine && (minLenth <= str.length() && str.length() <= maxLength)) {
                return true;
            } else if (!isConfine && (minLenth < str.length() && str.length() < maxLength)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 功能：验证身份证
     *
     * @return Hashtable 对象
     */
    public static String isIDCard(String IDStr) {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                return errorInfo;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable<?, ?> h = getAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        } else {
            return "YES";
        }
        // =====================(end)=====================
        return "YES";
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    private static Hashtable<String, String> getAreaCode() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param strDate
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))" +
                        "[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])" +
                        "|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])" +
                        "|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|" +
                        "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|" +
                        "(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }
    /*********************************** 身份证验证结束 ****************************************/


    /***************************************************************************
     * 正整数验证
     *
     * @param str 待验证字符串
     * @return true 验证通过 false 验证失败
     */
    public static boolean isValidInteger(String str) {
        boolean flag = false;
        Pattern p = Pattern.compile("^\\d*$");
        if (str != null) {
            Matcher match = p.matcher(str);
            flag = match.matches();
        }
        return flag;
    }

    /***************************************************************************
     * 整数验证(包括正整数与 负整数)
     *
     * @param str 待验证字符串
     * @return true 验证通过 false 验证失败
     */
    public static boolean isValidNo(String str) {
        boolean flag = false;
        Pattern p = Pattern.compile("^-?\\d*$");
        if (str != null) {
            Matcher match = p.matcher(str);
            flag = match.matches();
        }
        return flag;
    }

    /**
     * 验证非负整数(正整数+0)
     *
     * @param str 待验证字符串
     * @return true 验证通过 false 验证失败
     */
    public static boolean isValidNonNegative(String str) {
        boolean flag = false;
        Pattern p = Pattern.compile("^\\d+$");
        if (str != null) {
            Matcher match = p.matcher(str);
            flag = match.matches();
        }
        return flag;
    }

    /**
     * 验证非负整数(正整数+0)
     *
     * @param str 待验证字符串
     * @return true 验证通过 false 验证失败
     */
    public static boolean isValidPositiveInteger(String str) {
        boolean flag = false;
        Pattern p = Pattern.compile("^\\d+$");
        if (str != null) {
            Matcher match = p.matcher(str);
            flag = match.matches();
            if (ZERO_STRING.equals(str)) {
                flag = false;
            }
        }

        return flag;
    }

    /***************************************************************************
     * 匹配英文字母(汉语拼音)
     *
     * @param str 待匹配字符串
     * @return true 匹配通过 false 匹配失败
     */
    public static boolean isValidEnglish(String str) {
        boolean flag = false;
        Pattern p = Pattern.compile("^[A-Za-z]*$");
        if (str != null) {
            Matcher match = p.matcher(str);
            flag = match.matches();
        }
        return flag;
    }

    /***************************************************************************
     * 匹配英文字母 或者汉字，数字 过滤特殊字符
     *
     * @param str 待匹配字符串
     * @return true 匹配通过 false 匹配失败
     */
    public static boolean isValidNonSpecialChar(String str) {
        boolean flag = false;
        Pattern p = Pattern.compile("^[A-Za-z\u4E00-\u9FA5\\d]*$");
        if (str != null) {
            Matcher match = p.matcher(str);
            flag = match.matches();
        }
        return flag;
    }

    /**
     * 验证HH时间格式的时间范围是否大于等于三小时 **注意此方法必须在isValidHour格式验证通过后调用
     *
     * @param startHour 开始时间 HH
     * @param endHour   结束时间HH
     * @return true 通过 false 不通过
     */
    public static boolean isVaildHourZone(String startHour, String endHour) {
        boolean flag = false;
        if (startHour != null && endHour != null) {
            if (isValidHour(startHour) && isValidHour(endHour)) // 格式验证，避免可能抛类型转换异常
            {
                int sHour = Integer.parseInt(startHour);
                int eHour = Integer.parseInt(endHour);
                flag = (eHour - sHour >= 3);
            }
        }
        return flag;

    }

    /***************************************************************************
     * 验证结束时间是否大于开始时间 **注意：此方法必须先调用isValidDate 方法后调用 yyMMdd
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return true 验证通过 false 验证失败
     */
    public static boolean isValidTimeZone(String startDate, String endDate) {
        boolean flag = false;
        if (startDate != null && endDate != null) {
            if (isValidDate(startDate) && isValidDate(endDate)) // 格式验证，避免可能抛类型转换异常
            {
                flag = (Integer.parseInt(endDate) > Integer.parseInt(startDate));
            }
        }
        return flag;
    }

    /***************************************************************************
     * 验证结束时间是否大于等于开始时间 **注意：此方法必须先调用isValidDate 方法后调用 yyMMdd（包含等于）
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return true 验证通过 false 验证失败
     */
    public static boolean isValidTwoTimes(String startDate, String endDate) {
        boolean flag = false;
        if (startDate != null && endDate != null) {
            if (isValidDate(startDate) && isValidDate(endDate)) // 格式验证，避免可能抛类型转换异常
            {
                flag = (Integer.parseInt(endDate) > Integer.parseInt(startDate) || Integer
                        .parseInt(endDate) == Integer.parseInt(startDate));
            }
        }
        return flag;
    }

    /***************************************************************************
     * 验证电话号码 后可接分机号 区号3位或者4位 电话7位或者8位后 后面可加3位或者4位分机号
     *
     * @param telephoeNo 电话号码字符串
     * @return
     */
    public static boolean isValidTelephoeNo(String telephoeNo) {
        // 1、\\d{3,4} 区号 3位或者4位的匹配
        // 2、\\d{7,8} 号码 7味或者8位的匹配
        // 3、(\\d{3,4})? 分机号3位或者4位的匹配 ？可匹配一次或者两次
        boolean flag = false;
        Pattern p = Pattern.compile("^\\d{3,4}\\d{7,8}(\\d{3,4})?$");
        Matcher match = p.matcher(telephoeNo);
        if (telephoeNo != null) {
            flag = match.matches();
        }
        return flag;
    }

    /***************************************************************************
     * 验证手机号码
     *
     * @param telNo 电话号码字符串 130到139 和 150，152 ，157，158，159 ，186，189，187
     * @return
     */
    public static boolean isValidMobileNo(String mobileNo) {
        // 1、(13[0-9])|(15[02789])|(18[679]) 13段 或者15段 18段的匹配
        // 2、\\d{8} 整数出现8次
        boolean flag = false;
        // Pattern p = Pattern.compile("^(1[358][13567890])(\\d{8})$");
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[0-9])|(17[0678])|(18[0-9]))\\d{8}$");
        Matcher match = p.matcher(mobileNo);
        if (mobileNo != null) {
            flag = match.matches();
        }
        return flag;
    }

    /***************************************************************************
     * 验证是否是正确的邮箱格式
     *
     * @param email
     * @return true表示是正确的邮箱格式, false表示不是正确邮箱格式
     */
    public static boolean isValidEmail(String email) {
        // 1、\\w+表示@之前至少要输入一个匹配字母或数字或下划线 \\w 单词字符：[a-zA-Z_0-9]
        // 2、(\\w+\\.)表示域名. 如新浪邮箱域名是sina.com.cn
        // {1,3}表示可以出现一次或两次或者三次.
        String reg = "\\w+@(\\w+\\.){1,3}\\w+";
        Pattern pattern = Pattern.compile(reg);
        boolean flag = false;
        if (email != null) {
            Matcher matcher = pattern.matcher(email);
            flag = matcher.matches();
        }
        return flag;
    }

    /***************************************************************************
     * 验证整点时间格式是否正确 HH格式 时间范围00时~23时
     *
     * @param hour 时间格式字符串
     * @return true表示是正确的整点时间格式, false表示不是正确整点时间格式
     */
    public static boolean isValidHour(String hour) {
        boolean flag = false;
        String reg = "^[0-2][0-9]$";
        Pattern pattern = Pattern.compile(reg);
        if (hour != null) {
            Matcher matcher = pattern.matcher(hour);
            flag = matcher.matches();
            int firstNum = Integer.parseInt(hour.substring(0, 1));
            if (flag && firstNum == 2) {
                int secondNum = Integer.parseInt(hour.substring(1, 2));
                flag = secondNum < 4; // 时间小于24时
            }
        }
        return flag;
    }

    /***************************************************************************
     * 匹配日期格式 yyMMdd 并验证日期是否合法 此方法忽略了闰年的验证 用于15位身份证出生日期的验证
     *
     * @param dayStr 日期字符串
     * @return true 日期合法 false 日期非法
     */
    public static boolean isValidDay(String dayStr) {
        Pattern p = Pattern.compile("^\\d{2}\\d{2}\\d{2}$");
        Matcher match = p.matcher(dayStr);
        if (dayStr != null) {
            if (match.matches()) // 格式验证通过 yyMMdd
            {
                int month = Integer.parseInt(dayStr.substring(2, 4)); // 月
                int day = Integer.parseInt(dayStr.substring(4, 6)); // 日
                if (!isValidMonth(month)) {
                    return false; // 月份不合法
                }
                if (!(day >= 1 && day <= DAYS_OF_MONTH[month - 1])) {
                    return false; // 日期不合法
                }
                return true;
            }

            return false;
        } else {
            return false;
        }
    }

    /***************************************************************************
     * 匹配日期格式 yyyyMMdd 并验证日期是否合法
     *
     * @param date 日期字符串
     * @return true 日期合法 false 日期非法
     */
    public static boolean isValidDate(String date) {
        // 1、 \\d{4} 年，\\d{2}月，\\d{2}日匹配
        Pattern p = Pattern.compile("^\\d{4}\\d{2}\\d{2}$");
        Matcher match = p.matcher(date);
        if (date != null) {
            if (match.matches()) // 格式验证通过 yyyyMMdd
            {
                int year = Integer.parseInt(date.substring(0, 4)); // 年
                int month = Integer.parseInt(date.substring(4, 6)); // 月
                int day = Integer.parseInt(date.substring(6, 8)); // 日
                if (!isValidYear((year))) {
                    return false; // 年份不在有效年份中
                }
                if (!isValidMonth(month)) {
                    return false; // 月份不合法
                }
                if (!isValidDay(year, month, day)) {
                    return false; // 日期不合法
                }
                return true;
            }

            return false;
        } else {
            return false;
        }
        // return Pattern.matches("", date);
    }

    /**
     * 检查year是否在有效的年份范围内 此处验证大于1900年 小于2101年
     *
     * @param year
     * @return
     */
    public static boolean isValidYear(int year) {
        return year >= START_YEAR && year <= END_YEAR;
    }

    /**
     * 验证月份是否在有效月份内
     *
     * @param month
     * @return
     */
    public static boolean isValidMonth(int month) {
        return month >= 1 && month <= 12;
    }

    /**
     * 检查天数是否在有效的范围内，因为天数会根据年份和月份的不同而不同 所以必须依赖年份和月份进行检查
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static boolean isValidDay(int year, int month, int day) {
        if (month == 2 && isLeapYear(year))// 闰年且是2月份
        {
            return day >= 1 && day <= 29;
        }
        return day >= 1 && day <= DAYS_OF_MONTH[month - 1];// 其他月份
    }

    /**
     * 验证是否是闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    /**
     * 验证用户名注册是否合法-----------由数字、26个英文字母或者下划线组成的字符串
     *
     * @param userName
     * @return
     */
    /**
     * @param userName
     * @return
     */
    /**
     * @param userName
     * @return
     */
    public static boolean isRegUserName(String userName) {
        String str = "^[^_]\\w+$";
        boolean flag = true;
        if (userName != null) {
            Pattern p = Pattern.compile(str);
            Matcher match = p.matcher(userName);
            flag = match.matches();
        }
        return flag;
    }
}
