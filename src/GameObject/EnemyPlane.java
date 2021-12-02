package GameObject;

import android.content.Context;

// 敌机
public class EnemyPlane extends FlyingObject {
	Boolean isDied = false;

	public EnemyPlane(Context context, int resuorceId) {
		super(context, resuorceId);
		posY = -50; // 屏幕外生成
		speed = 10;
	}

}
