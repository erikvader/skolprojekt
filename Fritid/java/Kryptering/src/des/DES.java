package des;

import java.nio.charset.Charset;
import java.util.Arrays;

public class DES {
	
	//tables
	private final int[] PC_1 = {
							57, 49, 41, 33, 25, 17, 9,
							1, 58, 50, 42, 34, 26, 18,
							10, 2, 59, 51, 43, 35, 27,
							19, 11, 3, 60, 52, 44, 36,
							63, 55, 47, 39, 31, 23, 15,
							7, 62, 54, 46, 38, 30, 22,
							14, 6, 61, 53, 45, 37, 29,
							21, 13, 5, 28, 20, 12, 4
							};
	
	private final int[] PC_2 = {
								14, 17, 11, 24, 1, 5,
								3, 28, 15, 6, 21, 10,
								23, 19, 12, 4, 26, 8,
								16, 7, 27, 20, 13, 2,
								41, 52, 31, 37, 47, 55,
								30, 40, 51, 45, 33, 48,
								44, 49, 39, 56, 34, 53,
								46, 42, 50, 36, 29, 32
								};
	
	private final int[] LEFT_SHIFT = {
									1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
									};
	
	private final int[] IP = {
							58, 50, 42, 34, 26, 18, 10, 2,
							60, 52, 44, 36, 28, 20, 12, 4,
							62, 54, 46, 38, 30, 22, 14, 6, 
							64, 56, 48, 40, 32, 24, 16, 8,
							57, 49, 41, 33, 25, 17, 9, 1,
							59, 51, 43, 35, 27, 19, 11, 3,
							61, 53, 45, 37, 29, 21, 13, 5,
							63, 55, 47, 39, 31, 23, 15, 7
							};
	
	
	private final int[] IP_1 = {
								40, 8, 48, 16, 56, 24, 64, 32,
								39, 7, 47, 15, 55, 23, 63, 31,
								38, 6, 46, 14, 54, 22, 62, 30,
								37, 5, 45, 13, 53, 21, 61, 29,
								36, 4, 44, 12, 52, 20, 60, 28,
								35, 3, 43, 11, 51, 19, 59, 27,
								34, 2, 42, 10, 50, 18, 58, 26,
								33, 1, 41, 9, 49, 17, 57, 25
								};
	
	private final int[] E = {
							32, 1, 2, 3, 4, 5,
							4, 5, 6, 7, 8, 9,
							8, 9, 10, 11, 12, 13,
							12, 13, 14, 15, 16, 17,
							16, 17, 18, 19, 20, 21,
							20, 21, 22, 23, 24, 25,
							24, 25, 26, 27, 28, 29,
							28, 29, 30, 31, 32, 1
							};
	
	private final int[][][] SBOXES = { //box, y, x
									{{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7}, {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8}, {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0}, {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}}, //S1
									{{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10}, {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5}, {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15}, {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}},
									{{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8}, {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1}, {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7}, {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}},
									{{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15}, {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9}, {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4}, {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}},
									{{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9}, {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6}, {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14}, {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}},
									{{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11}, {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8}, {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6}, {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}},
									{{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1}, {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6}, {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2}, {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}},
									{{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7}, {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2}, {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8}, {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}}
									};

	private final int[] P = {
							16, 7, 20, 21,
							29, 12, 28, 17,
							1, 15, 23, 26,
							5, 18, 31, 10,
							2, 8, 24, 14,
							32, 27, 3, 9,
							19, 13, 30, 6,
							22, 11, 4, 25
							};
	
	//keys k
	private long[] keys; //-16st 0- + -48 bits-
	
	public DES(){
		keys = new long[16];
	}
	
	public String encrypt(String msg){
		//göra b1 som har längden som är en multipel av 16
		byte[] b = msg.getBytes(Charset.forName("UTF-8"));
		int rest = (8 - (b.length % 8)) % 8;
		byte[] b1 = new byte[b.length + rest];
		for(int i = 0; i < b.length; i++)
			b1[i] = b[i]; 
		
		for(int i = b.length; i < b1.length; i++){
			b1[i] = 0x0020; //space
		}

		//dela upp i blocks av 64
		long[] blocks = new long[b1.length/8];
		for(int i = 0; i < blocks.length; i++){
			byte[] c = Arrays.copyOfRange(b1, i*8, i*8+8);
			blocks[i] = concatBytes64(c, 8);
		}
		
		//kryptera
		long[] crypted = new long[blocks.length];
		for(int i = 0; i < crypted.length; i++){
			crypted[i] = encode(blocks[i]);
		}
		
		String sista = longsToHex(crypted);
		
		return sista;
	}
	
