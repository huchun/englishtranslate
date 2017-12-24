package cc.icoc.javaxu.englishtranslate.view;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cc.icoc.javaxu.bean.EnglishBean;
import cc.icoc.javaxu.httpsoap.WebFunction;
import cc.icoc.javaxu.util.ParseDailyEnglish;

/**
 * 英语学习----每日一句的页面
 */
public class EnglishDailyActivity extends Fragment implements OnClickListener,TextToSpeech.OnInitListener{
	Button btnSpeak,btnChange;
	EditText english;
	TextView translate;
	TextToSpeech speech;
	GestureLibrary library;
	WebFunction function;
	EnglishBean bean;
	Thread thread;
	ProgressBar progressBar;
	private int mColor = Color.WHITE;
	private final static String URL = "http://dict.hjenglish.com/rss/daily/en";
	
	public static EnglishDailyActivity newInstance() {
		EnglishDailyActivity fragment = new EnglishDailyActivity();
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		speech=new TextToSpeech(getActivity(),this);
		speech.setSpeechRate(1);
		
		handler.sendEmptyMessage(2);
		View view = inflater.inflate(R.layout.english_study, container, false);
		view.setBackgroundColor(mColor);
		
		progressBar = (ProgressBar)view.findViewById(R.id.english_daily_progress);
		translate = (TextView)view.findViewById(R.id.translate);
		btnChange = (Button)view.findViewById(R.id.btnChange);
		btnChange.setOnClickListener(this);
		btnSpeak = (Button)view.findViewById(R.id.btnSpeak);
		btnSpeak.setOnClickListener(this);
		english = (EditText)view.findViewById(R.id.everyday_one_english);
		
		function = new WebFunction();
		
        library = GestureLibraries.fromRawResource(getActivity(), R.raw.gestures);
        library.load();//加载手势�?
        
        GestureOverlayView gestureOverlayView = (GestureOverlayView)view.findViewById(R.id.myGesture);
        gestureOverlayView.addOnGesturePerformedListener(new GestureClick());

        handler.sendEmptyMessage(2);
        return view;
	}

	//按钮事件
	public void onClick(View v) 
	{
		if(v.getId() == R.id.btnSpeak)
		{
			if(thread == null)
			{
				progressBar.setVisibility(View.VISIBLE);
				thread = new Thread(runnable);
				thread.start();
			}
			speech.speak(english.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
		}
		else if(v.getId() == R.id.btnChange)
		{
			showDialog();
		}
	}
	
	public void showDialog()
	{
		AlertDialog.Builder b = new Builder(getActivity());		
		b.setIcon(R.drawable.exit_tip);
		b.setTitle("温馨提示");
		b.setMessage("陛下是否要退出应用？");
		b.setNegativeButton("我点错了", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		b.setPositiveButton("立即退出", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Process.killProcess(Process.myPid());
			}
		});
		b.show();
	}

	//初始
	public void onInit(int status) 
	{
		if(status==TextToSpeech.SUCCESS)
		{
			int result=speech.setLanguage(Locale.US);
			if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED)
			{
				Toast.makeText(getActivity(), "不支持指定语", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	
	public void onDestroy() {
		super.onDestroy();
		if(speech!=null)
		{
			speech.stop();
			speech.shutdown();
		}
	}
	
	//手势识别的处理
	class GestureClick implements OnGesturePerformedListener
	{

		public void onGesturePerformed(GestureOverlayView overlay,
				Gesture gesture) {
			ArrayList<Prediction> predictions = library.recognize(gesture);//这个集合中，匹配度越高的就越是放在最前面
			if(!predictions.isEmpty())
			{
				Prediction prediction = predictions.get(0);
				if(prediction.score > 6)
				{
					if("读".equals(prediction.name))
					{
						Toast.makeText(getActivity(), "朗读", Toast.LENGTH_SHORT).show();
						speech.speak(english.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
					}
					
					else if("换句".equals(prediction.name))
					{
						Toast.makeText(getActivity(), "换一句", Toast.LENGTH_SHORT).show();
						progressBar.setVisibility(View.VISIBLE);
						Thread thread = new Thread(runnable);
						thread.start();
					}
					
				}
				else 
				{
					Toast.makeText(getActivity(), "手势不匹配", Toast.LENGTH_SHORT).show();
				}
			}
			else 
			{
				Toast.makeText(getActivity(), "无此手势", Toast.LENGTH_SHORT).show();
			}	
			
		}
		
	}
	
	/***该线程用于获取数据****/
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try 
			{
				bean = getData();
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			handler.sendEmptyMessage(1);
		}
	};
	
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1)
			{
				if(bean != null)
				{
					english.setText(bean.getEnglish());
					translate.setText(bean.getChinese());
					speech.speak(english.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
				}
				else
				{
					Toast.makeText(getActivity(), "未获取到数据!", Toast.LENGTH_SHORT).show();
				}
				progressBar.setVisibility(View.GONE);
			}
			if(msg.what == 2)
			{
				speech.speak("Welcome to here", TextToSpeech.QUEUE_FLUSH, null);
			}
		}
	};
	

	/**
	 * 获取网络数据
	 * @return
	 */
	public EnglishBean getData()
	{
		InputStream inStr=null;
		
		try {
			URL url = new URL(URL);			
		    HttpURLConnection conn=(HttpURLConnection) url.openConnection(); 
		    conn.connect();
		    inStr=conn.getInputStream();
		} catch (Exception e) {
			Toast.makeText(getActivity(), "连接过程出现未知问题，请稍后重试", Toast.LENGTH_SHORT).show();
		}     
		
		InputStream in= inStr;
		
		 SAXParser parser = null;  
	        try {  
	            //构建SAXParser  
	            parser = SAXParserFactory.newInstance().newSAXParser();//.newInstance().newSAXParser();  
	            //实例化  DefaultHandler对象  
	            ParseDailyEnglish parseXml=new ParseDailyEnglish();  
	            //调用parse()方法  
	            parser.parse(in, parseXml);
	            
	            bean = parseXml.getBean();
	        }catch (Exception e) {
	        	e.printStackTrace();
			}
		return bean;
	}
}	
	
