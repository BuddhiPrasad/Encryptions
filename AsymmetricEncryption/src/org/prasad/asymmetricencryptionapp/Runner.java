package org.prasad.asymmetricencryptionapp;import java.util.Scanner;

public class Runner {

	public static void main(String[] args) throws Exception {

		Scanner sc = new Scanner(System.in);

		AsymmetricEncryption ae = new AsymmetricEncryption();
		
		System.out.println("Welcome For Asymmetric Encryption....");

		System.out.println("---Press 1 For Encrypt the File---");
		System.out.println("---Press 2 For Decrypt the File---");
		int op = sc.nextInt();

		switch (op) {
		case 1:
			System.out.print("Enter File Name(With Extention): ");
			String plainFileName = sc.next();
			ae.encrypt(plainFileName);
			break;
		case 2:
			System.out.print("Enter Encrypted File Name(With Extention): ");
			String encryptedFileName = sc.next();
			ae.decrypt(encryptedFileName);
			break;
		default:
			System.out.println("Wrong Input!");
		}

		sc.close();

	}
}