	public String decrypt(String msg){
		
		//dela in i longs
		long[] longs = hexToLongs(msg);
		
		//flippa
		flipKeys();
		
		long[] decrypted = new long[longs.length];
		for(int i = 0; i < longs.length; i++){
			decrypted[i] = encode(longs[i]);
		}
		
		//flippa tillbax
		flipKeys();
		
		//gör om till UTF-8
		String org = "";
		Charset cs = Charset.forName("UTF-8");
		for(int i = 0; i < decrypted.length; i++){
			byte[] b = splitLong(decrypted[i], 8, 8);
			org += new String(b, cs);
		}
		
		while(org.endsWith(" ")){
			org = org.substring(0, org.length()-1);
		}
		
		return org;
	}
	
	public String encryptHex(String msg){
		//padding
		int rest = (16 - (msg.length() % 16)) % 16;
		for(int i = 0; i < rest; i++)
			msg = "0"+msg;
		
		//hitta blocks
		long[] blocks = new long[msg.length()/16];
		for(int i = 0; i < blocks.length; i++){
			//blocks[i] = Long.parseLong(msg.substring(i*16, i*16+16), 16);
			blocks[i] = hexToLong(msg.substring(i*16, i*16+16));
		}
		
		//kryptera
		long[] crypted = new long[blocks.length];
		for(int i = 0; i < crypted.length; i++){
			crypted[i] = encode(blocks[i]);
		}
		
		String sista = longsToHex(crypted);
		
		return sista;
		
	}
	
	public String decryptToHex(String msg){
		
		//hex to longs
		long[] longs = hexToLongs(msg);

		//flippa nycklar
		flipKeys();
		
		//avkryptera
		long[] avcrypted = new long[longs.length];
		for(int i = 0; i < avcrypted.length; i++){
			avcrypted[i] = encode(longs[i]);
		}
		
		//flippa tillbaka
		flipKeys();
		
		String org = ""; //fixa så att bevara nollor i början
		for(int i = 0; i < avcrypted.length; i++){
			//org += Long.toHexString(avcrypted[i]).toUpperCase();
			org += longToHex(avcrypted[i]).toUpperCase();
		}
		
		while(org.startsWith("0")){
			org = org.substring(1, org.length());
		}
		
		return org;
	}
	
	public void generateKeysHex(String key){
		while(key.length() < 16){
			key = "0"+key;
		}
		byte[] b = new byte[8];
		for(int i = 0; i < key.length()/2; i++){
			b[i] = Byte.parseByte(String.valueOf(key.charAt(2*i)), 16);
			b[i] = (byte)(b[i] << 4);
			b[i] += Byte.parseByte(String.valueOf(key.charAt(2*i+1)), 16);
		}
		
		generateKeys(b);
	}
	
	public void generateKeysString(String key){
		//gör key till 64 bitars long
		byte[] b = key.getBytes(Charset.forName("UTF-8"));
		byte[] lb = new byte[8];
		
		for(int i = 0; i < b.length && i < 8; i++){
			lb[i] = b[i];
		}
		
		for(int i = b.length; i < lb.length && i < 8; i++){
			lb[i] = 0x0020; //space
		}
			
		generateKeys(lb);
		
	}
	
	  ////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////Helper Functions///////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	
	private long encode(long l){
		long ip = permuteLong(l, IP, 0);
		
		//göra 16 steg
		int[] left = new int[2];
		int[] right = new int[2];
		int row = 0;
		int prevRow = -1;
		
		left[0] = (int)(ip >>> 32);
		right[0] = (int)ip;
		
		for(int i = 1; i <= 16; i++){
			row++;
			prevRow++;
			int index = row%2;
			int prevIndex = prevRow%2;
			
			left[index] = right[prevIndex];
			right[index] = left[prevIndex] ^ f(right[prevIndex], keys[i-1]);
		}
		
		//sista vara på 0; vända ordning
		long reversed = right[0];
		reversed = reversed << 32;
		reversed |= (left[0] & 0xFFFFFFFFl);
		
		//permute
		long sista = permuteLong(reversed, IP_1, 0);
		
		return sista;
		
	}
	
	private int f(int r, long k){
		long e = permuteLong((long)r, E, 32);
		long xored = e ^ k;
		
		//s-boxes
		byte[] groups = splitLong(xored, 6, 8);
		byte[] groups4 = new byte[8]; //output
		
		for(int i = 0; i < groups.length; i++){
			groups4[i] = sbox(groups[i], i);
		}
		
		//permute P
		long con = concatBytes64(groups4, 4);
		
		int res = (int)permuteLong(con, P, 32);
		
		return res;
	}
	
	private byte sbox(byte b, int box){
		int y = ((b >>> 4) & 0b10) + (b & 0b1);
		int x = (b >>> 1) & 0b01111;
		return (byte)SBOXES[box][y][x];
	}
	
