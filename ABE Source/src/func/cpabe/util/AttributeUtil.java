package func.cpabe.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

import func.cpabe.object.CipherText;
import func.cpabe.object.Hash;
import func.cpabe.object.PublicKey;
import func.lsss.object.AttSubtree;
import func.lsss.object.FencAttribute;
import func.lsss.object.NodeType;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;

public class AttributeUtil {
	
	CreateTreeV1 tree = new CreateTreeV1();
	LinkedList<FencAttribute> attList = new LinkedList<FencAttribute>();

	/**
	 * 
	 * @Title: policyToListOrdered 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param policy
	 * @param: @return    
	 * @return: LinkedList<FencAttribute>    
	 * @throws
	 */
	public LinkedList<FencAttribute> policyToListOrdered(String policy){
		FencAttribute fenc = new FencAttribute();
		AttSubtree subTree = new AttSubtree();
		//nanio 11-15 need to fix
		//policy =policy.substring(12);
		fenc.att="ABXZYTUB";
		attList.add(fenc);
		//nanio 11-15 need to fix
		System.out.println("policy To List Ordered "+policy);
		subTree = tree.stringToPolicy(policy);
		subTree.treeToString();
		getTree(subTree);
		return attList;
	}
	
	public LinkedList<FencAttribute> policyToListOrder(String policy){
		FencAttribute fenc = new FencAttribute();
		AttSubtree subTree = new AttSubtree();
		//nanio 11-15 need to fix
		//policy =policy.substring(12);
		//fenc.att="ABXZYTUB";
		//attList.add(fenc);
		//nanio 11-15 need to fix
		System.out.println("policy To List Ordered "+policy);
		subTree = tree.stringToPolicy(policy);
		//subTree.treeToString();
		getTree(subTree);
		return attList;
	}
	/**
	 * 
	 * @Title: getTree 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param subTree    
	 * @return: void    
	 * @throws
	 */
	public void getTree(AttSubtree subTree){
		FencAttribute fenc = new FencAttribute();
		if(subTree.getType() == NodeType.FENC_ATTRIBUTE_POLICY_NODE_LEAF){
			fenc.att = subTree.att;
			attList.add(fenc);
			System.out.println("^%$&^&^%^&%"+fenc.att);
			return;
		}else if(subTree.getType() == NodeType.FENC_ATTRIBUTE_POLICY_NODE_AND ||
				subTree.getType() == NodeType.FENC_ATTRIBUTE_POLICY_NODE_OR){
			for(int i=0; i<subTree.subtrees.size(); i++){
				getTree(subTree.subtrees.get(i));
			}
		}
		return;
	}
	
	/**
	 * 
	 * @Title: policyToList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param policy
	 * @param: @return    
	 * @return: LinkedList<FencAttribute>    
	 * @throws
	 */
	public LinkedList<FencAttribute> policyToList(String policy){
		LinkedList<FencAttribute> attList = new LinkedList<FencAttribute>();
		FencAttribute fencAtt = new FencAttribute();
		String p1 = "";
		int num =0; 						//count the num of logic node
		judgeBracket(policy);
		p1 = policy.replaceAll("\\(", "");
		p1 = p1.replaceAll("\\)", "");
		String[] policyList = p1.split(" ");
		System.out.println(" p1 " +p1);
		
		//count the num of logic node
		for(int i=0; i<policyList.length; i++){
			if(policyList[i]!=null && policyList[i].length()>0){
				if(policyList[i].equals("and") || policyList[i].equals("or"))
					num++;
			}
		}
		
		for(int i=0; i<policyList.length; i++){
			fencAtt = new FencAttribute();
			System.out.println(i+"  " +policyList[i]);
			if(policyList[i]!=null && policyList[i].length()>0){
				if(policyList[i].equals("and") || policyList[i].equals("or"))
					continue;
				else{
					fencAtt.att = policyList[i];
					attList.add(fencAtt);
				}
			}
			// if the att num bigger than nodeNum+1, end the for
			if(attList.size() >num+1)
				break;
		}

		return attList;
	}
	
