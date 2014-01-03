/* eMafiaServer - StringFunctions.java
GNU GENERAL PUBLIC LICENSE V3
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaServer.includes;

import java.io.UnsupportedEncodingException;
import java.util.Random;

//import eMafiaServer.includes.classes.GameObjects.Character;

public class StringFunctions {
	//static java.lang.reflect.Method method;

	public static boolean isInteger(String s){
		if(s != null && s != "")return isInteger(s,10);
		else return false;
	}
	private static boolean isInteger(String s, int radix){
		if(s.isEmpty()) return false;
		for(int i = 0; i < s.length(); i++) {
			if(i == 0 && s.charAt(i) == '-') {
				if(s.length() == 1) return false;
				else continue;
			}
			if(Character.digit(s.charAt(i),radix) < 0) return false;
		}
		return true;
	}
	public static boolean betweenInt(int value, int from, int to){
		boolean bool = false;
		if(value >= from)if(value <= to)bool = true;
		if(value >= to)if(value <= from)bool = true;
		return bool;
	}
	/**
	 * Output a randomly generated name
	 * @return string
	 */
	public static String make_rand_name(){
		String name = "";
		Random rand = new Random();//makes new random number
		String[] firstnames = {
			"Alphonse",
			"Archie",
			"Babbo",
			"Benny",
			"Billy",
			"Bosco",
			"Bruno",
			"Bugatti",
			"Bugsy",
			"Carlo",
			"Carlito",
			"Charles",
			"Chico",
			"Dante",
			"Dino",
			"Enrico",
			"Fabrizio",
			"Fat Larry",
			"Figaro",
			"Flavio",
			"Frank",
			"Freddy",
			"George",
			"Giovanni",
			"Hector",
			"Jerome",
			"Joey",
			"Joseph",
			"Lambo",
			"Lil Willy",
			"Luigi",
			"Mario",
			"Merlino",
			"Micheal",
			"Micky",
			"Nando",
			"Napoleon",
			"Pavarotti",
			"Pablo",
			"Picasso",
			"Romeo",
			"Salvatore",
			"Skippy",
			"Sonny",
			"Sonya",
			"Tommy",
			"Vic",
			"Vincent",
			"William",
			"Yvette"
		};
		String[] lastnames = {
			"Amarillo",
			"Aviena",
			"Ballafuco",
			"Ballerino",
			"Barone",
			"Bonaduce",
			"Brantano",
			"Bravo",
			"Capo",
			"Cappuccino",
			"Colombo",
			"Cornetto",
			"De Niro",
			"Diego",
			"Elric",
			"Estelle",
			"Gallo",
			"Gallucio",
			"Gambino",
			"Genovese",
			"Guantanamo",
			"Gucci",
			"Johnson",
			"Lexington",
			"Liberace",
			"Lorenzo",
			"Louie",
			"Luciano",
			"Maranzano",
			"Masseria",
			"Menendez",
			"Montana",
			"Panini",
			"Pesto",
			"Prada",
			"Primo",
			"Profaci",
			"Rameriz",
			"Ryvita",
			"Sassoon",
			"Senza",
			"Soprano",
			"Valentino",
			"Williams",
			"Yager"
		};
		String[] subnames = {
			"The Big",
			"The Fat",
			"The Fella",
			"The Great",
			"The Kid",
			"The Meek",
			"The Mook",
			"The Nut",
			"The Sharp",
			"The Short",
			"The Small",
			"The Spook",
			"The Strong",
			"The Tall",
			"The Thin",
			"The Toad",
			"The Troll",
			"The Weak",
			"The Weasel",
			"XIII"
		};

		name = firstnames[rand.nextInt(firstnames.length)];
		if(rand.nextInt(101) <= 90){
			name += " "+lastnames[rand.nextInt(lastnames.length)];
		}
		if(rand.nextInt(101) <= 12){
			name += " "+subnames[rand.nextInt(subnames.length)];
		}

		return name;
	}
	/**
	 * Encrypts a String to MD5
	 * @param md5 String
	 * @return String encrypted to MD5
	 */
	public static String MD5(String str) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(str.getBytes("UTF-8"));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			return sb.toString();
		}
		catch (java.security.NoSuchAlgorithmException e) {}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @param str String
	 * @return String with \n \r and \0 removed from front and back.
	 */
	public static String stripEnds(String str){
		str = str.replace("\n", "");
		str = str.replace("\r", "");
		str = str.replace("\0", "");
		return str;
	}
	/**
	 * @param str String
	 * @param start Starting position of char in string
	 * @param length How many characters to return
	 * @return String with -length- number of characters
	 */
	public static String substr(String str, int start, int length){
		return str.substring(start,Math.min(str.length(), length));
	}
	/**
	 * @param str String
	 * @return String with only the single last char from provided string
	 */
	public static String substrLastChar(String str){
		return str.substring(str.length()-1);
	}
	/**
	 * @param str String
	 * @param length Number of characters
	 * @return String of last number of chars from provided string
	 */
	public static String substrLastChar(String str, int length){
		return str.substring(str.length()-(1*length));
	}
//////////////////
////HTML Edits////
//////////////////
	public static String HTMLColor(String hexcolor,String string){
		return "<font color=\"#"+hexcolor+"\">"+string+"</font>";
	}

	public static char rndChar () {
		int rnd = (int) (Math.random() * 52); // or use Random or whatever
		char base = (rnd < 26) ? 'A' : 'a';
		return (char) (base + rnd % 26);

	}

	private static byte[] zeroPad(int length, byte[] bytes) {
		byte[] padded = new byte[length]; // initialized to zero by JVM
		System.arraycopy(bytes, 0, padded, 0, bytes.length);
		return padded;
	}

	public static String Base64encode(String string) {
		String base64code = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "0123456789" + "+/";
		String encoded = "";
		byte[] stringArray;
		try {
			stringArray = string.getBytes("UTF-8");  // use appropriate encoding string!
		} catch (Exception ignored) {
			stringArray = string.getBytes();  // use locale default rather than croak
		}
		// determine how many padding bytes to add to the output
		int paddingCount = (3 - (stringArray.length % 3)) % 3;
		// add any necessary padding to the input
		stringArray = zeroPad(stringArray.length + paddingCount, stringArray);
		// process 3 bytes at a time, churning out 4 output bytes
		// worry about CRLF insertions later
		for (int i = 0; i < stringArray.length; i += 3) {
			int j = ((stringArray[i] & 0xff) << 16) +
				((stringArray[i + 1] & 0xff) << 8) +
				(stringArray[i + 2] & 0xff);
			encoded = encoded + base64code.charAt((j >> 18) & 0x3f) +
				base64code.charAt((j >> 12) & 0x3f) +
				base64code.charAt((j >> 6) & 0x3f) +
				base64code.charAt(j & 0x3f);
		}
		// replace encoded padding nulls with "="
		//return splitLines(encoded.substring(0, encoded.length() - paddingCount) + "==".substring(0, paddingCount));
		return encoded.substring(0, encoded.length() - paddingCount) + "==".substring(0, paddingCount);

	}

}
