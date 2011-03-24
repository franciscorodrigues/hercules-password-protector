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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Esta classe é sugerida ao usar SQLite, sendo um DAO de baixo nível.
 * É muito importante prestar atenção ao field "DATABASE_VERSION". Ele foi usado para criar o banco
 * de dados e será no caso de Update. Se houver update, deve haver uma comparação da nova versão do
 * banco com a antiga e deve ser decidido o que fazer.
 * 
 * 
 * @author Cleuton Sampaio e Francisco Rogrigues - thecodebakers@gmail.com
 *
 */
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
