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



package br.com.thecodebakers.hppfree.util;

import java.security.InvalidKeyException;
import java.security.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * Classe utilitária para encriptar e decriptar os registros.
 * 
 * @author Cleuton Sampaio e Francisco Rogrigues - thecodebakers@gmail.com
 *
 */
public class CriptoUtil {

	 private static String algorithm = "DESede";  //triplo DES
     private static Key key = null;
     private static Cipher cipher = null;

     public static void setUp(String keyText) throws Exception {

    	
        cipher = Cipher.getInstance(algorithm);
        String chave = recheiaChave(keyText); 
        DESedeKeySpec desKeySpec = new DESedeKeySpec(chave.getBytes());
   	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
   	    SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
   	    key = secretKey;
     }
     
     private static String recheiaChave(String key) {
    	 String retorno = "";
    	 
    	 String chave = "";
    	 if (key.length() > 24) {
    		 chave = key.substring(0, 24);
    		 retorno = chave;
    	 }
    	 else {
    		 chave = key;
        	 StringBuffer buffer = new StringBuffer();
        	 int falta = 24 - chave.length();
        	 int pos = 0;
        	 for (int x = 0; x < falta; x++) {
        		 buffer.append(chave.charAt(pos));
        		 pos++;
        		 if (pos >= chave.length()) {
        			 pos = 0;
        		 }
        	 }
    		 retorno = chave + buffer.toString();
    	 }
    	 return retorno;
     }
    
     public static byte[] encrypt(String input)
         throws InvalidKeyException, 
                BadPaddingException,
                IllegalBlockSizeException {
         cipher.init(Cipher.ENCRYPT_MODE, key);
         byte[] inputBytes = input.getBytes();
         return cipher.doFinal(inputBytes);
     }

     public static String decrypt(byte[] encryptionBytes)
         throws InvalidKeyException, 
                BadPaddingException,
                IllegalBlockSizeException {
         cipher.init(Cipher.DECRYPT_MODE, key);
         byte[] recoveredBytes = cipher.doFinal(encryptionBytes);
         String recovered = new String(recoveredBytes);
         return recovered;
       }
	
}
