package GameObject;

import util.MyUtil;
import android.content.Context;

public class Background extends FlyingObject {

	public Background(Context context, int resuorceId) {
		super(context, resuorceId);
		speed = 5;
	}

	public void fitScreen(int screenWidth, int screenHeight) {
		bitmap = MyUtil.zoomImg(bitmap, screenWidth, screenHeight);
	}
}
