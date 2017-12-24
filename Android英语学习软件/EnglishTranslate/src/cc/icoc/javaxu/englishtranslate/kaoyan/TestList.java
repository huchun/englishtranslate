package cc.icoc.javaxu.englishtranslate.kaoyan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.icoc.javaxu.bean.BookList;
import cc.icoc.javaxu.bean.WordList;
import cc.icoc.javaxu.database.DataAccess;
import cc.icoc.javaxu.englishtranslate.view.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class TestList extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.test_list);
		DataAccess dataAccess = new DataAccess(this);
		BookList book =dataAccess.QueryBook("ID ='"+DataAccess.bookID+"'", null).get(0);
		this.setTitle("测试-"+book.getName());		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.test_list_item,
                new String[]{"word_list","last_score","image"},
                new int[]{R.id.test_list_item_1,R.id.test_list_item_2,R.id.test_list_image});
				setListAdapter(adapter);
	}

	private List<? extends Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		DataAccess dataAccess = new DataAccess(this);
		ArrayList<WordList> wordList = dataAccess.QueryList("BOOKID = '"+DataAccess.bookID+"'", null);
		
		System.out.println("size:" + wordList.size());
		
		Map<String, Object> map;	
		
		for (int i = 0; i < wordList.size(); i++) {
			map = new HashMap<String, Object>();
			map.put("word_list", "List" + wordList.get(i).getList());
			map.put("last_score", "最高正确率:" + wordList.get(i).getBestScore() + "%");
			if(wordList.get(i).getBestScore().equals(""))map.put("image", android.R.drawable.btn_star_big_off);
			else map.put("image", android.R.drawable.btn_star_big_on);
			list.add(map);
		}
		
		return list;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent intent = new	Intent();
		intent.setClass(TestList.this, Test.class);
		intent.putExtra("wordList", ""+position);
		TestList.this.startActivity(intent);
		
		System.out.println(position);
	}
}
