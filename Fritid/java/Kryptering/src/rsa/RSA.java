package rsa;

import funktioner.Funktioner;
import isPrime.Prime;

public class RSA {

	private int d, e, n;
	private int lowerBound = 500, upperBound = 1000; //inkulsive lower till inklusive upper, 500, 1000
	
	public RSA(){
		prepareKeys();
	}
	
	public RSA(int publicKey, int pq){
		n =  pq;
		e = publicKey;
	}
	
	private void prepareKeys(){
		//hitta primtalen
		int p = findPrime();
		int q = 0;
		do{
			q = findPrime();
		}while(p == q);
		
		int fiAvN = (p-1)*(q-1);
		n = p*q;
		
		//hitta d
		do{
			d = (int)(Math.random()*(fiAvN-1)+1);
		}while(Funktioner.GCD(d, fiAvN) != 1);
		
		//hitta e
		e = Funktioner.findInvers(d, fiAvN);
		if(e < 0)
			e += fiAvN;
		
	}
	
	private int findPrime(){
		int tal;
		
		do{
			tal = (int)(Math.random()*(upperBound-lowerBound+1)+lowerBound);
		}while(!Prime.isPrime(tal));
		
		return tal;
	}
	
	public int encrypt(int msg){
		int tal = Funktioner.bigExponentModulos(msg, e, n);
		if(tal < 0) tal += n;
		return tal;
	}
	
	public int decrypt(int msg){
		return Funktioner.bigExponentModulos(msg, d, n);
	}
	
	public int getN(){return n;}
	public int getD(){return d;}
	public int getE(){return e;}
	
}
