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



package br.com.thecodebakers.hppfree.util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import br.com.thecodebakers.hppfree.activities.ListaComDados01;

/**
 * Esta classe mantém a aplicação ativa quando a tela estiver bloqueada. É usada pelo HPPReceiver.
 * 
 * @author Cleuton Sampaio e Francisco Rogrigues - thecodebakers@gmail.com
 *
 */
public class HPPUpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
	   super.onCreate();
	   Log.d("HPPUpdateService","HPPUpdateService.onCreate ");
	   // registra o receiver que captura o screen on/off das telas
	   IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
	   filter.addAction(Intent.ACTION_SCREEN_OFF);
	   BroadcastReceiver mReceiver = new HPPReceiver();
	   registerReceiver(mReceiver, filter);
	}
	
	@Override
	public int onStartCommand (Intent intent, int flags, int startId){
		 boolean screenOn = intent.getBooleanExtra("screen_state", false);
	     Log.d("HPPUpdateService","onStartCommand " + screenOn);
	     if (screenOn) {
	    	Log.d("HPPUpdateService", "onStartCommand true");
	        Intent i = new Intent(this, ListaComDados01.class); 
	        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	       	startActivity(i);
	     } 
	     return START_STICKY;

	}
	
	
	
}