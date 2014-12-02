/**
 * FileName:     MasterKey.java
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

import it.unisa.dia.gas.jpbc.Element;

public class MasterKey {
	
	public Element alpha1; 	/* alpha1 */
	public Element alpha2; 	/* alpha2 */	
	public Element alpha; /* G_alpha */
	public Element getAlpha1() {
		return alpha1;
	}
	public void setAlpha1(Element alpha1) {
		this.alpha1 = alpha1;
	}
	public Element getAlpha2() {
		return alpha2;
	}
	public void setAlpha2(Element alpha2) {
		this.alpha2 = alpha2;
	}
	public Element getAlpha() {
		return alpha;
	}
	public void setAlpha(Element alpha) {
		this.alpha = alpha;
	}	
}
