/**
 * FileName:     AbeSetup.java
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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import func.cpabe.object.InterValue;
import func.cpabe.object.MasterKey;
import func.cpabe.object.PublicKey;
import func.cpabe.util.AttributeUtil;
import func.cpabe.util.Common;
import func.cpabe.util.SerializeUtilsFixed;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.DefaultCurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;

public class AbeSetup {	

	 // Generate a public key and corresponding master secret key.

	private static String curveParams = "type a\n"
			+ "q 87807107996633125224377819847540498158068831994142082"
			+ "1102865339926647563088022295707862517942266222142315585"
			+ "8769582317459277713367317481324925129998224791\n"
			+ "h 12016012264891146079388821366740534204802954401251311"
			+ "822919615131047207289359704531102844802183906537786776\n"
			+ "r 730750818665451621361119245571504901405976559617\n"
			+ "exp2 159\n" + "exp1 107\n" + "sign1 1\n" + "sign0 1\n";
	
	AttributeUtil util = new AttributeUtil();
	/**
	 * 
	 * @Title: setup 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param pubfile
	 * @param: @param mskfile    
	 * @return: void    
	 * @throws
	 */
	public void setup(String pubfile, String mskfile){
		byte[] pub_byte, msk_byte;
		String mk;
		String pk;
		PublicKey pub = new PublicKey();
		MasterKey msk = new MasterKey();
		setupInit(pub,msk);
		
		/* store Pub into public key file */
		pub_byte = SerializeUtilsFixed.serializeBswabePub(pub);
		pk = Base64.encodeBytes(pub_byte);
		try {
			Common.spitFile(pubfile, pk.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* store BswabeMsk into master key file */
		msk_byte = SerializeUtilsFixed.serializeBswabeMsk(msk);
		mk = Base64.encodeBytes(msk_byte);
		try {
			Common.spitFile(mskfile, mk.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @Title: setup 
	 * @Description: TODO(setup and store into InterValue) 
	 * @param: @param value 
	 * @return: void    
	 * @throws
	 */
	public void setup(InterValue value){
		byte[] pubByte, mskByte;
		String mk;
		String pk;
		PublicKey pub = new PublicKey();
		MasterKey msk = new MasterKey();
		setupInit(pub,msk);
		
		/* store Pub into pk */
		pubByte = SerializeUtilsFixed.serializeBswabePub(pub);
		pk = Base64.encodeBytes(pubByte);
		value.setPubKey(pk);

		/* store BswabeMsk into mskfile */
		mskByte = SerializeUtilsFixed.serializeBswabeMsk(msk);
		mk = Base64.encodeBytes(mskByte);
		value.setMasKey(mk);

	}
	
	/**
	 * 
	 * @Title: setupInit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param pk
	 * @param: @param mk    
	 * @return: void    
	 * @throws
	 */
	private void setupInit(PublicKey pk, MasterKey mk){
		Element alpha1, alpha2, alpha = null;
		
		CurveParameters params = new DefaultCurveParameters()
			.load(new ByteArrayInputStream(curveParams.getBytes()));
		Pairing pairing = PairingFactory.getPairing(params);
		
		System.out.println("pairing = "+pairing.toString());
		alpha1 = pairing.getZr().newElement();
		alpha2 = pairing.getZr().newElement();
		alpha = pairing.getZr().newElement();
		//pk assignment
	//	pk.Des = curveParams;
		pk.p = PairingFactory.getPairing(params);
		pk.g1 = pairing.getG1().newElement();
		pk.g1a = pairing.getG1().newElement();
		pk.g2a = pairing.getG1().newElement();
		pk.g2 = pairing.getG2().newElement();
		pk.a = pairing.getZr().newElement();
		pk.eggAlphaT = pairing.getGT().newElement();
		
		pk.g1.setToRandom();
		pk.g2.setToRandom();
	    pk.g1a.setToRandom();
	    pk.g2a.setToRandom();
		alpha1.setToRandom();
		alpha2.setToRandom();
		pk.a.setToRandom();
		alpha = alpha2.duplicate();
		alpha.add(alpha1);
		pk.g1a = pk.g1.duplicate();
		pk.g1a.powZn(pk.a);
		pk.g2a = pk.g2.duplicate();
		pk.g2a.powZn(pk.a);
		pk.eggAlphaT = pairing.pairing(pk.g1, pk.g2);
		pk.eggAlphaT.powZn(alpha);
		//mk assignment
		mk.alpha1 = pairing.getZr().newElement();
		mk.alpha2 = pairing.getZr().newElement();
		mk.alpha = pairing.getZr().newElement();
		mk.alpha1 = alpha1.duplicate();
		mk.alpha2 = alpha2.duplicate();
		mk.alpha = alpha.duplicate();
	}
	
	public static void main(String args[]){
		AbeSetup abesetup = new AbeSetup();
		InterValue value = new InterValue();
		abesetup.setup("pk.key", "mk.key");
		abesetup.setup(value);
		System.out.println(value.getPubKey());
		System.out.println(value.getMasKey());
	}	
}
