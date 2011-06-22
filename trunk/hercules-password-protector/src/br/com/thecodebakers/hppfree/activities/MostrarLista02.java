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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import br.com.thecodebakers.hppfree.R;
import br.com.thecodebakers.hppfree.business.HppFreeBO;
import br.com.thecodebakers.hppfree.constants.GlobalConstants;
import br.com.thecodebakers.hppfree.model.Elemento;
import br.com.thecodebakers.hppfree.util.HPPReceiver;
import static br.com.thecodebakers.hppfree.constants.GlobalConstants.*;

/**
 * Activity que exibe a lista de registros atualmente guardada no cofre.
 * 
 * @author Cleuton Sampaio e Francisco Rogrigues - thecodebakers@gmail.com
 *
 */
public class MostrarLista02 extends Activity implements OnItemClickListener {
	
	private HppFreeBO bo;
	private final String TAG = "MostrarLista02";
	private ListView listView;
	private String[] lv_arr = {};
	private ArrayAdapter<String> adapter = null;

    public void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, "Entrou no oncreate.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrarlista02);
        
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Log.d(TAG, "Retorno de restaurar backup - Mostrando mensagem.");
			showMessage(extras.getString("bkpmsg"), null);
    	}
	    	
        
        
        bo = HppFreeBO.getInstance(this.getApplicationContext());
        listView = (ListView)findViewById(android.R.id.list);
        listView.setTextFilterEnabled(true);
        
        //Resources res = getResources();
		//String text = res.getString(R.string.lblLocal);
		
		List<String> listaEl = PreparaLista(bo.getLista());
       
        lv_arr = listaEl.toArray(new String[0]);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, lv_arr);
        listView.setAdapter(adapter);
                
        //trata o evento onclick na lista
        listView.setOnItemClickListener(this);
        Log.d(TAG, "Terminou de montar a lista");
        
        registerForContextMenu(listView);
        //openContextMenu(layout);
        
        //REGISTRANDO O RECEIVER PARA MONITORAR O SCREEN ON/OFF
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new HPPReceiver();
        registerReceiver(mReceiver, filter);
		
        
    }
        
    
    @Override
	protected void onStart() {
		super.onStart();
        if (!bo.isVaultOpen()) {
        	Log.d(TAG, "Entrou no create da lista, com o cofre fechado.");
    		Intent i = new Intent(this, ListaComDados01.class);
            startActivity(i);
            finish();
        }		
	}

	private void showMessage(String caption, String title){
        AlertDialog.Builder bilder = new AlertDialog.Builder(MostrarLista02.this);
        bilder.setMessage(caption);
        bilder.setNeutralButton("Ok", null);
        AlertDialog alert = bilder.create();
        alert.setTitle(title);
        alert.show();
    }
    
    private List<String> PreparaLista(List<Elemento> lista) {
    	
    	List<String> listaItens = new ArrayList<String>();
        for(Elemento e : lista) {
        	listaItens.add(e.getLocal().toUpperCase());
        }
        
        return listaItens;
     }

    /**
     * Handler do evento de clique em um elemento da lista.
     */
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Log.d(TAG, "Vai invocar a activity de edição.");
		List<Elemento> lista = bo.getLista();
		Elemento el = lista.get(arg2);
		Intent i = new Intent(this, EditarDados03.class);
		i.putExtra("uuid", el.getUUID());
        startActivity(i);
		
	}
	
	/**
	 * Fecha a lista e retorna para quem chamou.
	 * @param view Botão que foi acionado.
	 */
	public void fechar(View view) {
		Log.d(TAG, "Selecionou fechar a lista.");
		executaFechamento();
	}
	
	private void executaFechamento() {
		this.bo.closeVault();
		Intent i = new Intent(this, ListaComDados01.class);
        startActivity(i);
        finish();
	}

	/**
	 * O usuário selecionou o botão de "back" do dispositivo.
	 * Faz a mesma coisa que clicar no botão "fechar".
	 */
	@Override
	public void onBackPressed() {
		Log.d(TAG, "Acionou o botão \"back\" do dispositivo.");
		executaFechamento();
	}

    /**
     * Chama a activity de incluir registro.
     * @param view Controle que acionou o evento.
     */
    public void incluirRegistro(View view) {
    	Log.d(TAG, "Selecionou incluir registro.");
		Intent i = new Intent(this, EditarDados03.class);
        startActivity(i);
        this.finish();
    }
    
    public void alterarSenha(View view) {
    	Log.d(TAG, "Selecionou alterar senha.");
		Intent i = new Intent(this, AlterarSenha20.class);
        startActivity(i);
        this.finish();    	
    }
	
	public void ajuda(View view) {
		Intent i = new Intent(this, Ajuda.class);
		startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_mostrar_lista, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   
	    switch (item.getItemId()) {
		    case R.id.mnuIncluirRegistro:
		    	incluirRegistro(this.findViewById(R.layout.mostrarlista02));
		        return true;
		    case R.id.mnuFechar:
		    	executaFechamento();
		        return true;
		    case R.id.mnuAlterarSenha:
		    	alterarSenha (this.findViewById(R.layout.mostrarlista02));
		        return true;    
		    case R.id.mnuAjuda:
		    	Intent i = new Intent(this, Ajuda.class);
				startActivity(i);
		        return true;
		    case R.id.mnuSalvarBackup:
		    	salvarBackup();
		    	return true;
		    case R.id.mnuRestaurarBackup:
		    	restaurarBackup();
		    	return true;
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void restaurarBackup() {
		Resources res = getResources();
		String mensagem = "";
   		String txtSim = res.getString(R.string.opc_sim);
   		String txtNao = res.getString(R.string.opc_nao);
           new AlertDialog.Builder(this).setMessage(R.string.msg_confirmar_restaurar_backup)
           .setPositiveButton(txtSim, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
               	obterSenha();
               } })
           .setNegativeButton(txtNao, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                   return;
               } })
           .show(); 
		 
	}


	protected void comandarRestaurar(String senha) {
		Resources res = getResources();
		int resultado = bo.restoreBackup(senha);
		if (resultado == GlobalConstants.RETORNO_OK) {
			int restaurados = bo.getQtdeRestaurados();
			this.bo.closeVault();
			bo.setKey(senha);
			bo.openVault();
			String msg = res.getString(R.string.msg_backup_restaurado) + " " + restaurados + ".";
			Intent i = new Intent(this, this.getClass());
			i.putExtra("bkpmsg", msg);
			startActivity(i);
			this.finish();
		}
		else {
			int msgString = 0;
			switch (resultado) {
			case GlobalConstants.HASHCODE_BACKUP_INVALIDO:
				msgString = R.string.msg_erro_restaurar_backup_hashcode;
				break;
			case GlobalConstants.ERRO_VERSAO_BACKUP_INCOMPATIVEL:
				msgString = R.string.msg_erro_restaurar_backup_versao_incompativel;
				break;
			case GlobalConstants.ARQUIVO_BACKUP_INVALIDO:
				msgString = R.string.msg_erro_restaurar_backup_xml_invalido;
				break;
			case GlobalConstants.ERRO_HASHCODE_ENTRADA_INVALIDO:
				msgString = R.string.msg_erro_restaurar_backup_hashcode_entrada;
				break;
			default:
				msgString = R.string.msg_erro_restaurar_backup;
				break;
			}
			
			new AlertDialog.Builder(this).setMessage(res.getString(msgString))
	        .setNeutralButton(res.getString(R.string.opc_ok), new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	return;
	            } })
	        .show(); 			
		}
	}


	private void obterSenha() {
		final String saida = "";
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        Resources res = getResources();
      
        alert.setMessage(res.getString(R.string.msg_informar_senha_backup));

        // Set an EditText view to get user input 
        final EditText input = new EditText(this.getApplicationContext());
        input.setTransformationMethod
        	(android.text.method.PasswordTransformationMethod.getInstance());
        alert.setView(input);

        alert.setPositiveButton(res.getString(R.string.opc_ok), new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
        	Editable value = input.getText();
        	comandarRestaurar(value.toString());
          }
        });

        alert.setNegativeButton(res.getString(R.string.opc_cancelar), new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            // Canceled.
          }
        });

        alert.show();
	}


	private void salvarBackup() {
		int retorno = bo.saveBackup();
		Resources res = getResources();
		String mensagem = "";
		if (retorno == RETORNO_OK) {
			mensagem = res.getString(R.string.msg_salvar_backup_ok); 
		}
		else {
			if (retorno == GlobalConstants.ERRO_PROBLEMA_MEDIA_SALVAR_BACKUP) {
				mensagem = res.getString(R.string.msg_erro_salvar_problema_media);
			}
			else {
				mensagem = res.getString(R.string.msg_erro_salvar_backup);
			}
		}
        new AlertDialog.Builder(this).setMessage(mensagem)
        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	return;
            } })
        .show(); 
	}


	@Override
	 public void onCreateContextMenu(ContextMenu menu, View v,
	 		 						 ContextMenuInfo menuInfo) {
		Resources res = getResources();
	 	 super.onCreateContextMenu(menu, v, menuInfo);
	 	 menu.setHeaderTitle(res.getString(R.string.menu_context_titulo));
	 	 MenuInflater inflater = getMenuInflater();
	 	 inflater.inflate(R.menu.menu_context_mostrar_lista, menu);
	    
	 }
	
	 public boolean onContextItemSelected(MenuItem item) {

           AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
           final Elemento elemento = (Elemento)bo.getLista().get(info.position);
           
           switch (item.getItemId()) {
	           case R.id.mnuCntEditar:
	        	 Log.d(TAG, "Editar o registro." + elemento.getLocal());
	        	 Intent i = new Intent(this, EditarDados03.class);
	        	 i.putExtra("uuid", elemento.getUUID());
	        	 startActivity(i);
	             return true;
	             
	           case R.id.mnuCntExcluir:
	        	   
	          		Resources res = getResources();
	           		String txtSim = res.getString(R.string.opc_sim);
	           		String txtNao = res.getString(R.string.opc_nao);
	                   new AlertDialog.Builder(this).setMessage(R.string.msg_confirmar_exclusao)
	                   .setPositiveButton(txtSim, new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int which) {
	                       	comandarExclusao(elemento);
	                       } })
	                   .setNegativeButton(txtNao, new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int which) {
	                           return;
	                       } })
	                   .show(); 
	             return true;
	           default:
	             return super.onContextItemSelected(item);
           }

     }
	 
	 private void comandarExclusao(Elemento elemento){
       	 Log.d(TAG, "Excluir o registro.");
		 bo.removeElement(elemento); 				// Remove o elemento da lista e do BD
    	 Intent i = null;
    	 if (bo.getLista().size() > 0) {
        	 i = new Intent(this, MostrarLista02.class); //atualiza a listView
        	 startActivity(i);
    	 }
    	 else {
    		 i = new Intent(this, ListaVazia01.class);
    		 bo.closeVault();
    		 startActivity(i);
    		 this.finish();
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
