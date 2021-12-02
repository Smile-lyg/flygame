package com.example.flygame;

import java.util.ArrayList;
import java.util.Random;
import com.example.flaygame.R;
import GameObject.Background;
import GameObject.Bullet;
import GameObject.EnemyPlane;
import GameObject.Explosion;
import GameObject.HeroPlane;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyGameView extends SurfaceView implements SurfaceHolder.Callback,
		Runnable, SensorEventListener {
	// ÿ50֡ˢ��һ����Ļ
	public static final int TIME_IN_FRAME = 50;
	private SurfaceHolder mSurfaceHolder;
	// ������������
	private SensorManager mSensorManager = null;
	// ��ͼ��Canvas
	private Canvas canvas;
	private Paint paint;
	// ���̱߳�־λ
	public boolean isRun;
	private int count = 0;
	// �ֻ���Ļ���
	private int mScreenWidth = 0;
	private int mScreenHeight = 0;
	// ��������
	private Background bg1, bg2;
	// �л�����
	private EnemyPlane enemy;
	private ArrayList<EnemyPlane> enemyList = new ArrayList<EnemyPlane>();
	private int[] ufoList = new int[] { R.drawable.ufo_01, R.drawable.ufo_02,
			R.drawable.ufo_03, R.drawable.ufo_04, R.drawable.ufo_05,
			R.drawable.ufo_06, R.drawable.ufo_07, R.drawable.ufo_08,
			R.drawable.ufo_09, R.drawable.ufo_10, R.drawable.ufo_11,
			R.drawable.ufo_12 };
	// ��ҷɴ�����
	private HeroPlane myplane;
	// �ӵ�����
	private Bullet bullet;
	private ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
	// ��ը����
	private Explosion explosion;
	// ��Ч
	private SoundPool soundPool;
	private int loadId_shoot, loadId_explosion;
	public MediaPlayer music;

	public MyGameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mSurfaceHolder = getHolder();
		// ע��ص�����
		mSurfaceHolder.addCallback(this);
		// �õ�SensorManager����
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		// ���ٶȴ�����ע��listener�������������Ǽ��ľ�ȷ��
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);

		canvas = new Canvas();
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(50);

		bg1 = new Background(context, R.drawable.bg1);
		bg2 = new Background(context, R.drawable.bg2);
		myplane = new HeroPlane(context, R.drawable.plane);
		explosion = new Explosion(context, R.drawable.explosion);

		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
		loadId_shoot = soundPool.load(context, R.raw.shoot, 1); // �����Ч
		loadId_explosion = soundPool.load(context, R.raw.explosion, 1); // ��ը��Ч
		music = MediaPlayer.create(context, R.raw.music); // ��������
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRun) {
			// ȡ�ø�����Ϸ֮ǰ��ʱ��
			long startTime = System.currentTimeMillis();
			// ����������̰߳�ȫ��
			synchronized (mSurfaceHolder) {
				// �õ���ǰ���� Ȼ������
				canvas = mSurfaceHolder.lockCanvas();
				if (!myplane.isDied) {
					drawGameUI();
					logic();
				} else {
					// ������
					canvas.drawBitmap(bg1.bitmap, 0, 0, paint);
					// �������ִӻ����е㿪ʼ����
					paint.setTextAlign(Align.CENTER);
					canvas.drawText("��Ϸ�����������Ļȥ����", mScreenWidth / 2,
							mScreenHeight / 2, paint);
				}
				// ���ƽ����������ʾ����Ļ��
				mSurfaceHolder.unlockCanvasAndPost(canvas);
			}
			// ȡ�ø�����Ϸ������ʱ��
			long endTime = System.currentTimeMillis();
			// �������Ϸһ�θ��µĺ�����
			int diffTime = (int) (endTime - startTime);
			// ȷ��ÿ�θ���ʱ��Ϊ50֡
			while (diffTime <= TIME_IN_FRAME) {
				diffTime = (int) (System.currentTimeMillis() - startTime);
				// �̵߳ȴ�
				Thread.yield();
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		music.setLooping(true); // ����ѭ������
		music.start(); // ���ű�������
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		// ��ʼ��Ϸѭ���߳�
		isRun = true;
		new Thread(this).start();
		// �õ���ǰ��Ļ���
		mScreenWidth = this.getWidth();
		mScreenHeight = this.getHeight();
		initGame();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		// �����������ϱ仯��ֵ���ı�ɴ������꣬ʵ�ַɴ����ƶ�
		myplane.posX -= -event.values[1] * myplane.speed;
		myplane.posY += event.values[0] * myplane.speed;
		// �ɴ��߽����
		if (myplane.posX < 0)
			myplane.posX = 0;
		if (myplane.posX > mScreenWidth - myplane.width)
			myplane.posX = mScreenWidth - myplane.width;
		if (myplane.posY < 0)
			myplane.posY = 0;
		if (myplane.posY > mScreenHeight - myplane.width)
			myplane.posY = mScreenHeight - myplane.width;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (myplane.isDied) {
			isRun = false;
			music.stop();
			// ��ת����ҳ��
			Intent intent = new Intent(this.getContext(), GameOver.class);
			intent.putExtra("score", myplane.score);
			this.getContext().startActivity(intent);
			((Activity) this.getContext()).finish();
		} else {
			myplane.isFire = true; // �����Ļ����
			soundPool.play(loadId_shoot, 1.0f, 1.0f, 1, 0, 1f); // ���������Ч
		}
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

	public void initGame() {
		bg1.fitScreen(mScreenWidth, mScreenHeight);
		bg2.fitScreen(mScreenWidth, mScreenHeight);
		bg2.posY = -mScreenHeight;
		// ��ҷɴ�������
		myplane.posX = mScreenWidth / 2 - myplane.width;
		myplane.posY = mScreenHeight / 3 * 2;
	}

	public void drawGameUI() {
		// ������
		bg1.draw(canvas, paint);
		bg2.draw(canvas, paint);
		// ����ը
		explosion.draw(canvas, paint);
		// ���л�
		for (int i = 0; i < enemyList.size(); i++) {
			enemyList.get(i).draw(canvas, paint);
		}
		// ����ҷɴ�
		myplane.draw(canvas, paint);
		// ���ӵ�
		for (int i = 0; i < bulletList.size(); i++) {
			bulletList.get(i).draw(canvas, paint);
		}
		paint.setTextAlign(Align.LEFT);
		canvas.drawText("�÷֣�" + myplane.score, 0, 50, paint);
		canvas.drawText("������" + myplane.life, 0, 100, paint);
	}

	public void logic() {
		count++;
		if (count >= Integer.MAX_VALUE)
			count = 0;

		bg1.move();
		bg2.move();
		// �������������Ļ�ͻص����
		if (bg1.posY >= mScreenHeight)
			bg1.posY = -mScreenHeight;

		if (bg2.posY >= mScreenHeight)
			bg2.posY = -mScreenHeight;

		// ÿ��һ��ʱ�䣬������ɵл�
		if (count % 15 == 0) {
			enemy = new EnemyPlane(getContext(),
					ufoList[new Random().nextInt(ufoList.length)]); // ����ɵ�
			enemy.posX = new Random().nextInt(mScreenWidth - enemy.width); // ���x����
			enemyList.add(enemy);
		}
		for (int i = 0; i < enemyList.size(); i++) {
			enemyList.get(i).move(); // �л��ƶ�
			// �����Ƴ�
			if (enemyList.get(i).posY > mScreenHeight) 
				enemyList.remove(i);
			
			// ��ɴ�����
			if (i < enemyList.size() && enemyList.get(i).isTouch(myplane)) {
				if (myplane.life > 0) {
					myplane.life--; // �ɴ�����ֵ��һ
					explosion.setExplosionPosition(enemyList.get(i));// ��ȡ��ը��
					soundPool.play(loadId_explosion, 1.0f, 1.0f, 1, 0, 1f); // ���ű�ը��
					enemyList.remove(i); // �Ƴ��л�
				} else
					myplane.isDied = true; // ��Ϸ����
			}
		}
		// ��ҷɴ����������ӵ�
		if (myplane.isFire && count % 5 == 0) {
			myplane.isFire = false;
			bullet = new Bullet(getContext(), R.drawable.zidan);
			// �ӵ�����Ϊ��ǰ�ɻ�������
			bullet.posX = myplane.posX + myplane.width / 2 - bullet.width / 2;
			bullet.posY = myplane.posY - bullet.height / 2;
			bulletList.add(bullet);
		}

		for (int i = 0; i < bulletList.size(); i++) {
			bulletList.get(i).move(); // �ӵ��ƶ�
			// �����Ƴ�
			if (bulletList.get(i).posY < 0) 
				bulletList.remove(i);
			
			// �����л����ж��Ƿ����
			for (int j = 0; j < enemyList.size(); j++) {
				if (j < enemyList.size() && i < bulletList.size()
						&& enemyList.get(j).isTouch(bulletList.get(i))) {
					myplane.score += 100; // �ӷ�
					explosion.setExplosionPosition(enemyList.get(j)); // ��ȡ��ը��
					soundPool.play(loadId_explosion, 1.0f, 1.0f, 1, 0, 1f); // ���ű�ը��
					enemyList.remove(j);
					bulletList.remove(i);
				}
			}
		}
		// ��ը����
		if (explosion.isStart && count % 3 == 0) {
			explosion.makeExplosion();
		}
	}
}
