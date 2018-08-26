package com.oysd.tuling;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity implements HttpGetDataListener, OnClickListener{

	private static final String URL = "http://www.tuling123.com/openapi/api?";
	private static final String KEY = "ae3ecf47d74a8904cc7e527eece961e6";
	private static final String TAG = "TuLingMain";
	//String info = "深圳今天天气";
	private HttpData httpData;
	private List<ListData> lists;
	private ListView lv;
	private EditText sendText;
	private Button btnSend;
	private TextAdapter adapter;
	private String [] welcome_array;
	private String content_str;
	private double currentTime,oldTime = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		//String url = getUrl(info);
	}
	
	private void initView(){
		//Log.d(TAG,getTime());
		lv = (ListView) findViewById(R.id.lv);
		sendText = (EditText) findViewById(R.id.sendText);
		btnSend = (Button) findViewById(R.id.btnSend);
		lists = new ArrayList<ListData>();
		btnSend.setOnClickListener(this);
		adapter = new TextAdapter(lists, this);//承接上下文
		lv.setAdapter(adapter);
		ListData listData;
		listData = new ListData(getRondomWelcomeTips(), ListData.REVEIVER,getTime());
		lists.add(listData);
	}
	
	private String getUrl(String msg){
		String str = URL + "key=" + KEY + "&info=" + msg;
		return str;
	}

	private String getRondomWelcomeTips(){
		String welcome_tip = null;
		welcome_array = this.getResources().getStringArray(R.array.welcome_tips);
		int index = (int) (Math.random()*(welcome_array.length - 1));
		welcome_tip = welcome_array[index];
		return welcome_tip;
	}
	@Override
	public void getDataUrl(String data) {
		// TODO Auto-generated method stub
		parseText(data);
	}
	
	private void parseText(String str){
		
		try {
			JSONObject jb = new JSONObject(str);
			ListData listData;
			listData = new ListData(jb.getString("text"),ListData.REVEIVER,getTime());
			lists.add(listData);	
			adapter.notifyDataSetChanged();//重新适配
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onClick(View arg0) { 
		//getTime();
		content_str = sendText.getText().toString();
		sendText.setText("");//发送之后，将输入框清空
		//点击的时候需要添加重新适配的代码
		String doTrim = content_str.replace(" ", "");//处理空格的问题
		String doEnter = doTrim.replace("\n", "");//处理回车的问题
		ListData listData;
		listData = new ListData(content_str, ListData.SEND,getTime());
		lists.add(listData);
		if(lists.size() > 30){
			for(int i = 0 ;i < lists.size() -20 ; i++){
				lists.remove(i);
			}
		}
		adapter.notifyDataSetChanged( );//不要忘记刷新
		
		httpData = (HttpData) new HttpData(getUrl(content_str), this).execute();
		
	}
	
	private String getTime(){
		currentTime = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date curDate = new Date();
		String str = format.format(curDate);
		int sjc = (int) (currentTime - oldTime);
		Log.d(TAG, "" + sjc);
		
		if((currentTime - oldTime) >= 5*1000*60){
			oldTime = currentTime;
			return str;
		}else{
			return "";
		}
	}
}
