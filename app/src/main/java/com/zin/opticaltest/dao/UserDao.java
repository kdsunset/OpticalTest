package com.zin.opticaltest.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zin.opticaltest.entity.User;
import com.zin.opticaltest.utils.DBUtils;
import com.zin.opticaltest.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class UserDao {

	static DBUtils dbUtils=null;
	public UserDao(Context context){
		dbUtils=new DBUtils(context, "user.db", null, 1);
	}
	
	public void add(User user){
		//String sql="insert into notes  title=? and content=?";
		/*"quesimg varchar(100),ansimg varchar(100),youransimg varchar(100),anylysisimg varchar(100),)";*/

		/*String usersql="create table if not exists user(id integer primary key autoincrement ,
		userId integer,userType varchar(100),userName varchar(300) ," +
				"passWord varchar(50) ,realName varchar(100), grade varchar(100),studentId integer,
				headImage varchar(100),lastLoginTime integer )";
		db.execSQL(sql);*/
		/*				"passWord varchar(50) ,realName varchar(100),
		grade varchar(100),studentId integer,mobilePhone varchar(100),emailAddress varchar(100)," +
*/
		String sql="insert into user (userType, userName ,passWord ,realName," +
				" grade, studentId,mobilePhone, emailAddress,headImage, lastLoginTime) values(?,?,?,?,?,?,?,?,?,?)";
		/*
		String sql2="create table if not exists paper(id integer primary key autoincrement ,questionId integer,title varchar(200) ," +
				"questionType integer ,itema varchar(100), itemb varchar(100),itemc varchar(100),itemd varchar(100),answer integer," +
				"fillAnswer integer,analysis varchar(300),image varchar(100),paperId integer )";;*/


		SQLiteDatabase db=dbUtils.getWritableDatabase();
		db.execSQL(sql, new Object[]{user.getUserType(),user.getUserName(),user.getPassWord(),user.getRealName(),
		user.getGrade(),user.getStudentId(),user.getHeadImage(),user.getLastLoginTime()});
		db.close();
		
	}




	public   void del(User user){
		String sql="delete from user where userId=?";
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		db.execSQL(sql, new Object[]{user.getUserId()});
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
/*String sql="insert into user (userId,userType, userName ,passWord ,realName," +
				" grade, studentId,mobilePhone, emailAddress,headImage, lastLoginTime) values(?,?,?,?,?,?,?,?,?,??)";*/
	public  int updateAll(User user){
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		String sql="update  user set userType=? ,userName=?,passWord=? ,realName=?,grade=?," +
				"studentId=?,mobilePhone=?,emailAddress=?,headImage=? where userId=?";
		db.execSQL(sql, new Object[]{user.getUserType(),user.getUserName(),user.getPassWord(),user.getRealName(),
				user.getGrade(),user.getStudentId(),user.getHeadImage()});
		ContentValues values=new ContentValues();
		values.put("userType", user.getUserType());
		values.put("userName", user.getUserName());
		values.put("passWord", user.getPassWord());
		values.put("realName", user.getRealName());
		values.put("grade", user.getGrade());
		values.put("studentId", user.getStudentId());
		values.put("mobilePhone", user.getMobilePhone());
		values.put("emailAddress", user.getEmailAddress());
		values.put("headImage", user.getHeadImage());
		String[] whereArgs=new String[]{user.getUserId()+""};
		int f=db.update("user", values, "userId=?", whereArgs);
		db.close();
		return f;
	}

	public  int updateUserName(User user,String newName){
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		String sql="update  user set userName=? where userId=?";
		db.execSQL(sql, new Object[]{newName});
		ContentValues values=new ContentValues();
		values.put("userName", newName);
		String[] whereArgs=new String[]{user.getUserId()+""};
		int f=db.update("user", values, "userId=?", whereArgs);
		db.close();
		return f;
	}
	public  int updateUserPassword(User user,String newPwd){
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		String sql="update  user set lastLoginTime=? where userId=?";
		db.execSQL(sql, new Object[]{newPwd});
		ContentValues values=new ContentValues();
		values.put("lastLoginTime",newPwd);
		String[] whereArgs=new String[]{user.getUserId()+""};
		int f=db.update("user", values, "userId=?", whereArgs);
		db.close();
		return f;
	}
	public  int updateUseLastLoginTime(User user,long time){
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		String sql="update  user set passWord=? where userId=?";
		db.execSQL(sql, new Object[]{time});
		ContentValues values=new ContentValues();
		values.put("passWord",time);
		String[] whereArgs=new String[]{user.getUserId()+""};
		int f=db.update("user", values, "userId=?", whereArgs);
		db.close();
		return f;
	}
	public  int updateUserHeadImg(User user,String path){
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		String sql="update  user set headImage=? where userId=?";
		db.execSQL(sql, new Object[]{path});
		ContentValues values=new ContentValues();
		values.put("headImage",path);
		String[] whereArgs=new String[]{user.getUserId()+""};
		int f=db.update("user", values, "userId=?", whereArgs);
		db.close();
		return f;
	}

	/*
	* userid,userType,realnae*/
	public  int updateUserInfo(User user,int userId){
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		String sql="update  user set userType=? ,userName=?,passWord=? ,realName=?,grade=?," +
				"studentId=?,mobilePhone=?,emailAddress=?,headImage=? where userId=?";
		db.execSQL(sql, new Object[]{user.getUserType(),user.getUserName(),user.getPassWord(),user.getRealName(),
				user.getGrade(),user.getStudentId(),user.getHeadImage()});
		ContentValues values=new ContentValues();
		values.put("userType", user.getUserType());
		values.put("userName", user.getUserName());
		values.put("passWord", user.getPassWord());
		values.put("realName", user.getRealName());
		values.put("grade", user.getGrade());
		values.put("studentId", user.getStudentId());
		values.put("mobilePhone", user.getMobilePhone());
		values.put("emailAddress", user.getEmailAddress());
		values.put("headImage", user.getHeadImage());
		String[] whereArgs=new String[]{userId+""};
		int f=db.update("user", values, "userId=?", whereArgs);
		db.close();
		return f;
	}
	/*public  void update(TestBank note){
		SQLiteDatabase db=dbUtils.getWritableDatabase();
		String sql="update  notes set title=?,date=?,content=? where id=?";
		db.execSQL(sql, new Object[]{note.getTitle(),note.getDate(),note.getContent(),note.getId()});

		db.close();
	}*/
	
	public List<User> selectUserByUserId(int id){
		User user;
		List<User> userList=new ArrayList<>();
		String sql="select * from user where userId=? ";
		SQLiteDatabase db=dbUtils.getReadableDatabase();
		Cursor cursor=db.rawQuery(sql, new String[]{id+""});
		while (cursor.moveToNext()) {

			/*	String sql="insert into user (userId,userType, userName ,passWord ,realName," +
				" grade, studentId, headImage, lastLoginTime) values(?,?,?,?,?,?,?,?,?)";*/
			int userId=cursor.getInt(cursor.getColumnIndex("userId"));
			String userType=cursor.getString(cursor.getColumnIndex("userType"));
			String userName=cursor.getString(cursor.getColumnIndex("userName"));
			String passWord=cursor.getString(cursor.getColumnIndex("passWord"));
			String realName=cursor.getString(cursor.getColumnIndex("realName"));
			String grade=cursor.getString(cursor.getColumnIndex("grade"));
			String studentId=cursor.getString(cursor.getColumnIndex("studentId"));
			String headImage=cursor.getString(cursor.getColumnIndex("headImage"));
			String mobilePhone=cursor.getString(cursor.getColumnIndex("mobilePhone"));
			String emailAddress=cursor.getString(cursor.getColumnIndex("emailAddress"));

			long lastLoginTime=cursor.getLong(cursor.getColumnIndex("lastLoginTime"));


			LogUtils.i("数据库里titke="+userName);
			user=new User(userId, userType, userName,passWord,realName, grade ,studentId,mobilePhone,
					emailAddress,headImage,lastLoginTime);
			userList.add(user);
		}
		cursor.close();
		db.close();
		LogUtils.i(userList.size()+"大小");
		return userList;
		
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
