package com.example.flygame;

import com.example.flaygame.R;

import dao.userDAO;
import util.MyUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
	private EditText et1, et2;
	private Button b1, b2;
	private String s1, s2, uname;
	private MyUtil mt;
	private userDAO myUserDAO;
	private SharedPreferences sp;
	private CheckBox cb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 自定义工具包
		mt = new MyUtil(this);
		myUserDAO = new userDAO(this);
		// 创建 sharedPreference 对象
		sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
		// 获取储存的用户名，如果没有默认为空
		uname = sp.getString("username", "");

		cb = (CheckBox) findViewById(R.id.remember); // 记住用户名复选框
		et1 = (EditText) findViewById(R.id.et1);// 账号
		et2 = (EditText) findViewById(R.id.et2);// 密码
		b1 = (Button) findViewById(R.id.bt1);// 登录
		b2 = (Button) findViewById(R.id.bt2);// 注册
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);

		// 记住上次勾选状态
		if (uname.length() != 0) {
			cb.setChecked(true);
			et1.setText(uname);
		} else {
			cb.setChecked(false);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 获取输入的用户名、密码
		s1 = et1.getText().toString().trim();
		s2 = et2.getText().toString().trim();

		// 非空判断
		if (s1.equals("") || s2.equals("")) {
			mt.showDialog("请输入用户名和密码！");
		} else {
			// 检查复选框
			if (cb.isChecked()) {
				// 如果复选框勾选上，用户名保存到xml文件
				sp.edit().putString("username", s1).commit();
			} else {
				// 否则，清空已保存的值，即不保存用户名
				sp.edit().putString("username", "").commit();
			}

			// 先查找用户名是否存在
			int r = myUserDAO.checkUsername(s1);
			// 登录
			if (v.getId() == R.id.bt1) {
				// 没找到用户名
				if (r == 0) {
					mt.showDialog("用户名不存在，请先注册！");
				} else {
					// 用户验证
					int res = myUserDAO.checkUser(s1, s2);
					if (res == 1) {
						// 保存已经登录的用户名
						sp.edit().putString("loginName", s1).commit();
						mt.showToast("登录成功！");
						// 跳转
						Intent in1 = new Intent(MainActivity.this, Welcome.class);
						startActivity(in1);
						finish();
					} else {
						mt.showDialog("用户名或密码错误！");
					}
				}
				et2.setText("");
			}
			// 注册
			if (v.getId() == R.id.bt2) {
				// 找到了用户名
				if (r == 1) {
					et1.setText("");
					et2.setText("");
					mt.showDialog("该用户名已被注册，请重新输入！");
				} else {
					int res = myUserDAO.addUser(s1, s2);
					if (res == 1)
						mt.showToast("注册成功！");
					else
						mt.showToast("注册失败！");
				}

			}
		}
	}

}
