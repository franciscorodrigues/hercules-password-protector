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
 * @author cleuton
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
