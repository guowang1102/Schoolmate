package com.weiguowang.schoolmate.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * function :
 * Created by 韦国旺 on 2017/3/25.
 * Copyright (c) 2017 All Rights Reserved.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class QQResult {
    public int ret;
    public String msg;
    public int is_lost;
    public String nickname;
    public String gender;
    public String province;
    public String city;
    public String figureurl;
    public String figureurl_1;
    public String figureurl_2;
    public String figureurl_qq_1;
    public String figureurl_qq_2;
    public String is_yellow_vip;
    public String vip;
    public String yellow_vip_level;
    public String level;
    public String is_yellow_year_vip;
}
