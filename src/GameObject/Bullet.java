package GameObject;

import android.content.Context;

// ×Óµ¯
public class Bullet extends FlyingObject {

	public Bullet(Context context, int resuorceId) {
		super(context, resuorceId);
		// TODO Auto-generated constructor stub
		speed = 15;
	}
	public void move() {
		posY -= speed;
	}
	
}
