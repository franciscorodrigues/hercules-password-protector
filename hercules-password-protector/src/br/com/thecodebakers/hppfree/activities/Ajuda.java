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

import br.com.thecodebakers.hppfree.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
/**
 * Activity para mostrar a tela de ajuda.
 * @author Cleuton Sampaio e Francisco Rogrigues - thecodebakers@gmail.com
 *
 */
public class Ajuda extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ajuda);
	}
	
	public void fechar(View view) {
		finish();
	}
	
	public void web(View view) {
		Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://thecodebakers.blogspot.com"));
		startActivity(viewIntent); 
	}
	
	public void mostrarManual(View view) {
		Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://code.google.com/p/hercules-password-protector/downloads/detail?name=HPP_Manual_V01010002.pdf"));
		startActivity(viewIntent); 
	}
	
}
