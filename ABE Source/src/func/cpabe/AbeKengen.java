/**
 * FileName:     AbeKengen.java
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

package func.cpabe;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;

import java.io.IOException;

import func.cpabe.object.AttributeKey;
import func.cpabe.object.Hash;
import func.cpabe.object.InterValue;
import func.cpabe.object.MasterKey;
import func.cpabe.object.PrivateKey;
import func.cpabe.object.PublicKey;
import func.cpabe.util.AbeError;
import func.cpabe.util.AttributeUtil;
import func.cpabe.util.Common;
import func.cpabe.util.SerializeUtilsFixed;
import func.lsss.object.FencAttribute;


public class AbeKengen {
	
	AttributeUtil attUtil = new AttributeUtil();
	public AbeKengen(){

	}
	
	/**
	 * 
	 * @Title: keyGen 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param pubfile
	 * @param: @param mkfile
	 * @param: @param attfile
	 * @param: @param prifile
	 * @param: @param list
	 * @param: @return    
	 * @return: AbeError    
	 * @throws
	 */
	public AbeError keyGen(String pubfile, String mkfile, String attfile, String prifile, String list){
		byte[] pubByte, mkByte, priByte, akByte;
		byte[] pkB, mkB;
		String atStr, priStr;
		PublicKey pk = new PublicKey();
		MasterKey mk = new MasterKey();
		PrivateKey priKey = new PrivateKey();
		AttributeKey attKey = new AttributeKey();
		
		// get the pk and mk from file
//		try {
//			pubByte = Common.suckFile(pubfile);
//			pkB = Base64.decode(pubByte);
//			pk = SerializeUtilsFixed.unserializeBswabePub(pkB);
//			mkByte = Common.suckFile(mkfile);
//			mkB = Base64.decode(mkByte);
//			mk = SerializeUtilsFixed.unserializeBswabeMsk(pk, mkB);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		pk = Common.readPublicKey(pubfile);
		if(pk == null){
			return null;
		}
		mk = Common.readMasterKey(mkfile, pk);
		if(mk == null){
			return null;
		}
		if(list.length() == 0){
			System.err.println("FENC_ERROR_INVALID_POLICY");
			return AbeError.ERROR_INVALID_POLICY;
		}
		//nanio!!!!
		genInit(pk, mk, priKey, attKey, list);
		
		//serialize and write into file
		priByte = SerializeUtilsFixed.serializeBswabePrv(priKey);
		akByte = SerializeUtilsFixed.serializeBswabeAtt(attKey);
		priStr = Base64.encodeBytes(priByte);
		atStr = Base64.encodeBytes(akByte);

		try {
			Common.spitFile(prifile, priStr.getBytes());
			Common.spitFile(attfile, atStr.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AbeError.NONE_ERROR;
	}
	
	/**
	 * 
	 * @Title: keyGen 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param value
	 * @param: @param list
	 * @param: @return    
	 * @return: AbeError    
	 * @throws
	 */
	public AbeError keyGen(InterValue value, String list){
		byte[] priByte, akByte;
		byte[] pkB, mkB;
		String atStr, priStr;
		PublicKey pk = new PublicKey();
		MasterKey mk = new MasterKey();
		PrivateKey priKey = new PrivateKey();
		AttributeKey attKey = new AttributeKey();
		
		if(value.getPubKey().length() == 0){
			System.err.println("ERROR_INVALID_PUBLICKEY");
			return AbeError.ERROR_INVALID_PUBLICKEY;
		}else if(value.getMasKey().length() == 0){
			System.err.println("FENC_ERROR_INVALID_POLICY");
			return AbeError.ERROR_INVALID_MASTERKEY;
		}
		else if(list.length() == 0){
			System.err.println("FENC_ERROR_INVALID_POLICY");
			return AbeError.ERROR_INVALID_POLICY;
		}else{
			// get the pk from file
			try {
				pkB = Base64.decode(value.getPubKey().getBytes());
				pk = SerializeUtilsFixed.unserializeBswabePub(pkB);
				mkB = Base64.decode(value.getMasKey().getBytes());
				mk = SerializeUtilsFixed.unserializeBswabeMsk(pk, mkB);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//nanio!!!!
		genInit(pk, mk, priKey, attKey, list);
		
		//serialize and write into file
		priByte = SerializeUtilsFixed.serializeBswabePrv(priKey);
		akByte = SerializeUtilsFixed.serializeBswabeAtt(attKey);
		priStr = Base64.encodeBytes(priByte);
		atStr = Base64.encodeBytes(akByte);
		
		value.setAttKey(atStr);
		value.setPriKey(priStr);
		
		return AbeError.NONE_ERROR;
	}
	
	/**
	 * 
	 * @Title: genInit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param pk
	 * @param: @param mk
	 * @param: @param priKey
	 * @param: @param attKey
	 * @param: @param attList1    
	 * @return: void    
	 * @throws
	 */
	private void genInit(PublicKey pk ,MasterKey mk, PrivateKey priKey,
			AttributeKey attKey, String attList1){
		
		FencAttribute attribute;
		String[] list;
		Hash h = new Hash();
		String attList = "(";
		
		Element hash;
		Element hashG1;
		Element g2 = pk.p.getG2().newElement();
		Element t = pk.p.getZr().newElement();
		Element gat = pk.p.getG2().newElement();
		h.hashG1 = pk.p.getG1().newElement();
		h.hashZr = pk.p.getZr().newElement();
		//attUtil.getHash(h);
		t.setToRandom();
		//init the k1 and k2
		g2 = pk.g2.duplicate();	
		attKey.setK1(g2.powZn(mk.alpha1));
		gat = pk.g2a.duplicate();
		gat.powZn(t);
		attKey.getK1().mul(gat);
		
		g2 = pk.g2.duplicate();
		priKey.k2 = g2.powZn(mk.alpha2);
		priKey.k2.mul(gat);
		
		g2 = pk.g2.duplicate();
		attKey.l = g2.powZn(t);
		attList1 = attList1.replaceAll("\\(", "");
		attList1 = attList1.replaceAll("\\)", "");
		list = attList1.split(",");
		
		for(int i=0; i<list.length; i++){
			attribute = new FencAttribute();
			hash = pk.p.getZr().newElement();
			hashG1 = pk.p.getG1().newElement();
			attribute.attHash = pk.p.getG1().newElement();			
			attribute.att = list[i];
			// fixed
			h = attUtil.getHashTest(attribute.att, pk.p);
			hashG1 = h.hashG1.duplicate();
			hash = h.hashZr.duplicate();

			attList = attList + list[i];
			if(i < list.length-1){
				attList += ",";
			}
			attribute.attHash = hash.duplicate();
			attKey.attList.add(attribute);
			hashG1.powZn(t);
			System.out.println("###"+hashG1);
			attKey.kx.add(hashG1);
		}
		attList +=")";
		attKey.setAttStr(attList);
		attKey.setSize(attKey.attList.size());
		System.out.println("attList = "+attList+" "+attKey.getSize());
	}
	
	public static void main(String[] args){
		AbeKengen gen = new AbeKengen();
		//String[] list ={"A","C","D","Z","mytech"};
		String listStr ="(A,C,D,E,Z,mytech)";
		gen.keyGen("pk.key", "mk.key","ak.key", "pri.key", listStr);
	}
}
