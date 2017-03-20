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

import com.weiguowang.schoolmate.MessageEvent;
import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.adapter.SelectCallback;
import com.weiguowang.schoolmate.entity.MyUser;
import com.weiguowang.schoolmate.entity.School;
import com.weiguowang.schoolmate.utils.ImageUtils;
import com.weiguowang.schoolmate.utils.SystemUtils;
import com.weiguowang.schoolmate.view.CircleImageView;
import com.weiguowang.schoolmate.view.ListPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
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

        headImageView = (CircleImageView) findViewById(R.id.head_img);

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
        setUserInfo(userInfo);
        checkSDPermission();
    }

    private static final String Camera_permission = Manifest.permission.CAMERA;
    private static final String SD_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int request_sd = 99;
    public static final int request_camera2 = 101;
    private File mFile;
    public static final int INTENT_CODE_IMAGE_CAPTURE2 = 11;

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
//        if(data==null){
//            return;
//        }
        switch (requestCode) {
            case INTENT_CODE_IMAGE_CAPTURE2:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(mFile.getAbsolutePath(), mWidth, mHeight);
                    headImageView.setImageBitmap(bitmap);
                    final BmobFile bmobFile = new BmobFile(mFile);
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                toastyInfo("上传文件成功:" + bmobFile.getFileUrl());
                                userInfo.setHeadUrl(bmobFile.getFileUrl());
                                Log.d(TAG, "done: userInfo url"+userInfo.getHeadUrl());

                            }else{
                                toastyInfo("上传文件失败：" + e.getMessage());
                            }
                        }
                    });
                }
                break;
            case INTENT_CODE_IMAGE_GALLERY1:
                if (SystemUtils.isMIUI()) {
                    Log.d("info", "isminui");
                    setPhotoForMiuiSystem(data);
                } else {
                    setPhotoForNormalSystem(data);
                    Log.d("info", "no miui");
                }
                break;
        }
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
        if (!TextUtils.isEmpty(myUser.getHeadUrl())) {
            toastyInfo("userInfo is not null");
            BmobFile bmobfile =new BmobFile("abc.png","",userInfo.getHeadUrl());
            final File saveFile = new File(Environment.getExternalStorageDirectory(), bmobfile.getFilename());
            bmobfile.download(saveFile, new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if(e==null){
                        Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(saveFile.getAbsolutePath(), mWidth, mHeight);
                        headImageView.setImageBitmap(bitmap);
//                        toast("下载成功,保存路径:"+savePath);
                    }else{
//                        toast("下载失败："+e.getErrorCode()+","+e.getMessage());
                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        }else {
            toastyInfo("userInfo is null");
        }
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
        myUser.setHeadUrl(userInfo.getHeadUrl());
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
            case R.id.head_img:
                updateHeadImg();
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

    private final String IMAGE_TYPE = "image/*";
    public static final int INTENT_CODE_IMAGE_GALLERY1 = 10;

    private void updateHeadImg() {

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
//                        Intent i = new Intent(Intent.ACTION_GET_CONTENT, null);
//                        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE);
//                        startActivityForResult(i, INTENT_CODE_IMAGE_GALLERY1);
                        openLocalAlbum();
                        break;
                    case 2:
                        break;
                }
            }
        });


