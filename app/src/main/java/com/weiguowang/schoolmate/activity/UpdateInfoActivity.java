package com.weiguowang.schoolmate.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.weiguowang.schoolmate.config.AppConfig;
import com.weiguowang.schoolmate.event.NoticeEvent;
import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.adapter.SelectCallback;
import com.weiguowang.schoolmate.entity.MyUser;
import com.weiguowang.schoolmate.entity.School;
import com.weiguowang.schoolmate.utils.ImageUtils;
import com.weiguowang.schoolmate.utils.SystemUtils;
import com.weiguowang.schoolmate.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * function: 修改信息
 * Created by 韦国旺 on 2017/3/9.
 * Copyright (c) 2017 All Rights Reserved.
 */
public class UpdateInfoActivity extends TActivity implements View.OnClickListener {

    private EditText nickNameEt, realNameEt, sexEt, jobEt, mobilePhoneEt, schoolNameeEt, collegeEt, majorEt, sessionEt;
    private MyUser userInfo;
    private CircleImageView headImageView;
    private int mHeight;
    private int mWidth;
    private static final String Camera_permission = Manifest.permission.CAMERA;
    private static final String SD_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int request_sd = 99;
    public static final int request_camera2 = 101;
    private File mFile;
    public static final int INTENT_CODE_IMAGE_CAPTURE2 = 11;
    private final String IMAGE_TYPE = "image/*";
    public static final int INTENT_CODE_IMAGE_GALLERY1 = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        Toolbar toolbar = findView(R.id.toolbar);
        toolbar.setTitle(getString(R.string.modify_info));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nickNameEt = findView(R.id.nick_name);
        realNameEt = findView(R.id.real_name);
        sexEt = findView(R.id.sex);
        jobEt = findView(R.id.job);
        mobilePhoneEt = findView(R.id.mobile_phone);
        schoolNameeEt = findView(R.id.school_name);
        collegeEt = findView(R.id.college);
        majorEt = findView(R.id.major);
        sessionEt = findView(R.id.session);
        headImageView = findView(R.id.head_img);

