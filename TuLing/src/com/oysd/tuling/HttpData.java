package com.oysd.tuling;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class HttpData extends AsyncTask<String, Void, String> {

	private DefaultHttpClient mHttpClient;
	private HttpGet mHttpGet;
	private String url;
	private HttpResponse mHttpResponse;//获取Http请求的返回
	private HttpEntity mHttpEntity;//创建Http实体
	private InputStream in;//将获取的数据转换成流文件来处理
	private HttpGetDataListener listener;
	
	public HttpData(String url,HttpGetDataListener listener){
		this.url = url;
		this.listener = listener;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpGet = new HttpGet(url);
			mHttpResponse = mHttpClient.execute(mHttpGet);//通过客户端来进行发送
			mHttpEntity = mHttpResponse.getEntity();//通过Response对象获取数据
			in = mHttpEntity.getContent();//通过Http的实体来获取具体的内容
			//通过缓存区进行读取
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while((line = br.readLine()) != null){
				sb.append(line);
			}	
			return sb.toString();
			
		}catch(Exception e){
			e.printStackTrace();
			
		}
		
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		listener.getDataUrl(result);
		super.onPostExecute(result);
	}
	
	
}
