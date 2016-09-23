package com.zin.opticaltest.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.zin.opticaltest.R;
import com.zin.opticaltest.utils.BitmapUtils;
import com.zin.opticaltest.utils.LogUtils;
import com.zin.opticaltest.utils.MainUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class DrawView extends ImageView{

	private float preX;
	private float preY;
	private float curX;
	private float curY;
	private float endX;
	private float endY;
	private int WIDTH;
	private int HEIGHT;
	private float preXTemp;
	private float preYTemp;
	Path path=null;
	public Paint paint=null;
	
	Bitmap cacheBitmap=null;
	Bitmap origentBitmap;
	Canvas cacheCanvas=null;
	float mx;
	float mY;
	Bitmap initBitmap;
	float curXLine;
	float curYLine;
	DrawLocation mDrawLocal;
	private ArrayList<DrawLocation> savePath;
	private ArrayList<DrawLocation> deletePath;
	private  int touchState=1;
	public int draw_tag=100;

	private Context mContext;

	private  boolean hasChanged=false;

	private int oritation=0;
	private float drawSize=5;
	private String mText="";
	private Bitmap convexlens_ver;
	private Bitmap convexlens_hor;
	private Bitmap concavelens_hor;
	private Bitmap concavelens_ver;
	private Bitmap planemirror_cw0;
	private Bitmap planemirror_cw90;
	private Bitmap planemirror_cw180;
	private Bitmap planemirror_cw270;

	private Bitmap convex_mirror_cw0;
	private Bitmap convex_mirror_cw90;
	private Bitmap convex_mirror_cw180;
	private Bitmap convex_mirror_cw270;

	private Bitmap concave_mirror_cw0;
	private Bitmap concave_mirror_cw90;
	private Bitmap concave_mirror_cw180;
	private Bitmap concave_mirror_cw270;

	public void setDrawSize(float drawSize) {
		this.drawSize = drawSize;
		paint.setStrokeWidth(drawSize);
	}

	private Canvas mCanvas;

	public void setText(String text) {

		this.mText = text;
		Log.i("text=",""+text);
		this.setDrawType(105);
	}
	public void setDrawColor(int color){
		this.paint.setColor(color);
	}

	Bitmap pen;
	Bitmap eraser;
	public DrawView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void setBitmap(Bitmap cacheBitmap) {
		this.cacheBitmap = cacheBitmap;
	}

	public DrawView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/*public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		int[] wandh=new int[2];
		wandh=MainUtils.getWidthAndHeight(context);
		WIDTH=wandh[0];
		HEIGHT=wandh[1];

		pen = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_mode_edit_black_18dp);
		eraser = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_erase_s);


		initCanvas();

		savePath = new ArrayList<DrawLocation>();
		deletePath = new ArrayList<DrawLocation>();
	}*/
	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		int[] wandh=new int[2];
		wandh= MainUtils.getWidthAndHeight(context);
		WIDTH=wandh[0];
		HEIGHT=wandh[1];
		this.mContext=context;
		Resources resources = this.getResources();
		pen = BitmapFactory.decodeResource(resources, R.drawable.ic_mode_edit_black_18dp);
		eraser = BitmapFactory.decodeResource(resources, R.drawable.ic_erase_s);
		/*public  final static int DRAW_CONVEXLENS_ver=107;
		public  final static int DRAW_CONCAVELENS_HOR=108;
		public  final static int DRAW_CONCAVELENS_VER=109;
		public  final static int DRAW_PLANEMIRROR_HOR=110;
		public  final static int DRAW_PLANEMIRROR_VER=111;*/
		convexlens_ver = BitmapFactory.decodeResource(resources, R.drawable.convex_lens_ver_128);
		//convexlens_hor = BitmapFactory.decodeResource(resources, R.drawable.convex_lens_hor_128);
		convexlens_hor = BitmapUtils.adjustPhotoRotation(convexlens_ver,-90);
		concavelens_ver = BitmapFactory.decodeResource(resources, R.drawable.concave_lens_ver_128);
		//concavelens_hor = BitmapFactory.decodeResource(resources, R.drawable.concave_lens_hor_128);
		concavelens_hor = BitmapUtils.adjustPhotoRotation(concavelens_ver,-90);

		planemirror_cw0 = BitmapFactory.decodeResource(resources, R.drawable.plane_mirror_cw0_128);
		planemirror_cw90 =BitmapUtils.adjustPhotoRotation(planemirror_cw0,90);
		planemirror_cw180 = BitmapUtils.adjustPhotoRotation(planemirror_cw0,180);
		planemirror_cw270 = BitmapUtils.adjustPhotoRotation(planemirror_cw0,270);

		convex_mirror_cw0=BitmapFactory.decodeResource(resources,R.drawable.convex_mirror_cw0_128);
		convex_mirror_cw90=BitmapUtils.adjustPhotoRotation(convex_mirror_cw0,90);
		convex_mirror_cw180=BitmapUtils.adjustPhotoRotation(convex_mirror_cw0,180);
		convex_mirror_cw270=BitmapUtils.adjustPhotoRotation(convex_mirror_cw0,270);

		concave_mirror_cw0=BitmapFactory.decodeResource(resources,R.drawable.concave_mirror_cw0_128);
		concave_mirror_cw90=BitmapUtils.adjustPhotoRotation(concave_mirror_cw0,90);
		concave_mirror_cw180=BitmapUtils.adjustPhotoRotation(concave_mirror_cw0,180);
		concave_mirror_cw270=BitmapUtils.adjustPhotoRotation(concave_mirror_cw0,270);


		savePath = new ArrayList<>();
		deletePath = new ArrayList<>();
	}



	public void setInitBitmap(Bitmap initBitmap) {
		this.initBitmap = initBitmap;

		int height = initBitmap.getHeight();
		int width = initBitmap.getWidth();
		int w;
		int h;

		//如果横着的图（长大于高）
		if (width>height){
			w=WIDTH;
			h=height;
		}else {
			w=WIDTH;
			h=HEIGHT;
		}


		RectF rectF = new RectF(0, 0, w, h);

		if (false){
			initCanvas();
			cacheCanvas.drawBitmap(initBitmap,null,rectF,null);
			if(savePath != null && savePath.size() > 0) {

				//将路径保存列表中的路径重绘在画布上
				Iterator<DrawLocation> iter = savePath.iterator();        //重复保存
				while (iter.hasNext()) {
					DrawLocation dp = iter.next();
					drawNow(dp.tag, dp.stX, dp.stY, dp.enX, dp.enY, dp.path, dp.size, dp.text, dp.p);

				}
			}

		}else {
			cacheCanvas.drawBitmap(initBitmap,null,rectF,null);


		}


		invalidate();
	}
	public void  initBitmap(Bitmap init){
		this.initBitmap = init;
		LogUtils.i("获得的原始的图片="+initBitmap.toString());
		convertBitmapToAdapte();
		cacheBitmap=initBitmap;
		initCanvas2();

	}

	public  void  initCanvas2(){


		//cacheCanvas=new Canvas();
		LogUtils.i("initCanvas2()拿到的图片"+cacheBitmap.toString());
		cacheCanvas=new Canvas(cacheBitmap);
		path=new Path();
		paint=new Paint(Paint.DITHER_FLAG);
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		paint.setAntiAlias(true);
		paint.setDither(true);
		//cacheCanvas.setBitmap(cacheBitmap);


	}
	public  void  initCanvas2(Bitmap b){
		cacheBitmap=b;
		LogUtils.i("initCanvas2()拿到的图片"+b.toString());
		//cacheCanvas=new Canvas();
		cacheCanvas=new Canvas(initBitmap);
		path=new Path();
		paint=new Paint(Paint.DITHER_FLAG);
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		paint.setAntiAlias(true);
		paint.setDither(true);
		//cacheCanvas.setBitmap(cacheBitmap);

	}

	private void convertBitmapToAdapte(){
		int height = initBitmap.getHeight();
		int width = initBitmap.getWidth();

		//如果图片比屏幕小，则为屏幕的宽高
		int w=WIDTH;
		int h=HEIGHT;
		float pct;
		Matrix m = new Matrix();
		LogUtils.i("宽高1"+"W="+WIDTH+"H"+HEIGHT+"w"+width+"h"+height+"pct=");
		if (width>height){

			m.postRotate(90);
			initBitmap = Bitmap.createBitmap(initBitmap, 0, 0, width, height, m, true);
			LogUtils.i("宽高2"+"W="+WIDTH+"H"+HEIGHT+"w"+initBitmap.getWidth()+"h"+initBitmap.getHeight()+"pct=");
		}

		float scaleX = WIDTH / (float) initBitmap.getWidth();
		float scaleY = HEIGHT / (float) initBitmap.getHeight();
		pct = Math.min(scaleX, scaleY);// 获得缩放比例最大的那个缩放比，即scaleX和scaleY中小的那个


		final Matrix matrix = new Matrix();

		matrix.setScale(pct,pct);
		LogUtils.i("宽高3"+"W="+WIDTH+"H"+HEIGHT+"w"+initBitmap.getWidth()+"h"+initBitmap.getHeight()+"pct=");
		initBitmap = Bitmap.createBitmap(initBitmap, 0, 0, initBitmap.getWidth(), initBitmap.getHeight(), matrix, true);
		//
		this.origentBitmap=initBitmap.copy(Bitmap.Config.ARGB_8888, true);
		LogUtils.i("原始图片转换方向后的图片"+initBitmap.toString());
	}


	public void changeOritation( ) {

		int height = cacheBitmap.getHeight();
		int width = cacheBitmap.getWidth();
		Matrix m = new Matrix();
		float pct;
		hasChanged=true;
		  //旋转-90度
		//默认是横图，然后适应屏幕竖放，此时应当是720*1280

		float scaleX=1f;
		float scaleY=1f ;
		if (oritation==0){
			this.oritation=1;
			m.postRotate(-90);//1280*720----->720*406
			cacheBitmap = Bitmap.createBitmap(cacheBitmap, 0, 0, width, height, m, true);
			 scaleX = WIDTH / (float) cacheBitmap.getWidth();
			 scaleY = HEIGHT / (float) cacheBitmap.getHeight();

		}else  if (oritation==1){
			this.oritation=0;
			m.postRotate(90);
			cacheBitmap = Bitmap.createBitmap(cacheBitmap, 0, 0, width, height, m, true);
			 scaleX = WIDTH / (float) cacheBitmap.getWidth();
			 scaleY = HEIGHT / (float) cacheBitmap.getHeight();
		}
		RectF rectF = new RectF(0, 0, width, width);
		pct = Math.min(scaleX, scaleY);// 获得缩放比例最大的那个缩放比，即scaleX和scaleY中小的那个
		Matrix m2 = new Matrix();
		m2.postScale(pct,pct);
		cacheBitmap = Bitmap.createBitmap(cacheBitmap, 0, 0, cacheBitmap.getWidth(), cacheBitmap.getHeight(), m2, true);
		initCanvas2();
		//cacheCanvas.drawBitmap(initBitmap,null,rectF,null);

		invalidate();

	}


	private void initCanvas() {
		cacheBitmap= Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);

		cacheCanvas=new Canvas();
		path=new Path();
		paint=new Paint(Paint.DITHER_FLAG);
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		paint.setAntiAlias(true);
		paint.setDither(true);
		cacheCanvas.setBitmap(cacheBitmap);
		LogUtils.i("initbitmapkong");

	}
