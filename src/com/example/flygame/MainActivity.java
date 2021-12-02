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
		// �Զ��幤�߰�
		mt = new MyUtil(this);
		myUserDAO = new userDAO(this);
		// ���� sharedPreference ����
		sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
		// ��ȡ������û��������û��Ĭ��Ϊ��
		uname = sp.getString("username", "");

		cb = (CheckBox) findViewById(R.id.remember); // ��ס�û�����ѡ��
		et1 = (EditText) findViewById(R.id.et1);// �˺�
		et2 = (EditText) findViewById(R.id.et2);// ����
		b1 = (Button) findViewById(R.id.bt1);// ��¼
		b2 = (Button) findViewById(R.id.bt2);// ע��
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);

		// ��ס�ϴι�ѡ״̬
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
		// ��ȡ������û���������
		s1 = et1.getText().toString().trim();
		s2 = et2.getText().toString().trim();

		// �ǿ��ж�
		if (s1.equals("") || s2.equals("")) {
			mt.showDialog("�������û��������룡");
		} else {
			// ��鸴ѡ��
			if (cb.isChecked()) {
				// �����ѡ��ѡ�ϣ��û������浽xml�ļ�
				sp.edit().putString("username", s1).commit();
			} else {
				// ��������ѱ����ֵ�����������û���
				sp.edit().putString("username", "").commit();
			}

			// �Ȳ����û����Ƿ����
			int r = myUserDAO.checkUsername(s1);
			// ��¼
			if (v.getId() == R.id.bt1) {
				// û�ҵ��û���
				if (r == 0) {
					mt.showDialog("�û��������ڣ�����ע�ᣡ");
				} else {
					// �û���֤
					int res = myUserDAO.checkUser(s1, s2);
					if (res == 1) {
						// �����Ѿ���¼���û���
						sp.edit().putString("loginName", s1).commit();
						mt.showToast("��¼�ɹ���");
						// ��ת
						Intent in1 = new Intent(MainActivity.this, Welcome.class);
						startActivity(in1);
						finish();
					} else {
						mt.showDialog("�û������������");
					}
				}
				et2.setText("");
			}
			// ע��
			if (v.getId() == R.id.bt2) {
				// �ҵ����û���
				if (r == 1) {
					et1.setText("");
					et2.setText("");
					mt.showDialog("���û����ѱ�ע�ᣬ���������룡");
				} else {
					int res = myUserDAO.addUser(s1, s2);
					if (res == 1)
						mt.showToast("ע��ɹ���");
					else
						mt.showToast("ע��ʧ�ܣ�");
				}

			}
		}
	}

}
