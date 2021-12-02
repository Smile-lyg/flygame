package GameObject;

import android.content.Context;

// Ó¢ÐÛ·É»ú
public class HeroPlane extends FlyingObject {
	public int life = 3;
	public int score = 0;
	public Boolean isFire = false;
	public Boolean isDied = false;

	public HeroPlane(Context context, int resuorceId) {
		super(context, resuorceId);
		// TODO Auto-generated constructor stub
		speed = 5;
	}

}
