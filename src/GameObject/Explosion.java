package GameObject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

// ��ը
public class Explosion extends FlyingObject {
	Rect r1=null, r2;
	int step = 0;
	public Boolean isStart = false;

	public Explosion(Context context, int resuorceId) {
		super(context, resuorceId);
		// TODO Auto-generated constructor stub
		width = bitmap.getWidth() / 8;
		r2 = new Rect(0,-100,100,0); // ��ʼ������Ļ��
	}

	public void draw(Canvas cv, Paint pt) {
		cv.drawBitmap(bitmap, r1, r2, pt);
	}
	// ��ȡ��ը�������
	public void setExplosionPosition(EnemyPlane obj) {
		r2 = new Rect(obj.posX, obj.posY, obj.posX + width, obj.posY + height);
		isStart = true;
	}
	// ��ѭ������û�����֡��ը����
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
