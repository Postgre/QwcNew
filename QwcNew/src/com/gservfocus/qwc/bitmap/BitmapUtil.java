package com.gservfocus.qwc.bitmap;

import java.io.IOException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class BitmapUtil {
	private static BitmapUtil instance = null;
	/**
	 * 单例
	 * */
	private BitmapUtil(){}
	/**
	 * get BitmapUtil object
	 * */
	public static BitmapUtil getInstance(){
		if(instance == null)
			return new BitmapUtil();
		else
			return instance;
	}
	/**
	 * 计算图片压缩后比例
	 * @param options BitmapFactory.Options对象
	 * @param minSideLength 
	 * @param maxNumOfPixels 
	 * @return 位图对象
	 * */
	private int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 计算图片压缩后比例
	 * @param options BitmapFactory.Options对象
	 * @param minSideLength 
	 * @param maxNumOfPixels 
	 * @return 位图对象
	 * */
	public int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}
	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
    public  int readPictureDegree(String path) {
        int degree  = 0;
        try {
                ExifInterface exifInterface = new ExifInterface(path);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return degree;
    }
   
    /**
	 * 将图片缩放到指定的w和h
	 * 
	 * @param bgimage
	 *            位图对象
	 * @param newWidth
	 *            图片宽
	 * @param newHeight
	 *            图片高
	 * */
	private Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight,
			int sampleSize) {
		// 获取这个图片的宽和高
		int width = bgimage.getWidth();
		int height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth;
		float scaleHeight;

		scaleWidth = ((float) newWidth) / width;
		scaleHeight = ((float) newHeight) / height;

		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,
				matrix, true);

		return bitmap;
	}
	/**
	 * 获取压缩后的图片
	 * @param res resource对象
	 * @param drawableId Drawable id
	 * @param w 指定图片的宽度
	 * @return 位图对象
	 * */
	public Bitmap getCompressPicture(Resources res, int w,int drawableId) {
		BitmapFactory.Options opts = null;
		Bitmap obmp = null;
		Bitmap nbmp = null;
		int h = w*2/3;		//默认比率
		opts = new BitmapFactory.Options();
		opts.inSampleSize = 10;
		Bitmap bitmapNormal = BitmapFactory.decodeResource(res, drawableId,
				opts);
		if (bitmapNormal != null)
			h = bitmapNormal.getHeight() * w / bitmapNormal.getWidth();
		// 计算图片缩放比例
		final int minSideLength = Math.min(w, h);
		opts.inSampleSize = computeSampleSize(opts, minSideLength, w * h);
		opts.inJustDecodeBounds = false;
		opts.inInputShareable = true;
		opts.inPurgeable = true;
		try {
			obmp = BitmapFactory.decodeResource(res, drawableId, opts);
			if (obmp != null) {
				nbmp = zoomImage(obmp, w, h, opts.inSampleSize);
				System.gc();
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return nbmp;
	}
}
