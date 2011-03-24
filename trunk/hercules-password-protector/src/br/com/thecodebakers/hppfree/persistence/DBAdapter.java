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



package br.com.thecodebakers.hppfree.persistence;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import br.com.thecodebakers.hppfree.model.Agregador;
import br.com.thecodebakers.hppfree.model.Elemento;
import br.com.thecodebakers.hppfree.util.CriptoUtil;

/**
 * Esta classe é o que se poderia chamar de "DAO". Ela é uma interface de alto nível para o acesso ao
 * banco de dados SQLite, deixando o BO livre de comandos de persistência.
 * 
 * Ela usa os serviços de uma outra classe de baixo nível: DBHelper.
 * 
 * @author Cleuton Sampaio e Francisco Rogrigues - thecodebakers@gmail.com
 *
 */
public class DBAdapter implements IDBAdapter {
	
	private SQLiteDatabase db = null;
	private DBHelper dbHelper= null;
	private static final String TAG = "DBAdapter";
	

	
	public DBAdapter(Context context) {
        dbHelper = new DBHelper(context);
    }
	
	/* (non-Javadoc)
	 * @see br.com.thecodebakers.hppfree.persistence.IDBAdapter#insert(byte[])
	 */
	public long insert(String uuid, byte[] lob) throws SQLException{
		this.db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();  
		cv.put("uid", uuid);  
        cv.put("textoSecreto", lob);  
        long retorno = db.insert("elemento", null, cv);  
        db.close();
        Log.i(TAG, "Registro criado com sucesso.");
	    return retorno;
	    
	}
	
	/* (non-Javadoc)
	 * @see br.com.thecodebakers.hppfree.persistence.IDBAdapter#update(java.lang.String, byte[])
	 */
	public int update (String uid,byte[] lob) throws SQLException{
		this.db = dbHelper.getWritableDatabase(); 
		ContentValues cv = new ContentValues();  
        cv.put("uid", uid);  
        cv.put("textoSecreto", lob);  
        int retorno = db.update("elemento", cv, "uid = ?", new String[]{ uid });
        db.close();
        Log.i(TAG, "Registro atualizado com sucesso.");
		return retorno;
		   
	}
	
	/* (non-Javadoc)
	 * @see br.com.thecodebakers.hppfree.persistence.IDBAdapter#delete(java.lang.String)
	 */
	public int delete(String uid)throws SQLException{ 
		this.db = dbHelper.getWritableDatabase();
		int retorno = db.delete("elemento", "uid = ?", new String[]{ uid }); 
		db.close();
		Log.i(TAG, "Registro excluido com sucesso.");
		return  retorno;
    }  
	
	/* (non-Javadoc)
	 * @see br.com.thecodebakers.hppfree.persistence.IDBAdapter#listAll()
	 */
	public List<Elemento> listAll ()throws SQLiteException{ 
		
		List<Elemento> lista = new ArrayList<Elemento>();  
		String[] columns = new String[]{"uid", "textoSecreto"};  
		this.db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("elemento", columns,null, null, null, null, null);  
		cursor.moveToFirst();  
        
		while(!cursor.isAfterLast()){  
			Elemento  elemento = new Elemento();
        	elemento.setUUID(cursor.getString(0));
        	elemento.setLob(cursor.getBlob(1));
        	
        	lista.add(elemento);  
            cursor.moveToNext();  
        }  
        
        if (cursor != null && !cursor.isClosed()) {
        	cursor.close();
        }
        this.db.close();
        return lista;  
	}
	
	public List<Agregador> listSource () throws SQLiteException {
		List<Agregador> lista = new ArrayList<Agregador>();  
		String[] columns = new String[]{"uid", "textoSecreto"};  
		this.db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("elemento", columns,null, null, null, null, null);  
		cursor.moveToFirst();  
        
		while(!cursor.isAfterLast()){
			Agregador mapa = new Agregador();
			mapa.UUID = cursor.getString(0);
			byte[] blob = cursor.getBlob(1);
			StringBuffer saida = new StringBuffer();
			for (int x = 0; x < blob.length; x++) {
				saida.append(String.format("%02X", blob[x]));
			}
			mapa.textoSecreto = saida.toString();
			saida = null;
        	lista.add(mapa);  
            cursor.moveToNext();  
        }  
       
        if (cursor != null && !cursor.isClosed()) {
        	cursor.close();
        }
        this.db.close();
        return lista;  
		
	}

	public int getDatabaseVersion() {
		return DBHelper.DATABASE_VERSION;
	}

	// É uma duplicação de código... Mas depois eu tento refatorar: 
	private byte [] getEncripted (Elemento elemento, String chave) throws Exception {
		Log.d(TAG, "Vai encriptar a entrada: " + elemento.getUUID());
		DateFormat df = DateFormat.getDateTimeInstance();
		String clearText = (  elemento.getLocal() 
							 + "\t\r\t"
							 + elemento.getTextoSecreto()
							 + "\t\r\t"
							 + df.format(elemento.getDataModificacao())
							 + "\t\r\t"
							 + elemento.getHashArmazenado());
		CriptoUtil.setUp(chave);
		byte [] cypherText = CriptoUtil.encrypt(clearText);
		return cypherText;
	}	
	
	// Fim da duplicação
	
	public boolean updateNewList(List<Elemento> nLista, String newKey) {
		boolean retorno = true;
		this.db = dbHelper.getWritableDatabase();
		int contador = 0;
		db.beginTransaction();
		try {
			Log.d(TAG, "Iniciou a atualização da lista.");
			int qtde = db.delete("elemento", "1", null);
			Log.d(TAG, "Apagou " + qtde + " elementos pré-existentes.");
			
			ContentValues cv = new ContentValues();  
			for (Elemento e : nLista) {
				Log.d(TAG, "Vai atualizar o elemento: " + e.getUUID());
				cv.clear();
				cv.put("uid", e.getUUID());  
		        cv.put("textoSecreto", getEncripted(e, newKey));  
		        db.insert("elemento", null, cv);  
				contador++;
			}

			Log.d(TAG, "Terminou de atualizar a lista. Inseriu: " + contador + " registros.");
			db.setTransactionSuccessful();
		}
		catch (Exception e) {
			Log.e(TAG, "Exception ao atualizar a lista inteira: " + e.getLocalizedMessage());
			retorno = false;
		}
		finally {
			Log.d(TAG, "Encerrou a transação de atualização da lista.");
			db.endTransaction();
		}
		return retorno;
	}

	
	
}
