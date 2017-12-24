package cc.icoc.javaxu.englishtranslate.kaoyan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import cc.icoc.javaxu.bean.BookList;
import cc.icoc.javaxu.bean.WordList;
import cc.icoc.javaxu.business.OperationOfBooks;
import cc.icoc.javaxu.database.DataAccess;
import cc.icoc.javaxu.database.SqlHelper;
import cc.icoc.javaxu.englishtranslate.view.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Main extends Activity implements OnClickListener {

	public  static String SETTING_BOOKID="bookID";
	public  static String BOOKNAME = "BOOKNAME";
	private Spinner pickBook;
	private TextView info;
	private ImageButton deleteBu;
	private ImageButton resetBu;
	private Button learnBu;
	private Button reviewBu;
	private Button testBu;
	private Button attentionBu;
	private ProgressBar learn_progress;
	private ProgressBar review_progress;
	private TextView learn_text;
	private TextView review_text;
	public static final int MENU_SETTING = 1;
	public static final int MENU_ABOUT = MENU_SETTING+1;
	public static final int MENU_CONTACT = MENU_SETTING+2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		
		OperationOfBooks OOB = new OperationOfBooks();
		SharedPreferences setting = getSharedPreferences("wordroid.model_preferences", MODE_PRIVATE);
		OOB.setNotify(setting.getString("time", "18:00 下午"),Main.this);
		 File dir = new File("data/data/cc.icoc.javaxu.englishtranslate.view/databases"); 
	       
	     if (!dir.exists()) 
	            dir.mkdir(); 
		 if (!(new File(SqlHelper.DB_NAME)).exists()) { 	         
	            FileOutputStream fos;
				try {
					fos = new FileOutputStream(SqlHelper.DB_NAME);
				
	            byte[] buffer = new byte[8192]; 
	            int count = 0; 
	            InputStream is = getResources().openRawResource( 
	                    R.raw.wordorid); 
	            while ((count = is.read(buffer)) > 0) { 
	                fos.write(buffer, 0, count); 
	            }

	            fos.close(); 
	            is.close(); 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	        }
	    SharedPreferences settings=getSharedPreferences(SETTING_BOOKID, 0);
		DataAccess.bookID=settings.getString(BOOKNAME, "");
		OOB.UpdateListInfo(Main.this);
		initWidgets();
	}
	
	private void initWidgets() {
		this.deleteBu=(ImageButton) findViewById(R.id.delete);
		deleteBu.setOnClickListener(this);
		this.info=(TextView) findViewById(R.id.bookinfo);
		this.learnBu=(Button) findViewById(R.id.learn);
		learnBu.setOnClickListener(this);
		this.pickBook=(Spinner)findViewById(R.id.pickBook);
		this.resetBu=(ImageButton)findViewById(R.id.reset);
		resetBu.setOnClickListener(this);
		this.reviewBu=(Button)findViewById(R.id.review);
		reviewBu.setOnClickListener(this);
		this.testBu=(Button)findViewById(R.id.test);
		testBu.setOnClickListener(this);
		this.attentionBu=(Button) findViewById(R.id.attention);
		attentionBu.setOnClickListener(this);
		this.learn_progress= (ProgressBar)findViewById(R.id.learn_progress);
		this.review_progress= (ProgressBar)findViewById(R.id.review_progress);
		this.review_text=(TextView)findViewById(R.id.review_text);
		this.learn_text=(TextView)findViewById(R.id.learn_text);
		DisplayMetrics dm = new DisplayMetrics(); 
		   dm = getApplicationContext().getResources().getDisplayMetrics(); 
		   int screenWidth = dm.widthPixels; 
		   int padding = (screenWidth-200);
		   this.learnBu.setPadding(padding/5, 0, padding/10, 0);
		   this.resetBu.setPadding(padding/10, 0, padding/10, 0);
		   this.testBu.setPadding(padding/10, 0, padding/10, 0);
		   this.attentionBu.setPadding(padding/10, 0, padding/5, 0);
		initSpinner();
	}

	protected void onResume() {
		// TODO Auto-generated method stub
	//	Log.i("In Resume", DataAccess.bookID);
		this.initSpinner();
		super.onResume();
	}

	private void initSpinner() {
		DataAccess data = new DataAccess(this);
		final ArrayList<BookList> bookList = data.QueryBook(null, null);
		//Log.i("size", String.valueOf(bookList.size()));
		String[] books = new String[bookList.size()+1];
		int i=0;
		for (;i<bookList.size();i++){
			books[i]=bookList.get(i).getName();
		}
		books[i]="导入新词库";
		ArrayAdapter< CharSequence > adapter = new ArrayAdapter< CharSequence >(this, android.R.layout.simple_spinner_item, books);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pickBook.setAdapter(adapter);
		pickBook.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2<bookList.size()){
					DataAccess.bookID=bookList.get(arg2).getID();
					info.setText("\n词库名称:\n    "+bookList.get(arg2).getName()+"\n总词汇量:\n    "+bookList.get(arg2).getNumOfWord()+"\n创建时间: \n    "+bookList.get(arg2).getGenerateTime());
					deleteBu.setEnabled(true);
					learnBu.setEnabled(true);
					resetBu.setEnabled(true);
					reviewBu.setEnabled(true);
					testBu.setEnabled(true);
					DataAccess data2 = new DataAccess(Main.this);
					ArrayList<WordList> lists = data2.QueryList("BOOKID ='"+DataAccess.bookID+"'", null);
					learn_progress.setMax(lists.size());
					review_progress.setMax(lists.size());
					learn_progress.setProgress(0);
					review_progress.setProgress(0);
					review_progress.setSecondaryProgress(0);
					int learned=0,reviewed=0;
					for (int k=0;k<lists.size();k++){
						if (lists.get(k).getLearned().equals("1")){
							learn_progress.incrementProgressBy(1);
							learned++;
						}
						
						if (Integer.parseInt(lists.get(k).getReview_times())>=5){
							review_progress.incrementProgressBy(1);
							reviewed++;
						}
						
						if (Integer.parseInt(lists.get(k).getReview_times())>0)
						review_progress.incrementSecondaryProgressBy(1);
						review_text.setText("已复习"+reviewed+"/"+lists.size());
						learn_text.setText("已学习"+learned+"/"+lists.size());
					}
				}
				else if(arg2==bookList.size()){
					Intent intent = new Intent();
					intent.setClass(Main.this, ImportBook.class);
					startActivity(intent);
				}
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}			
		});
		if (bookList.size()==0) {
			pickBook.setSelection(1);
			info.setText("请先从上方选择一个词库！");
			this.deleteBu.setEnabled(false);
			this.learnBu.setEnabled(false);
			this.resetBu.setEnabled(false);
			this.reviewBu.setEnabled(false);
			this.testBu.setEnabled(false);
			this.learn_progress.setProgress(0);
			this.review_progress.setProgress(0);
			return;
		}
		int j=0;
		Log.i("BookID", DataAccess.bookID);
		for (;j<bookList.size();j++){
			if (DataAccess.bookID.equals(bookList.get(j).getID())){
				pickBook.setSelection(j);	
				break;
			}				
		}
	}
	
	protected void onDestroy() {
		SharedPreferences settings=getSharedPreferences(SETTING_BOOKID, 0);
		settings.edit()
		.putString(BOOKNAME, DataAccess.bookID)
		.commit();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (v==reviewBu){
			Intent intent = new Intent();
			intent.setClass(Main.this, ReviewMain.class);
			this.startActivity(intent);
		}
		if (v==testBu){
			Intent intent = new Intent();
			intent.setClass(Main.this, TestList.class);
			this.startActivity(intent);
		}
		if (v==deleteBu){
			Dialog dialog = new AlertDialog.Builder(this)
            .setIcon(R.drawable.dialog_icon)
            .setTitle("删除当前词库")
            .setMessage("确定要将这个词库删除吗？")
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	DataAccess data = new DataAccess(Main.this);
                	data.DeleteBook();
                	DataAccess.bookID="";
                	Toast.makeText(Main.this, "该词库已删除", Toast.LENGTH_SHORT).show();
                	initSpinner();
                }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                   }
            }).create();
			dialog.show();
		}
		if (v==this.resetBu){
			Dialog dialog = new AlertDialog.Builder(this)
            .setIcon(R.drawable.dialog_icon)
            .setTitle("重置当前词库")
            .setMessage("确定要将这个词库重置吗？它将失去所有学习记录")
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	DataAccess data = new DataAccess(Main.this);
                	data.ResetBook();
                	Toast.makeText(Main.this, "该词库已被重置", Toast.LENGTH_SHORT).show();
                	initSpinner();
                }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                   }
            }).create();
			dialog.show();
		}
		if (v==this.attentionBu){
			Intent intent = new Intent();
			intent.setClass(Main.this, Attention.class);
			startActivity(intent);
		}
		if (v==learnBu){
			Intent intent = new Intent();
			intent.setClass(Main.this, study.class);
			this.startActivity(intent);
		}
	}
}
