package GameObject;

import android.content.Context;

// �л�
public class EnemyPlane extends FlyingObject {
	Boolean isDied = false;

	public EnemyPlane(Context context, int resuorceId) {
		super(context, resuorceId);
		posY = -50; // ��Ļ������
		speed = 10;
	}

}
