package com.truedash.seccurity.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

/**
 * This class is used to encrypt and decrypt sensitive data before transferring over wire
 * 
 * @author rnagulapalle
 *
 */
@Component
public class EncryptDecryptUtil {

	//TODO:Store username,password and SAL2 in MongoDB
	public static String username = "truedash@secretkey.me";
    public static String password = "dummypasswordchangeme";
    public static String SALT2 = "truedash_salt_yummy!";
    public static Cipher cipher;
    
    public static String encrypt(String text) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
    	// Get the Key
	    byte[] key = (SALT2 + username + password).getBytes("UTF-8");
	    MessageDigest sha = MessageDigest.getInstance("SHA-1");
	    key = sha.digest(key);
	    key = Arrays.copyOf(key, 16); // use only first 128 bit

	    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

	    // Instantiate the cipher
	    Cipher cipher = Cipher.getInstance("AES");
	    
    	cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    	byte[] encrypted = cipher.doFinal((text).getBytes());
		return new String(Base64.encodeBase64URLSafeString(encrypted));
    }
    
    public static String decrypt(String encrypted) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
    	// Get the Key
    	if(encrypted == null) throw new IllegalArgumentException("Encrypted string can not be null");
    	
	    byte[] key = (SALT2 + username + password).getBytes("UTF-8");
	    MessageDigest sha = MessageDigest.getInstance("SHA-1");
	    key = sha.digest(key);
	    key = Arrays.copyOf(key, 16); // use only first 128 bit

	    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

	    // Instantiate the cipher
	    Cipher cipher = Cipher.getInstance("AES");
	    
	    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
	    byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted.getBytes()));
		return new String((original));
    }
    
    public static void main(String argsp[]) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException{
    	EncryptDecryptUtil test = new EncryptDecryptUtil();
    	String encrypted = test.encrypt("b231b3e7-2299-4d85-a468-a6f2ec41e6a4");
    	System.out.println(Base64.encodeBase64String(encrypted.getBytes()));
    	System.out.println(test.decrypt(encrypted));
    }
}
