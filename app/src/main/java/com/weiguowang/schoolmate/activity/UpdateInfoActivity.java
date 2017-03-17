package com.weiguowang.schoolmate.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.entity.MyUser;
import com.weiguowang.schoolmate.entity.School;
import com.weiguowang.schoolmate.view.ListPopupWindow;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
public class UpdateInfoActivity extends TActivity implements View.OnClickListener {

    private EditText nickNameEt, realNameEt, sexEt, jobEt, mobilePhoneEt, schoolNameeEt, collegeEt, majorEt, sessionEt;
    private MyUser userInfo;
    private ArrayList<String> schoolNameList = new ArrayList<>();

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

    private void updateHeadImg() {
        final Set<String> menuSet = new LinkedHashSet<>();
        menuSet.add("拍照");
        menuSet.add("本地相册");
        menuSet.add("取消");
        chooseList(menuSet, new ListPopupWindow.OnSelectListener() {
            @Override
            public void getValue(String value, int position) {
                switch (position) {
                    case 0:
//                        dispatchTakePictureIntent();
                        takePhone(null);
                        break;
                    case 1:
//                        openLocalAlbum();
                        choosePhone(null);
                        break;
                    case 2:
                        break;
                }
            }
        }, findViewById(R.id.head_img));
    }


    private static final int REQUEST_TAKE_PHOTO = 11111;
    private static final int START_ALBUM_CODE = 11112;

    private File output;
    private Uri imageUri;
    private static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE = 3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;


    public void takePhone(View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE2);

        } else {
            takePhoto();
        }

    }

    public void choosePhone(View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE2);

        } else {
            choosePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                // Permission Denied
//                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                toastyInfo("Permission Denied");
            }
        }


        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto();
            } else {
                // Permission Denied

                toastyInfo("Permission Denied");
//                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 拍照
     */
    void takePhoto() {
        /**
         * 最后一个参数是文件夹的名称，可以随便起
         */
        File file = new File(Environment.getExternalStorageDirectory(), "拍照");
        if (!file.exists()) {
            file.mkdir();
        }
        /**
         * 这里将时间作为不同照片的名称
         */
        output = new File(file, System.currentTimeMillis() + ".jpg");

        /**
         * 如果该文件夹已经存在，则删除它，否则创建一个
         */
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 隐式打开拍照的Activity，并且传入CROP_PHOTO常量作为拍照结束后回调的标志
         */
        imageUri = Uri.fromFile(output);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CROP_PHOTO);

    }

    /**
     * 从相册选取图片
     */
    void choosePhoto() {
        /**
         * 打开选择图片的界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

    }


    /**
     * 启动相机拍照
     */
    private void dispatchTakePictureIntent() {
        Context context = UpdateInfoActivity.this;
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            toastyInfo("This device does not have a camera.");
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        TActivity activity = (TActivity) UpdateInfoActivity.this;
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                toastyInfo("There was a problem saving the photo...");
            }
            if (photoFile != null) {
                Uri fileUri = Uri.fromFile(photoFile);
                activity.setCapturedImageURI(fileUri);
                activity.setCurrentPhotoPath(fileUri.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        activity.getCapturedImageURI());
                activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * Creates the image file to which the image must be saved.
     *
     * @return
     * @throws IOException
     */
    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        TActivity activity = (TActivity) UpdateInfoActivity.this;
        activity.setCurrentPhotoPath("file:" + image.getAbsolutePath());
        return image;
    }

    /**
     * Add the picture to the photo gallery.
     * Must be called on all camera images or they will
     * disappear once taken.
     */
    protected void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        TActivity activity = (TActivity) UpdateInfoActivity.this;
        File f = new File(activity.getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    /**
     * 打开系统相册
     */
    private void openLocalAlbum() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        //根据版本号不同使用不同的Action
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        startActivityForResult(intent, START_ALBUM_CODE);
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

    public interface QueryListener {
        /**
         * @param obj
         * @param e
         */
        void done(Object obj, BmobException e);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case REQUEST_TAKE_PHOTO:
//                    addPhotoToGallery();
//                    TActivity activity = (TActivity) UpdateInfoActivity.this;
//                    String imgPath = activity.getCurrentPhotoPath(); //保存图片的路径
//                    Log.v("info", " REQUEST_TAKE_PHOTO imagepath is  " + imgPath);
//                    setFullImageFromFilePath(imgPath, (ImageView) findViewById(R.id.head_img));
//                    break;
//                case START_ALBUM_CODE:  //通用取相片能正常返回
//                    Log.v("info", "uri is:" + data.getData().getPath());
//                    Log.v("info", "real path is:" + getRealFilePath(UpdateInfoActivity.this, data.getData()));
//                    setFullImageFromFilePath(getRealFilePath(UpdateInfoActivity.this, data.getData()), (ImageView) findViewById(R.id.head_img));
//                    break;
//                default:
//                    break;
//            }
//        } else {
//            toastyInfo("Image Capture Failed");
//        }

        switch (requestCode) {
            /**
             * 拍照的请求标志
             */
            case CROP_PHOTO:
                if (resultCode==RESULT_OK) {
                    try {
                        /**
                         * 该uri就是照片文件夹对应的uri
                         */
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                        imageView.setImageBitmap(bit);
                        ((ImageView)findViewById(R.id.head_img)).setImageBitmap(bit);
                    } catch (Exception e) {
//
//                        Toast.makeText(this,"程序崩溃",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Log.i("tag", "失败");
                }

                break;
            /**
             * 从相册中选取图片的请求标志
             */

            case REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        /**
                         * 该uri是上一个Activity返回的
                         */
                        Uri uri = data.getData();
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//                        imageView.setImageBitmap(bit);
                        ((ImageView)findViewById(R.id.head_img)).setImageBitmap(bit);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("tag",e.getMessage());
//                        Toast.makeText(this,"程序崩溃",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Log.i("liang", "失败");
                }

                break;

            default:
                break;
        }
    }

    public void setFullImageFromFilePath(String imagePath, ImageView imageView) {
        int targetW = imageView.getWidth();  // Get the dimensions of the View
        int targetH = imageView.getHeight();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();  // Get the dimensions of the bitmap
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        int scaleFactor = 2;
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }


}
