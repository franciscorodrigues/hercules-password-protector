package br.com.thecodebakers.hppfree.util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import br.com.thecodebakers.hppfree.activities.ListaComDados01;

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