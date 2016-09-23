package com.zin.opticaltest.ui;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.zin.opticaltest.R;
import com.zin.opticaltest.utils.UIUtils;

public abstract class NetStatusFrameLayout extends FrameLayout {
	//正在加载view
	private View loadingView;
	//加载失败view
	private View errorView;
	//加载为空view
	private View emptyView;
	//加载成功view
	private View successedView;
	
	//初始状态码
	public static final int STATE_UNLOAD = 0;
	//正在加载状态码
	public static final int STATE_LOADING = 1;
	//加载失败状态码
	public static final int STATE_LOAD_ERROR = 2;
	//加载为空状态码
	public static final int STATE_LOAD_EMPTY = 3;
	//加成功状态码
	public static final int STATE_SUCCESSED = 4;


	private int state = STATE_UNLOAD;
	private final Context mContext;


	public NetStatusFrameLayout(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	private void initView() {
		//1,正在加载的view
		loadingView = createLodingPage();
		//添加正在加载的view在帧布局上,稍后通过请求网络的状态决定显示隐藏
		//addView(loadingView);

		
		//2,加载失败view
		errorView = createErrorPage();
		//addView(errorView);
		
		//3,加载为空view
		emptyView = createEmptyPage();
		//addView(emptyView);

		//4,加载成功view,在正在加载网络数据成功以后,再将其添加到FrameLayout中,所以此处不做处理
		//5,根据当前的状态码决定上诉那个view显示,那个view隐藏
		showPageByStatus();
	}

	private  void  showpage(int state){
		if (state==STATE_UNLOAD||state==STATE_LOADING){
			removeAllViews();
			addView(loadingView);

		}else if (state==STATE_LOAD_EMPTY){
			removeAllViews();
			addView(emptyView);
		}else if (state==STATE_LOAD_ERROR){
			removeAllViews();
			addView(errorView);
		}else if (state==STATE_SUCCESSED){
			if(successedView == null){
				successedView = createSuccessPage();
			}
			removeAllViews();
			addView(successedView);
		}
	}

	/**
	 * 根据不同的请求网络状态,判断那种类型的界面显示,那个种类型的界面隐藏
	 */
	private void showPageByStatus() {
		if(loadingView!=null){
			//如果现在处理正在加载或者初始状态的是,都是中心转圈的进度条显示
			if(((state == STATE_LOADING) || (state == STATE_UNLOAD))){
				loadingView.setVisibility(View.VISIBLE);
			}else{
				loadingView.setVisibility(View.GONE);
			}
		}
		
		if(errorView!=null){
			//根据当前状态是否为加载出错,决定加载出错的view的显示隐藏状态
			errorView.setVisibility(
					(state == STATE_LOAD_ERROR)?View.VISIBLE:View.GONE);
		}
		
		if(emptyView!=null){
			//根据当前状态是否为加载为空,决定加载为空的view的显示隐藏状态
			emptyView.setVisibility(
					(state == STATE_LOAD_EMPTY)?View.VISIBLE:View.GONE);
		}
		
		//判断加载成功的view是否为空
		if(successedView == null){
			//调用完了此方法后,返回的即使每一个子模块的对应成功获取数据,展示的view对象
			successedView = createSuccessPage();
			//添加到帧布局中
			if(successedView!=null){
				addView(successedView);
			}
		}
		
		if(successedView!=null){
			successedView.setVisibility((state == STATE_SUCCESSED)?View.VISIBLE:View.GONE); 
		}
	}
	
	/**
	 * 请求网络,返回状态
	 */
	public void show(){
		//1,将上一次的请求结果归位
		if(state == STATE_LOAD_EMPTY || state == STATE_LOAD_ERROR || state == STATE_SUCCESSED){
			state = STATE_UNLOAD;
		}
		//2,再去做下一次请求过程
		if(state == STATE_UNLOAD){
			showPageByStatus();

		}
	}

	private View createEmptyPage() {
		return UIUtils.inflate(R.layout.layout_empty);
	}

	private View createErrorPage() {
		return UIUtils.inflate(R.layout.layout_error);
	}

	private View createLodingPage() {
		return UIUtils.inflate(R.layout.layout_loading);
	}
	
	/**
	 * @return	返回加载成功的view的方法,因为首页,应用,游戏.....加载数据成功的界面效果都有差异,并且loadingPage也不知道每一个页面具体的xml
	 * 			所以此方法无法实现,于是定义成抽象方法
	 */
	public abstract View createSuccessPage();
	
	/**
	 * @return	返回一个枚举类型的对象,其对象中维护了响应请求网络的状态码
	 */

	

		
		public void setNetStatus(int status){
			showpage(status);
			//return status;
		}

	
}