	/**
	 * 
	 * @Title: judgeBracket 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param policy
	 * @param: @return    
	 * @return: String    
	 * @throws
	 */
	public String judgeBracket(String policy){
		String[] leftB, rightB, blank;
		String result = "";
		int indexL = 0, indexR = 0, index = 0;
		int numLeftB = 0, numRightB = 0,numBlank;
		
		blank = policy.split(" ");
		numBlank = blank.length;
		for(int i =0; i<blank.length-1; i++){		
			numLeftB += getLBracketNum(blank[i]);
			numRightB += getRBracketNum(blank[i]);
			result += blank[i]+" ";
			System.out.println(" "+blank[i]);
		}

		index = getFirstLBracketIndex(blank[numBlank-1]);
		result = result + blank[numBlank-1].substring(0, index);
		
		for(int i=0; i<(numLeftB - numRightB); i++){
			result += ")";
		}

		System.out.println(" result = "+result);
		return result;
	}
	
	/**
	 * 
	 * @Title: getFirstLBracketIndex 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param str
	 * @param: @return    
	 * @return: int    
	 * @throws
	 */
	private int getFirstLBracketIndex(String str){
		int index = 0;
		char[] c = str.toCharArray();
		
		for(int i=0; i<c.length; i++){
			if(c[i] == ')'){
				index = i;
				System.out.println(" the first at "+index);
				return index;
			}
		}
		return index;
	}
	
	/**
	 * 
	 * @Title: getLBracketNum 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param str
	 * @param: @return    
	 * @return: int    
	 * @throws
	 */
	public int getLBracketNum(String str){
		
		int size =0;
		char[] c = str.toCharArray();
		
		for(int i=0; i<c.length; i++){
			if(c[i] == '('){
				size++;
			}
		}
		return size;
	}
	
	/**
	 * 
	 * @Title: getRBracketNum 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param str
	 * @param: @return    
	 * @return: int    
	 * @throws
	 */
	public int getRBracketNum(String str){
		
		int size =0;
		char[] c = str.toCharArray();
		
		for(int i=0; i<c.length; i++){
			if(c[i] == ')'){
				size++;
			}
		}
		return size;
	}

	/**
	 * 
	 * @Title: attToZr 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param att
	 * @param: @param p
	 * @param: @return    
	 * @return: Element    
	 * @throws
	 */
	public Element attToZr(String att, Pairing p){
		Element attHash = p.getZr().newElement();
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(att.getBytes());
			attHash.setFromHash(digest, 0, digest.length);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return attHash;
	}
	
	/**
	 * 
	 * @Title: elementFromString 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param h
	 * @param: @param s
	 * @param: @return    
	 * @return: Element    
	 * @throws
	 */
	public Element elementFromString(Element h, String s){
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(s.getBytes());
			h = h.setFromHash(digest, 0, digest.length);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return h;
	}
	
	/**
	 * 
	 * @Title: elementFromStringMD5 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param h
	 * @param: @param s
	 * @param: @return    
	 * @return: Element    
	 * @throws
	 */
	public Element elementFromStringMD5(Element h, String s){
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-2");
			byte[] digest = md.digest(s.getBytes());
			h = h.setFromHash(digest, 0, digest.length);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return h;
	}
	
	/**
	 * 
	 * @Title: g1FromZR 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param g1
	 * @param: @param zr
	 * @param: @return    
	 * @return: Element    
	 * @throws
	 */
	static public Element g1FromZR(Element g1, Element zr){
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(zr.toBytes());
			g1 = g1.setFromHash(digest, 0, digest.length);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return g1;
	}
	
	/**
	 * 
	 * @Title: bytesToHexString 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param src
	 * @param: @return    
	 * @return: String    
	 * @throws
	 */
	static public String bytesToHexString(byte[] src){   
	    StringBuilder stringBuilder = new StringBuilder("");   
	    if (src == null || src.length <= 0) {   
	        return null;   
	    }   
		    for (int i = 0; i < src.length; i++) {   
		        int v = src[i] & 0xFF;   
		        String hv = Integer.toHexString(v);   
		        if (hv.length() < 2) {   
		            stringBuilder.append(0);   
		        }   
		       stringBuilder.append(hv);   
		    }
		    return stringBuilder.toString();   
		} 
	
