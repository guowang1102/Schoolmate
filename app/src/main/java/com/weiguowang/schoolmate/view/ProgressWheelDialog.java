package com.weiguowang.schoolmate.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.weiguowang.schoolmate.R;

/**
 * Function:加载等待框<br/>
 * date: 2015年11月28日 下午 18:35:00<br/>
 * Copyright (c) 2015 北京联龙博通 All Rights Reserved.
 * 
 * @author wells
 */

public class ProgressWheelDialog extends ProgressDialog {
	private Context mContext;
	private static ProgressWheelDialog instance;
	private ProgressWheel progressWheel;

	private ProgressWheelDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public static ProgressWheelDialog getInstance(Context context) {
		if (instance == null) {
			instance = new ProgressWheelDialog(context);
		}
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_progress);
		progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
		final int defaultBarColor = progressWheel.getBarColor();
		progressWheel.setBarColor(defaultBarColor);
		setGravity();

	}

	private void setGravity() {
		if (null != instance) {
			Window dialogWindow = instance.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			lp.x = 0;
			lp.y = 0;
			lp.width = mContext.getResources().getDimensionPixelSize(R.dimen.loading_dialog_width);
			lp.height = mContext.getResources().getDimensionPixelSize(R.dimen.loading_dialog_height);
			dialogWindow.setAttributes(lp);
			instance.setCanceledOnTouchOutside(false); // 设置外部不可点击
		}
	}

	@Override
	public void dismiss() {
		if (instance != null) {
			try {
				super.dismiss();
				instance = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void show() {
		try {
			if (!instance.isShowing() && null != instance) {
				super.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
