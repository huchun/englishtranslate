package cc.icoc.javaxu.englishtranslate.view;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import cc.icoc.javaxu.bean.EnglishTranslateBean;
import cc.icoc.javaxu.util.SearchWords;

public class EnglishTransActivity extends Fragment implements OnClickListener,TextToSpeech.OnInitListener {
	/**
	 * 英语学习子页�?--单词翻译
	 */
	private ImageButton btn_type;
	private Button btn_tran;
	private Button btn_voice;
	private EditText et_value;
	private EditText et_result;
	TextToSpeech speech;
	ProgressBar progressBar;
	private int mColor = Color.WHITE;
	private ProgressDialog mProgressDialog = null;
	private static Boolean isTrue = false;
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 12345;
	private static final String TAG = "XU";
	EnglishTranslateBean bean;
	View view ;

	public static EnglishTransActivity newInstance() {
		EnglishTransActivity fragment = new EnglishTransActivity();
		return fragment;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.trans, container, false);
		view.setBackgroundColor(mColor);
		
		speech=new TextToSpeech(getActivity(),this);
        speech.setSpeechRate(1);

		bean = new EnglishTranslateBean();
		init();
		return view;
	}

	private void init() {
		progressBar = (ProgressBar)view.findViewById(R.id.english_translte_progress);
		btn_tran = (Button) view.findViewById(R.id.btn_tran);
		btn_tran.setOnClickListener(this);
		btn_voice = (Button) view.findViewById(R.id.btn_voice);
		btn_voice.setOnClickListener(this);
		et_value = (EditText) view.findViewById(R.id.et_value);
		et_result = (EditText) view.findViewById(R.id.et_result);

	}

//	/**
//	 * 查询网络是否连接
//	 * 
//	 * @return
//	 */
//	private Boolean CheckNet() {
//		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo info = manager.getActiveNetworkInfo();
//		if (info != null && info.isAvailable()) {
//			return true;
//		} else {
//			return false;
//		}
//	}

	
	public void onClick(View v) {
		int id = v.getId();		
		if (id == R.id.btn_tran) {
			if(et_value.getText().toString().trim().equals(""))
			{
				Toast.makeText(getActivity(), "还未输入要翻译的内容", Toast.LENGTH_SHORT).show();
			}
			else
			{
				LoadData data = new LoadData();
				data.execute("fdsadas");
			}
		}else if (id == R.id.btn_tran) {
			
		
	   }else if (id == R.id.btn_voice) {
			speech.speak(et_value.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
		}
	}
	
	public void search() 
	{
		SearchWords searchWords = new SearchWords();
		bean = searchWords.transWord(et_value.getText().toString().trim());
		System.out.println("bean="+bean.toString());
		if(bean == null)
		{
			Log.i(TAG, "空�?");
		}
		else
		{
			System.out.println("bean =  "+bean.getExplains() +"   fan"+ bean.getParagraph());
			Log.i(TAG, "取到值了"+bean.getExplains());
		}
	}
	
	//异步�?
	private class LoadData extends AsyncTask<String, Integer, Integer>
	{
		protected void onPreExecute()
		{
			progressBar.setVisibility(View.VISIBLE);
		}

		protected Integer doInBackground(String... params) {
			search();
			return 1;
		}
		
		protected void onPostExecute(Integer result)
		{
//			String s = ToDBC(bean.getExplains());
//			String s = stringFilter(bean.getExplains());
			progressBar.setVisibility(View.GONE);
			et_result.setText("\t"+bean.getParagraph()+bean.getExplains());
		}
	}
	
	public static String ToDBC(String input) {          
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {              
        if (c[i] == 12288) {                 
        c[i] = (char) 32;                  
        continue;
         }
         if (c[i] > 65280 && c[i] < 65375)
            c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }  
	
	/** * 去除特殊字符或将�?��中文标号替换为英文标�?
     * @param str
     * @return
     */
     public static String stringFilter(String str) {         
         str = str.replaceAll(" ", "");
        		 //.replaceAll("�?, "]")
                //.replaceAll("�?, "!").replaceAll("�?, ":");// 替换中文标号
         String regEx = "[『�?]"; // 清除掉特殊字�?        
         Pattern p = Pattern.compile(regEx);
         Matcher m = p.matcher(str);
         return m.replaceAll("").trim();
    }  


	public void onInit(int status) 
	{
		if(status==TextToSpeech.SUCCESS)
		{
			int result=speech.setLanguage(Locale.US);
			if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED)
			{
				Toast.makeText(getActivity(), "不支持指定语言", Toast.LENGTH_LONG).show();
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
}
