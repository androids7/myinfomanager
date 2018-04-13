package cn.edu.info_manage.util;

import java.util.ArrayList;
import java.util.List;

import cn.edu.info_manage.domain.Memorandum;
import cn.edu.info_manage.domain.User;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MemorandumService {
	private DatabaseHelper dbHelper;
	public MemorandumService(Context context){
		dbHelper=new DatabaseHelper(context);
	}
	//增
	public Boolean insertMemorandum(Memorandum m){
		
		SQLiteDatabase sdb=dbHelper.getWritableDatabase();
		String sql="insert into memorandum (category,summary,description,_date,user_id) values(?,?,?,?,?)";
		Object obj[]={
						m.getCategory()
						,m.getSummary()
						,m.getDescription()
						,m.getDate()
						,m.getUserid()};
		sdb.execSQL(sql, obj);	
		return true;
	}
	//查
	public List<Memorandum> slectAllInfo(int _id){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from memorandum where user_id=? order by _date desc, category";
		Cursor query = sdb.rawQuery(sql, new String[]{_id+""});
		ArrayList<Memorandum> infos = new ArrayList<Memorandum>();
		if(query.moveToFirst()){
			do {
				int id = query.getInt(query.getColumnIndex("id"));
				int category = query.getInt(query.getColumnIndex("category"));
				String summary = query.getString(query.getColumnIndex("summary"));
				String description = query.getString(query.getColumnIndex("description"));
				String _date = query.getString(query.getColumnIndex("_date"));
				int user_id = query.getInt(query.getColumnIndex("user_id"));
				infos.add(new Memorandum(id, category, summary, description, _date, user_id));
			} while (query.moveToNext());
		}
		return infos;
	}
	//查
	public Memorandum selectInfoByID(int id){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from memorandum where id=? order by _date desc, category";
		Cursor query = sdb.rawQuery(sql, new String[]{id+""});
		
		if(query!=null && query.moveToFirst()){
			int category = query.getInt(query.getColumnIndex("category"));
			String summary = query.getString(query.getColumnIndex("summary"));
			String description = query.getString(query.getColumnIndex("description"));
			String _date = query.getString(query.getColumnIndex("_date"));
			int user_id = query.getInt(query.getColumnIndex("user_id"));
			return new Memorandum(id, category, summary, description, _date, user_id);
		}
		return null;
	}
	//删
	public boolean deleteMemorandum(int id){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.execSQL("delete from memorandum where id=?", new String[]{id+""});
		return true;
	}
	//改
	public boolean updateMemorandum(Memorandum m){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		String sql = "update memorandum set category=?,summary=?,description=?,_date=? where id=?;";
		Object obj[]={
				m.getCategory()
				,m.getSummary()
				,m.getDescription()
				,m.getDate()
				,m.getId()};
		database.execSQL(sql, obj);
		return true;
	}
	//删
	public Boolean delInfoByUserID(String userid){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.execSQL("delete from memorandum where user_id="+userid);
		
		return true;
	}
	public void closeDB(){
		dbHelper.close();
	}
	
}
