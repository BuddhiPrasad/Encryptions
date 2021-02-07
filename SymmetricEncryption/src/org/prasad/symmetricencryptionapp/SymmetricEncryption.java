package org.prasad.symmetricencryptionapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SymmetricEncryption {

	private static final String ALORITHM = "AES";
	private static final String TRANSFORMATION = "AES";

	private KeyGenerator keyGenerator;
	private SecretKey secretKey;
	private Cipher cipher;

	private String password = "thisisthepassword";
	private KeyStore keyStore;
	private KeyStore.PasswordProtection keyPasswordProtection;
	private KeyStore.SecretKeyEntry keySecretKeyEntry;

	public SymmetricEncryption() throws Exception {

		keyGenerator = KeyGenerator.getInstance(ALORITHM);
		secretKey = keyGenerator.generateKey();
		cipher = Cipher.getInstance(TRANSFORMATION);

		keyStore = KeyStore.getInstance("JCEKS");

	}

	public void encrypt(String fileName) throws Exception {


		File file = new File(fileName);

		String newFileName = file.getName() + ".encrypted";

		FileInputStream fileInputStream = new FileInputStream(file);
		FileOutputStream fileOutputStream = new FileOutputStream(newFileName);

		keyStore.load(null, password.toCharArray());

		keyPasswordProtection = new KeyStore.PasswordProtection(password.toCharArray());
		keySecretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
		keyStore.setEntry("secretKeyAlias1", keySecretKeyEntry, keyPasswordProtection);

		FileOutputStream keyStoreOutputStream = new FileOutputStream("MyKeyStore");
		keyStore.store(keyStoreOutputStream, password.toCharArray());

		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		byte[] input = new byte[64];
		int byteRead;

		while ((byteRead = fileInputStream.read(input)) != -1) {
			byte[] output = cipher.update(input, 0, byteRead);
			if (output != null) {
				fileOutputStream.write(output);
			}
		}

		byte[] output = cipher.doFinal();
		if (output != null) {
			fileOutputStream.write(output);
		}

		fileInputStream.close();
		fileOutputStream.flush();
		fileOutputStream.close();

		// Delete original file
		Path path = FileSystems.getDefault().getPath("", fileName);
		Files.deleteIfExists(path);

		System.out.println("Successfully Encrypted..!");
	}

	public void decrypt(String fileName) throws Exception {

		

		String newFileName = fileName.replace(".encrypted", "");
		System.out.println(newFileName);

		FileInputStream keyStoreInputStream = new FileInputStream("MyKeyStore");
		keyStore.load(keyStoreInputStream, password.toCharArray());

		Key key = keyStore.getKey("secretKeyAlias1", password.toCharArray());

		FileInputStream fileInputStream = new FileInputStream(fileName);
		FileOutputStream fileOutputStream = new FileOutputStream(newFileName);

		cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.DECRYPT_MODE, key);

		byte[] input = new byte[53];
		int read;
		while ((read = fileInputStream.read(input)) != -1) {
			byte[] output = cipher.update(input, 0, read);
			if (output != null) {
				fileOutputStream.write(output);
			}
		}

		byte[] output = cipher.doFinal();
		if (output != null) {
			fileOutputStream.write(output);
		}

		fileInputStream.close();
		fileOutputStream.flush();
		fileOutputStream.close();

		// Delete encrypted file
		Path path = FileSystems.getDefault().getPath("", fileName);
		Files.deleteIfExists(path);

		System.out.println("Successfully Decrypted..!");
	}

}
