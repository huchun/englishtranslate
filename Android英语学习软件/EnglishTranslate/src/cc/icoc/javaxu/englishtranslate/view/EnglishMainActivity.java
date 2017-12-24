package cc.icoc.javaxu.englishtranslate.view;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Process;
import android.widget.RelativeLayout;


/**
 * 英语学习主页面
 */
public class EnglishMainActivity extends Activity{

	RelativeLayout rlMain;
	ActionBar actionBar;
	
	    public void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main_frament_activity);
	        init();
	    }
	    

		public void init()
	    {
	    	// 得到Activity的ActionBar
	    	actionBar = getActionBar();
	        //获取ActionBar并添加背景
			actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.header_title));
			actionBar.setTitle("颖老师");
	        // 设置AcitonBar的操作模型
	        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	        // 将Activity的头部去掉
	        actionBar.setDisplayShowTitleEnabled(false);
	        // 生成Tab
	        Tab kaosNews = actionBar.newTab().setIcon(R.drawable.kaoyan);
	        Tab schoolNews = actionBar.newTab().setIcon(R.drawable.daily_english);
	        Tab departNews = actionBar.newTab().setIcon(R.drawable.translate);
	        // 为每个Tab添加Listener
	        MyTabListener kaosNewsListener = new MyTabListener(EnglishKaosActivity.newInstance());
	        kaosNews.setTabListener(kaosNewsListener);
	        
	        MyTabListener schoolNewsListener = new MyTabListener(EnglishDailyActivity.newInstance());
	        schoolNews.setTabListener(schoolNewsListener);
	        
	        MyTabListener departNewsListener = new MyTabListener(EnglishTransActivity.newInstance());
	        departNews.setTabListener(departNewsListener);
	        
	        // 将Tab加入ActionBar中
	        actionBar.addTab(kaosNews);
	        actionBar.addTab(schoolNews);
	        actionBar.addTab(departNews);
	    }
	    

	    @Override
	    protected void onStop()
	    {
	        super.onStop();
	    }

	    /**
	     * 实现ActionBar.TabListener接口
	     */
	    class MyTabListener implements TabListener
	    {
	        // 接收每个Tab对应的Fragment，操作
	        private Fragment fragment;

	        public MyTabListener(Fragment fragment)
	        {
	            this.fragment = fragment;
	        }

	        public void onTabReselected(Tab tab, FragmentTransaction ft)
	        {

	        }

	        // 当Tab被选中的时候添加对应的Fragment
	        public void onTabSelected(Tab tab, FragmentTransaction ft)
	        {
	            ft.add(R.id.context, fragment, null);
	        }

	        // 当Tab没被选中的时候删除对应的此Tab对应的Fragment
	        public void onTabUnselected(Tab tab, FragmentTransaction ft)
	        {
	            ft.remove(fragment);
	        }
	    }
	    

		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			
		}


		@Override
		public void onBackPressed() {
			AlertDialog.Builder b = new Builder(EnglishMainActivity.this);
			b.setIcon(R.drawable.exit_tip);
			b.setTitle("温馨提示");
			b.setMessage("是否要退出应用？");
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

	    
}



