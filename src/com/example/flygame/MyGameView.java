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
	// 每50帧刷新一次屏幕
	public static final int TIME_IN_FRAME = 50;
	private SurfaceHolder mSurfaceHolder;
	// 传感器管理器
	private SensorManager mSensorManager = null;
	// 绘图的Canvas
	private Canvas canvas;
	private Paint paint;
	// 子线程标志位
	public boolean isRun;
	private int count = 0;
	// 手机屏幕宽高
	private int mScreenWidth = 0;
	private int mScreenHeight = 0;
	// 背景对象
	private Background bg1, bg2;
	// 敌机对象
	private EnemyPlane enemy;
	private ArrayList<EnemyPlane> enemyList = new ArrayList<EnemyPlane>();
	private int[] ufoList = new int[] { R.drawable.ufo_01, R.drawable.ufo_02,
			R.drawable.ufo_03, R.drawable.ufo_04, R.drawable.ufo_05,
			R.drawable.ufo_06, R.drawable.ufo_07, R.drawable.ufo_08,
			R.drawable.ufo_09, R.drawable.ufo_10, R.drawable.ufo_11,
			R.drawable.ufo_12 };
	// 玩家飞船对象
	private HeroPlane myplane;
	// 子弹对象
	private Bullet bullet;
	private ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
	// 爆炸对象
	private Explosion explosion;
	// 音效
	private SoundPool soundPool;
	private int loadId_shoot, loadId_explosion;
	public MediaPlayer music;

	public MyGameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mSurfaceHolder = getHolder();
		// 注册回调方法
		mSurfaceHolder.addCallback(this);
		// 得到SensorManager对象
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		// 加速度传感器注册listener，第三个参数是检测的精确度
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
		loadId_shoot = soundPool.load(context, R.raw.shoot, 1); // 射击音效
		loadId_explosion = soundPool.load(context, R.raw.explosion, 1); // 爆炸音效
		music = MediaPlayer.create(context, R.raw.music); // 背景音乐
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRun) {
			// 取得更新游戏之前的时间
			long startTime = System.currentTimeMillis();
			// 在这里加上线程安全锁
			synchronized (mSurfaceHolder) {
				// 拿到当前画布 然后锁定
				canvas = mSurfaceHolder.lockCanvas();
				if (!myplane.isDied) {
					drawGameUI();
					logic();
				} else {
					// 画背景
					canvas.drawBitmap(bg1.bitmap, 0, 0, paint);
					// 设置文字从基线中点开始绘制
					paint.setTextAlign(Align.CENTER);
					canvas.drawText("游戏结束，点击屏幕去结算", mScreenWidth / 2,
							mScreenHeight / 2, paint);
				}
				// 绘制结束后解锁显示在屏幕上
				mSurfaceHolder.unlockCanvasAndPost(canvas);
			}
			// 取得更新游戏结束的时间
			long endTime = System.currentTimeMillis();
			// 计算出游戏一次更新的毫秒数
			int diffTime = (int) (endTime - startTime);
			// 确保每次更新时间为50帧
			while (diffTime <= TIME_IN_FRAME) {
				diffTime = (int) (System.currentTimeMillis() - startTime);
				// 线程等待
				Thread.yield();
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		music.setLooping(true); // 设置循环播放
		music.start(); // 播放背景音乐
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		// 开始游戏循环线程
		isRun = true;
		new Thread(this).start();
		// 得到当前屏幕宽高
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
		// 过传感器不断变化的值，改变飞船的坐标，实现飞船的移动
		myplane.posX -= -event.values[1] * myplane.speed;
		myplane.posY += event.values[0] * myplane.speed;
		// 飞船边界控制
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
			// 跳转结算页面
			Intent intent = new Intent(this.getContext(), GameOver.class);
			intent.putExtra("score", myplane.score);
			this.getContext().startActivity(intent);
			((Activity) this.getContext()).finish();
		} else {
			myplane.isFire = true; // 点击屏幕开火
			soundPool.play(loadId_shoot, 1.0f, 1.0f, 1, 0, 1f); // 播放射击音效
		}
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

	public void initGame() {
		bg1.fitScreen(mScreenWidth, mScreenHeight);
		bg2.fitScreen(mScreenWidth, mScreenHeight);
		bg2.posY = -mScreenHeight;
		// 玩家飞船出生点
		myplane.posX = mScreenWidth / 2 - myplane.width;
		myplane.posY = mScreenHeight / 3 * 2;
	}

	public void drawGameUI() {
		// 画背景
		bg1.draw(canvas, paint);
		bg2.draw(canvas, paint);
		// 画爆炸
		explosion.draw(canvas, paint);
		// 画敌机
		for (int i = 0; i < enemyList.size(); i++) {
			enemyList.get(i).draw(canvas, paint);
		}
		// 画玩家飞船
		myplane.draw(canvas, paint);
		// 画子弹
		for (int i = 0; i < bulletList.size(); i++) {
			bulletList.get(i).draw(canvas, paint);
		}
		paint.setTextAlign(Align.LEFT);
		canvas.drawText("得分：" + myplane.score, 0, 50, paint);
		canvas.drawText("生命：" + myplane.life, 0, 100, paint);
	}

	public void logic() {
		count++;
		if (count >= Integer.MAX_VALUE)
			count = 0;

		bg1.move();
		bg2.move();
		// 背景如果超出屏幕就回到最顶部
		if (bg1.posY >= mScreenHeight)
			bg1.posY = -mScreenHeight;

		if (bg2.posY >= mScreenHeight)
			bg2.posY = -mScreenHeight;

		// 每隔一段时间，随机生成敌机
		if (count % 15 == 0) {
			enemy = new EnemyPlane(getContext(),
					ufoList[new Random().nextInt(ufoList.length)]); // 随机飞碟
			enemy.posX = new Random().nextInt(mScreenWidth - enemy.width); // 随机x坐标
			enemyList.add(enemy);
		}
		for (int i = 0; i < enemyList.size(); i++) {
			enemyList.get(i).move(); // 敌机移动
			// 出界移除
			if (enemyList.get(i).posY > mScreenHeight) 
				enemyList.remove(i);
			
			// 与飞船碰触
			if (i < enemyList.size() && enemyList.get(i).isTouch(myplane)) {
				if (myplane.life > 0) {
					myplane.life--; // 飞船生命值减一
					explosion.setExplosionPosition(enemyList.get(i));// 获取爆炸点
					soundPool.play(loadId_explosion, 1.0f, 1.0f, 1, 0, 1f); // 播放爆炸声
					enemyList.remove(i); // 移除敌机
				} else
					myplane.isDied = true; // 游戏结束
			}
		}
		// 玩家飞船开火，生成子弹
		if (myplane.isFire && count % 5 == 0) {
			myplane.isFire = false;
			bullet = new Bullet(getContext(), R.drawable.zidan);
			// 子弹坐标为当前飞机的坐标
			bullet.posX = myplane.posX + myplane.width / 2 - bullet.width / 2;
			bullet.posY = myplane.posY - bullet.height / 2;
			bulletList.add(bullet);
		}

		for (int i = 0; i < bulletList.size(); i++) {
			bulletList.get(i).move(); // 子弹移动
			// 出界移除
			if (bulletList.get(i).posY < 0) 
				bulletList.remove(i);
			
			// 遍历敌机，判断是否击中
			for (int j = 0; j < enemyList.size(); j++) {
				if (j < enemyList.size() && i < bulletList.size()
						&& enemyList.get(j).isTouch(bulletList.get(i))) {
					myplane.score += 100; // 加分
					explosion.setExplosionPosition(enemyList.get(j)); // 获取爆炸点
					soundPool.play(loadId_explosion, 1.0f, 1.0f, 1, 0, 1f); // 播放爆炸声
					enemyList.remove(j);
					bulletList.remove(i);
				}
			}
		}
		// 爆炸动画
		if (explosion.isStart && count % 3 == 0) {
			explosion.makeExplosion();
		}
	}
}
