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



package br.com.thecodebakers.hppfree.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;

/**
 * Principal entidade da aplicação. Uma instância de "Elemento" representa um registro guardado no
 * cofre. A propriedade "uuid" serve como chave primária, mas a propriedade "Local" é a ordem natural.
 * 
 * O método "hashCode()" foi sobrescrito. O Hashcode gerado pela classe é usado ao decriptar, de modo
 * a verificar se a senha utilizada foi válida.
 * 
 * @author Cleuton Sampaio e Francisco Rogrigues - thecodebakers@gmail.com
 *
 */
public class Elemento implements Comparable<Object> {
	private String local;
	private String textoSecreto;
	private String UUID;
	private byte[] lob ;
	private int hashArmazenado;
	
	
	
	public int getHashArmazenado() {
		return hashArmazenado;
	}
	public void setHashArmazenado(int hashArmazenado) {
		this.hashArmazenado = hashArmazenado;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}

	private Date   dataModificacao;
		
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getTextoSecreto() {
		return textoSecreto;
	}
	public void setTextoSecreto(String textoSecreto) {
		this.textoSecreto = textoSecreto;
	}
	public Date getDataModificacao() {
		return dataModificacao;
	}
	public void setDataModificacao(Date dataModificacao) {
		this.dataModificacao = dataModificacao;
	}

	public byte[] getLob() {
		return lob;
	}
	public void setLob(byte[] lob) {
		this.lob = lob;
	}
	@Override
	public boolean equals(Object o) {
		return this.getUUID().equals(((Elemento) o).getUUID());
	}
	
	@Override
	public int hashCode() {
		DateFormat df = DateFormat.getDateTimeInstance();
		String texto = this.getLocal() + this.getTextoSecreto() + 
					   df.format(this.getDataModificacao());
	    MessageDigest md = null;  
	    try {  
	        md = MessageDigest.getInstance("MD5");  
	    } catch (NoSuchAlgorithmException e) {  
	        e.printStackTrace();  
	    }  

	    return new BigInteger(md.digest(texto.getBytes())).intValue();
	}
	
	public int compareTo(Object another) {
		return this.getLocal().compareToIgnoreCase(((Elemento) another).getLocal());
	}

	
	
}
