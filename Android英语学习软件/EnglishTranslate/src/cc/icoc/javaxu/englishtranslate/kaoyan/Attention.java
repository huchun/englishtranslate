package cc.icoc.javaxu.englishtranslate.kaoyan;

import java.util.ArrayList;

import cc.icoc.javaxu.bean.Word;
import cc.icoc.javaxu.database.DataAccess;
import cc.icoc.javaxu.englishtranslate.view.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Attention extends ListActivity {

	private ArrayList<Word> list;
	public static final int MENU_ADD = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setTitle("生词本");
		this.setContentView(R.layout.attention);
		setAdapter();		
	}
	
	protected void setAdapter() {
		DataAccess data = new DataAccess(this);
		list=data.QueryAttention(null, null);
		ArrayList<String> showlist = new ArrayList<String>();
		for (int i=0;i<list.size();i++){
			showlist.add((i+1)+"."+list.get(i).getSpelling()+"\n"+list.get(i).getMeanning());
		}
		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.file_row, showlist));
	}
	
	@Override
	protected void onResume() {
		this.setAdapter();
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ADD, 0, "添加新单词");
		return true;		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case MENU_ADD:{
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("action", "add");
			intent.putExtras(bundle);
			intent.setClass(Attention.this, EditWord.class);
			startActivity(intent);
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		super.onListItemClick(l, v, position, id);
		Dialog dialog = new AlertDialog.Builder(Attention.this)
        .setIcon(R.drawable.dialog_icon)
        .setTitle("操作")
        .setItems(new String[]{"编辑该单词","从生词本中删除"}, new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				switch(which){
				case 0:{
					Intent intent = new Intent();
					intent.setClass(Attention.this, EditWord.class);
					Bundle bundle = new Bundle();
					bundle.putString("action", "edit");
					bundle.putString("id", list.get(position).getID());
					intent.putExtras(bundle);
					startActivity(intent);
					break;
				}
				case 1:{
					DataAccess data = new DataAccess(Attention.this);
					data.DeleteFromAttention(list.get(position));
					setAdapter();
					break;
				}
				}				
			}        	
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
                  dialog.cancel();
			}
		})
        .create();		
		dialog.show();
	}
}