	/**
	 * 
	 * @Title: getHash 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param h
	 * @param: @return    
	 * @return: Hash    
	 * @throws
	 */
//	static public Hash getHash(Hash h){
//		
//		byte[] test, test1, hashg1, hashG1;
//		int offset = 0, offset1 = 0;
//		try {
//			test = Common.suckFile("hashzr");
//			hashg1 = Common.suckFile("g1hash");
//			test1 = Base64.decode(test);
//			hashG1 = Base64.decode(hashg1);
//			
//			offset = SerializeUtilsFixed.unserializeElement(test1, offset, h.hashaZr);
//			System.out.println("h.hashaZr"+h.hashaZr);
//			offset = SerializeUtilsFixed.unserializeElement(test1, offset, h.hashbZr);
//			System.out.println("h.hashbZr"+h.hashbZr);
//			offset1 = SerializeUtilsFixed.unserializeElement(hashG1, offset1, h.hashG1);
//			System.out.println("hashaG1="+h.hashaG1);
//			offset1 = SerializeUtilsFixed.unserializeElement(hashG1, offset1, h.hashaG1);
//			System.out.println("hashaG1="+h.hashaG1);
//			offset1 = SerializeUtilsFixed.unserializeElement(hashG1, offset1, h.hashbG1);
//			System.out.println("hashbG1="+h.hashbG1);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return h;
//	}
	
	/**
	 * 
	 * @Title: getHashTest 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param att
	 * @param: @param pairing
	 * @param: @return    
	 * @return: Hash    
	 * @throws
	 */
	public Hash getHashTest(String att, Pairing pairing){
		
		Hash h = new Hash();
		byte[] test, test1, hashg1, hashG1;

		h.hashZr = pairing.getZr().newElement();
		h.hashG1 = pairing.getG1().newElement();
			
		elementFromString(h.hashZr, att);
		g1FromZR(h.hashG1, h.hashZr);

		return h;
	}
	
	/**
	 * transform the str(key) into list
	 * @param str
	 * @param list
	 */
	public void stringToList(PublicKey pub, String str, LinkedList<FencAttribute> list){
		String[] node;
		AttributeUtil util = new AttributeUtil();
		FencAttribute att;
		str = str.replaceAll("\\(", "");
		str = str.replaceAll("\\)", "");
		node = str.split(",");
		
		for(int i=0;i<node.length;i++){
			att = new FencAttribute();
			att.attHash = pub.p.getZr().newElement();
			if(node[i].equals("and") || node[i].equals("or") ||
					node[i].equals("")){
				continue;
			}else
				att.att = node[i];
				att.attHash = util.elementFromString(att.attHash, att.att);
				list.add(att);
		}
	}
	

	static public CipherText tokenizeCT(byte[] ctByte){
		
		String ct = new String(ctByte);
		String abe, iv, aes , policy;
		String[] list = new String[10];
		System.out.println("ct = "+ct);
		byte[] aesCT, aesBase;
		CipherText ctext = new CipherText();	
		
		list = ct.split(":");
		for(int i=0; i<list.length; i++){
			System.out.println(i+" "+list[i]);
		}

		abe = list[1];
		SerializeUtilsFixed.writeCT(abe, ctext);
		iv = list[4];
		//log.info("aesCT hex output =");
		try {
			aesBase = Base64.decode(iv);
			ctext.aes = aesBase;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		aes = list[7];

		return ctext;
	}
	
	static public CipherText tokenizeCT1(byte[] ctByte){
		
		String ct = new String(ctByte);
		String abe, iv, aes , policy;
		String[] list = new String[10];
		System.out.println("ct = "+ct);
		byte[] aesCT, aesBase;
		CipherText ctext = new CipherText();	
		
		list = ct.split(":");
		for(int i=0; i<list.length; i++){
			System.out.println(i+" "+list[i]);
		}

		abe = list[1];
		SerializeUtilsFixed.writeCT1(abe, ctext);
		iv = list[4];
		try {
			aesBase = Base64.decode(iv);
			ctext.aes = aesBase;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		aes = list[7];

		return ctext;
	}
	
	public static void main(String[] args){
		AttributeUtil util = new AttributeUtil();
		//getHashTest("A");
		//util.policyToList("ABCDEF or (A and B)");
		//System.out.println(util.judgeBracket("ABCDEF or (A and B)) asdfa"));
//		util.judgeBracket("((A and B) or (C and D))");
		util.policyToListOrdered("(abcde or ((A and B) or mytech))");
		for(int i =0; i<util.attList.size(); i++){
			System.out.println(" THE ORDERED LIST IS "+util.attList.get(i).att);
		}
		
		
	}
}
