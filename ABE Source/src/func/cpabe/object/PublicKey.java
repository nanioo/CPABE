/**
 * FileName:     PublicKey.java
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
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * 公钥
 * @author nanio
 * @time 2014-10-15
 */
public class PublicKey {
	
	public Pairing p;					/* Pairing*/
	public Element a;					/*a*/
	public Element g1;					//G1
	public Element g2;					//G2
	public Element g1a;					//G1^a
	public Element g2a;					//G2^a
	public Element gAlpha1;				/* alpha1 */
	public Element gAlpha2;				/* alpha2 */
	public Element eggAlphaT;			/* e(g,g)^alpha   GT*/
}
