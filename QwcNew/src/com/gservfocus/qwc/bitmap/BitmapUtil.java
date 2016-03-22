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
	 * ����
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
	 * ����ͼƬѹ�������
	 * @param options BitmapFactory.Options����
	 * @param minSideLength 
	 * @param maxNumOfPixels 
	 * @return λͼ����
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
	 * ����ͼƬѹ�������
	 * @param options BitmapFactory.Options����
	 * @param minSideLength 
	 * @param maxNumOfPixels 
	 * @return λͼ����
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
	 * ��ȡͼƬ���ԣ���ת�ĽǶ�
	 * @param path ͼƬ����·��
	 * @return degree��ת�ĽǶ�
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
	 * ��ͼƬ���ŵ�ָ����w��h
	 * 
	 * @param bgimage
	 *            λͼ����
	 * @param newWidth
	 *            ͼƬ��
	 * @param newHeight
	 *            ͼƬ��
	 * */
	private Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight,
			int sampleSize) {
		// ��ȡ���ͼƬ�Ŀ�͸�
		int width = bgimage.getWidth();
		int height = bgimage.getHeight();
		// ��������ͼƬ�õ�matrix����
		Matrix matrix = new Matrix();
		// ���������ʣ��³ߴ��ԭʼ�ߴ�
		float scaleWidth;
		float scaleHeight;

		scaleWidth = ((float) newWidth) / width;
		scaleHeight = ((float) newHeight) / height;

		// ����ͼƬ����
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,
				matrix, true);

		return bitmap;
	}
	/**
	 * ��ȡѹ�����ͼƬ
	 * @param res resource����
	 * @param drawableId Drawable id
	 * @param w ָ��ͼƬ�Ŀ��
	 * @return λͼ����
	 * */
	public Bitmap getCompressPicture(Resources res, int w,int drawableId) {
		BitmapFactory.Options opts = null;
		Bitmap obmp = null;
		Bitmap nbmp = null;
		int h = w*2/3;		//Ĭ�ϱ���
		opts = new BitmapFactory.Options();
		opts.inSampleSize = 10;
		Bitmap bitmapNormal = BitmapFactory.decodeResource(res, drawableId,
				opts);
		if (bitmapNormal != null)
			h = bitmapNormal.getHeight() * w / bitmapNormal.getWidth();
		// ����ͼƬ���ű���
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
