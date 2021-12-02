package com.example.flygame;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class MyGame extends Activity {
	private MyGameView myview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		myview = new MyGameView(this);
		setContentView(myview);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		myview.isRun = false;
		myview.music.stop();
		finish();
	}

}
