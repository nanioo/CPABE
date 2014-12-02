/**
 * FileName:     LSSSDec.java
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

import java.util.LinkedList;

import func.cpabe.util.AbeError;
import func.cpabe.util.AttributeUtil;
import func.lsss.object.AbePolicy;
import func.lsss.object.AttSubtree;
import func.lsss.object.FencAttribute;
import func.lsss.object.FencCoefficient;
import func.lsss.object.NodeType;


public class LSSSDec {
	
	//AttSubtree node = new AttSubtree();
	AttributeUtil attUtil = new AttributeUtil();
	public LinkedList<FencCoefficient> coefList = new LinkedList<FencCoefficient>();
	AbePolicy pol = new AbePolicy();
	
	public LSSSDec(){
		//node = stringToTree();
	}
	
	/**
	 * 
	 * @Title: coeFromPolicy 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param policy
	 * @param: @param p
	 * @param: @param list
	 * @param: @return    
	 * @return: AbeError    
	 * @throws
	 */
	public AbeError coeFromPolicy(AbePolicy policy ,Pairing p, 
			LinkedList<FencAttribute> list){
		
		int numLeave = 0;
		Element identity = p.getZr().newElement();

		// judge the policy
		pol = policy;
		//System.out.println("attkey list size is "+list.size()+", enc policy size is");
		
		numLeave = pruneTree(pol.root, list);
		System.out.println("=====numLeave="+numLeave);
		System.out.println("=====numLeave="+pol.root.subtrees.size());
		System.out.println("=====numLeave="+list.size());
		//pol.root.treeToString();
		
		if(numLeave == 0){
			return AbeError.ERROR_INVALID_POLICY;
		}
		//clean out the coeflist
		for(int i=0; i<coefList.size(); i++){
			coefList.get(i).isSet = false;
			//System.out.println("=====i"+i+coefList.get(i).coefficient);
		}
		//compute the coefficients
		identity.setToOne();
		getCoefOnTree(identity, true, p, policy.root);
		
/*		for(int i=0; i<coefList.size(); i++){
			if(coefList.get(i).isSet == true){
				System.err.println("=====coefficient"+i+"="+coefList.get(i).coefficient);
			}
		}*/
		return AbeError.NONE_ERROR;
	}
	

	/**
	 * 
	 * @Title: getCoefOnTree 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param coef
	 * @param: @param active_subtree
	 * @param: @param p
	 * @param: @param tree    
	 * @return: void    
	 * @throws
	 */
	public void getCoefOnTree(Element coef, Boolean active_subtree, 
			Pairing p, AttSubtree tree){
		
		int k = 0;
		//int listIndex = 0;
		Element temp1, temp2, temp3;
		FencCoefficient coefficient;
		
		switch(tree.getType()){
		case FENC_ATTRIBUTE_POLICY_NODE_LEAF:
			coefficient = new FencCoefficient();
			if(active_subtree == true){
				coefficient.isSet = true;
				coefficient.attribute.att = tree.att;
				coefficient.coefficient = p.getZr().newElement();
				coefficient.coefficient = coef.duplicate();
				coefList.add(coefficient);
			}else{
				coefficient = new FencCoefficient();
				coefficient.isSet = false;
				coefList.add(coefficient);
			}
			System.out.println("when the coef created");
			System.out.println("att = "+tree.att +" coef ="+coef);
			//listIndex++;
			return;
		case FENC_ATTRIBUTE_POLICY_NODE_AND:
			k = tree.subtrees.size();
			break;
		case FENC_ATTRIBUTE_POLICY_NODE_OR:
			k=1;
			break;
		case FENC_ATTRIBUTE_POLICY_NODE_THRESHOLD:
			k = tree.getThreshold();
			break;
		default:
			return;
		}
		
		//share = p.getZr().newElement();
		temp1 = p.getZr().newElement();
		temp2 = p.getZr().newElement();
		temp3 = p.getZr().newElement();
		
		for(int i=0; i<tree.subtrees.size(); i++){
			if(active_subtree == true && tree.subtrees.get(i).useSubnode == true 
					){
				//compute the lagrange basis polynomial coefficient
				temp1 = computeLa(k, i, tree, temp1,temp2, temp3);
				temp2 = temp1.mul(coef);
				//nanio fixed!!!!!!!!!!!!!!!!!!!!!!!!
				temp1 = p.getZr().newElement();
				System.out.println(tree.subtrees.get(i).att+",temp1=  "+temp1);
				System.out.println("coef ="+coef);
				//System.out.println("temp2="+temp2);
				getCoefOnTree(temp2, true, p, tree.subtrees.get(i));
			}else{
				getCoefOnTree(temp2, false, p, tree.subtrees.get(i));
				System.out.println("when active_subtree is false,"+tree.subtrees.get(i).att+" " + coef);
			}
		}
	}
	

	/**
	 * 
	 * @Title: computeLa 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param threshold
	 * @param: @param index
	 * @param: @param tree
	 * @param: @param result
	 * @param: @param temp2
	 * @param: @param temp3
	 * @param: @return    
	 * @return: Element    
	 * @throws
	 */
	public Element computeLa(int threshold, int index, AttSubtree tree, Element result,
			Element temp2, Element temp3){
		int total = 0;
		result.setToOne();
		
		for (int i = 0; i < tree.subtrees.size(); i++) {
			if(tree.subtrees.get(i).useSubnode == true){
/*				System.out.println("index "+i);
				System.out.println("att "+tree.subtrees.get(i).att);
				System.out.println("useSubnode "+tree.subtrees.get(i).useSubnode);
				System.out.println("type "+tree.type);*/
				total++;
				if(i != index){
					temp2.set(0-(i+1));
					System.out.println("result = "+result);
					temp3 = temp2.duplicate();
					System.out.println("result = "+result);
					temp3.mul(result);
					System.out.println("result = "+result);
					System.out.println("temp3 = "+temp3);
					temp2.set(index+1-(i+1));
					result = temp2.duplicate();
					result = result.invert();
					temp2 = result.duplicate();
					temp2.mul(temp3);
					result = temp2.duplicate();
				}
			}
		}
		if(total > threshold){
			System.out.println("compute_lagrange: too many child nodes");
		}
		return result;
	}
	
	/**
	 * 
	 * @Title: pruneTree 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param tree
	 * @param: @param list
	 * @param: @return    
	 * @return: int    
	 * @throws
	 */
	public int pruneTree(AttSubtree tree, LinkedList<FencAttribute> list){	
		int k, result = 0;
		int attIndex = -1;
		int satisfiedNum = 0;
		int satisfiedNode[];
		int smallestNode, smallestNodeCount;
		int size = 0;
		FencAttribute attribute = new FencAttribute();
		
		if(tree.att == null || tree.att.length() == 0){
			size = tree.subtrees.size();
		}
		satisfiedNode = new int[size];
		switch(tree.getType()){
			case FENC_ATTRIBUTE_POLICY_NODE_LEAF:
				result = 0;
				//search the index int the policy.tree
				/*nanio 待修改*/
				for(int i=0; i<list.size(); i++){
					if(list.get(i).att.equals(tree.att)){
						attribute =  list.get(i);
						System.out.println("tree.att" + tree.att);
					}
				}
				attIndex = getIndex(attribute, list);
				if(attIndex >= 0)	
					result = 1;
				return result;
			case FENC_ATTRIBUTE_POLICY_NODE_AND:
				k = tree.subtrees.size();
				break;
			case FENC_ATTRIBUTE_POLICY_NODE_OR:
				k=1;
				break;
			case FENC_ATTRIBUTE_POLICY_NODE_THRESHOLD:
				k = tree.getThreshold();
				break;
			default:
				return 0;
		}
		//Recurse on each subnode to determine the number of satisfied 
		//leaves hanging underneath it
		for(int i=0; i<tree.subtrees.size(); i++){
			//System.out.println("i="+i);
			satisfiedNode[i] = pruneTree(tree.subtrees.get(i),list);
			tree.subtrees.get(i).useSubnode = false;
			if(satisfiedNode[i] > 0){
				satisfiedNum++;
			}
		}
		//make sure the threshold
		if(satisfiedNum < k){
			tree.useSubnode = false;
			return 0;
		}
		
		result = 0;
		if(k == tree.subtrees.size()){
			//and gate
			for (int i = 0; i < k; i++) {
				tree.subtrees.get(i).useSubnode = true;
				System.out.println("####att "+i+" "+tree.subtrees.get(i).att);
				//System.out.println("useSubnode "+i+" "+tree.subtrees.get(i).useSubnode);
				result += satisfiedNode[i];
			}
		}else{
			//or or other condition
			for (int j = 0; j < k; j++) {
				//Find the smallest non-zero value in the list.	
				smallestNode = 0;
				smallestNodeCount = 400000;
				for (int i = 0; i < tree.subtrees.size(); i++) {
					if (satisfiedNode[i] != 0 && satisfiedNode[i] <= smallestNodeCount){
						smallestNodeCount = satisfiedNode[i];
						smallestNode = i;
					}
				}
				// Sanity check
				if(smallestNodeCount >= 400000) {
					result = 0;
					return 0;
				}
				// Mark the node.	
				tree.subtrees.get(smallestNode).useSubnode = true;
				satisfiedNode[smallestNode] = 0;
				result += smallestNodeCount;		
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @Title: getIndex 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param attribute
	 * @param: @param list
	 * @param: @return    
	 * @return: int    
	 * @throws
	 */
	public int getIndex(FencAttribute attribute, LinkedList<FencAttribute> list){
		
		for(int i=0; i<list.size(); i++){
			// att is not null
			System.out.println("att ???"+attribute.att);
			
			if(list.get(i).att != null && list.get(i).att.length() != 0 &&
					attribute.att != null && attribute.att.length() != 0){
				if(attribute.att.equals(list.get(i).att)){
					//System.out.println(" "+list.get(i).att);
					return i;
				}
//				else{
//					System.out.println(" "+list.get(i).att);
//					System.out.println(" "+attribute.att);
//					System.out.println(" attribute.att size "+attribute.att.length());
//				}
					
			}else if(attribute.isHashed == true && list.get(i).isHashed == true){
				// one of att is null
				if(attribute.attHash.isEqual(list.get(i).attHash))
					return i;
			}			
		}
		return -1;
	}

}
