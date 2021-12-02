package util;

import com.example.flaygame.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.widget.Toast;

public class MyUtil {
	private Context context;

	public MyUtil(Context cont) {
		this.context = cont;
	}

	// ͼƬ������ָ�����
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// ���ͼƬ�Ŀ��
		int width = bm.getWidth();
		int height = bm.getHeight();
		// �������ű���
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// ȡ����Ҫ���ŵ�matrix����
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// �õ��µ�ͼƬ
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	// ��ʾ�Ի���Dialog
	public void showDialog(String title) {
		Builder dialog = new AlertDialog.Builder(context);
		// ������ʾ����
		dialog.setTitle(title);
		// ����ͼ��
		dialog.setIcon(R.drawable.info);
		dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface d, int arg1) {
				// TODO Auto-generated method stub
				// ȡ������
				d.cancel();
			}
		});
		dialog.show();

	}

	// ��ʾ��Ϣ��Toast
	public void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
