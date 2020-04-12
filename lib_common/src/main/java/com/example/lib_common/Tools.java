package com.example.lib_common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class Tools {

	/**
	 * 获取指定格式的时间(注意ms数不能溢出)
	 * 
	 * @param formatStr
	 *            形如"yyyy年MM月dd日HH:mm:ss"
	 * @param overTime
	 *            0为当前时间,其他根据值加减时间ms
	 * @return 时间
	 */
	public static String getAssignTime(String formatStr, long overTime) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		Date curDate = new Date(System.currentTimeMillis() + overTime);
		String str = formatter.format(curDate);
		return str;
	}

	// 获取view的measure高度
	public static int getMeasureHeight(View view) {
		if (view == null)
			return 0;
		view = setViewMeasure(view);
		return view.getMeasuredHeight();
	}

	// 获取view的onMeasure方法
	private static View setViewMeasure(View view) {
		int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		int height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		view.measure(width, height);
		return view;
	}

	/**
	 * @param context
	 * @param id
	 *            dimens文件中的id(仅适用于dp)
	 * @return dimen 对应分辨率的dp或者sp值
	 */
	public static int getDimen(Context context, int id) {
		float dimen = 0;
		String string = context.getResources().getString(id).replace("dip", "");
		dimen = Float.parseFloat(string);
		return dp2px(context, dimen);
	}

	/**
	 * @param context
	 * @param id
	 *            dimens文件中的id(仅适用于dp)
	 * @return dimen sp值
	 */
	public static Float getDimenSp(Context context, int id) {
		float dimen = 0;
		String string = context.getResources().getString(id).replace("sp", "");
		dimen = Float.parseFloat(string);
		return dimen;
	}

	private static Toast mToast;

	/**
	 * @param context
	 *            上下文
	 * @param returnObj
	 *            需要弹出的内容,如果是空的内容,就不弹.
	 */
	public static void showToast(Context mContext, String msg) {
		if (null != mContext && msg != "" && msg != null && !"[]".equals(msg)) {
			Tools.cancelToast();
			if (null == mToast) {
				mToast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
			}
			mToast.show();
		}

	}

	/**
	 * 取消toast
	 */
	public static void cancelToast() {
		if (null != mToast) {
			mToast.cancel();
			mToast = null;
		}
	}

	public static String timeFormat(long timeMillis, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
		return format.format(new Date(timeMillis));
	}

	public static String formatPhotoDate(long time) {
		return timeFormat(time, "yyyy-MM-dd");
	}

	public static String getTimeShowStringTwo(int Seconds) {

		String strtime = new String();
		int hour = Seconds / (60 * 60);
		int min = (Seconds / 60) % 60;
		int s = Seconds % 60;
		String hourStr = (hour >= 10) ? "" + hour : "0" + hour;
		String minStr = (min >= 10) ? "" + min : "0" + min;
		String seondStr = (s >= 10) ? "" + s : "0" + s;

		strtime = minStr + "：" + seondStr;
		return strtime;
	}
	
	 /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        int res = 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        if (dpValue < 0)
            res = -(int) (-dpValue * scale + 0.5f);
        else
            res = (int) (dpValue * scale + 0.5f);
        return res;
    }

    /**
     * 获取手机像素高宽
     */
    public static DisplayMetrics getWindowPx(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        if(manager!=null) manager.getDefaultDisplay().getMetrics(metric);
        return metric;
    }

}
