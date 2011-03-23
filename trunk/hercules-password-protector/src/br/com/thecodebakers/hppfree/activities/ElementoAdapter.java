package br.com.thecodebakers.hppfree.activities;

import java.util.ArrayList;
import java.util.List;

import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.thecodebakers.hppfree.model.Elemento;

public class ElementoAdapter extends BaseAdapter {

	private List<Elemento> elementos;
	private Context localContext;
	private LayoutInflater mInflater;


	public ElementoAdapter(Context context, List<Elemento> results) {
		  mInflater = LayoutInflater.from(context);
		  localContext = context;
		  this.elementos = (List<Elemento>) results;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) localContext
                    							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(android.R.id.list, null);
            
        }
       // TextView local = (TextView) v.findViewById(R.id.);
        //local.setText((String)elementos.get(position).getLocal());
              
        return convertView;
    }
	
	public int getCount() {
		return elementos.size();
	}

	public Object getItem(int position) {
		return elementos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	
}
