package com.cpigeon.app.modular.usercenter.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.usercenter.model.bean.UserInfo;
import com.cpigeon.app.modular.usercenter.presenter.UserInfoPresenter;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IUserInfoView;
import com.cpigeon.app.modular.usercenter.view.fragment.MyDialogFragment;
import com.cpigeon.app.utils.CpigeonConfig;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.PictureCutUtil;
import com.cpigeon.app.utils.customview.SaActionSheetDialog;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/4/10.
 */

public class UserInfoActivity extends BaseActivity implements IUserInfoView {
    private static final int CHANGE_SIGN = 1;
    private static final int TAKE_PHOTO = 4;
    private static final int CHOOSE_PHPOTO = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    private static final int CHANGE_NICKNAME = 5;

    @BindView(R.id.iv_user_head_img)
    CircleImageView ivUserHeadImg;
    @BindView(R.id.ll_user_head_img)
    LinearLayout llUserHeadImg;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.ll_nick_name)
    LinearLayout llNickName;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.ll_user_name)
    LinearLayout llUserName;
    @BindView(R.id.tv_user_brithday)
    TextView tvUserBrithday;
    @BindView(R.id.ll_user_birthday)
    LinearLayout llUserBirthday;
    @BindView(R.id.tv_user_sex)
    TextView tvUserSex;
    @BindView(R.id.ll_user_sex)
    LinearLayout llUserSex;
    @BindView(R.id.tv_user_sign)
    TextView tvUserSign;
    @BindView(R.id.ll_user_sign)
    LinearLayout llUserSign;
    @BindView(R.id.ll_change_pwd)
    LinearLayout llChangePwd;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Uri imageUri;
    private Intent intent = null;
    UserInfoPresenter mPresenter;
    private UserInfo.DataBean userinfo;
    private boolean isChangedUserHeadImage = false;
    private String mUserHeadImageLocalPath;

    private SaActionSheetDialog.OnSheetItemClickListener mOnSheetItemClickListener = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 2:
                    tvUserSex.setText("女");
                    break;
                case 1:
                    tvUserSex.setText("男");
                    break;
                case 3:
                    tvUserSex.setText("保密");
                    break;
            }
        }
    };
    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListener = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 2:
                    choseHeadImageFromCamera();//相机
                    break;
                case 1:
                    choseHeadImageFromGallry();//相册
                    break;
            }
        }
    };

    /**
     * 选择相册
     */
    private void choseHeadImageFromGallry() {
        if (ContextCompat.checkSelfPermission(UserInfoActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserInfoActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHPOTO);
    }

    /**
     * 选择相机的
     */
    private void choseHeadImageFromCamera() {
        if (ContextCompat.checkSelfPermission(UserInfoActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserInfoActivity.this,
                    new String[]{Manifest.permission.CAMERA}, 2);
        } else {
            openCamera();
        }

    }

    private void openCamera() {
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(UserInfoActivity.this,
                    "com.example.andysong.cameraalbumtest.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void initPresenter() {
        mPresenter = new UserInfoPresenter(this);
    }

    @Override
    public void initView() {
        toolbar.setTitle("个人信息");
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (CpigeonData.getInstance().getUserInfo() == null)
            mPresenter.loadUserInfo();
        displayUserInfo(CpigeonData.getInstance().getUserInfo());
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case TAKE_PHOTO:
                crop(imageUri);
                break;
            case CHOOSE_PHPOTO:
                handleImageOnKikat(data);
                break;
            case PHOTO_REQUEST_CUT:
                Bitmap bitmap = data.getParcelableExtra("data");
                String filePath = PictureCutUtil.cutPictureQuality(bitmap, CpigeonConfig.CACHE_FOLDER);
                filePath = CpigeonConfig.CACHE_FOLDER + filePath;
//                Picasso.with(mContext).load(filePath).into(ivUserHeadImg);
                Logger.d(filePath);
                mUserHeadImageLocalPath = filePath;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bm = BitmapFactory.decodeFile(filePath, options);

                ivUserHeadImg.setImageBitmap(bm);
                isChangedUserHeadImage = true;
                break;
            case CHANGE_NICKNAME:
                String nickname = data.getStringExtra(EditActivity.INTENT_KEY_NEW_VALUE);
                tvNickName.setText(nickname);
                break;
            case CHANGE_SIGN:
                String usersign = data.getStringExtra(EditActivity.INTENT_KEY_NEW_VALUE);
                tvUserSign.setText(usersign);
                break;
            default:
                break;
        }
    }

    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "PNG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);//同样的在onActivityResult中处理剪裁好的图片
    }


    private void handleImageOnKikat(Intent data) {
        Uri uri = data.getData();
        crop(uri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "您没有给我权限", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode) {
            case 1:
                openAlbum();
                break;
            case 2:
                openCamera();
                break;
            default:
        }
    }

    @OnClick({R.id.iv_user_head_img, R.id.ll_user_head_img, R.id.ll_nick_name, R.id.ll_user_birthday, R.id.ll_user_sex, R.id.ll_user_sign, R.id.ll_change_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_user_head_img:
                break;
            case R.id.ll_user_head_img:
                new SaActionSheetDialog(mContext)
                        .builder()
                        .addSheetItem("相册选取", OnSheetItemClickListener)
                        .addSheetItem("拍一张", OnSheetItemClickListener)
                        .show();
                break;
            case R.id.ll_nick_name:
                intent = new Intent(UserInfoActivity.this, EditActivity.class);
                intent.putExtra(EditActivity.INTENT_KEY_NEW_TITLE, "更改昵称");
                intent.putExtra(EditActivity.INTENT_KEY_SHOW_TIPS, true);
                intent.putExtra(EditActivity.INTENT_KEY_EDITTEXT_HINT, "昵称");
                intent.putExtra(EditActivity.INTENT_KEY_TIPS_TEXT, "好名字可以让你的朋友更容易记住你");
                intent.putExtra(EditActivity.INTENT_KEY_OLD_VALUE, tvNickName.getText().toString());
                startActivityForResult(intent, CHANGE_NICKNAME);
                break;
            case R.id.ll_user_birthday:
                MyDialogFragment.getInstance(MyDialogFragment.DIALOG_TYPE_DATE).show(getFragmentManager(), "提示");
                break;
            case R.id.ll_user_sex:
                new SaActionSheetDialog(mContext)
                        .builder()
                        .addSheetItem("男", mOnSheetItemClickListener)
                        .addSheetItem("女", mOnSheetItemClickListener)
                        .addSheetItem("保密", SaActionSheetDialog.SheetItemColor.Red, mOnSheetItemClickListener)
                        .show();
                break;
            case R.id.ll_user_sign:
                intent = new Intent(UserInfoActivity.this, EditActivity.class);
                intent.putExtra(EditActivity.INTENT_KEY_NEW_TITLE, "个性签名");
                intent.putExtra(EditActivity.INTENT_KEY_EDITTEXT_HINT, "签名");
                intent.putExtra(EditActivity.INTENT_KEY_OLD_VALUE, tvUserSign.getText().toString());
                startActivityForResult(intent, CHANGE_SIGN);
                break;
            case R.id.ll_change_pwd:
//                Intent intent = new Intent(UserInfoActivity.this, SetUserPwdActivity.class);
//                startActivity(intent);
                break;
        }
    }

    /**
     * 判断当前是否有数据修改
     *
     * @return
     */
    @Override
    public boolean hasChangedUserInfo() {
        if (CpigeonData.getInstance().getUserInfo() == null) return true;
        if (CpigeonData.getInstance().getUserInfo().getNickname() != null && !tvNickName.getText().toString().equals(CpigeonData.getInstance().getUserInfo().getNickname()))
            return true;
        if (CpigeonData.getInstance().getUserInfo().getSex() != null && !tvUserSex.getText().toString().equals(CpigeonData.getInstance().getUserInfo().getSex()))
            return true;
        if (CpigeonData.getInstance().getUserInfo().getBrithday() != null && !tvUserBrithday.getText().toString().equals(CpigeonData.getInstance().getUserInfo().getBrithday()))
            return true;
        if (CpigeonData.getInstance().getUserInfo().getSigns() != null && !tvUserSign.getText().toString().equals(CpigeonData.getInstance().getUserInfo().getSigns()))
            return true;
        return false;
    }

    @Override
    public boolean hasChangedUserHeadImage() {
        return isChangedUserHeadImage;
    }

    @Override
    public String getChangedUserHeadImageLocalPath() {
        return mUserHeadImageLocalPath;
    }

    @Override
    public UserInfo.DataBean getModifiedUserInfo() {
        if (userinfo == null)
            userinfo = new UserInfo.DataBean();
        userinfo.setBrithday(tvUserBrithday.getText().toString());
        userinfo.setNickname(tvNickName.getText().toString());
        userinfo.setSex(tvUserSex.getText().toString());
        userinfo.setSigns(tvUserSign.getText().toString());
        return userinfo;
    }

    @Override
    public void showUserinfo(UserInfo.DataBean userinfo) {
        displayUserInfo(userinfo);
    }

    private void displayUserInfo(UserInfo.DataBean data) {
        if (data != null) {
            if (!TextUtils.isEmpty(data.getHeadimg())) {
                Picasso.with(mContext).load(data.getHeadimg()).into(ivUserHeadImg);
            } else {
                ivUserHeadImg.setImageResource(R.mipmap.head_image_default);
            }
            tvNickName.setText(TextUtils.isEmpty(data.getNickname()) ? "" : data.getNickname());
            tvUserBrithday.setText(TextUtils.isEmpty(data.getBrithday()) ? "" : data.getBrithday());
            tvUserName.setText(TextUtils.isEmpty(data.getUsername()) ? "" : data.getUsername());
            tvUserSex.setText(TextUtils.isEmpty(data.getSex()) ? "" : data.getSex());
            tvUserSign.setText(TextUtils.isEmpty(data.getSigns()) ? "" : data.getSigns());
            userinfo = data;
        }
    }

    @Override
    public void finish() {
        mPresenter.updateUserInfo();
        super.finish();
    }
}
