package cc.icoc.javaxu.englishtranslate.view;

import cc.icoc.javaxu.englishtranslate.kaoyan.Main;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class EnglishKaosActivity extends Fragment{

	View view ;
	
	public static Fragment newInstance() {
		EnglishKaosActivity fragment = new EnglishKaosActivity();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.trans_view, container, false);
		initView(view);
		return view;
	}

	private void initView(View v1) {
		v1.findViewById(R.id.layout_view).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), Main.class);
				startActivity(intent);
			}
		});
	}
}
