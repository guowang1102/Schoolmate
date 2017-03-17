package com.weiguowang.schoolmate.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.entity.MyUser;
import com.weiguowang.schoolmate.entity.School;
import com.weiguowang.schoolmate.view.ListPopupWindow;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * function: 修改信息
 * Created by 韦国旺 on 2017/3/9.
 * Copyright (c) 2017 All Rights Reserved.
 */
public class ModifyInfoActivity extends TActivity implements View.OnClickListener {

    private EditText nickNameEt, realNameEt, sexEt, jobEt, mobilePhoneEt, schoolNameeEt, collegeEt, majorEt, sessionEt;
    private MyUser userInfo;
    private ArrayList<String> schoolNameList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_myinfo);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.modify_info));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nickNameEt = (EditText) findViewById(R.id.nick_name);
        realNameEt = (EditText) findViewById(R.id.real_name);
        sexEt = (EditText) findViewById(R.id.sex);
        jobEt = (EditText) findViewById(R.id.job);
        mobilePhoneEt = (EditText) findViewById(R.id.mobile_phone);
        schoolNameeEt = (EditText) findViewById(R.id.school_name);
        collegeEt = (EditText) findViewById(R.id.college);
        majorEt = (EditText) findViewById(R.id.major);
        sessionEt = (EditText) findViewById(R.id.session);
    }

    private void initData() {
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        setUserInfo(userInfo);
    }

    private void initEvent() {

    }

    /**
     * @param myUser
     */
    private void setUserInfo(MyUser myUser) {
        nickNameEt.setText(myUser.getNickName());
        realNameEt.setText(myUser.getRealName());
        sexEt.setText(myUser.getSex() ? getString(R.string.female) : getString(R.string.male));
        jobEt.setText(myUser.getJob());
        mobilePhoneEt.setText(myUser.getMobilePhoneNumber());
        schoolNameeEt.setText(myUser.getSchoolName());
        collegeEt.setText(myUser.getCollege());
        majorEt.setText(myUser.getMajor());
        sessionEt.setText(myUser.getSession());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateUserInfo() {
        MyUser myUser = new MyUser();
        myUser.setMobilePhoneNumber(mobilePhoneEt.getText().toString());
        myUser.setNickName(nickNameEt.getText().toString());
        myUser.setRealName(realNameEt.getText().toString());
        myUser.setSex(false);  //TODO
        myUser.setJob(jobEt.getText().toString());
        myUser.setSchoolName(schoolNameeEt.getText().toString());
        myUser.setCollege(collegeEt.getText().toString());
        myUser.setMajor(majorEt.getText().toString());
        myUser.setSession(sessionEt.getText().toString());
        myUser.update(userInfo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toastyInfo("更新用户信息成功");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    toastyInfo("更新用户信息失败:" + e.getMessage());
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_tv:
                updateUserInfo();
                break;
            case R.id.modify_school_name:
                getSchoolNameList();
                break;
            case R.id.modify_college:
                getCollegeList();
                break;
            case R.id.modify_major:
                getMajorList();
                break;
            case R.id.modify_session:
                getSessionList();
                break;
        }
    }

    public void getSchoolNameList() {
        schoolNameList.clear();
        BmobQuery<School> query = new BmobQuery<>();
        query.order("-createdAt").findObjects(new FindListener<School>() {
            @Override
            public void done(List<School> list, BmobException e) {
                if (list.size() > 0) {
                    for (School school : list) {
                        schoolNameList.add(school.getSchoolName());
                    }
                    chooseList(schoolNameList, new ListPopupWindow.OnSelectListener() {
                        @Override
                        public void getValue(String value) {
                            toastyInfo(value);
                            schoolNameeEt.setText(value);
                        }
                    }, findViewById(R.id.modify_school_name));
                }
            }
        });
    }

    /**
     *
     */
    public void getCollegeList() {
        BmobQuery<School> query = new BmobQuery<>();
        final String schoolName = schoolNameeEt.getText().toString().trim();
        query.addWhereEqualTo("schoolName", schoolName);
        query.setLimit(100);
        query.findObjects(new FindListener<School>() {
            @Override
            public void done(List<School> schoolList, BmobException e) {
                ArrayList<String> collegeList = new ArrayList<>();
                if (schoolList.size() > 0) {
                    for (School school : schoolList) {
                        collegeList.add(school.getCollege());
                    }
                    chooseList(collegeList, new ListPopupWindow.OnSelectListener() {
                        @Override
                        public void getValue(String value) {
                            toastyInfo(value);
                            collegeEt.setText(value);
                        }
                    }, findViewById(R.id.modify_college));
                }

            }
        });
    }

    /**
     *
     */
    private void getMajorList() {
        BmobQuery<School> query = new BmobQuery<>();
        final String schoolName = schoolNameeEt.getText().toString().trim();
        final String college = collegeEt.getText().toString().trim();
        query.addWhereEqualTo("schoolName", schoolName);
        query.addWhereEqualTo("college", college);
        query.setLimit(100);
        query.findObjects(new FindListener<School>() {
            @Override
            public void done(List<School> schoolList, BmobException e) {
                ArrayList<String> majorList = new ArrayList<>();
                if (schoolList.size() > 0) {
                    for (School school : schoolList) {
                        majorList.add(school.getMajor());
                    }
                    chooseList(majorList, new ListPopupWindow.OnSelectListener() {
                        @Override
                        public void getValue(String value) {
                            toastyInfo(value);
                            majorEt.setText(value);
                        }
                    }, findViewById(R.id.modify_major));
                }
            }
        });
    }

    /**
     *
     */
    private void getSessionList() {
        BmobQuery<School> query = new BmobQuery<>();
        final String schoolName = schoolNameeEt.getText().toString().trim();
        final String college = collegeEt.getText().toString().trim();
        final String major = majorEt.getText().toString().trim();
        query.addWhereEqualTo("schoolName", schoolName);
        query.addWhereEqualTo("college", college);
        query.addWhereEqualTo("major", major);
        query.setLimit(100);
        query.findObjects(new FindListener<School>() {
            @Override
            public void done(List<School> schoolList, BmobException e) {
                final ArrayList<String> sessionList = new ArrayList<>();
                if (schoolList.size() > 0) {
                    for (School school : schoolList) {
                        sessionList.add(school.getSession());
                    }
                    chooseList(sessionList, new ListPopupWindow.OnSelectListener() {
                        @Override
                        public void getValue(String value) {
                            toastyInfo(value);
                            sessionEt.setText(value);
                        }
                    }, findViewById(R.id.modify_session));
                }
            }
        });
    }


    private ListPopupWindow popupWindow;

    private void chooseList(ArrayList<String> arrayList, ListPopupWindow.OnSelectListener listener, View AsDropDownView) {
        if (popupWindow == null) {
            popupWindow = new ListPopupWindow(getApplicationContext(), arrayList);
            popupWindow.setSelectListener(listener);
            popupWindow.showAsDropDown(findViewById(R.id.modify_school_name), 5, 5);
        } else if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.showAsDropDown(findViewById(R.id.modify_school_name), 5, 5);
        }

    }

    public interface QueryDoneListener {
        /**
         * @param obj
         * @param e
         */
        void done(Object obj, BmobException e);
    }


}