	private long[] hexToLongs(String s){
		long[] longs = new long[s.length()/16];
		
		for(int i = 0; i < longs.length; i++){
			longs[i] = hexToLong(s.substring(i*16, i*16+16));
			
		}
		
		return longs;
	}
	
	private long hexToLong(String s){
		long l = 0;
		for(int i = 0; i < 16; i++){
			l = l << 4;
			l = l | Long.parseLong(s.substring(i, i+1), 16);
		}
		return l;
	}
	
	private String longsToHex(long[] longs){
		String hex = "";
		
		for(int i = 0; i < longs.length; i++){
			hex += longToHex(longs[i]);
		}
		
		return hex;
	}
	
	private String longToHex(long l){
		String s = "";
		long marker = 0b1111 << 60;
		for(int i = 0; i < 16; i++){
			long grej = (l & marker) >>> 60;
			s += Long.toHexString(grej).toUpperCase();
			l = l << 4;
		}
		return s;
	}
	
	private void generateKeys(byte[] b){
		long lKey = concatBytes64(b, 8);
		
		//permute PC-1
		long lKeyP = permuteLong(lKey, PC_1, 0); //-8st 0 skräp- + -56 bits key-
		
		//splitta
		int[] c = new int[17];
		int[] d = new int[17];

		c[0] = (int)(lKeyP >>> 28);
		d[0] = (int)(lKeyP & 0xFFFFFFF);
		
		//shifta
		for(int i = 1; i <= 16; i++){
			c[i] = shiftLeft(c[i-1], LEFT_SHIFT[i-1], 4);
			d[i] = shiftLeft(d[i-1], LEFT_SHIFT[i-1], 4);
		}
		
		//generera alla 16 k
		for(int i = 0; i < 16; i++){
			long cd = concatCD(c[i+1], d[i+1]);
			keys[i] = permuteLong(cd, PC_2, 8);
		}
		
		/*
		//**DEBUG**
		//skriv ut
		for(int i = 0; i < keys.length; i++){
			printBinaryLong(keys[i]);
		}
		*/
	}
	
	private long concatCD(int c, int d){ //-8st 0- + -56 bits-
		long l = 0;
		l += c;
		l = l << 28;
		l += d;
		return l;
	}
	
	//offset från vänster
	private int shiftLeft(int a, int amount, int offset){
		int b = (a << amount);
		int c = a & ((int)((1 << (amount+1))-1) << 32-amount-offset); //antal ettor vid vänster lIgnore från kanten 
		int d = b | (c >>> 32-amount-offset);
		int e = ((1 << offset)-1) << 32-offset;
		e = ~e;
		d = d & e;
		
		return d;
	}
	
	private void flipKeys(){
		for(int i = 0; i < keys.length/2; i++){
			long temp = keys[i];
			keys[i] = keys[keys.length - 1 - i];
			keys[keys.length -1 -i] = temp;
		}
	}
	
	/*
	private int shiftRight(int a, int amount){
		int b = (a >>> amount);
		int c = a & (int)((1 << (amount+1))-1);
		int d = b | (c << 32-amount);
		
		return d;
	}
	*/
	
	//length som ska tas ut ur long och göras om till byte, alltså max 8
	//Size hur många bytes man vill ha
	private byte[] splitLong(long l, int length, int size){
		byte[] b = new byte[size];
		long marker = (1 << length)-1;
		for(int i = size-1; i >= 0; i--){
			b[i] = (byte)(l & marker);
			l = l >>> length;
		}
		return b;
	}
	
	//offset från vänster i l
	private long permuteLong(long l, int[] table, int offset){
		long re = 0;
		for(int i = 0; i < table.length; i++){
			re = re << 1;
			re += bitAtLeft(l, table[i], offset);
			
		}
		return re;
	}
	
	//length antalet bitar i varje byte som man vill ha (de onödiga bitarna ska vara 0)
	private long concatBytes64(byte[] b, int length){
		long ans = 0;
		for(int i = 0; i < b.length; i++){
			ans = ans << length;
			ans = ans | (b[i] & 0b11111111); //b[i] fylls med ettor i början
		}
		return ans;
	}
	
	/*
	private int bitAtRight(long l, int pos){
		return (int)((l >> (pos-1)) & 1);
	}
	*/
	
	//offset från vänster, antal bitar som ska ignoreras
	//l talet som ska kollas
	//pos positionen från vänster, 1 är positinen längst ut.
	private int bitAtLeft(long l, int pos, int offset){
		return (int)((l >> ((64-offset) - pos)) & 1);
	}
	
	public static void printBinaryLong(long l){
		System.out.println(String.format("%64s", Long.toBinaryString(l)).replaceAll(" ", "0"));
	}
	
}
