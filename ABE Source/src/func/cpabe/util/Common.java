/**
 * FileName:     AbeError.java
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

package func.cpabe.util;
import func.cpabe.object.AttributeKey;
import func.cpabe.object.CipherText;
import func.cpabe.object.MasterKey;
import func.cpabe.object.PrivateKey;
import func.cpabe.object.PublicKey;
import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Common {

	/**
	 * 
	 * @Title: suckFile 
	 * @Description: TODO(read byte[] from inputfile) 
	 * @param: @param inputfile
	 * @param: @return
	 * @param: @throws IOException    
	 * @return: byte[]    
	 * @throws
	 */
	public static byte[] suckFile(String inputfile) throws IOException {
		InputStream is = new FileInputStream(inputfile);
		int size = is.available();
		byte[] content = new byte[size];

		is.read(content);

		is.close();
		return content;
	}

	/**
	 * 
	 * @Title: spitFile 
	 * @Description: TODO(write byte[] into outputfile) 
	 * @param: @param outputfile
	 * @param: @param b
	 * @param: @throws IOException    
	 * @return: void    
	 * @throws
	 */

	public static void spitFile(String outputfile, byte[] b) throws IOException {
		OutputStream os = new FileOutputStream(outputfile);
		os.write(b);
		os.close();
	}
	
	public static PublicKey readPublicKey(String pubfile){
		byte[] pubByte = null, pubBase;
		PublicKey pub = new PublicKey();
		
		try {
			pubByte = Common.suckFile(pubfile);
			pubBase = Base64.decode(pubByte);
			pub = SerializeUtilsFixed.unserializeBswabePub(pubBase);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(pubByte.length == 0){
				return null;
			}
		}
		return pub;
	}
	
	public static MasterKey readMasterKey(String mkfile, PublicKey pk){
		byte[] mkByte = null, mkB;
		MasterKey mk = new MasterKey();
		try {
			mkByte = Common.suckFile(mkfile);
			mkB = Base64.decode(mkByte);
			mk = SerializeUtilsFixed.unserializeBswabeMsk(pk, mkB);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(mkByte.length ==0){
				return null;
			}
		}
		return mk;
	}
	
	public static CipherText readCipherText(String encfile){
		byte[] ctByte = null;
		CipherText ct = new CipherText();
		try {
			ctByte = Common.suckFile(encfile);
			ct = AttributeUtil.tokenizeCT(ctByte);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			if(ctByte == null || ctByte.length == 0){
				return null;
			}
		}
		return ct;
	}
	
	public static AttributeKey readAttributeKey(String attfile, PublicKey pub){
		byte[] attByte = null, attBase;
		AttributeKey attKey = new AttributeKey();
		try {
			attByte = Common.suckFile(attfile);
			attBase = Base64.decode(attByte);
			attKey = SerializeUtilsFixed.unserializeAttributeKey(pub, attBase);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(attByte == null || attByte.length == 0){
				return null;
			}
		}
		return attKey;
	}
	
	public static PrivateKey readPrivateKey(String prifile, PublicKey pub){
		byte[] priByte = null, priBase;
		PrivateKey pri = new PrivateKey();
		try {
			priByte = Common.suckFile(prifile);
			priBase = Base64.decode(priByte);
			pri = SerializeUtilsFixed.unserializePrivateKey(pub, priBase);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(priByte == null || priByte.length == 0){
				return null;
			}
		}
		return pri;
	}
	
	public static byte[] encryptAES(String plaintext, CipherText ct){
		byte[] plt = null, aesBuf = null;
		try {
			plt = plaintext.getBytes();
			aesBuf = AESCoder.encrypt(ct.eggalphas.toBytes(), plt);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(plt.length!=0 && plt != null){
			for(int i=0; i<plt.length; i++){
				System.out.println("plt =="+plt[i]);
			}
		}
		return aesBuf;
	}
}
