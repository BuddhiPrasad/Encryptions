package org.prasad.symmetricencryptionapp;import java.util.Scanner;

public class Runner {

	public static void main(String[] args) throws Exception {

		Scanner sc = new Scanner(System.in);

		SymmetricEncryption se = new SymmetricEncryption();
		
		System.out.println("Welcome For Symmetric Encryption....");

		System.out.println("---Press 1 For Encrypt the File---");
		System.out.println("---Press 2 For Encrypt the File---");
		int op = sc.nextInt();

		switch (op) {
		case 1:
			System.out.print("Enter File Name (With Extention) : ");
			String plainFileName = sc.next();
			se.encrypt(plainFileName);
			break;
		case 2:
			System.out.print("Enter Encrypted File Name (With Extention) : ");
			String encryptedFileName = sc.next();
			se.decrypt(encryptedFileName);
			break;
		default:
			System.out.println("Wrong Input!");
		}

		sc.close();
	}
}
