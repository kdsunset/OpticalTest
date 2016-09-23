package com.zin.opticaltest.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zin.opticaltest.entity.Question;
import com.zin.opticaltest.entity.TestBank;
import com.zin.opticaltest.utils.DBUtils;
import com.zin.opticaltest.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class PaperDao {

	static DBUtils dbUtils=null;
	public PaperDao(Context context){
		dbUtils=new DBUtils(context, "paper.db", null, 1);
	}
	
	public void add(Question question,int paperId){
		//String sql="insert into notes  title=? and content=?";
		/*"quesimg varchar(100),ansimg varchar(100),youransimg varchar(100),anylysisimg varchar(100),)";*/
		String sql="insert into paper (questionId,title, questionType ,itema ,itemb," +
				" itemc, itemd, answer, fillAnswer, analysis ,image, paperId,quesimg," +
				"ansimg,youransimg,analysisimg) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		/*
		String sql2="create table if not exists paper(id integer primary key autoincrement ,questionId integer,title varchar(200) ," +
				"questionType integer ,itema varchar(100), itemb varchar(100),itemc varchar(100),itemd varchar(100),answer integer," +
				"fillAnswer integer,analysis varchar(300),image varchar(100),paperId integer )";;*/


		SQLiteDatabase db=dbUtils.getWritableDatabase();
		db.execSQL(sql, new Object[]{question.getItemId(),question.getTitle(),question.getTestType(),
				question.getItema(),question.getItemb(),question.getItemc(),question.getItemd(),
				question.getAnswer(),question.getYourAnswer(),question.getAnalysis(),question.getImage(),paperId,
				question.getQuesimg(),question.getAnsimg(),question.getYouransimg(),question.getAnalysisimg()});
		db.close();
		
	}

	public  void addOnePaper(List<Question> list,int paperId){
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		String sql="insert into paper (questionId,title, questionType ,itema ,itemb," +
				" itemc, itemd, answer, fillAnswer, analysis ,image, paperId,quesimg," +
				"ansimg,youransimg,analysisimg) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		for (int i=0;i<list.size();i++){
			Question question=list.get(i);
			db.execSQL(sql, new Object[]{question.getItemId(),question.getTitle(),question.getTestType(),
					question.getItema(),question.getItemb(),question.getItemc(),question.getItemd(),
					question.getAnswer(),question.getYourAnswer(),question.getAnalysis(),question.getImage(),paperId,
					question.getQuesimg(),question.getAnsimg(),question.getYouransimg(),question.getAnalysisimg()});
		}
		db.close();

	}
	public   void delAllQuestionOfPaper(TestBank paper){
		String sql="delete from paper where paperId=?";
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		db.execSQL(sql, new Object[]{paper.getPaperId()});
		db.close();
	}
	/*public  void update(TestBank note){
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		String sql="update  notes set title=?,date=?,content=? where id=?";
		db.execSQL(sql, new Object[]{note.getTitle(),note.getDate(),note.getContent(),note.getId()});
		
		db.close();
	}*/

	/*public void saveFinllinAnswer(List<Question> list, int pagerId){
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		String sql="update  paper set questionId=?,title=?, questionType=? ,itema=? ,itemb=?," +
				" itemc=?, itemd=?, answer=?, fillAnswer=?, analysis=? ,image=?, paperId=? where paperId=?";
		db.execSQL(sql, new Object[]{note.getTitle(),note.getDate(),note.getContent(),note.getId()});

		db.close();
	}*/

	/*public  int update2(Note note){
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		*//*String sql="update  notes set title=? ,date=? ,content=? where id=?";
		db.execSQL(sql, new Object[]{note.getTitle(),note.getDate(),note.getContent(),note.getId()});*//*
		ContentValues values=new ContentValues();
		values.put("title", note.getTitle());
		values.put("date", note.getDate());
		values.put("content", note.getContent());
		String[] whereArgs=new String[]{note.getId()+""};
		int f=db.update("notes", values, "id=?", whereArgs);
		db.close();
		return f;
	}*/
	
	public List<Question> showALLQuestionOfPaper(int paperId){
		Question question;
		List<Question> questionList=new ArrayList<>();
		String sql="select * from paper where paperId=? ";
		SQLiteDatabase db=dbUtils.getReadableDatabase();
		Cursor cursor=db.rawQuery(sql, new String[]{paperId+""});
		while (cursor.moveToNext()) {

		/*
	"insert into paper (questionId,title, questionType ,itema ,itemb" +
				" itemc, itemd, answer, fillAnswer, analysis ,image, paperId,quesimg," +
				"ansimg,youransimg,anylysisimg) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"*/

			int questionId=cursor.getInt(cursor.getColumnIndex("questionId"));
			String title=cursor.getString(cursor.getColumnIndex("title"));
			String questionType=cursor.getString(cursor.getColumnIndex("questionType"));
			String itema=cursor.getString(cursor.getColumnIndex("itema"));
			String itemb=cursor.getString(cursor.getColumnIndex("itemb"));
			String itemc=cursor.getString(cursor.getColumnIndex("itemc"));
			String itemd=cursor.getString(cursor.getColumnIndex("itemd"));
			String answer=cursor.getString(cursor.getColumnIndex("answer"));
			String fillAnswer=cursor.getString(cursor.getColumnIndex("fillAnswer"));
			String analysis=cursor.getString(cursor.getColumnIndex("analysis"));
			String image=cursor.getString(cursor.getColumnIndex("image"));
			String quesimg=cursor.getString(cursor.getColumnIndex("quesimg"));
			String ansimg=cursor.getString(cursor.getColumnIndex("ansimg"));
			String youransimg=cursor.getString(cursor.getColumnIndex("youransimg"));
			String analysisimg=cursor.getString(cursor.getColumnIndex("analysisimg"));

			LogUtils.i("数据库里titke="+title);
			question=new Question(questionId, title, questionType,itema,itemb, itemc ,itemd,answer,fillAnswer,analysis,image,paperId,
					quesimg,ansimg,youransimg,analysisimg);
			questionList.add(question);
		}
		cursor.close();
		db.close();
		LogUtils.i(questionList.size()+"大小");
		return questionList;
		
	}
	public boolean isEmpty(){
		SQLiteDatabase db=dbUtils.getReadableDatabase();
		String sql="select * from paper";
		
		Cursor cursor=db.rawQuery(sql, null);
		if (cursor.getCount()==0) {
			return true;
		}
		
		return false;
		
	}
	 
 }
