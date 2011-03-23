package br.com.thecodebakers.hppfree.activities;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import br.com.thecodebakers.hppfree.R;
import br.com.thecodebakers.hppfree.business.HppFreeBO;
import br.com.thecodebakers.hppfree.constants.GlobalConstants;
import br.com.thecodebakers.hppfree.util.HPPReceiver;

public class ListaVazia01 extends Activity {
	
	private HppFreeBO bo;
	private final String TAG = "ListaVazia01";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listavazia01);
        bo = HppFreeBO.getInstance(this.getApplicationContext());
        
        
    }
    
    /**
     * Cadastra a nova senha no BO e desvia para a view de incluir registro.
     * @param view Controle que acionou o evento.
     */
    public void incluirRegistro(View view) {
    	
    	Log.d(TAG, "Selecionou incluir registro.");
    	EditText novaSenha = (EditText) findViewById(R.id.novaSenha);
    	EditText confirmacao = (EditText) findViewById(R.id.confirmarSenha);
    	
    	if (novaSenha.getText().toString().equals(confirmacao.getText().toString())) {
    		if (senhaValida(novaSenha.getText().toString())) {
        		bo.setKey(novaSenha.getText().toString());
    			Log.d(TAG, "Invocando editar dados.");
    			Intent i = new Intent(this, EditarDados03.class);
    	        startActivity(i);
    	        this.finish();    			
    		}
    		else {
        		Log.d(TAG, "Senha de tamanho ou formato inválido.");
        		Resources res = getResources();
        		String text = res.getString(R.string.msg_senha_tamanho_formato_invalida);
                new AlertDialog.Builder(this).setMessage(text)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    } }).show();     			
    		}

    	}
    	else {
    		Log.d(TAG, "Senha diferente.");
    		Resources res = getResources();
    		String text = res.getString(R.string.msg_senha_nao_confere);
        	
            new AlertDialog.Builder(this).setMessage(text)
            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                } }).show(); 
    	}
    }

	private boolean senhaValida(String senha) {
		boolean resultado = false;
		if (senha.length() > 7 && senha.length() < 25) {
			resultado = true;
		}
		return resultado;
	}
	
	public void ajuda(View view) {
		Intent i = new Intent(this, Ajuda.class);
		startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_lista_vazia, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   
	    switch (item.getItemId()) {
		    case R.id.mnuNovoRegistro:
		    	incluirRegistro(this.findViewById(R.layout.listavazia01));
		        return true;
		    case R.id.mnuAjuda:
		    	Intent i = new Intent(this, Ajuda.class);
				startActivity(i);
		        return true;
		    case R.id.mnuRestaurarBackup:
		    	obterSenha();
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}

	
	
	/* Os dois métodos a seguir são uma duplicação de MostrarLista02... Depois veremos como consertar
	 * 
	 * 
	 */
	protected void comandarRestaurar(String senha) {
		Resources res = getResources();
		int resultado = bo.restoreBackup(senha);
		if (resultado == GlobalConstants.RETORNO_OK) {
			this.bo.closeVault();
			Intent i = new Intent(this, ListaComDados01.class);
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
	
	/*
	 * Fim da duplicação
	 * 
	 */
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

	@Override
	public void onBackPressed() {
    	moveTaskToBack(true);
    	this.finish();
	}
	 
	 
}	
