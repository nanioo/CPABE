/**
 * FileName:     AttributeKey.java
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

import func.lsss.object.FencAttribute;
import it.unisa.dia.gas.jpbc.Element;

public class AttributeKey {
	public Element k1;					//G2
	public Element k2;					//G2
	public Element l;					//G2
	public LinkedList<Element> kx = new LinkedList<Element>();    //G1
	public LinkedList<FencAttribute> attList = new LinkedList<FencAttribute>();
	private String attStr;
	private int size;
	
	/**
	 * 
	 * @Title: priToString 
	 * @Description: TODO(to string) 
	 * @param:     
	 * @return: void    
	 * @throws
	 */
	public void priToString(){
		
		for(int i=0; i<attList.size(); i++){
			System.out.println("attList"+i+attList.get(i).att);
			System.out.println("attList"+i+attList.get(i).attHash);
		}
		System.out.println("k1 = "+k1);
		System.out.println("k2 = "+k2);
		for(int i=0; i<kx.size(); i++){
			System.out.println("kx"+i+kx.get(i));
		}
	}

	public Element getK1() {
		return k1;
	}

	public void setK1(Element k1) {
		this.k1 = k1;
	}

	public Element getK2() {
		return k2;
	}

	public void setK2(Element k2) {
		this.k2 = k2;
	}

	public Element getL() {
		return l;
	}

	public void setL(Element l) {
		this.l = l;
	}

	public LinkedList<Element> getKx() {
		return kx;
	}

	public void setKx(LinkedList<Element> kx) {
		this.kx = kx;
	}

	public LinkedList<FencAttribute> getAttList() {
		return attList;
	}

	public void setAttList(LinkedList<FencAttribute> attList) {
		this.attList = attList;
	}

	public String getAttStr() {
		return attStr;
	}

	public void setAttStr(String attStr) {
		this.attStr = attStr;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
