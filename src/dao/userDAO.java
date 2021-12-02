package dao;

import java.util.ArrayList;
import bean.User;
import util.DButil;
import util.MD5Utils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class userDAO {
	private SQLiteDatabase myDB;
	private SQLiteOpenHelper dbHelper;

	public userDAO(Context context) {
		dbHelper = new DButil(context, "mygame.db", null, 1);
		// 获取数据库对象
		try {
			myDB = dbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			myDB = dbHelper.getReadableDatabase();
		}
	}

	public void close() {
		if (myDB != null) {
			myDB.close();
			myDB = null;
		}
	}

	// 查询分数前十名的用户
	public ArrayList<User> queryAllUserInfo() {
		ArrayList<User> list = new ArrayList<User>();
		Cursor cursor = myDB.rawQuery(
				"select * from user order by score desc limit 10", null);
		while (cursor.moveToNext()) {
			User user = new User(cursor.getInt(0), cursor.getString(1),
					cursor.getString(2), cursor.getInt(3));
			list.add(user);
		}
		return list;
	}

	// 查询用户名是否存在
	public int checkUsername(String username) {
		Cursor c = myDB.rawQuery("select * from user where name=?",
				new String[] { username });
		if (c.getCount() == 0)
			return 0;
		else
			return 1;
	}

	// 登录验证
	public int checkUser(String username, String password) {
		String psw = null;
		Cursor c = myDB.rawQuery("select * from user where name=?",
				new String[] { username });
		// 获取数据库里的密码
		if (c.moveToNext())
			psw = c.getString(2);
		// 两者进行比较
		if (MD5Utils.valid(password, psw))
			return 1;
		else
			return 0;
	}

	// 注册添加用户
	public int addUser(String username, String password) {
		// myDB.execSQL("insert into user values(null,?,?,0)", new String[] {
		// username, password });

		// 对密码进行md5加密
		password = MD5Utils.MD5Upper(password);

		ContentValues cv = new ContentValues();
		cv.put("name", username);
		cv.put("psw", password);
		cv.put("score", 0);
		long rowid = myDB.insert(DButil.TB_NAME, null, cv);
		if (rowid == -1)
			return 0;
		else
			return 1;

	}

	// 根据用户名查询用户游戏分数
	public int getScoreByUname(String username) {
		int score = 0;
		Cursor c = myDB.rawQuery("select score from user where name=?",
				new String[] { username });
		if (c.moveToNext())
			score = c.getInt(0);
		return score;
	}

	// 保存用户的最高分数
	public void updateUserScore(int score, String username) {
		myDB.execSQL("update user set score=? where name=?", new Object[] {
				score, username });
	}

	// 清空表
	public void cleanTable() {
		myDB.execSQL("drop TABLE user");
	}
}
