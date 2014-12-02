/**
 * FileName:     CipherText.java
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
package func.cpabe.object;

import java.util.LinkedList;

import func.lsss.object.AbePolicy;
import func.lsss.object.AttSubtree;
import func.lsss.object.FencAttribute;
import func.lsss.object.NodeType;
import it.unisa.dia.gas.jpbc.Element;

public class CipherText {
	
	public AbePolicy policy = new AbePolicy();
	private String pol;
	public Element m;				//GT
	public Element eggalphas;		//GT me(g,g)^alpha*s，
	public Element c; 				//g^s	G1     pk,g1^s
	public LinkedList<Element> ci = new LinkedList<Element>();		//G1
	public LinkedList<Element> di = new LinkedList<Element>();		//G2   pk,g2^r
	public byte[] aes;

	/**
	 * 
	 * @Title: ctToString 
	 * @Description: TODO(to string) 
	 * @param:     
	 * @return: void    
	 * @throws
	 */
	public void ctToString(){
		System.out.println("########This is CipherText:");
		System.out.println("m = "+m);
		System.out.println("eggalphas = "+eggalphas);
		System.out.println("c = "+c);
		System.out.println("policy = "+policy.getPolicy());
		for(int i=0; i<ci.size(); i++){
			System.out.println("c"+i+" = "+ci.get(i));
			System.out.println("d"+i+" = "+di.get(i));
		}
		
		if(policy.root.getType() == NodeType.FENC_ATTRIBUTE_POLICY_NODE_LEAF){
			System.out.println("This is CT's Policy");
			System.out.println("type = "+policy.root.getType());
			System.out.println("threshold = "+policy.root.getThreshold());
			System.out.println("att = "+policy.root.att);
			System.out.println("sbutree size = "+policy.root.subtrees.size());
		}else{	
			subtree(policy.root);
			}
			
		System.out.println("This is policy's attfenc list");
		for(int i=0; i<policy.attList.size(); i++){
			System.out.println("att = "+policy.attList.get(i).att);
			System.out.println("attHash = "+policy.attList.get(i).attHash);
			System.out.println("isHashed = "+policy.attList.get(i).isHashed);
			System.out.println("share = "+policy.attList.get(i).share);
			System.out.println("containShare = "+policy.attList.get(i).containShare);
		}
		System.out.println("########CipherText end");
	}

		public AbePolicy getPolicy() {
		return policy;
	}

	public void setPolicy(AbePolicy policy) {
		this.policy = policy;
	}

	public String getPol() {
		return pol;
	}

	public void setPol(String pol) {
		this.pol = pol;
	}

	public byte[] getAes() {
		return aes;
	}

	public void setAes(byte[] aes) {
		this.aes = aes;
	}

		public void subtree(AttSubtree tree){
			
			System.out.println("threshold = "+tree.getThreshold());
			//System.out.println("sbutree size = "+tree.subtrees.size());
			System.out.println("type = "+tree.getType());
			if(tree.att == null || tree.att.length() == 0)
				for(int i=0; i< tree.subtrees.size(); i++){
					//System.out.println("sub size = "+tree.subtrees.get(i).subtrees.size());
					System.out.println("att = "+tree.subtrees.get(i).att);
					subtree(tree.subtrees.get(i));
				}
		}

}