/*	private void initCanvas() {
		cacheBitmap= Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);

		cacheCanvas=new Canvas();
		path=new Path();
		paint=new Paint(Paint.DITHER_FLAG);
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		paint.setAntiAlias(true);
		paint.setDither(true);
		cacheCanvas.setBitmap(cacheBitmap);
	}*/

	public  void setDrawType(int type){
		this.draw_tag=type;
		Log.i("tag=",""+draw_tag);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		Paint bPaint=new Paint();

		if (touchState==1){
			if (draw_tag!=Pen.DRAW_ERASER){
				canvas.drawBitmap(pen, preX, preY- pen.getHeight(), bPaint);
			}else {
				canvas.drawBitmap(eraser, preX, preY- pen.getHeight(), bPaint);
			}

		}else if (touchState==2){
			if (draw_tag!=Pen.DRAW_ERASER){
				canvas.drawBitmap(pen, curX, curY- pen.getHeight(), bPaint);
			}else {
				canvas.drawBitmap(eraser, curX, curY- pen.getHeight(), bPaint);
			}
		}
		//canvas.drawBitmap(initBitmap,0,0,bPaint);
		canvas.drawBitmap(cacheBitmap, 0, 0,bPaint);
		LogUtils.i("onDraw()拿到的图片"+cacheBitmap.toString());

		//canvas.drawPath(path, paint);
		
	}

	public Bitmap getBitmap(){
	  
		return cacheBitmap;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub


		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				preX=event.getX();
				preY=event.getY();
				if (draw_tag==Pen.DRAW_CURVE){
					preXTemp=preX;
					preYTemp=preY;
					path.moveTo(preXTemp, preYTemp);
				}
				touchState=1;
				break;
			case MotionEvent.ACTION_MOVE:

				curX=event.getX();
				curY=event.getY();
				if (draw_tag==Pen.DRAW_CURVE){
				//	path.quadTo(preXTemp, preYTemp, (curX+preXTemp)/2, (curY+preYTemp)/2);
					path.quadTo(preXTemp, preYTemp, (curX+preXTemp)/2, (curY+preYTemp)/2);
					preXTemp=curX;
					preYTemp=curY;
				}
				touchState=2;

				break;
			case MotionEvent.ACTION_UP:
				endX=event.getX();
				endY=event.getY();

				touchState=3;
				drawNow();

				mDrawLocal = new DrawLocation();
				mDrawLocal.path = path;
				mDrawLocal.p = paint;
				mDrawLocal.tag=draw_tag;
				mDrawLocal.stX=preX;
				mDrawLocal.stY=preY;
				mDrawLocal.enX=endX;
				mDrawLocal.enY=endY;
				mDrawLocal.size=drawSize;
				mDrawLocal.text=mText;
				savePath.add(mDrawLocal);
				Log.i("保存了2",""+mDrawLocal.stX+","+mDrawLocal.stY+","+mDrawLocal.enX+","+mDrawLocal.enY+","+mDrawLocal.tag);
				mDrawLocal = null;
				break;

			default:
				break;
		}


		invalidate();
		
		return true;
	}

	/*
	*清空的主要思想就是初始化画布
	* 将保存路径的两个List清空
	*
	* */
	public void removeAllPaint(){
		//调用初始化画布函数以清空画布
		LogUtils.i("执行removeAllPaint()");
		cacheBitmap=origentBitmap.copy(Bitmap.Config.ARGB_8888, true);
		initCanvas2();

		//initCanvas2();
		invalidate();//刷新
		savePath.clear();
		deletePath.clear();
	}
	/**
	 * 撤销的核心思想就是将画布清空，
	 * 将保存下来的Path路径最后一个移除掉，
	 * 重新将路径画在画布上面。
	 */
	public void undo(){
		for (int i=0;i<savePath.size();i++){
			Log.i("集合里的"+i,""+savePath.get(i).stX+","+savePath.get(i).stY+","+savePath.get(i).enX+","+savePath.get(i).enY+","+savePath.get(i).tag);
		}

		System.out.println(savePath.size()+"--------------");
		if(savePath != null && savePath.size() > 0){
			//调用初始化画布函数以清空画布
			cacheBitmap=origentBitmap.copy(Bitmap.Config.ARGB_8888, true);
			initCanvas2();



			//将路径保存列表中的最后一个元素删除 ,并将其保存在路径删除列表中
			DrawLocation drawPath = savePath.get(savePath.size() - 1);
			deletePath.add(drawPath);
			savePath.remove(savePath.size() - 1);

			//将路径保存列表中的路径重绘在画布上
			Iterator<DrawLocation> iter = savePath.iterator();		//重复保存
			while (iter.hasNext()) {
				DrawLocation dp = iter.next();
				drawNow(dp.tag,dp.stX,dp.stY,dp.enX,dp.enY,dp.path,dp.size,dp.text,dp.p);

			}
			invalidate();// 刷新
		}
	}
	/**
	 * 恢复的核心思想就是将撤销的路径保存到另外一个列表里面(栈)，
	 * 然后从redo的列表里面取出最顶端对象，
	 * 画在画布上面即可
	 */
	public void redo(){
		if(deletePath.size() > 0){
			//将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
			DrawLocation dp = deletePath.get(deletePath.size() - 1);
			savePath.add(dp);
			//将取出的路径重绘在画布上
			drawNow(dp.tag,dp.stX,dp.stY,dp.enX,dp.enY,dp.path,dp.size,dp.text,dp.p);
			//将该路径从删除的路径列表中去除
			deletePath.remove(deletePath.size() - 1);
			invalidate();
		}
	}



	private void drawNow(){
		int color = paint.getColor();
		Paint.Style style = paint.getStyle();
		float strokeWidth = paint.getStrokeWidth();
		paint.setAntiAlias(true);
		paint.setDither(true);

		switch (draw_tag){
			case Pen.DRAW_LINE:
				//cacheCanvas.save();
				cacheCanvas.drawLine(preX,preY,endX,endY,paint);
				//cacheCanvas.restore();
				Log.i("绘制了直线",""+preX+","+preY+","+endX+","+endY+","+draw_tag);
				break;
			case Pen.DRAW_DOT:
				Paint dotPaint=new Paint(Paint.DITHER_FLAG);
				dotPaint.setColor(color);
				//cacheCanvas.save();
				dotPaint.setAntiAlias(true);
				dotPaint.setDither(true);
				dotPaint.setStyle(Paint.Style.FILL);
				dotPaint.setStrokeWidth(strokeWidth);
				cacheCanvas.drawCircle(endX,endY,paint.getStrokeWidth(),dotPaint);
				//cacheCanvas.restore();
				Log.i("绘制了点",""+preX+","+preY+","+endX+","+endY+","+draw_tag);

				break;
			case Pen.DRAW_CIRCUL:
				//cacheCanvas.save();
				Paint cirPaint=new Paint(Paint.DITHER_FLAG);
				cirPaint.setColor(color);
				//cacheCanvas.save();
				cirPaint.setAntiAlias(true);
				cirPaint.setDither(true);
				cirPaint.setStyle(style);

				cirPaint.setStrokeWidth(2);

				cacheCanvas.drawCircle(endX,endY,drawSize,cirPaint);
				//cacheCanvas.restore();
				break;
			case Pen.DRAW_CURVE:

				cacheCanvas.drawPath(path, paint);
				path.reset();
				break;
			case Pen.DRAW_ERASER:
				Paint eraPaint=new Paint(Paint.DITHER_FLAG);
				eraPaint.setStyle(Paint.Style.FILL);
				eraPaint.setColor(Color.WHITE);
				eraPaint.setStrokeWidth(20);

				cacheCanvas.drawLine(preX,preY,endX,endY,eraPaint);


				break;
			case Pen.DRAW_TEXT:
				Paint textPaint=new Paint(Paint.DITHER_FLAG);
				textPaint.setColor(color);
				textPaint.setTextSize(30);
				textPaint.setStyle(style);
				Log.i("case,text=",mText);
				cacheCanvas.drawText(mText, endX, endY, textPaint);
				this.setDrawType(100);
				//cacheCanvas.drawText("a", 300, 300, textPaint);
				break;
			case Pen.DRAW_CONVEXLENS_HOR:

				cacheCanvas.drawBitmap(convexlens_hor,endX,endY,paint);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONVEXLENS_ver:
					cacheCanvas.drawBitmap(convexlens_ver,endX,endY,paint);
				this.setDrawType(100);

				break;
			case Pen.DRAW_CONCAVELENS_HOR:
				cacheCanvas.drawBitmap(concavelens_hor,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONCAVELENS_VER:
				cacheCanvas.drawBitmap(concavelens_ver,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_PLANEMIRROR_CW0:
				cacheCanvas.drawBitmap(planemirror_cw0,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_PLANEMIRROR_CW90:
				cacheCanvas.drawBitmap(planemirror_cw90,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_PLANEMIRROR_CW180:
				cacheCanvas.drawBitmap(planemirror_cw180,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_PLANEMIRROR_CW270:
				cacheCanvas.drawBitmap(planemirror_cw270,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONVEX_MIRROR_CW0_128:
				cacheCanvas.drawBitmap(convex_mirror_cw0,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONVEX_MIRROR_CW90_128:
				cacheCanvas.drawBitmap(convex_mirror_cw90,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONVEX_MIRROR_CW180_128:
				cacheCanvas.drawBitmap(convex_mirror_cw180,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONVEX_MIRROR_CW270_128:
				cacheCanvas.drawBitmap(convex_mirror_cw270,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONCAVE_MIRROR_CW0_128:
				cacheCanvas.drawBitmap(concave_mirror_cw0,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONCAVE_MIRROR_CW90_128:
				cacheCanvas.drawBitmap(concave_mirror_cw90,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONCAVE_MIRROR_CW180_128:
				cacheCanvas.drawBitmap(concave_mirror_cw180,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONCAVE_MIRROR_CW270_128:
				cacheCanvas.drawBitmap(concave_mirror_cw270,endX,endY,null);
				this.setDrawType(100);
				break;




		}

	}


	private void drawNow(int tag,float x1,float y1,float x2,float y2,Path tpath,float size,String str,Paint pt){
		switch (tag){
			case Pen.DRAW_LINE:
				cacheCanvas.drawLine(x1,y1,x2,y2,pt);
				break;
			case Pen.DRAW_DOT:
				Paint dotPaint=pt;

				dotPaint.setStyle(Paint.Style.FILL);
				cacheCanvas.drawCircle(x2,y2,paint.getStrokeWidth(),dotPaint);
				break;
			case Pen.DRAW_CIRCUL:
				Paint cirPaint=pt;
				cirPaint.setStyle(Paint.Style.STROKE);
				cirPaint.setStrokeWidth(2);
				cacheCanvas.drawCircle(x2,y2,size,cirPaint);
				break;
			case Pen.DRAW_CURVE:
				Paint curPaint=paint;
				curPaint.setStyle(Paint.Style.STROKE);
				cacheCanvas.drawPath(tpath, curPaint);
				//path.reset();
				break;
			case Pen.DRAW_ERASER:
				Paint eraPaint=new Paint();
				eraPaint.setStyle(Paint.Style.FILL);
				eraPaint.setColor(Color.WHITE);
				eraPaint.setStrokeWidth(20);
				cacheCanvas.drawLine(x1,y1,x2,y2,eraPaint);

				break;
			case Pen.DRAW_TEXT:
				Paint textPaint=new Paint();
				textPaint.setStyle(Paint.Style.STROKE);
				textPaint.setStrokeWidth(2);
				cacheCanvas.drawText(str, x2, y2, textPaint);
				break;
			case Pen.DRAW_CONVEXLENS_HOR:

				cacheCanvas.drawBitmap(convexlens_hor,x2,y2,paint);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONVEXLENS_ver:
				cacheCanvas.drawBitmap(convexlens_ver,x2,y2,paint);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONCAVELENS_HOR:
				cacheCanvas.drawBitmap(concavelens_hor,x2,y2,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONCAVELENS_VER:
				cacheCanvas.drawBitmap(concavelens_ver,x2,y2,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_PLANEMIRROR_CW0:
				cacheCanvas.drawBitmap(planemirror_cw0,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_PLANEMIRROR_CW90:


				cacheCanvas.drawBitmap(planemirror_cw90,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_PLANEMIRROR_CW180:
				cacheCanvas.drawBitmap(planemirror_cw180,endX,endY,null);
				this.setDrawType(100);

				break;
			case Pen.DRAW_PLANEMIRROR_CW270:
				cacheCanvas.drawBitmap(planemirror_cw270,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONVEX_MIRROR_CW0_128:
				cacheCanvas.drawBitmap(convex_mirror_cw0,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONVEX_MIRROR_CW90_128:
				cacheCanvas.drawBitmap(convex_mirror_cw90,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONVEX_MIRROR_CW180_128:
				cacheCanvas.drawBitmap(convex_mirror_cw180,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONVEX_MIRROR_CW270_128:
				cacheCanvas.drawBitmap(convex_mirror_cw270,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONCAVE_MIRROR_CW0_128:
				cacheCanvas.drawBitmap(concave_mirror_cw0,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONCAVE_MIRROR_CW90_128:
				cacheCanvas.drawBitmap(concave_mirror_cw90,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONCAVE_MIRROR_CW180_128:
				cacheCanvas.drawBitmap(concave_mirror_cw180,endX,endY,null);
				this.setDrawType(100);
				break;
			case Pen.DRAW_CONCAVE_MIRROR_CW270_128:
				cacheCanvas.drawBitmap(concave_mirror_cw270,endX,endY,null);
				this.setDrawType(100);
				break;





		}

	}

	private class DrawLocation {
		float stX;
		float stY;
		float enX;
		float enY;
		Path path;
		Paint p;
		int tag;
		float size;
		String text;


	}
	public class Pen{
		public  final static int DRAW_LINE=100;
		public final static int DRAW_DOT=101;
		public final static int DRAW_CIRCUL=102;
		public final static int DRAW_CURVE=103;
		public  final static int DRAW_ERASER=104;
		public  final static int DRAW_TEXT=105;
		public  final static int DRAW_CONVEXLENS_HOR=106;
		public  final static int DRAW_CONVEXLENS_ver=107;
		public  final static int DRAW_CONCAVELENS_HOR=108;
		public  final static int DRAW_CONCAVELENS_VER=109;
		public  final static int DRAW_PLANEMIRROR_CW0=110;
		public  final static int DRAW_PLANEMIRROR_CW90=111;
		public  final static int DRAW_PLANEMIRROR_CW180=112;
		public  final static int DRAW_PLANEMIRROR_CW270=113;
		public  final static int DRAW_CONVEX_MIRROR_CW0_128=114;
		public  final static int DRAW_CONVEX_MIRROR_CW90_128=115;
		public  final static int DRAW_CONVEX_MIRROR_CW180_128=116;
		public  final static int DRAW_CONVEX_MIRROR_CW270_128=117;
		public  final static int DRAW_CONCAVE_MIRROR_CW0_128=118;
		public  final static int DRAW_CONCAVE_MIRROR_CW90_128=119;
		public  final static int DRAW_CONCAVE_MIRROR_CW180_128=120;
		public  final static int DRAW_CONCAVE_MIRROR_CW270_128=121;

	}



}

