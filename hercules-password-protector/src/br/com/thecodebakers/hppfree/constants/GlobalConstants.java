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


package br.com.thecodebakers.hppfree.constants;

/**
 * Guarda algumas constantes comuns, principalmente usadas na função de backup.
 * Isto evita "números mágicos" dentro do código-fonte (Fowler).
 * 
 * @author Cleuton Sampaio e Francisco Rogrigues - thecodebakers@gmail.com
 *
 */
public class GlobalConstants {
	public static final int RETORNO_OK = 0;
	public static final int ARQUIVO_BACKUP_INVALIDO = 10;
	public static final int HASHCODE_BACKUP_INVALIDO = 20;
	public static final int ERRO_AO_SALVAR_BACKUP = 30;
	public static final int ERRO_PROBLEMA_MEDIA_SALVAR_BACKUP = 40;
	public static final int ERRO_RESTAURAR_ARQUIVO_NAO_EXISTE = 50;
	public static final int ERRO_LER_BACKUP = 60;
	public static final int ERRO_PROBLEMA_MEDIA_LER_BACKUP = 70;
	public static final int ERRO_HASHCODE_ENTRADA_INVALIDO = 80;
	public static final int ERRO_VERSAO_BACKUP_INCOMPATIVEL = 90;
	public static final int ERRO_RESTAURAR_BACKUP_NO_BANCO = 100;
}
