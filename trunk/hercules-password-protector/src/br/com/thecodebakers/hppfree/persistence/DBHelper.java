package br.com.thecodebakers.hppfree.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
		
	   private static final String DATABASE_NAME = "hppfree.db";
	   public static final int DATABASE_VERSION = 1;
	   private static final String TABLE_NAME = "elemento";

		
		public DBHelper(Context context) {
		    super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
				 
		@Override
		public void onCreate(SQLiteDatabase db) {
			 Log.d("HPPFree", "Criando a tabela no banco de dados.");
		     db.execSQL("CREATE TABLE " + TABLE_NAME + "(uid TEXT, textoSecreto BLOB)");
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		     Log.d("HPPFree", "Verificando atualização do banco de dados.");
		     if ((newVersion - oldVersion) > 2) {
		    	 Log.d("HPPFree", "Há necessidade de atualizar o banco.");
		    	 db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		    	 onCreate(db);
		     }
		     else {
		    	 Log.d("HPPFree", "A versão do banco de dados é praticamente a mesma. Os registros serão preservados.");
		     }
		     
		}

	
}
