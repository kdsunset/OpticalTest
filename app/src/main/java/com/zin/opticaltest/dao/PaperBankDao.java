package com.zin.opticaltest.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zin.opticaltest.entity.TestBank;
import com.zin.opticaltest.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

public class PaperBankDao {

	static DBUtils dbUtils=null;
	public PaperBankDao(Context context){
		dbUtils=new DBUtils(context, "paperlist.db", null, 1);
	}
	
	public void add(TestBank paper){
		//String sql="insert into notes  title=? and content=?";
		String sql="insert into paperlist (paperId,title,titileTwo ,issueTime ,paperType," +
				" qualified ,totalScore, respondenceTime, provider, remark，subject,hasdone integer) " +
				"values(?,?,?,?,?,?,?,?,?,?,?,?)";
		/*
		String sql="create table if not exists papers(id integer primary key autoincrement ,paperId integer,title varchar(100) ," +
				"titileTwo varchar(100) ,issueTime varchar(100), paperType integer,qualified integer,totalScore integer," +
				"respondenceTime integer,provider varchar(100),remark varchar(300),subject integer)";*/


		SQLiteDatabase db=dbUtils.getWritableDatabase();
		db.execSQL(sql, new Object[]{paper.getPaperId(),paper.getTitle(),paper.getTitletwo(),
				paper.getIssuetime(),paper.getPapertype(),paper.getQualified(),paper.getAllscore(),
				paper.getRespondencetime(),paper.getProvider(),paper.getRemark(),paper.getSubject()});
		db.close();
		
	}
	public  void addList(List<TestBank> list){
		String sql="insert into paperlist (paperId,title,titileTwo ,issueTime ,paperType," +
				"qualified ,totalScore, respondenceTime, provider, remark,subject,hasdone ) " +
				"values(?,?,?,?,?,?,?,?,?,?,?,?)";
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		for (TestBank paper:list){
			db.execSQL(sql, new Object[]{paper.getPaperId(),paper.getTitle(),paper.getTitletwo(),
					paper.getIssuetime(),paper.getPapertype(),paper.getQualified(),paper.getAllscore(),
					paper.getRespondencetime(),paper.getProvider(),paper.getRemark(),paper.getSubject()});

		}
		db.close();
	}

	public static  void del(TestBank paper){
		String sql="delete from paperlist where paperId=?";
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		db.execSQL(sql, new Object[]{paper.getPaperId()});
		db.close();
	}
	public  void clear(){
		//String sql="truncate table paperlist";
		String sql="delete from paperlist";
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		db.execSQL(sql);
		db.close();

	}
	/*public  void update(TestBank note){
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		String sql="update  notes set title=?,date=?,content=? where id=?";
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
	
	public List<TestBank> showALL(){
		TestBank note=null;
		List<TestBank> noteList=new ArrayList<TestBank>();
		String sql="select * from paperlist ORDER BY paperId asc";
		SQLiteDatabase db=dbUtils.getReadableDatabase();
		Cursor cursor=db.rawQuery(sql, null);
		while (cursor.moveToNext()) {

			/*
				String sql="create table if not exists papers(id integer primary key autoincrement ,paperId integer,title varchar(100) ," +
				"titileTwo varchar(100) ,issueTime varchar(100), paperType integer,qualified integer,totalScore integer," +
				"respondenceTime integer,provider varchar(100),remark varchar(300),subject integer)";*/
			Integer paperId=cursor.getInt(cursor.getColumnIndex("paperId"));
			String title=cursor.getString(cursor.getColumnIndex("title"));
			String titileTwo=cursor.getString(cursor.getColumnIndex("titileTwo"));
			String issueTime=cursor.getString(cursor.getColumnIndex("issueTime"));
			int respondenceTime=cursor.getInt(cursor.getColumnIndex("respondenceTime"));
			String provider=cursor.getString(cursor.getColumnIndex("provider"));
			String remark=cursor.getString(cursor.getColumnIndex("remark"));
			String paperType=cursor.getString(cursor.getColumnIndex("paperType"));
			int qualified=cursor.getInt(cursor.getColumnIndex("qualified"));
			int totalScore=cursor.getInt(cursor.getColumnIndex("totalScore"));
			int subject=cursor.getInt(cursor.getColumnIndex("subject"));
			int hansDone=cursor.getInt(cursor.getColumnIndex("hasDone"));
			note=new TestBank(paperId, title, titileTwo,issueTime,paperType, qualified ,totalScore,
					respondenceTime,provider,remark,subject,hansDone);
			noteList.add(note);
		}
		cursor.close();
		db.close();
		return noteList;
		
	}
	public boolean isEmpty(){
		SQLiteDatabase db=dbUtils.getReadableDatabase();
		String sql="select * from paperlist";

		Cursor cursor=db.rawQuery(sql, null);
		if (cursor.getCount()==0) {
			return true;
		}
		
		return false;
		
	}
	 
 }
