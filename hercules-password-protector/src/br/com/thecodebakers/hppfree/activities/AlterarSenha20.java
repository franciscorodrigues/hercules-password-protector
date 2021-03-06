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

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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
 * Activity que permite alterar a senha do cofre.
 * @author Cleuton Sampaio e Francisco Rogrigues - thecodebakers@gmail.com
 *
 */
public class AlterarSenha20 extends Activity {

	private EditText senha;
	private EditText novaSenha;
	private EditText confirmacao;
	private HppFreeBO bo;
	private AlterarSenha20 selfRef = this;
	private final String TAG = "AlterarSenha20";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alterarsenha20);
		senha = (EditText) findViewById(R.id.editSenha);
		novaSenha = (EditText) findViewById(R.id.novaSenha);
		confirmacao = (EditText) findViewById(R.id.confirmarSenha);
		bo = HppFreeBO.getInstance(this.getApplicationContext());
		
		
		AdView adView = (AdView)this.findViewById(R.id.adViewAlterarSenha);
	    adView.loadAd(new AdRequest());
	}
	
	/**
	 * Botão "Salvar" foi acionado.
	 * @param view
	 */
	public void salvar(View view) {
		String senhadig = senha.getText().toString();
		String novaSenhadig = novaSenha.getText().toString();
		String confirmaSenhadig = confirmacao.getText().toString();
		int msg = 0;
		if (!senhadig.equals(bo.getKey())) {
			msg = R.string.msg_senha_invalida;
			senha.setText("");
		}
		else {
			if (novaSenhadig.length() < 8 || novaSenhadig.length() > 24) {
				msg = R.string.msg_senha_tamanho_formato_invalida;
			}
			else {
				if (senhadig.equals(novaSenhadig)) {
					msg = R.string.msg_senha_igual_a_atual;
				}
				else {
					if (!novaSenhadig.equalsIgnoreCase(confirmaSenhadig)) {
						msg = R.string.msg_senha_nao_confere;
					}
					else {
						msg = R.string.msg_senha_alerada;
					}
				}
			}
		}
		if (msg != 0) {
			// Invoca o método do BO que altera senha.
	    	Resources res = getResources();
			String text = null;
			if (msg == R.string.msg_senha_alerada) {
				bo.setKey(novaSenhadig);
				boolean resultado = bo.reEncryptAll();
				if (resultado) {
					text = res.getString(msg);
					new AlertDialog.Builder(this).setMessage(text)
					.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent i = new Intent(selfRef, MostrarLista02.class);
							startActivity(i);
							selfRef.finish();  
						} }).show();
				}
				else {
					res.getString(R.string.msg_erro_alteracao_senha);
					new AlertDialog.Builder(this).setMessage(text)
					.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							return;
						} }).show();					
				}
			}
			else {
				text = res.getString(msg);
				new AlertDialog.Builder(this).setMessage(text)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					} }).show();				
			}
		}
	}
	
	public void cancelar(View view) {
		this.executarCancelamento();
	}
	
	public void executarCancelamento() {
		Intent i = new Intent(this, MostrarLista02.class);
		startActivity(i);
		this.finish();    
	}

	/**
	 * O usuário selecionou o botão de "back" do dispositivo.
	 * Faz a mesma coisa que clicar no botão "fechar".
	 */
	@Override
	public void onBackPressed() {
		Log.d(TAG, "Acionou o botão \"back\" do dispositivo.");
		this.executarCancelamento();
	}
	
	public void ajuda(View view) {
		Intent i = new Intent(this, Ajuda.class);
		startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_alterar_senha, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   
	    switch (item.getItemId()) {
		    case R.id.mnuSalvar:
		    	salvar(this.findViewById(R.layout.alterarsenha20));
		        return true;
		    case R.id.mnuCancelar:
		    	cancelar(this.findViewById(R.layout.alterarsenha20));
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
