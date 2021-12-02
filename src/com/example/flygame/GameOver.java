package com.example.flygame;

import com.example.flaygame.R;

import util.MyUtil;
import dao.userDAO;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GameOver extends Activity implements OnClickListener {
	private Intent intent;
	private Button goback, again;
	private TextView name, score1, score2;
	private int nowScore = 0, maxScore = 0;
	private String uname;
	private SharedPreferences sp;
	private userDAO myDAO;
	private MyUtil mt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameover);
		name = (TextView) findViewById(R.id.uname); // �û���
		score1 = (TextView) findViewById(R.id.nowScore); // ���ֵ÷�
		score2 = (TextView) findViewById(R.id.maxScore); // ��ʷ��߷�
		goback = (Button) findViewById(R.id.goback); // �ص���ҳ
		again = (Button) findViewById(R.id.again); // ����һ��
		goback.setOnClickListener(this);
		again.setOnClickListener(this);
		
		mt = new MyUtil(this);
		myDAO = new userDAO(this);
		
		intent = getIntent();
		// ��ȡ��Ϸ���洫�ݵķ���
		nowScore = intent.getIntExtra("score", 0);
		
		sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
		// ��ȡ��¼���û���
		uname = sp.getString("loginName", "");
		// ��ȡ�û��ķ���
		maxScore = myDAO.getScoreByUname(uname);
		
		// �����û�����߷ֵ����ݿ�
		if(nowScore > maxScore){
			maxScore = nowScore;
			myDAO.updateUserScore(maxScore, uname);
		}
		
		name.setText("�û�:"+uname);
		score1.setText(nowScore + "");
		score2.setText(maxScore + "");

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getId() == R.id.goback) {
			intent = new Intent(GameOver.this, Welcome.class);

		}

		if (v.getId() == R.id.again) {
			intent = new Intent(GameOver.this, MyGame.class);

		}
		startActivity(intent);
		finish();
	}

}
