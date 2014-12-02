/**
 * FileName:     Cpabe.java
 * @Description: TODO() 
 * All rights Reserved, Designed By HangZhou YuanTiao LTD.
 * Copyright:    Copyright(C) 2013-2014 
 * Company       HangZhou YuanTiao LTD.
 * @author:    Nanio
 * @version    V1.0  
 * Createdate:         Nov 16, 2014 3:20:06 PM 
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * Nov 16, 2014       Nanio          1.0             1.0
 * Why & What is modified: <修改原因描述>
 */
package func.main;

import func.cpabe.AbeDecrypt;
import func.cpabe.AbeEncrypt;
import func.cpabe.AbeKengen;
import func.cpabe.AbeSetup;
import func.cpabe.object.InterValue;

public class Cpabe {

	
	/**
	 * 
	 * @Title: setup 
	 * @Description: TODO() 
	 * @param: @param pubfile
	 * @param: @param mkfile    
	 * @return: void    
	 * @throws
	 */
	public void setup(String pubfile, String mkfile){
		AbeSetup setup = new AbeSetup();
		setup.setup(pubfile, mkfile);
	}
	
	public void setup(InterValue value){
		AbeSetup setup = new AbeSetup();
		setup.setup(value);
	}
	

	/**
	 * 
	 * @Title: keygen 
	 * @Description: TODO() 
	 * @param: @param pubfile
	 * @param: @param mkfile
	 * @param: @param attfile
	 * @param: @param prifile
	 * @param: @param attList    
	 * @return: void    
	 * @throws
	 */
	public void keygen(String pubfile, String mkfile, String attfile, String prifile, String attList){
		AbeKengen gen = new AbeKengen();
		gen.keyGen(pubfile, mkfile, attfile, prifile, attList);
	}
	
	public void keygen(InterValue value, String list){
		AbeKengen gen = new AbeKengen();
		gen.keyGen(value, list);
	}

	/**
	 * 
	 * @Title: enc1 
	 * @Description: TODO() 
	 * @param: @param policy
	 * @param: @param pubfile
	 * @param: @param plaintext
	 * @param: @param outputfile
	 * @param: @return    
	 * @return: String    
	 * @throws
	 */
	public String encrypt(String policy, String pubfile, String plaintext, String outputfile){
		AbeEncrypt enc1 = new AbeEncrypt();
		String result  = enc1.encrypt(policy, pubfile, plaintext, outputfile);
		return result;
	}
	
	public String encrypt(InterValue value, String policy, String plaintext){
		AbeEncrypt enc1 = new AbeEncrypt();
		String result  = enc1.encrypt(value, policy, plaintext);
		return result;
	}

	/**
	 * 
	 * @Title: dec1 
	 * @Description: TODO() 
	 * @param: @param pubfile
	 * @param: @param attfile
	 * @param: @param encfile
	 * @param: @param outfile
	 * @param: @return    
	 * @return: String    
	 * @throws
	 */
	public String decrypt(String pubfile, String attfile, String prifile, String encfile, String outfile){
		AbeDecrypt dec = new AbeDecrypt();
		String result = dec.dec(pubfile, attfile, prifile, encfile, outfile);
		return result;
	}
	
	public String decrypt(InterValue value){
		AbeDecrypt dec = new AbeDecrypt();
		String result = dec.dec(value);
		return result;
	}
	
	private void testFile(){
		Cpabe cpabe = new Cpabe();
		String listStr ="(mytech,a,b,c,a2)";	
		//String listStr ="(aaa,aaabbb,ccc,ddd,eee,fff,hhh,iii)";
		cpabe.setup("pk.key", "mk.key");
		cpabe.keygen("pk.key", "mk.key","ak.key", "pri.key", listStr);
		cpabe.encrypt("(((((((mytech and a) and b) and c) or ((mymoney and a) and b)) or (mymakert and a)) or (mybank and a)) or (myshell and a))", "pk.key", "success!!", "encfile1");
		//cpabe.encrypt("(aaa and aaabbb and ccc and ddd and eee and fff and hhh and iii)", "pk.key", "success!!", "encfile1");
		//cpabe.encrypt("(mytech and a)", "pk.key", "success!!", "encfile1");

		String result = cpabe.decrypt("pk.key", "ak.key", "pri.key", "encfile1", "decfile");
	}
	
	private void testInterValue(){
		InterValue value = new InterValue();
		String attList = "(mytech,a,b,c)";
		String policy = "(((((((mytech and a) and b) and c) or ((mymoney and a) and b1)) or (mymakert and a)) or (mybank and a3)) or (myshell and a4))";
		String plainText = "success intervalue";
		setup(value);
		keygen(value, attList);
		encrypt(value, policy, plainText);
		if(decrypt(value) == null){
			System.out.printf("invalid policy");
		}
		value.printToString();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Cpabe cpabe = new Cpabe();
		//cpabe.testFile();
		cpabe.testInterValue();
	}
}