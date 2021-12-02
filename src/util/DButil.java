package util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DButil extends SQLiteOpenHelper {
	// 表名
	public static final String TB_NAME = "user";

	public DButil(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	// 创建数据库时执行
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// 当表不存在时，创建表
		db.execSQL("create table if not exists "
				+ TB_NAME
				+ "(id integer primary key autoincrement,name text not null,psw text not null, score integer)");
	}

	// 数据库版本更新时执行
	@Override
	public void onUpgrade(SQLiteDatabase db, int ondVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists" + TB_NAME);
		onCreate(db);
	}

}
