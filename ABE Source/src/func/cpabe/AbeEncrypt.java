/**
 * FileName:     AbeEncrypt.java
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

import func.cpabe.object.CipherText;
import func.cpabe.object.Hash;
import func.cpabe.object.InterValue;
import func.cpabe.object.PublicKey;
import func.cpabe.util.AESCoder;
import func.cpabe.util.AbeError;
import func.cpabe.util.AttributeUtil;
import func.cpabe.util.Common;
import func.cpabe.util.CreateTreeV1;
import func.cpabe.util.SerializeUtilsFixed;
import func.lsss.LSSSEnc;
import func.lsss.object.AbePolicy;
import func.lsss.object.AttSubtree;
import func.lsss.object.FencAttribute;
import func.lsss.object.NodeType;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/** 
 * @ClassName:     Enc1 
 * @Description:TODO(这里用一句话描述这个类的作用) 
 * @author:    Nanio
 * @date:        Nov 16, 2014 3:20:06 PM 
 *  
 */

public class AbeEncrypt {
	
	PublicKey pk = new PublicKey();
	AbePolicy pol = new AbePolicy();
	CipherText ct = new CipherText();
	AttributeUtil attUtil = new AttributeUtil();
	Map<String, Element> shareMap = new HashMap<String, Element>();
	LinkedList<FencAttribute> attList = new LinkedList<FencAttribute>();
	LSSSEnc lsss = new LSSSEnc();

