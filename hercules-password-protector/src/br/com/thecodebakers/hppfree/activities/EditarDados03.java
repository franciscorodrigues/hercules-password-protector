package br.com.thecodebakers.hppfree.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import br.com.thecodebakers.hppfree.R;
import br.com.thecodebakers.hppfree.business.HppFreeBO;
import br.com.thecodebakers.hppfree.model.Elemento;
import br.com.thecodebakers.hppfree.util.HPPReceiver;

public class EditarDados03 extends Activity implements DialogInterface.OnClickListener {
	
	private HppFreeBO bo;
	private String uuidInformado;
	private final String TAG = "EditarDados03";
	private EditText local;
	private EditText textoSecreto;
	private boolean alterouTexto = false;
	private EditarDados03 selfRef;
	private String localOriginal = "";
	private String textoSecretoOriginal = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editardados03);
        selfRef = this;
        bo = HppFreeBO.getInstance(this.getApplicationContext());
       
    }
    
    
    
    /**
     * Evento gerado quando a view é ativada. Tem que testar se veio um UUID nos extras.
     */
    @Override
	public void onStart() {
		super.onStart();
		Bundle extras = getIntent().getExtras();
		local = (EditText) findViewById(R.id.txtLocal);
		
		textoSecreto = (EditText) findViewById(R.id.txtTextoSecreto);
		
		local.setEnabled(true);
		if (extras != null) {
			Log.d(TAG, "Vai obter o uuid informado.");
		    this.uuidInformado = extras.getString("uuid");
		    local.setEnabled(false);
	    	Elemento elemento = new Elemento();
	    	elemento.setUUID(uuidInformado);
	    	elemento = bo.getLista().get(bo.getLista().indexOf(elemento));
	    	
	    	if (elemento == null) {
	    		Log.e(TAG, "Não encontrou um elemento na lista: " + uuidInformado);
	    	}
	    	
	    	local.setText(elemento.getLocal());
	    	localOriginal = elemento.getLocal();
	    	textoSecreto.setText(elemento.getTextoSecreto());
	    	textoSecretoOriginal = elemento.getTextoSecreto();
	    	
		}
	}


    
    /**
     * Salva o registro recém informado no banco. Se já existir, será atualizado.
     * @param view A View que gerou o evento.
     */
    public void salvar(View view) {
    	
    	EditText local = (EditText) findViewById(R.id.txtLocal);
    	EditText textoSecreto = (EditText) findViewById(R.id.txtTextoSecreto);
    	
    	if (local.getText().toString().length() == 0) {
    		mensagem (R.string.msg_campo_local_obrigatorio);
    	}
    	else {
    		if (textoSecreto.getText().toString().length() == 0) {
    			mensagem (R.string.msg_campo_textosecreto_obrigatorio);
    		}
    		else {
    			if (uuidInformado == null) {
    				Log.d(TAG, "Vai inserir o registro no BO;");
    				Elemento elemento = new Elemento();
    				boolean achou = false;
    				for (Elemento e : bo.getLista()) {
    					if (e.getLocal().equalsIgnoreCase(local.getText().toString())) {
    						mensagem (R.string.msg_campo_local_duplicado);
    						achou = true;
    						break;
    					}
    				}
    				if (!achou) {
    					Log.d(TAG, "Vai invocar o BO.");
    					elemento.setLocal(local.getText().toString());
    					elemento.setTextoSecreto(textoSecreto.getText().toString());
    					bo.addElement(elemento);
    					mensagem (R.string.msg_inserido);
    					local.setText("");
    			    	textoSecreto.setText("");
    				}
    			}
    			else {
    				Log.d(TAG, "Vai atualizar o registro no BO;");
    				Elemento elemento = new Elemento();
 
					elemento.setLocal(local.getText().toString());
					elemento.setTextoSecreto(textoSecreto.getText().toString());
					bo.saveElement(elemento);
    				
    				for (Elemento e : bo.getLista()) {
    					if (e.getLocal().equalsIgnoreCase(local.getText().toString())) {
    						e.setTextoSecreto(textoSecreto.getText().toString());
    						break;
    					}
    				}
    				
    		    	Resources res = getResources();
    				String text = res.getString(R.string.msg_alterado);
    		        new AlertDialog.Builder(this).setMessage(text)
    		        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
    		            public void onClick(DialogInterface dialog, int which) {
    						Intent i = new Intent(selfRef, MostrarLista02.class);
    				        startActivity(i);
    				        selfRef.finish();  
    		            } }).show();  
    				}
    		}
    	}
    	
    }
    
    /**
     * Exclui o registro editado.
     * @param view
     */
    public void excluir (View view) {
   		Resources res = getResources();
   		String txtSim = res.getString(R.string.opc_sim);
   		String txtNao = res.getString(R.string.opc_nao);
           new AlertDialog.Builder(this).setMessage(R.string.msg_confirmar_exclusao)
           .setPositiveButton(txtSim, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
               	comandarExclusao();
               } })
           .setNegativeButton(txtNao, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                   return;
               } })
           .show(); 			
    }
    
    private void comandarExclusao () {
    	EditText local = (EditText) findViewById(R.id.txtLocal);
    	Elemento el = new Elemento();
    	el.setLocal(local.toString());
		boolean achou = false;
		for (Elemento e : bo.getLista()) {
			if (e.getLocal().equalsIgnoreCase(local.getText().toString())) {
				el = e;
				achou = true;
				break;
			}
		}
		if (achou) {
			Intent i = null;
			bo.removeElement(el);
			if (bo.getLista().size() > 0) {
				i = new Intent(this, MostrarLista02.class);
			}
			else {
				i = new Intent(this, ListaVazia01.class);
				bo.closeVault();
			}
			startActivity(i);
			this.finish();  
		}
		else {
			Log.d(TAG, "Excluir não encontrou na lista.");
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



	@Override
	public void onBackPressed() {
		
		tratarCancelamento();
	}
    
    public void cancelar(View view) {
    	tratarCancelamento();
    }
    
    private void tratarCancelamento() {
    	// voltar tem que voltar para a lista. A activity da lista tem que testar se está vazia.
    	// Tem que perguntar se o cara jã escreveu alguma cois (onchange???)
    	if ((!local.getText().toString().equals(localOriginal)) ||
    			(!textoSecreto.getText().toString().equals(textoSecretoOriginal))) {
    		Resources res = getResources();
    		String txtSim = res.getString(R.string.opc_sim);
    		String txtNao = res.getString(R.string.opc_nao);
            new AlertDialog.Builder(this).setMessage(R.string.msg_alteracoes_pendentes)
            .setPositiveButton(txtSim, this)
            .setNegativeButton(txtNao, this)
            .show();  
    	}
    	else {
			Intent i = new Intent(this, MostrarLista02.class);
			startActivity(i);
			this.finish();     		
    	}

    }



	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
			Intent i = new Intent(this, MostrarLista02.class);
			startActivity(i);
			this.finish(); 
		}
	}

	public void ajuda(View view) {
		Intent i = new Intent(this, Ajuda.class);
		startActivity(i);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_editar_dados, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   
	    switch (item.getItemId()) {
		    case R.id.mnuSalvar:
		    	salvar(this.findViewById(R.layout.editardados03));
		        return true;
		    case R.id.mnuCancelar:
		    	cancelar(this.findViewById(R.layout.editardados03));
		        return true;
		    case R.id.mnuExcluir:
		    	excluir(this.findViewById(R.layout.editardados03));
		        return true;
		    case R.id.mnuAjuda:
		    	Intent i = new Intent(this, Ajuda.class);
				startActivity(i);
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * Se é uma alteração mostra o item EXCLUIR,<BR/> 
	 * senão, esconde o item do menu.
	 */
	@Override 
	public boolean onPrepareOptionsMenu(Menu menu) { 
		super.onPrepareOptionsMenu(menu);
		
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			MenuItem item = menu.findItem(R.id.mnuExcluir); 
			item.setVisible(false); 
		}
		return true; 
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
