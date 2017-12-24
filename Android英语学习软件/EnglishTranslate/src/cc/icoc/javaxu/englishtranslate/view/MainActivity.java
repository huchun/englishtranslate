package cc.icoc.javaxu.englishtranslate.view;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(new MyView(getApplicationContext()));
        new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(1);
			}
		}, 1600);
	}
	
    Handler handler = new Handler()
    {
    	public void handleMessage(android.os.Message msg) 
    	{
    		if(msg.what == 1)
    		{
    			Intent intent = new Intent(MainActivity.this, EnglishMainActivity.class);
    			startActivity(intent);
    			finish();
    		}
    	}
    };
	
    class MyView extends View
    {
		public MyView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sp);
			int bitmapH = bitmap.getHeight();
			int bitmpaW = bitmap.getWidth();
				
			WindowManager manage=getWindowManager();
			Display display=manage.getDefaultDisplay();
			
			int screenH = display.getHeight();
			int screenW = display.getWidth();
				
			float sx = (float) screenW / bitmpaW ; 
			float sy = (float) screenH / bitmapH ;
				
			Matrix matrix = new Matrix();
			matrix.postScale(sx, sy);
				
			Paint paint = new Paint();
			canvas.drawBitmap(bitmap, matrix, paint);
		}
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
