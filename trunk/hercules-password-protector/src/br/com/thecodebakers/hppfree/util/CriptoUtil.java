package br.com.thecodebakers.hppfree.util;

import java.security.InvalidKeyException;
import java.security.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

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
