package GameObject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

// 爆炸
public class Explosion extends FlyingObject {
	Rect r1=null, r2;
	int step = 0;
	public Boolean isStart = false;

	public Explosion(Context context, int resuorceId) {
		super(context, resuorceId);
		// TODO Auto-generated constructor stub
		width = bitmap.getWidth() / 8;
		r2 = new Rect(0,-100,100,0); // 初始画在屏幕外
	}

	public void draw(Canvas cv, Paint pt) {
		cv.drawBitmap(bitmap, r1, r2, pt);
	}
	// 获取爆炸坐标矩形
	public void setExplosionPosition(EnemyPlane obj) {
		r2 = new Rect(obj.posX, obj.posY, obj.posX + width, obj.posY + height);
		isStart = true;
	}
	// 在循环里调用绘制逐帧爆炸动画
	public void makeExplosion() {
		r1 = new Rect(step * width, 0, (step + 1) * width, height);
		if (step == 7){
			step = 0;
			isStart = false;
		}	
		else
			step++;
		
	}
}