	/**
	 * 
	 * @Title: encrypt 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param policy
	 * @param: @param inputfile
	 * @param: @param outputfile    
	 * @return: void    
	 * @throws
	 */
	public String encrypt(String policy, String pubfile, String plaintext, String outputfile){
		Element share;
		byte[] cphBuf, aesBuf = null;
		String output, outAes;
		pol.setPolicy(policy);
		System.out.println("plaintext"+plaintext);
		if(plaintext == null || plaintext.equals("")){
			return null;
		}
		//get pk
		pk = Common.readPublicKey(pubfile);
		if(pk == null){
			return null;
		}
		//init share
		share = pk.p.getZr().newElement();
		//get ciphertext
		share = encryptInit(policy, pk);
		ct.m = share.duplicate();
		cphBuf = SerializeUtilsFixed.bswabeCphSerialize(ct);
		System.out.println("eggalphas =="+ct.eggalphas);
		if(cphBuf.length == 0){
			return null;
		}
		// encrypt the plaintext
		aesBuf = Common.encryptAES(plaintext, ct);
		output = Base64.encodeBytes(cphBuf);
		outAes = Base64.encodeBytes(aesBuf);
		output = "ABE_CP:"+ output+":ABE_CP_END:IV:"+outAes+":IV_END:"
				+ "AES: :AES_END";
		//store the ciphertext
		try {
			Common.spitFile(outputfile, output.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return output;
	}
	
	public String encrypt(InterValue value, String policy, String plaintext){
		Element share;
		byte[] cphBuf, result = null;
		byte[] plt = null;
		byte[] aesBuf = null;
		String output, outAes;
		//String plaintext;
		pol.setPolicy(policy);
		System.out.println("plaintext"+plaintext);
		if(plaintext == null || plaintext.equals("")){
			return null;
		}
		//get pk
		try {
			result = Base64.decode(value.getPubKey());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(result.length == 0){
				return null;
			}else
				pk = SerializeUtilsFixed.unserializeBswabePub(result);
		}
		
		share = pk.p.getZr().newElement();
		share = encryptInit(policy, pk);
		ct.m = share.duplicate();
		cphBuf = SerializeUtilsFixed.bswabeCphSerialize(ct);
		System.out.println("eggalphas =="+ct.eggalphas);
		
		if(cphBuf.length == 0){
			return null;
		}
		try {
			plt = plaintext.getBytes();
			aesBuf = AESCoder.encrypt(ct.eggalphas.toBytes(), plt);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0; i<plt.length; i++){
			System.out.println("plt =="+plt[i]);
		}
		output = Base64.encodeBytes(cphBuf);
		outAes = Base64.encodeBytes(aesBuf);
		output = "ABE_CP:"+ output+":ABE_CP_END:IV:"+outAes+":IV_END:"
				+ "AES: :AES_END";
		value.setEncrypted(output);
		
		return output;
	}
	/**
	 * 
	 * @Title: encryptInit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param policy
	 * @param: @param pk
	 * @param: @return    
	 * @return: Element    
	 * @throws
	 */
	private Element encryptInit(String policy, PublicKey pk){
		Element s, m; 
		Element eggalpha, hash;
		AttSubtree tree = new AttSubtree();
		AttSubtree tree1 = new AttSubtree();
		CreateTreeV1 createTree = new CreateTreeV1();
		Pairing p;	
		
		p = pk.p;
		//init the ct	
		s = p.getZr().newElement();
		m = p.getGT().newElement();
		ct.m = p.getGT().newElement();
		eggalpha = p.getGT().newElement();
		s.setToRandom();	
		m.setToRandom();
		// calculate the shares
		System.out.println("Test enc init -AbeEncrypt.java");
		System.out.println("s "+s);
		policy = pol.getPolicy();
		System.out.println("policy "+policy);
		//nanio:test
		tree.setType(NodeType.FENC_ATTRIBUTE_POLICY_NODE_OR);
		tree1.att = "ABXZYTUB";
		tree.subtrees.add(tree1);
		tree.subtrees.add(createTree.stringToPolicy(policy));
		pol.root = tree;
		//test
		pol.setPolicy("ABXZYTUB or "+policy);
		attList = attUtil.policyToListOrder(pol.getPolicy());
		
		System.out.println("policy = "+pol.getPolicy()+"after serilize, the attList size is "+attList.size());
		for(int i=0 ;i<attList.size(); i++){
			System.out.println(attList.get(i).att);
		}
		shareMap = lsss.sharesFromPolicy(s, pol.root, policy, attList, p);

		ct.c = pk.g1.powZn(s);
		ct.m = m.duplicate();
		eggalpha = pk.eggAlphaT.duplicate();
		System.err.println("pk's eggalpha"+eggalpha);
		eggalpha = eggalpha.powZn(s);
		ct.eggalphas = eggalpha.duplicate();
		System.err.println("eggalphas"+ct.eggalphas);
		//set the ci and di in ct
		setAttPara(pk.p);
		pol.attList = attList;     //
		ct.policy = pol;
		return s;
	}
	
	/**
	 * 
	 * @Title: setAttParaTest 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param p    
	 * @return: void    
	 * @throws
	 */
	private void setAttPara(Pairing p){
		LinkedList<Element> sList = new LinkedList<Element> ();
		Element hash, r, cg, ga,hashG1; 
		Hash h = new Hash();
//		h.hashaZr = pk.p.getZr().newElement();
//		h.hashbZr = pk.p.getZr().newElement();
//		h.hashaG1 = pk.p.getG1().newElement();
//		h.hashbG1 = pk.p.getG1().newElement();
		h.hashG1 = pk.p.getG1().newElement();
		h.hashZr = pk.p.getZr().newElement();
		sList = lsss.sList;
		//attUtil.getHash(h);
		r = pk.p.getZr().newElement();
		hash = pk.p.getZr().newElement();
		hashG1 = pk.p.getG1().newElement();
		System.out.println("sList size"+sList.size());
		//set lenda
		for(int i=0; i<attList.size(); i++){
			String att = attList.get(i).att;
			//attList.get(i).share = shareMap.get(att).duplicate();
			attList.get(i).share = sList.get(i+1);
			System.out.println("att = "+ att+" share = "+ attList.get(i).share);
			attList.get(i).containShare = true;
		}
		//set c c` ci and di
		for(int i=0; i<attList.size(); i++){
			r.setToRandom();
			hash.setToRandom();
			hashG1.setToRandom();
			cg = pk.p.getG2().newElement();
			ga = pk.p.getG1().newElement();
			ga = pk.g1a.duplicate();
			cg = pk.g2.duplicate();
			ga.powZn(attList.get(i).share);
			h = attUtil.getHashTest(attList.get(i).att, p);
			hashG1 = h.hashG1.duplicate();
			hash = h.hashZr.duplicate();
			attList.get(i).attHash = hash.duplicate();
			attList.get(i).isHashed = true;
			hashG1.powZn(r);
			hashG1.invert();
			ga.mul(hashG1);
			cg.powZn(r);
			ct.ci.add(ga);
			ct.di.add(cg);
			System.out.println("att "+attList.get(i).att);
			System.out.println("c "+ga);
			System.out.println("d "+cg);
		}
	}
	
	public static void main(String args[]){
		AbeEncrypt enc = new AbeEncrypt();
		String result = enc.encrypt("(((((((mytech and a) and b) and c) or ((mymoney and a) and b1)) or (mymakert and a2)) or (mybank and a3)) or (myshell and a4))", "pk.key", "success", "encfile1");
		System.out.println(" "+result);
	}
}
