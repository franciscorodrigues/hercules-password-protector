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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import br.com.thecodebakers.hppfree.R;
import br.com.thecodebakers.hppfree.business.HppFreeBO;
import br.com.thecodebakers.hppfree.util.HPPReceiver;

/**
 * Activity invocada no início. Se o cofre estiver vazio, ela vai invocar a activity ListaVazia01.
 * 
 * @author Cleuton Sampaio e Francisco Rogrigues - thecodebakers@gmail.com
 *
 */
public class ListaComDados01 extends Activity {
	
	private static final String TAG = "ListaComDados01";
	private HppFreeBO bo;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bo = HppFreeBO.getInstance(this.getApplicationContext());
        if (bo.isVaultOpen()) {
        	Log.d(TAG, "O cofre estava aberto no onCreate. Será fechado.");
        	bo.closeVault();
        }
        
       
    }

	@Override
	public void onStart() {
		super.onStart();
		if (bo.isVaultOpen()) {
			Log.d(TAG, "O cofre estava aberto no onStart. Será fechado.");
			bo.closeVault();
		}
		this.hasElements();
	}
	
	
    
    @Override
	public void onRestart() {
		super.onRestart();
		this.hasElements();
	}

	private void hasElements() {
		if (bo.isEmpty()) {
			Log.d("Activity Lista cheia", "Lista vazia.");
			Intent i = new Intent(this, ListaVazia01.class);
	        startActivity(i);
	        this.finish();
		}
    	
    }
	
	/**
	 * Evento disparado quando o usuário clica no botão de abrir.
	 * @param view O botão que gerou o evento
	 */
	public void abrirCofre(View view) {
		Log.d(TAG, "Vai abrir o cofre.");
		EditText texto = (EditText) this.findViewById(R.id.editSenha);
		this.bo.setKey(texto.getText().toString());
		boolean resultado = bo.openVault();
		Log.d(TAG, "Resuldado = " + resultado);
		if (!resultado) {
			Log.e(TAG, "Senha inválida.");
			mensagem(R.string.msg_senha_invalida);
			texto.setText("");
		}
		else {
			Log.d(TAG, "OK, vai mostrar a lista.");
			texto = (EditText) this.findViewById(R.id.editSenha);
			texto.setText("");
			Intent i = new Intent(this, MostrarLista02.class); // chama a lista
	        startActivity(i);
		}
	}
	
    private void mensagem (int indTexto) {
    	Resources res = getResources();
		String text = res.getString(indTexto);
        new AlertDialog.Builder(this).setMessage(text)
        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	
                return;
            } }).show();     	
    }

	/**
	 * Tem que sair fora!
	 */
    @Override
	public void onBackPressed() {
    	moveTaskToBack(true);
    	this.finish();
	}
    
	public void ajuda(View view) {
		Intent i = new Intent(this, Ajuda.class);
		startActivity(i);
	}
	
	public void web(View view) {
		Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.com"));
		startActivity(viewIntent); 
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_lista_com_dados, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   
	    switch (item.getItemId()) {
		    case R.id.mnuAbrirCofre:
		    	abrirCofre(this.findViewById(R.layout.listavazia01));
		        return true;
		    case R.id.mnuAjuda:
		    	Intent i = new Intent(this, Ajuda.class);
				startActivity(i);
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}
	
	 @Override
	 protected void onPause() {

	     if (HPPReceiver.isScreenOn) {
	    	Log.d(TAG,"SCREEN TURNED OFF AND SCREEN STATE CHANGED");
	     } 
	     else {
	        Log.d(TAG,"SCREEN TURNED OFF AND SCREEN STATE HAS NOT CHANGED");
	     }
	     super.onPause();
	 }
	 	
	 @Override
	 protected void onResume() {
	    
		 if (!HPPReceiver.isScreenOn) {
	        Log.d(TAG,"SCREEN TURNED ON AND SCREEN STATE CHANGE");
	    	Intent intent = new Intent(this, ListaComDados01.class); 
	       	startActivity(intent);
	     }
		 else {
	        Log.d(TAG,"SCREEN TURNED ON AND SCREEN STATE HAS NOT CHANGE");
	     }
	     super.onResume();
	 }
    
}