package com.zin.opticaltest.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBUtils extends SQLiteOpenHelper{

	public DBUtils(Context context, String name, CursorFactory factory, int version) {
		
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		
		String sql="create table if not exists paperlist(id integer primary key autoincrement ,paperId varchar(20),title varchar(100) ," +
				"titileTwo varchar(100) ,issueTime varchar(100), paperType varchar(20),qualified integer,totalScore integer," +
				"respondenceTime integer,provider varchar(100),remark varchar(300),subject integer,hasDone integer)";

		String sql2="create table if not exists paper(id integer primary key autoincrement ,questionId integer,title varchar(300) ," +
				"questionType varchar(50) ,itema varchar(100), itemb varchar(100),itemc varchar(100),itemd varchar(100),answer varchar(300)," +
				"fillAnswer varchar(300) ,analysis varchar(300),image varchar(100) ,paperId integer," +
				"quesimg varchar(100),ansimg varchar(100),youransimg varchar(100),analysisimg varchar(100))";

		/* private int userId;
			private String studentId;
			private String mobilePhone;
			private String grade;
			private String passWord;
			private String realName;
			private String userName;
			private String userType;
			private String emailAddress;
			private String headImage;
			private long lastLoginTime;*/

		String usersql="create table if not exists user(id integer primary key autoincrement ,userId integer,userType varchar(100),userName varchar(300) ," +
				"passWord varchar(50) ,realName varchar(100), grade varchar(100),studentId integer,mobilePhone varchar(100),emailAddress varchar(100)," +
				"headImage varchar(100),lastLoginTime integer )";
		db.execSQL(sql);
		db.execSQL(sql2);
		db.execSQL(usersql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
