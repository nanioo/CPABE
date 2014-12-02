/**
 * FileName:     LSSSEnc.java
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
package func.lsss;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import func.lsss.object.AttSubtree;
import func.lsss.object.FencAttribute;
import func.lsss.object.NodeType;

public class LSSSEnc {	
	
	AttSubtree node = new AttSubtree();
	Map<String, Element> shareList = new HashMap<String, Element>();
	public LinkedList<Element> sList = new LinkedList<Element> ();
	public LSSSEnc(){
		//stringToTree();
	}
	
	/**
	 * 
	 * @Title: policyAttNum 
	 * @Description: TODO(count the node of the policy) 
	 * @param: @param policy
	 * @param: @return    
	 * @return: int    
	 * @throws
	 */
	public int policyAttNum(String policy){

		String[] node;
		int count=0,i=0,count1=0;
		policy = policy.replaceAll("\\(", "");
		policy = policy.replaceAll("\\)", "");
		node = policy.split(" ");
		
		for(i=0;i<node.length;i++){
			if(node[i].equals("and") || node[i].equals("or")){
				count++;
			}else
				count1++;
		}
		return count1;
	}
	
	/**
	 * 
	 * @Title: sharesFromPolicy 
	 * @Description: TODO(get the shares from policy) 
	 * @param: @param s
	 * @param: @param tree
	 * @param: @param policy
	 * @param: @param attlist
	 * @param: @param p
	 * @param: @return    
	 * @return: Map<String,Element>    
	 * @throws
	 */
	public Map<String, Element> sharesFromPolicy(Element s, AttSubtree tree, String policy, 
			LinkedList<FencAttribute> attlist, Pairing p){
		
		int numLeave=0;
		int index = 0;
		//LinkedList<Element> share = new LinkedList<Element>();
/*		Element e = p.getZr().newElement();
		e = s.duplicate();*/
		//计算叶子节点个数
		//numLeave = policyAttNum(policy);
		numLeave = attlist.size();
		//System.out.println("num of leaves"+numLeave);
		//System.out.println("share === "+" s "+s);
		//定义属性列表attbute list
		
		//利用attList和attSubTree计算节点share
		shareOnSubTree(s, p, tree, numLeave);
//		for(int i=0; i<shareList.size();i++){
//			System.out.println(" shareList"+shareList.get("a"));
//		}
		return shareList;
	}
	
	/**
	 * 
	 * @Title: shareOnSubTree 
	 * @Description: TODO(get shares from each subtree) 
	 * @param: @param s
	 * @param: @param p
	 * @param: @param subTree
	 * @param: @param index    
	 * @return: void    
	 * @throws
	 */
	public void shareOnSubTree(Element s,
			Pairing p, AttSubtree subTree, int index){
		
		Element shareZ;
		Element temp1, temp2, temp3, temp4;
		LinkedList<Element> coefficient = new LinkedList<Element>();
		String att = null;
		int threshold=0, numCoef = 0;
		//System.out.println("1 share === "+s);
		//System.out.println("subtree size=== "+subTree.subtrees.size());
		//set the list base on the node type
		switch(subTree.getType()){
			case FENC_ATTRIBUTE_POLICY_NODE_LEAF:
				att = subTree.att;
				shareList.put(att, s);
				System.out.println("129att="+att+", share= "+s);
				if(sList.size()<= index){
					sList.add(s);
				}
				//index++;
				return;
			case FENC_ATTRIBUTE_POLICY_NODE_AND:
				threshold = subTree.subtrees.size();
				break;
			case FENC_ATTRIBUTE_POLICY_NODE_OR:
				threshold = 1;
				break;
			case FENC_ATTRIBUTE_POLICY_NODE_THRESHOLD:
				threshold = subTree.getThreshold();
			default:
				System.err.println("Invalid node type");
				return;
		}
		//allocate coefficients
		numCoef = threshold;
		shareZ = p.getZr().newElement();
		temp1 = p.getZr().newElement();
		temp2 = p.getZr().newElement();
		temp3 = p.getZr().newElement();
		temp4 = p.getZr().newElement();
		//init the coefficient
		for(int i=0; i<numCoef; i++){
			Element e = p.getZr().newElement();
			
			if(i == 0){
				 e = s.duplicate();
			}
			else{
				e.setToRandom();
			}
			coefficient.add(e);
		}
		//evaluate the polynomial
		for(int i=0; i<subTree.subtrees.size(); i++){
			shareZ = lsssEvaPoly((i+1), s, temp1, temp2, temp3, temp4
					, coefficient, threshold);
			System.out.println("129att="+subTree.subtrees.get(i).att+", share= "+shareZ);
			//recurse
			shareOnSubTree(shareZ, p,
					subTree.subtrees.get(i), index);
		}
	}
	
	/**
	 * get poly by lsss
	 * @param x
	 * @param s
	 * @param temp1
	 * @param temp2
	 * @param temp3
	 * @param temp4
	 * @param coefficient
	 * @param threhold
	 */
	public Element lsssEvaPoly(int x, Element s, Element temp1, Element temp2,
			Element temp3, Element temp4, LinkedList<Element> coefficient,
			int threhold){
		
		s = coefficient.get(0).duplicate();
		temp1.set(x);
		temp4.set(x);
		
		for(int i=1; i<coefficient.size(); i++){
			temp2 = temp1.duplicate();
			temp2.mul(coefficient.get(i));
			temp3 = temp2.duplicate();
			temp3.add(s);
			s = temp3.duplicate();
			temp2 = temp1.duplicate();
			temp2.mul(temp4);
			temp1 = temp2.duplicate();
		}
		return s;
	}

	
	public static void main(String args[]){
	}
}