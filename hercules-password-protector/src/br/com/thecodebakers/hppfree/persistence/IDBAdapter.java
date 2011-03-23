/*
 * Copyright (C) 2011 The Code Backers - Cleuton Sampaio e Francisco Rodrigues
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
 */

package br.com.thecodebakers.hppfree.persistence;

import java.util.List;

import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import br.com.thecodebakers.hppfree.model.Agregador;
import br.com.thecodebakers.hppfree.model.Elemento;

public interface IDBAdapter {

	/**
	 * insere um registro na base de dados 
	 * @param uuid  - id de controle do registro
	 * @param lob	- dados para carregar o campo blob
	 * @return		- long com o retorno da operação. Se -1 ocorreu algum erro.
	 * @throws SQLException
	 */
	public abstract long insert(String uuid, byte[] lob) throws SQLException;
	/**
	 * atualiza um registro na base
//	 * @param uid - id de controle do registro
	 * @param lob - dados para carregar o campo blob
	 * @return    - int com o retorno da operação. 
	 * @throws SQLException
	 */
	public abstract int update(String uid, byte[] lob) throws SQLException;
     /**
      * Exclui um registro na base
      * @param Se -1 ocorreu algum erro.
      * @return  - int com o retorno da operação. 
      * @throws SQLException
      */
	public abstract int delete(String uid)throws SQLException;
	
	/**lista todos os registros
	 * 
	 * @return
	 */
	public List<Elemento> listAll ()throws SQLiteException;
	
	public int getDatabaseVersion();
	public List<Agregador> listSource () throws SQLiteException ;
	
	/**
	 * Atualiza a nova lista no banco, após deletar todos os registros existentes.
	 * @param lista Nova lista de elementos.
	 * @param newKey Nova chave.
	 * @return True ok.
	 */
	public boolean updateNewList(List<Elemento> lista, String newKey);
}