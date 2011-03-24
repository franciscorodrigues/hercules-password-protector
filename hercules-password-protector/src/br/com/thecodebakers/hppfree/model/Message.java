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

/**
 * Esta classe representa uma mensagem gerada pelo BO. Pode ser consultada. Na verdade, deveríamos
 * usar mais esta classe e não estamos fazendo. Depois, faremos um refactoring para isto.
 * Seria bom que ela também oferecesse a posibilidade de gerar AlertDialog. 
 * 
 * @author Cleuton Sampaio e Francisco Rogrigues - thecodebakers@gmail.com
 *
 */
public class Message {
	private int code;
	private String text;
	
	public int getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

	public Message(int code, String text) {
		super();
		this.code = code;
		this.text = text;
	}
}
