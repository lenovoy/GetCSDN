package com.vivo.getcsdn;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	private static final int MSG_SUCCESS=0;
	private static final int MSG_FAILURE=1;
	
	private Button btn;
	private ImageView image;
	private Thread mThread;
	private Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_SUCCESS:
				image.setImageBitmap((Bitmap)msg.obj);
				Toast.makeText(getApplication(), getApplication().getString(R.string.get_pic_success), Toast.LENGTH_LONG).show();
				break;
			case MSG_FAILURE:
				Toast.makeText(getApplication(), getApplication().getString(R.string.get_pic_failure),Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	
	Runnable runnable=new Runnable(){
		@Override
		public void run(){
			HttpClient hc=new DefaultHttpClient();
			HttpGet hg=new HttpGet("http://csdnimg.cn/www/images/csdnindex_logo.gif");
			final Bitmap bm;			
			try {
				HttpResponse hr=hc.execute(hg);
				bm=BitmapFactory.decodeStream(hr.getEntity().getContent());
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block¡®
				mHandler.obtainMessage(MSG_FAILURE).sendToTarget();
				return;
			}
//			mHandler.obtainMessage(MSG_SUCCESS,bm).sendToTarget();
			image.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					image.setImageBitmap(bm);
				}
			});
		}
	};
	

	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        image=(ImageView)findViewById(R.id.image);
        btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mThread==null){
					mThread=new Thread(runnable);
					mThread.start();
				}else{
					Toast.makeText(getApplication(), getApplication().getString(R.string.thread_started), Toast.LENGTH_LONG).show();
				}
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
