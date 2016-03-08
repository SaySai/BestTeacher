package com.shanghai.haojiajiao.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.Toast;

import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.util.HttpUtil.RequestHandler;
import com.shanghai.haojiajiao.util.HttpUtil.RequestListener;
import com.shanghai.haojiajiao.util.HttpUtil.ResponseOwn;
import com.shanghai.haojiajiao.weight.EmptyLayout;


public class BaseFragment extends Fragment implements RequestListener, View.OnClickListener {
	protected RequestHandler requestHandler;
	protected Context context;
	private EmptyLayout mEmptyLayout; // this is required to show different layouts (loading or empty or error)
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		 listener = (FragmentListener)activity;
	}

	public BaseFragment() {
		super();
		requestHandler = new RequestHandler();
		requestHandler.listener = this;
		context = HaojiajiaoApplication.context;
	}

	@Override
	public void onRequestSuccess(ResponseOwn response) {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
		if (HaojiajiaoApplication.isCanFullScreen) {
			// 透明状态栏
			this.getActivity().getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}

	@Override
	public void onRequestError(ResponseOwn response) {
		if (response.errorMessage == null || response.errorMessage.equals("")) {
			Toast.makeText(context, "网络请求失败，请稍后重试", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(context, response.errorMessage, Toast.LENGTH_SHORT)
			.show();
		}
		
	}
	//设置EmptyLayout
	protected  void setEmptyLayout(GridView mGridView){
		mEmptyLayout = new EmptyLayout(this.getActivity(),mGridView);
		mEmptyLayout.setErrorButtonClickListener(this);
	}

	// Triggered when "Empty" button is clicked
	public void onShowEmpty(View view) {
		mEmptyLayout.showEmpty();
	}

	// Triggered when "Loading" button is clicked
	public void onShowLoading(View view) {
		mEmptyLayout.showLoading();
	}


	// 展示所有的信息
	public void onShowError(View view) {
		mEmptyLayout.showError();
	}
	public void onShowList(View view) {
		mEmptyLayout.stopAnimation();
	}

	@Override
	public void onClick(View v) {

	}
	@Override
	public void onPause() {
		super.onPause();
		if(this.getActivity().isFinishing()&&mEmptyLayout!=null)
		{
			mEmptyLayout.stopAnimation();
		}
	}
}