//        final Set<String> menuSet = new LinkedHashSet<>();
//        menuSet.add("拍照");
//        menuSet.add("本地相册");
//        menuSet.add("取消");
//        chooseList(menuSet, new ListPopupWindow.OnSelectListener() {
//            @Override
//            public void getValue(String value, int position) {
//                switch (position) {
//                    case 0:
//                        getHighPictureFromCamera(Camera_permission);
//                        break;
//                    case 1:
//                        Intent i = new Intent(Intent.ACTION_GET_CONTENT, null);
//                        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                        startActivityForResult(i, INTENT_CODE_IMAGE_GALLERY1);
//                        break;
//                    case 2:
//                        break;
//                }
//            }
//        }, findViewById(R.id.head_img));
    }

    /**
     * 打开系统相册
     */
    private void openLocalAlbum() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(IMAGE_TYPE);
        //根据版本号不同使用不同的Action
        if (Build.VERSION.SDK_INT <19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        startActivityForResult(intent, INTENT_CODE_IMAGE_GALLERY1);
    }


    /**
     * 向onActivityResult发出请求，的到拍摄生成的图片
     */
    private void getHighPictureFromCamera(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {//还没有授予权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                toastyInfo("您已禁止该权限，需要重新开启。");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, request_camera2);
            }
        } else {// 已经授予权限
            startCameraWithHighBitmap();
        }
    }


    private void startCameraWithHighBitmap() {
        //确定存储拍照得到的图片文件路径
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mFile = new File(Environment.getExternalStorageDirectory(),
                    getName());
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
        //加载Uri型的文件路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
        //向onActivityResult发送intent，requestCode为INTENT_CODE_IMAGE_CAPTURE2
        startActivityForResult(intent, INTENT_CODE_IMAGE_CAPTURE2);
    }

    /**
     * 解析Intent.getdata()得到的uri为String型的filePath
     *
     * @param contentUri
     * @return
     */
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
        } else if ("file".equals(scheme)) {//小米4选择云相册中的图片是根据此方法获得路径
            imagePath = localUri.getPath();
        }
        Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(imagePath, mWidth, mHeight);
        headImageView.setImageBitmap(bitmap);

        final BmobFile bmobFile = new BmobFile(new File(imagePath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    toastyInfo("上传文件成功:" + bmobFile.getFileUrl());
                    userInfo.setHeadUrl(bmobFile.getFileUrl());
                    Log.d(TAG, "done: userInfo url"+userInfo.getHeadUrl());
                }else{
                    toastyInfo("上传文件失败：" + e.getMessage());
                }
            }
        });
    }

    private void setPhotoForNormalSystem(Intent data) {
        Log.d(TAG, "setPhotoForNormalSystem: data uri is " + data.getData());
//        String filePath = getRealPathFromURI(data.getData());
//        Log.d("info", "setPhotoForNormalSystem:filePath is "+filePath);
        String filePath = ImageUtils.getPhotoPathFromContentUri(this, data.getData());
        Log.d("info", "setPhotoForNormalSystem:filePath is " + filePath);
        Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(filePath, mWidth, mHeight);
        Log.d(TAG, "bitmap width and height is"+bitmap.getWidth()+"|"+bitmap.getHeight());


        final BmobFile bmobFile = new BmobFile(new File(filePath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    toastyInfo("上传文件成功:" + bmobFile.getFileUrl());
                    userInfo.setHeadUrl(bmobFile.getFileUrl());

                    EventBus.getDefault().post(new MessageEvent("Hello everyone!"));
                    Log.d(TAG, "done: userInfo url"+userInfo.getHeadUrl());
                }else{
                    toastyInfo("上传文件失败：" + e.getMessage());
                }
            }
        });
        headImageView.setImageBitmap(bitmap);
    }

    private static final String TAG = "info";

    @NonNull
    private String getName() {
        return System.currentTimeMillis() + ".jpg";
    }

    public void getSchoolNameList() {
        BmobQuery<School> query = new BmobQuery<>();
        query.order("-createdAt").findObjects(new FindListener<School>() {
            @Override
            public void done(List<School> list, BmobException e) {
                final Set<String> schoolNameSet = new LinkedHashSet<>();

                if (list.size() > 0) {
                    for (School school : list) {
                        schoolNameSet.add(school.getSchoolName());
                    }
                    chooseList(schoolNameSet, new ListPopupWindow.OnSelectListener() {
                        @Override
                        public void getValue(String value, int position) {
                            toastyInfo(value);
                            schoolNameeEt.setText(value);
                        }
                    }, findViewById(R.id.modify_school_name));
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
                Set<String> collegeSet = new LinkedHashSet<>();
                if (schoolList.size() > 0) {
                    for (School school : schoolList) {
                        collegeSet.add(school.getCollege());
                    }
                    chooseList(collegeSet, new ListPopupWindow.OnSelectListener() {
                        @Override
                        public void getValue(String value, int position) {
                            toastyInfo(value);
                            collegeEt.setText(value);
                        }
                    }, findViewById(R.id.modify_college));
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
                Set<String> majorSet = new LinkedHashSet<>();
                if (schoolList.size() > 0) {
                    for (School school : schoolList) {
                        majorSet.add(school.getMajor());
                    }
                    chooseList(majorSet, new ListPopupWindow.OnSelectListener() {
                        @Override
                        public void getValue(String value, int position) {
                            toastyInfo(value);
                            majorEt.setText(value);
                        }
                    }, findViewById(R.id.modify_major));
                }
            }
        });
    }

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
                final Set<String> sessionSet = new LinkedHashSet<>();
                if (schoolList.size() > 0) {
                    for (School school : schoolList) {
                        sessionSet.add(school.getSession());
                    }
                    chooseList(sessionSet, new ListPopupWindow.OnSelectListener() {
                        @Override
                        public void getValue(String value, int position) {
                            toastyInfo(value);
                            sessionEt.setText(value);
                        }
                    }, findViewById(R.id.modify_session));
                }
            }
        });
    }


    private ListPopupWindow popupWindow;

    private void chooseList(Set<String> arrayList, ListPopupWindow.OnSelectListener listener, View AsDropDownView) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
        Log.d("info", "list data is" + arrayList.toString());
        popupWindow = new ListPopupWindow(getApplicationContext(), new ArrayList<>(arrayList));
        popupWindow.setSelectListener(listener);
        popupWindow.showAsDropDown(AsDropDownView, 5, 5);

    }


}
