package org.prasad.asymmetricencryptionapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class AsymmetricEncryption {

	private static final String ALGORITHM = "RSA";
	private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

	private KeyPairGenerator keyPairGenerator;
	private KeyPair keyPair;
	private Cipher cipher;

	public AsymmetricEncryption() throws Exception {

		keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		keyPairGenerator.initialize(1024);
		keyPair = keyPairGenerator.generateKeyPair();
		cipher = Cipher.getInstance(TRANSFORMATION);

	}

	public void encrypt(String fileName) throws Exception {
		
		
		File file = new File(fileName);

		String newFileName = file.getName() + ".encrypted";

		FileInputStream fileInputStream = new FileInputStream(fileName);
		FileOutputStream fileOutputStream = new FileOutputStream(newFileName);

		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
		FileOutputStream publicKeyFileOutputStream = new FileOutputStream("public.key");
		publicKeyFileOutputStream.write(x509EncodedKeySpec.getEncoded());

		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
		FileOutputStream privateFileOutputStream = new FileOutputStream("private.key");
		privateFileOutputStream.write(pkcs8EncodedKeySpec.getEncoded());

		cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());

		byte[] input = new byte[117];
		int byteRead;

		while ((byteRead = fileInputStream.read(input)) != -1) {
			byte[] output = cipher.update(input, 0, byteRead);
			if (output != null) {
				fileOutputStream.write(cipher.doFinal());
			}
		}

		byte[] output = cipher.doFinal();
		if (output != null) {
			fileOutputStream.write(output);
		}

		fileInputStream.close();
		fileOutputStream.flush();
		fileOutputStream.close();

		publicKeyFileOutputStream.close();
		privateFileOutputStream.close();

		// Delete original file
		Path path = FileSystems.getDefault().getPath("", fileName);
		Files.deleteIfExists(path);

		System.out.println("Successfully Encrypted...!");
	}

	public void decrypt(String fileName) throws Exception {
		
		String newFileName = fileName.replace(".encrypted", "");
		System.out.println(newFileName);

		FileInputStream fileInputStream = new FileInputStream(fileName);
		FileOutputStream fileOutputStream = new FileOutputStream(newFileName);

		File filePrivateKey = new File("private.key");
		FileInputStream privateKeyFileInputStream = new FileInputStream(filePrivateKey);
		byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
		privateKeyFileInputStream.read(encodedPrivateKey);

		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);

		Key privateKey = keyFactory.generatePrivate(privateKeySpec);

		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		byte[] input = new byte[128];
		int read;
		while ((read = fileInputStream.read(input)) != -1) {
			byte[] output = cipher.update(input, 0, read);
			if (output != null) {
				fileOutputStream.write(cipher.doFinal());
			}
		}

		fileInputStream.close();
		fileOutputStream.flush();
		fileOutputStream.close();

		privateKeyFileInputStream.close();
		privateKeyFileInputStream.close();

		// Delete encrypted file
		Path path = FileSystems.getDefault().getPath("", fileName);
		Files.deleteIfExists(path);

		System.out.println("Successfully Decrypted..!");
	}
}

