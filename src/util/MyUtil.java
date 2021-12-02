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

	// 图片缩放至指定宽高
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	// 显示对话框Dialog
	public void showDialog(String title) {
		Builder dialog = new AlertDialog.Builder(context);
		// 设置显示标题
		dialog.setTitle(title);
		// 设置图标
		dialog.setIcon(R.drawable.info);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface d, int arg1) {
				// TODO Auto-generated method stub
				// 取消弹窗
				d.cancel();
			}
		});
		dialog.show();

	}

	// 显示消息框Toast
	public void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
