/*
 * Copyright (C) 2011 The Code Bakers
 * Authors: Cleuton Sampaio e Francisco Rodrigues
 * e-mail: thecodebakers@gmail.com
 * Project: http://code.google.com/p/hercules-password-protector
 * Site: http://thecodebakers.blogspot.com
 *
 * Licensed under the GNU GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://gplv3.fsf.org/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Cleuton Sampaio e Francisco Rogrigues - thecodebakers@gmail.com
 */



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

/**
 * Adapter para preencher a lista de registros. Não está sendo mais utilizado no momento.
 * 
 * @author Cleuton Sampaio e Francisco Rogrigues - thecodebakers@gmail.com
 *
 */
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
