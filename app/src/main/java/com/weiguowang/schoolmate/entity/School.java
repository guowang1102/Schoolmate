package com.weiguowang.schoolmate.entity;

import cn.bmob.v3.BmobObject;

/**
 * function:
 * Created by 韦国旺 on 2017/3/9 0009.
 * Copyright (c) 2017  All Rights Reserved.
 */
public class School extends BmobObject {

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 院/系
     */
    private String college;

    /**
     * 专业
     */
    private String major;

    /**
     * 届 (班级)
     */
    private String session;


    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "School{" +
                "schoolName='" + schoolName + '\'' +
                ", college='" + college + '\'' +
                ", major='" + major + '\'' +
                ", session='" + session + '\'' +
                '}';
    }

    public void clear(){
        schoolName = "";
        college = "";
        major = "";
        session = "";
    }
}
