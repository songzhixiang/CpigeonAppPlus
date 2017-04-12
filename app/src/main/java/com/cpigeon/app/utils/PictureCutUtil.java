package com.cpigeon.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.Display;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * Created by Administrator on 2017/3/8.
 */

public class PictureCutUtil {
    private Context context;

    /**
     * 构造器
     *
     * @param context
     */
    public PictureCutUtil(Context context) {
        this.context = context;
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int cutBySize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 根据路径获得图片并压缩返回bitmap
     *
     * @param filePath
     * @return
     */
    public Bitmap cutPictureSize(String filePath) {

        Display mDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        int width = mDisplay.getWidth();
        int height = mDisplay.getHeight();

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = cutBySize(options, width, height);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 存储图片
     *
     * @param bitmap
     * @param filePath
     * @return 文件名
     */
    public static String cutPictureQuality(Bitmap bitmap, String filePath) {

        String fileName = UUID.randomUUID().toString().replace("-", "") + ".png";

        // 判断文件夹存在
        File file = new File(filePath);
        if (file != null && !file.exists()) {
            file.mkdirs();
        }

        try {

            // 第一次压缩
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            FileOutputStream fos = new FileOutputStream(new File(filePath, fileName));

            int options = 100;
            // 如果大于150kb则再次压缩,最多压缩三次
            while (baos.toByteArray().length / 1024 > 150 && options != 10) {
                // 清空baos
                baos.reset();
                // 这里压缩options%，把压缩后的数据存放到baos中
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 30;
            }
            fos.write(baos.toByteArray());
            fos.close();
            baos.close();

        } catch (Exception e) {
        }
        return fileName;
    }
}
