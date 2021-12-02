package GameObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class FlyingObject {
	// 坐标
	public int posX = 0, posY = 0;
	// 宽高
	public int width, height;
	// 运动速度
	public int speed;
	// 资源文件
	public Bitmap bitmap;

	public FlyingObject(Context context, int resuorceId) {
		bitmap = BitmapFactory.decodeResource(context.getResources(),
				resuorceId);
		width = bitmap.getWidth();
		height = bitmap.getHeight();
	}

	public void draw(Canvas cv, Paint pt) {
		cv.drawBitmap(bitmap, posX, posY, pt);
	}

	public void move() {
		posY += speed;
	}

	public Boolean isTouch(FlyingObject obj) {
		// 判断两个矩形是否相交
		// 分别比较两个矩形重心在X轴方向、Y轴方向的距离，与两个矩形的长、宽的一半的和的大小，小于则相交
		int zx = Math.abs((this.posX + this.width / 2) - (obj.posX + obj.width / 2)); // 两个矩形重心在x轴上的距离
		int x = (this.width + obj.width) / 2; // 两个矩形宽的一半和
		int zy = Math.abs((this.posY + this.height / 2) - (obj.posY + obj.height / 2)); // 两个矩形重心在y轴上的距离
		int y = (this.height + obj.height) / 2; // 两个矩形长的一半和
		if(zx <= x && zy <= y)
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		return "Object [posX=" + posX + ", posY=" + posY + ", width=" + width
				+ ", height=" + height + ", speed=" + speed + ", bitmap="
				+ bitmap + "]";
	}

}
