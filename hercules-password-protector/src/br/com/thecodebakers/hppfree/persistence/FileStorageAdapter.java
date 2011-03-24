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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import br.com.thecodebakers.hppfree.constants.GlobalConstants;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

/**
 * Um adapter para encapsular o acesso ao mecanismo de armazenamento de beckups.
 * Futuramente, queremos guardar o backup nos contatos do usuário. 
 * 
 * Isto deixa o BO livre de dependências físicas.
 * 
 * @author Cleuton Sampaio e Francisco Rogrigues - thecodebakers@gmail.com
 *
 */
public class FileStorageAdapter {

	private static final String BackupRowId = "HPPFREE01";
	private static Context appctx;
	private static String TAG = "HPPContactsAdapter"; 
	public static int lastError = GlobalConstants.RETORNO_OK;
	
	public static String getXMLString(Context context) {
		StringBuffer retorno = new StringBuffer();
		appctx = context;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File backupFile = new File(Environment.getExternalStorageDirectory().toString() + "/HPP/" +  BackupRowId + ".xml");
			if (!backupFile.exists()) {
				lastError = GlobalConstants.ERRO_RESTAURAR_ARQUIVO_NAO_EXISTE;
				Log.e(TAG, "O Arquivo de bacbup não existe.");
			}
			else {
				try {
					FileReader entrada = new FileReader(backupFile);
					BufferedReader reader = new BufferedReader(entrada);
					String linha = null;
					while ((linha = reader.readLine()) != null) {
						retorno.append(linha);
						retorno.append("\r\n");
					}
					entrada.close();
					Log.d(TAG, "Leu o backup");
				} catch (IOException e) {
					lastError = GlobalConstants.ERRO_LER_BACKUP;
					Log.e(TAG, "Exception ao ler: " + e.getLocalizedMessage());
				}
			}
		}
		else {
			Log.e(TAG, "Erro com o sdcard.");
			lastError = GlobalConstants.ERRO_PROBLEMA_MEDIA_LER_BACKUP;
		}
		
		
		return retorno.toString();
	}
	
	public static int setXMLString(String xml2Save, Context context) {
		Log.d(TAG, "Entrou em salvar.");
		appctx = context;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			try {
				File directory = new File(Environment.getExternalStorageDirectory().toString() + "/HPP");
				if (!directory.exists()) {
					directory.mkdirs();
				}
				FileOutputStream out = new FileOutputStream(directory.getPath() + "/" + BackupRowId + ".xml");
				out.write(xml2Save.getBytes());
				out.close();
				Log.d(TAG, "Gravou o backup");
			} catch (IOException e) {
				lastError = GlobalConstants.ERRO_PROBLEMA_MEDIA_SALVAR_BACKUP;
				Log.e(TAG, "Exception ao salvar: " + e.getLocalizedMessage());
			}
		}
		else {
			lastError = GlobalConstants.ERRO_PROBLEMA_MEDIA_SALVAR_BACKUP;
		}
		return lastError;
	}
	
	
}
