/*
 * Copyright (C) 2011 The Code Backers - Cleuton Sampaio e Francisco Rodrigues
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
 */

package br.com.thecodebakers.hppfree.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HPPReceiver extends BroadcastReceiver {
	
	private final String TAG = "HPPReceiver";
	public static boolean isScreenOn = true;
	 
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			Log.d(TAG, "Intent.ACTION_SCREEN_OFF " + Intent.ACTION_SCREEN_OFF);
			isScreenOn = false;
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			Log.d(TAG, "Intent.ACTION_SCREEN_ON " + Intent.ACTION_SCREEN_ON);
			isScreenOn = true;
		}
		/*
		//chama o servico e passa o parametro
		Intent i = new Intent(context, HPPUpdateService.class);
		i.putExtra("screen_state", isScreenOn);
		context.startService(i);*/
		
	}
	
}