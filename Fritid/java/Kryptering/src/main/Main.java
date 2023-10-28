package main;

import des.DES;

public class Main {

	public static void main(String[] args) {
		/*
		RSA server = new RSA();
		RSA client = new RSA(server.getE(), server.getN());
		
		int msg = 1337;
		int crypted = client.encrypt(msg);
		int decrypted = server.decrypt(crypted);
		
		System.out.println("d = "+server.getD()+" : e = "+server.getE()+" : n = "+server.getN());
		System.out.println("msg = "+msg+" : crypted = "+crypted+" : decrypted = "+decrypted);
		*/
		
		
		DES des = new DES();
		des.generateKeysHex("1337");
		
		String msg = "a       a        ";
		String krypt = des.encrypt(msg);
		String back = des.decrypt(krypt);
		
		System.out.println(krypt);
		System.out.println(back);
		
	}
	
}
