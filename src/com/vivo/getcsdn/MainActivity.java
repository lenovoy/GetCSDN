package com.vivo.getcsdn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	private static final int MSG_SUCCESS=0;
	private static final int MSG_FAILURE=1;
	
	private Button btn;
	private ImageView image;
	private TextView text;
	private Thread mThread;
	private Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_SUCCESS:
				text.setText((String)msg.obj);
//				image.setImageBitmap((Bitmap)msg.obj);
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

			String url="http://172.20.200.148:8080/Servlet/apes";
			HttpPost httpPost=new HttpPost(url);
			ArrayList<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("itemName", "Min"));
			
			HttpResponse httpResponse=null;
			
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				httpResponse=new DefaultHttpClient().execute(httpPost);
				Log.i("APES", "the return number "+httpResponse.getStatusLine().getStatusCode());
				int num=httpResponse.getStatusLine().getStatusCode();
				if(httpResponse.getStatusLine().getStatusCode()==200){
					String result=EntityUtils.toString(httpResponse.getEntity());
					mHandler.obtainMessage(MSG_SUCCESS,URLDecoder.decode(result, "utf-8")).sendToTarget();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			HttpClient hc=new DefaultHttpClient();
//			HttpGet hg=new HttpGet("http://172.20.200.148:8080/Servlet/login");
//			
//			try {
//				HttpResponse hr=hc.execute(hg);
//				InputStream in=hr.getEntity().getContent();
//				BufferedReader br=new BufferedReader(new InputStreamReader(in));
//				String str=br.readLine();
//				mHandler.obtainMessage(MSG_SUCCESS, str).sendToTarget();
//			} catch (ClientProtocolException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
//			Bitmap bm = null;			
//			try {
//				HttpResponse hr=hc.execute(hg);
//				bm=BitmapFactory.decodeStream(hr.getEntity().getContent());
//			} catch (IllegalStateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block¡®
//				mHandler.obtainMessage(MSG_FAILURE).sendToTarget();
//				return;
//			}
//			mHandler.obtainMessage(MSG_SUCCESS,bm).sendToTarget();
//			image.post(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					image.setImageBitmap(bm);
//				}
//			});
		}
	};
	

	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        image=(ImageView)findViewById(R.id.image);
        btn=(Button)findViewById(R.id.btn);
        text=(TextView)findViewById(R.id.text);
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
