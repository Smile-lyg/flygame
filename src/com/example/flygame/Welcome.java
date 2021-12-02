package com.example.flygame;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.flaygame.R;

import bean.User;
import dao.userDAO;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Welcome extends Activity {
	private SharedPreferences sp;
	private String uname;
	private TextView tv1, tv2, logout;
	private userDAO myUserDAO;
	private ImageButton play;
	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
		uname = sp.getString("loginName", "");

		tv1 = (TextView) findViewById(R.id.info);
		tv2 = (TextView) findViewById(R.id.userinfo);
		tv2.setText("排行榜");
		logout = (TextView) findViewById(R.id.logout); // 注销
		logout.setText(Html.fromHtml("<u>" + "注销" + "</u>")); // 格式化文本，添加下划线
		lv = (ListView) findViewById(R.id.lv);
		play = (ImageButton) findViewById(R.id.play);

		tv1.setText("欢迎您：" + uname);

		myUserDAO = new userDAO(this);
		// 查询所有用户信息，列表
		ArrayList<User> userList = myUserDAO.queryAllUserInfo();
		// 排行榜数据源
		ArrayList<HashMap<String, Object>> ranks = new ArrayList<HashMap<String, Object>>();
		if (!userList.isEmpty()) {
			int i = 0;
			for (User u : userList) {
				i++;
				HashMap<String, Object> m = new HashMap<String, Object>();
				m.put("rank", i);
				m.put("name", u.getUsername());
				m.put("score", u.getScore());
				ranks.add(m);
			}
		} else {
			tv2.setText("暂无数据");
		}

		// 排行榜列表适配
		SimpleAdapter myadp = new SimpleAdapter(this, ranks,
				R.layout.rank_item, new String[] { "rank", "name", "score" },
				new int[] { R.id.ids, R.id.names, R.id.scores });
		lv.setAdapter(myadp);
		
		// 注销登录
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 清除已保存的用户名
				sp.edit().putString("loginName", "").commit();
				// 跳转回登陆界面
				Intent intent = new Intent(Welcome.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

		// 开始游戏按钮
		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Welcome.this, MyGame.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
