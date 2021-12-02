package util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DButil extends SQLiteOpenHelper {
	// ����
	public static final String TB_NAME = "user";

	public DButil(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	// �������ݿ�ʱִ��
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// ��������ʱ��������
		db.execSQL("create table if not exists "
				+ TB_NAME
				+ "(id integer primary key autoincrement,name text not null,psw text not null, score integer)");
	}

	// ���ݿ�汾����ʱִ��
	@Override
	public void onUpgrade(SQLiteDatabase db, int ondVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists" + TB_NAME);
		onCreate(db);
	}

}
