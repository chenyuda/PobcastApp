package de.danoeh.antennapod.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.danoeh.antennapod.R;

public class PopAdapter extends BaseAdapter{
	private int count;
	private Context context;
	private String[] area;
	private static int[] imgs={R.drawable.cn,R.drawable.ar,R.drawable.ae,R.drawable.au,R.drawable.be,R.drawable.br,R.drawable.ca,
			R.drawable.ch,R.drawable.de,R.drawable.es,R.drawable.fi,R.drawable.fr,R.drawable.gb,R.drawable.ie,R.drawable.in,R.drawable.it,
			R.drawable.jp,R.drawable.kr,R.drawable.mx,R.drawable.nl,R.drawable.no,R.drawable.nz,R.drawable.pt,R.drawable.ru,R.drawable.se,
			R.drawable.ua,R.drawable.us,};

	public PopAdapter(Context context,String[] area){
		this.context=context;
		this.area=area;
	}
	
	@Override
	public int getCount() {
		return area.length;
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		View inflate = View.inflate(context, R.layout.popliist_itemes, null);

		TextView textView=(TextView) inflate.findViewById(R.id.name);
		textView.setText(area[arg0]);
		ImageView iv= (ImageView) inflate.findViewById(R.id.country_icon);
		iv.setImageResource(imgs[arg0]);
		return inflate;
	}

}