        headImageView.post(new Runnable() {
            @Override
            public void run() {
                mWidth = headImageView.getWidth();
                mHeight = headImageView.getHeight();
            }
        });
    }

    private void initData() {
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        mFile = new File(AppConfig.HEAD_IMG_LOCAL_PATH);
        setUserInfo(userInfo);
        checkSDPermission();
        initHeadImg(headImageView, mWidth, mHeight);

    }

    private void initEvent() {

    }

    private void checkSDPermission() {
        if (ContextCompat.checkSelfPermission(this, SD_permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, SD_permission)) {
                toastyInfo("您已禁止该权限，需要重新开启。");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{SD_permission}, request_sd);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case request_sd:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    toastyInfo("Permission Denied");
                }
                break;
            case request_camera2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCameraWithHighBitmap();
                } else {
                    toastyInfo("Permission Denied");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_CODE_IMAGE_CAPTURE2:
                if (resultCode == RESULT_OK) {
                    uploadHeadImg(mFile);
                }
                break;
            case INTENT_CODE_IMAGE_GALLERY1:
                if (SystemUtils.isMIUI()) {
                    Log.d("info", "is minui");
                    setPhotoForMiuiSystem(data);
                } else {
                    setPhotoForNormalSystem(data);
                    Log.d("info", "no miui");
                }
                break;
        }
    }

    /**
     * 更新头像图标并上传到服务器
     *
     * @param mFile
     */
    private void uploadHeadImg(File mFile) {
        Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(mFile.getAbsolutePath(), mWidth, mHeight);
        headImageView.setImageBitmap(bitmap);
        ImageUtils.saveBitmap(bitmap,mFile.getAbsolutePath());
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
        final MyUser myUser = new MyUser();
        myUser.setMobilePhoneNumber(mobilePhoneEt.getText().toString());
        myUser.setNickName(nickNameEt.getText().toString());
        myUser.setRealName(realNameEt.getText().toString());
        myUser.setSex(false);  //TODO
        myUser.setJob(jobEt.getText().toString());
        myUser.setSchoolName(schoolNameeEt.getText().toString());
        myUser.setCollege(collegeEt.getText().toString());
        myUser.setMajor(majorEt.getText().toString());
        myUser.setSession(sessionEt.getText().toString());


        final BmobFile bmobFile = new BmobFile(mFile);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toastyInfo("上传文件成功:" + bmobFile.getFileUrl());
                    userInfo.setHeadUrl(bmobFile.getFileUrl());
                    myUser.setHeadUrl(userInfo.getHeadUrl());
                    myUser.update(userInfo.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                toastyInfo("更新用户信息成功");
                                EventBus.getDefault().post(new NoticeEvent(NoticeEvent.WHAT_UPDATE_HEAD));
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                toastyInfo("更新用户信息失败:" + e.getMessage());
                            }
                        }
                    });
                } else {
                    toastyInfo("上传文件失败：" + e.getMessage());
                }
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_tv:  //保存
                updateUserInfo();
                break;
            case R.id.head_img: //更改头像
                clickHeadImg();
                break;
            case R.id.modify_school_name: //更改学校
                getSchoolNameList();
                break;
            case R.id.modify_college: //更改院系
                getCollegeList();
                break;
            case R.id.modify_major: //更改专业
                getMajorList();
                break;
            case R.id.modify_session://更改班级
                getSessionList();
                break;
        }
    }

    /**
     * 更新用户头像
     */
    private void clickHeadImg() {
        final List<String> menuList = new ArrayList<>();
        menuList.add("拍照");
        menuList.add("本地相册");
        menuList.add("取消");
        showSelectDialogFragment("please choose", menuList, "", new SelectCallback() {
            @Override
            public void getValue(String value, int position) {
                switch (position) {
                    case 0:
                        getHighPictureFromCamera(Camera_permission);
                        break;
                    case 1:
                        openLocalAlbum();
                        break;
                    case 2:
                        break;
                }
            }
        });
    }

    /**
     * 打开系统相册
     */
    private void openLocalAlbum() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(IMAGE_TYPE);
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        startActivityForResult(intent, INTENT_CODE_IMAGE_GALLERY1);
    }

    /**
     * 获取原图
     *
     * @param permission
     */
    private void getHighPictureFromCamera(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {//还没有授予权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                toastyInfo("您已禁止该权限，需要重新开启。");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, request_camera2);
            }
        } else { // 已经授予权限
            startCameraWithHighBitmap();
        }
    }


    /**
     * 启动相机
     */
    private void startCameraWithHighBitmap() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mFile = new File(AppConfig.HEAD_IMG_LOCAL_PATH);
        } else {
            toastyInfo("请插入sd卡");
            return;
        }
        try {
            mFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
        startActivityForResult(intent, INTENT_CODE_IMAGE_CAPTURE2);
    }

    /**
     * MIUI系统的相册选择
     *
     * @param data
     */
    private void setPhotoForMiuiSystem(Intent data) {
        Uri localUri = data.getData();
        String scheme = localUri.getScheme();
        String imagePath = "";
        if ("content".equals(scheme)) {
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(localUri, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);
            c.close();
        } else if ("file".equals(scheme)) {  //小米4选择云相册中的图片是根据此方法获得路径
            imagePath = localUri.getPath();
        }
        uploadHeadImg(new File(imagePath));
    }

    /**
     * 普通相册返回
     *
     * @param data
     */
    private void setPhotoForNormalSystem(Intent data) {
        String filePath = ImageUtils.getPhotoPathFromContentUri(this, data.getData());
        uploadHeadImg(new File(filePath));
    }

    @NonNull
    private String getName() {
        return System.currentTimeMillis() + ".jpg";
    }

    public void getSchoolNameList() {
        BmobQuery<School> query = new BmobQuery<>();
        query.order("-createdAt").findObjects(new FindListener<School>() {
            @Override
            public void done(List<School> list, BmobException e) {
                final List<String> schoolNameList = new ArrayList<>(list.size());
                if (list.size() > 0) {
                    for (School school : list) {
                        schoolNameList.add(school.getSchoolName());
                    }
                    showSelectDialogFragment("请选择学校", schoolNameList, "", new SelectCallback() {
                        @Override
                        public void getValue(String value, int position) {
                            schoolNameeEt.setText(value);
                        }
                    });
                }
            }
        });
    }

    public void getCollegeList() {
        BmobQuery<School> query = new BmobQuery<>();
        final String schoolName = schoolNameeEt.getText().toString().trim();
        query.addWhereEqualTo("schoolName", schoolName);
        query.setLimit(100);
        query.findObjects(new FindListener<School>() {
            @Override
            public void done(List<School> schoolList, BmobException e) {
                List<String> collegeList = new ArrayList<>(schoolList.size());
                if (schoolList.size() > 0) {
                    for (School school : schoolList) {
                        collegeList.add(school.getCollege());
                    }
                    showSelectDialogFragment("请选择院系", collegeList, "", new SelectCallback() {
                        @Override
                        public void getValue(String value, int position) {
                            collegeEt.setText(value);
                        }
                    });
                }

            }
        });
    }

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
                List<String> majorList = new ArrayList<>();
                if (schoolList.size() > 0) {
                    for (School school : schoolList) {
                        majorList.add(school.getMajor());
                    }
                    showSelectDialogFragment("请选择专业", majorList, "", new SelectCallback() {
                        @Override
                        public void getValue(String value, int position) {
                            majorEt.setText(value);
                        }
                    });
                }
            }
        });
    }

    /**
     * 获取班级列表
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
                final List<String> sessionList = new ArrayList<>();
                if (schoolList.size() > 0) {
                    for (School school : schoolList) {
                        sessionList.add(school.getSession());
                    }
                    showSelectDialogFragment("请选择班级", sessionList, "", new SelectCallback() {
                        @Override
                        public void getValue(String value, int position) {
                            sessionEt.setText(value);
                        }
                    });
                }
            }
        });
    }
}
