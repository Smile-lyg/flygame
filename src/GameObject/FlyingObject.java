package GameObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class FlyingObject {
	// ����
	public int posX = 0, posY = 0;
	// ���
	public int width, height;
	// �˶��ٶ�
	public int speed;
	// ��Դ�ļ�
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
		// �ж����������Ƿ��ཻ
		// �ֱ�Ƚ���������������X�᷽��Y�᷽��ľ��룬���������εĳ������һ��ĺ͵Ĵ�С��С�����ཻ
		int zx = Math.abs((this.posX + this.width / 2) - (obj.posX + obj.width / 2)); // ��������������x���ϵľ���
		int x = (this.width + obj.width) / 2; // �������ο��һ���
		int zy = Math.abs((this.posY + this.height / 2) - (obj.posY + obj.height / 2)); // ��������������y���ϵľ���
		int y = (this.height + obj.height) / 2; // �������γ���һ���
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
