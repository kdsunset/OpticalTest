package com.zin.opticaltest.ui.widget;

import android.content.Context;
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
import android.view.View;

import com.zin.opticaltest.R;
import com.zin.opticaltest.utils.MainUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class DrawView2 extends View{

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
	Bitmap resultBitmap;
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
	public DrawView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void setBitmap(Bitmap cacheBitmap) {
		this.cacheBitmap = cacheBitmap;
	}

	public DrawView2(Context context) {
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
	public DrawView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		int[] wandh=new int[2];
		wandh= MainUtils.getWidthAndHeight(context);
		WIDTH=wandh[0];
		HEIGHT=wandh[1];
		this.mContext=context;
		pen = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_mode_edit_black_18dp);
		eraser = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_erase_s);


		initCanvas();

		savePath = new ArrayList<DrawLocation>();
		deletePath = new ArrayList<DrawLocation>();
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

		if (hasChanged){
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
			//cacheCanvas.drawBitmap(initBitmap,null,rectF,null);


		}
		cacheCanvas.setBitmap(initBitmap);
		invalidate();
	}


	public void changeOritation( ) {

		int height = initBitmap.getHeight();
		int width = initBitmap.getWidth();
		Matrix m = new Matrix();
		hasChanged=true;
		  //旋转-90度

		if (oritation==0){
			this.oritation=1;
			m.postRotate(90);
		}else  if (oritation==1){
			this.oritation=0;
			m.postRotate(-90);
		}
		initBitmap = Bitmap.createBitmap(initBitmap, 0, 0, width, height, m, true);

		setInitBitmap(initBitmap);

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
			if (draw_tag!= Pen.DRAW_ERASER){
				canvas.drawBitmap(pen, preX, preY- pen.getHeight(), bPaint);
			}else {
				canvas.drawBitmap(eraser, preX, preY- pen.getHeight(), bPaint);
			}

		}else if (touchState==2){
			if (draw_tag!= Pen.DRAW_ERASER){
				canvas.drawBitmap(pen, curX, curY- pen.getHeight(), bPaint);
			}else {
				canvas.drawBitmap(eraser, curX, curY- pen.getHeight(), bPaint);
			}
		}
		//canvas.drawBitmap(initBitmap,0,0,bPaint);
		canvas.drawBitmap(cacheBitmap, 0, 0,bPaint);

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
				if (draw_tag== Pen.DRAW_CURVE){
					preXTemp=preX;
					preYTemp=preY;
					path.moveTo(preXTemp, preYTemp);
				}
				touchState=1;
				break;
			case MotionEvent.ACTION_MOVE:

				curX=event.getX();
				curY=event.getY();
				if (draw_tag== Pen.DRAW_CURVE){
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
		initCanvas();
		invalidate();//刷新
		//savePath.clear();
		//deletePath.clear();
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
			initCanvas();

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
		switch (draw_tag){
			case Pen.DRAW_LINE:
				cacheCanvas.drawLine(preX,preY,endX,endY,paint);
				Log.i("绘制了直线",""+preX+","+preY+","+endX+","+endY+","+draw_tag);
				break;
			case Pen.DRAW_DOT:
				Paint dotPaint=paint;

				dotPaint.setStyle(Paint.Style.FILL);
				cacheCanvas.drawCircle(endX,endY,paint.getStrokeWidth(),dotPaint);
				Log.i("绘制了点",""+preX+","+preY+","+endX+","+endY+","+draw_tag);
				break;
			case Pen.DRAW_CIRCUL:
				Paint cirPaint=paint;

				cirPaint.setStyle(Paint.Style.STROKE);
				cirPaint.setStrokeWidth(2);

				cacheCanvas.drawCircle(endX,endY,drawSize,cirPaint);
				break;
			case Pen.DRAW_CURVE:
				Paint curPaint=paint;
				curPaint.setStyle(Paint.Style.STROKE);
				cacheCanvas.drawPath(path, curPaint);
				path.reset();
				break;
			case Pen.DRAW_ERASER:
				Paint eraPaint=new Paint();
				eraPaint.setStyle(Paint.Style.FILL);
				eraPaint.setColor(Color.WHITE);
				eraPaint.setStrokeWidth(20);
				cacheCanvas.drawLine(preX,preY,endX,endY,eraPaint);

				break;
			case Pen.DRAW_TEXT:
				Paint textPaint=new Paint();
				textPaint.setTextSize(30);
				Log.i("case,text=",mText);
				cacheCanvas.drawText(mText, endX, endY, textPaint);
				this.setDrawType(100);
				//cacheCanvas.drawText("a", 300, 300, textPaint);
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
	}



}
