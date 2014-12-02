package func.cpabe.util;

import func.cpabe.object.AttributeKey;
import func.cpabe.object.CipherText;
import func.cpabe.object.MasterKey;
import func.cpabe.object.PrivateKey;
import func.cpabe.object.PublicKey;
import func.lsss.object.AttSubtree;
import func.lsss.object.FencAttribute;
import func.lsss.object.NodeType;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.DefaultCurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class SerializeUtilsFixed {

	private static String curveParams = "type a\n"
			+ "q 87807107996633125224377819847540498158068831994142082"
			+ "1102865339926647563088022295707862517942266222142315585"
			+ "8769582317459277713367317481324925129998224791\n"
			+ "h 12016012264891146079388821366740534204802954401251311"
			+ "822919615131047207289359704531102844802183906537786776\n"
			+ "r 730750818665451621361119245571504901405976559617\n"
			+ "exp2 159\n" + "exp1 107\n" + "sign1 1\n" + "sign0 1\n";
	
	static CurveParameters params = new DefaultCurveParameters()
	.load(new ByteArrayInputStream(curveParams.getBytes()));
	static Pairing pairing = PairingFactory.getPairing(params);
	
	static LinkedList<FencAttribute> attList = new LinkedList<FencAttribute>();
	
	public SerializeUtilsFixed(){

	}
	 /*Method has been test okay */
	public static void serializeElement(ArrayList<Byte> arrlist, Element e) {
		byte[] arr_e = e.toBytes();
		//serializeUint32(arrlist, arr_e.length);
		//System.out.println("e length = "+e.getField().toString());
		byteArrListAppend(arrlist, arr_e);
	}
	
	/**
	 * 
	 * @param arr
	 * @param offset
	 * @param e
	 * @return
	 */
 /*	 Method has been test okay */
	public static int unserializeElement(byte[] arr, int offset, Element e) {
		int len =0;
		int i;
		byte[] e_byte;

		len = e.getLengthInBytes();
		e_byte = new byte[(int) len];
		//offset += 1;
		for (i = 0; i < len; i++)
			e_byte[i] = arr[offset + i];
		e.setFromBytes(e_byte);

		//e.setFromBytes(arr, offset);
		//offset += 20;
		return (int) (offset + len);
	}

	/**
	 * 
	 * @param arr
	 * @param offset
	 * @param e
	 * @return
	 */
	public static int unserializeElementG(byte[] arr, int offset, Element e) {
		int len =0;
		int i;
		byte[] e_byte;

		len = e.getLengthInBytes();
		e_byte = new byte[(int) len];
		//offset += 1;
		for (i = 0; i < len; i++)
			e_byte[i] = arr[offset + i];
		e.setFromBytes(e_byte);
		//e.setFromBytes(arr, offset);
		//offset += 20;
		return (int) (offset+len);
	}
	
	/**
	 * 
	 * @param arr
	 * @param offset
	 * @param e
	 * @return
	 */
	public static int unserializeElementGT(byte[] arr, int offset, Element e) {
		int len =0;
		int i;
		byte[] e_byte;

		len = e.getLengthInBytes();
		e_byte = new byte[(int) len];
		//offset += 1;
		for (i = 0; i < len; i++)
			e_byte[i] = arr[offset + i];
		e.setFromBytes(e_byte);
		//e.setFromBytes(arr, offset);
		//offset += 20;
		return (int) (offset+len);
	}
	
	/**
	 * 
	 * @param arrlist
	 * @param s
	 */
//	public static void serializeString(ArrayList<Byte> arrlist, String s) {
//		byte[] b = s.getBytes();
//		serializeUint32(arrlist, b.length);
//		byteArrListAppend(arrlist, b);
//	}

	/**
	 * 
	 * @param arr
	 * @param offset
	 * @param sb
	 * @return
	 */
	public static int unserializeString(byte[] arr, int offset, StringBuffer sb) {
		int i;
		int len;
		byte[] str_byte;
		String str = "";
	
		len = 200;
		str_byte = new byte[len];
		for (i = 0; i < len; i++)
			str_byte[i] = arr[offset + i];
	
		sb.append(new String(str_byte));
		str = sb.substring(0);
		len = str.indexOf(")");
		return offset + len;
	}
	
	public static int unserializeString(int len, byte[] arr, int offset, StringBuffer sb) {
		int i;
		byte[] str_byte;
		String str = "";
	
		str_byte = new byte[len];
		for (i = 0; i < len; i++)
			str_byte[i] = arr[offset + i];
	
		sb.append(new String(str_byte));
		str = sb.substring(0);
		len = str.indexOf(")");
		return offset + len;
	}

	/**
	 * 
	 * @param arr
	 * @param offset
	 * @param sb
	 * @return
	 */
	public static int unserializeAttList(byte[] arr, int offset, StringBuffer sb) {
		int i;
		int len;
		byte[] str_byte;
		String str = "";
	
		len = 300;
		str_byte = new byte[len];
		for (i = 0; i < len; i++)
			str_byte[i] = arr[offset + i];
	
		sb.append(new String(str_byte));
		str = sb.substring(0);
		len = str.indexOf(")");
		return offset + len + 1;
	}
	/**
	 * 
	 * @param pub
	 * @return
	 */
	public static byte[] serializeBswabePub(PublicKey pub) {
		ArrayList<Byte> arrlist = new ArrayList<Byte>();
	
		serializeElement(arrlist, pub.g1);
		serializeElement(arrlist, pub.g2);
		serializeElement(arrlist, pub.g1a);
		serializeElement(arrlist, pub.g2a);
		serializeElement(arrlist, pub.eggAlphaT);
	
		return Byte_arr2byte_arr(arrlist);
	}
	
	 /*  ->public_params.gONE), 
	   &(->public_params.gTWO),
	   &(->public_params.gaONE),
	   &(->public_params.gaTWO),
	   &(->public_params.eggalphaT))
	  */
	/**
	 * 
	 * @param b
	 * @return
	 */
	public static PublicKey unserializeBswabePub(byte[] b) {
		PublicKey pub;
		int offset;
	
		pub = new PublicKey();
		offset = 0;

		pub.p = PairingFactory.getPairing(params);
		Pairing pairing = pub.p;
	
		pub.g1 = pairing.getG1().newElement();
		pub.g2 = pairing.getG2().newElement();
		//pub.a = pairing.getZr().newElement();
		pub.g1a = pairing.getG1().newElement();
		pub.g2a = pairing.getG2().newElement();
		pub.eggAlphaT = pairing.getGT().newElement();
		
		//offset =4;
		offset = unserializeElementG(b, offset, pub.g1);
//		System.out.println("g1 = "+pub.g1);
//		System.out.println("offset = "+ offset);
		offset = unserializeElementG(b, offset, pub.g2);
//		System.out.println("g2 = "+pub.g2);
//		System.out.println("offset = "+ offset);
		//offset = unserializeElement(b, offset, pub.a);
		offset = unserializeElementG(b, offset, pub.g1a);
//		System.out.println("g1a = "+pub.g1a);
//		System.out.println("offset = "+ offset);
		offset = unserializeElementG(b, offset, pub.g2a);
//		System.out.println("g2a = "+pub.g2a);
//		System.out.println("offset = "+ offset);
		offset = unserializeElementGT(b, offset, pub.eggAlphaT);
//		System.out.println("eggAlphaT = "+pub.eggAlphaT);
//		System.out.println("offset = "+offset);
	
		return pub;
	}

	/**
	 * 
	 * @param msk
	 * @return
	 */
	/* Method has been test okay*/ 
	public static byte[] serializeBswabeMsk(MasterKey msk) {
		ArrayList<Byte> arrlist = new ArrayList<Byte>();
		
		serializeElement(arrlist, msk.alpha1);
		serializeElement(arrlist, msk.alpha2);
		serializeElement(arrlist, msk.alpha);
		return Byte_arr2byte_arr(arrlist);
	}

	/**
	 * 
	 * @param pub
	 * @param b
	 * @return
	 */
	public static MasterKey unserializeBswabeMsk(PublicKey pub, byte[] b) {
		
		int offset = 0;
		MasterKey mk = new MasterKey();
	
		mk.alpha = pub.p.getZr().newElement();
		mk.alpha1 = pub.p.getZr().newElement();
		mk.alpha2 = pub.p.getZr().newElement();
		
		offset = unserializeElement(b, offset, mk.alpha1);
		offset = unserializeElement(b, offset, mk.alpha2);
		offset = unserializeElement(b, offset, mk.alpha);
		System.out.println("after get mk, offset = "+offset);
		return mk;
	}

	/**
	 * 
	 * @param pri
	 * @return
	 */
	public static byte[] serializeBswabePrv(PrivateKey pri) {
		
		ArrayList<Byte> arrlist = new ArrayList<Byte>();
		
		serializeElement(arrlist, pri.k2);

		return Byte_arr2byte_arr(arrlist);
	}
	
	/**
	 * 
	 * @param pri
	 * @return
	 */
	public static byte[] serializeBswabeAtt(AttributeKey attKey) {
		
		ArrayList<Byte> arrlist = new ArrayList<Byte>();
		int size=0, attListSize =0;
		String s, attListSizeStr;
		byte[] len={2,0,0,0};
		//att list string size
		attListSize = attKey.getAttStr().length();
		attListSizeStr = String.valueOf(attListSize);
		System.out.println("attListSizeStr size "+attListSizeStr);
		arrlist.add(Byte.valueOf(attListSizeStr));
		for(int i=0; i<3; i++)
			arrlist.add(len[1]);
		
		byteArrListAppend(arrlist, attKey.getAttStr().getBytes());
		size = attKey.attList.size();
		s = String.valueOf(size);

		System.out.println("s = "+s+"  "+Byte.valueOf(s));
		len[0] = Byte.valueOf(s);
		arrlist.add(len[1]);
		arrlist.add(Byte.valueOf(s));
		arrlist.add(len[1]);
		arrlist.add(len[2]);
		arrlist.add(len[3]);

		System.out.println("bytesToHexString = "+bytesToHexString(len));
		serializeElement(arrlist, attKey.getK1());
		System.out.println(arrlist.size());
		serializeElement(arrlist, attKey.l);
		System.out.println(arrlist.size());
		for (int i = 0; i < attKey.kx.size(); i++) {
			serializeElement(arrlist, attKey.kx.get(i));
			System.out.println(arrlist.size());
		}
		System.out.println(" after serialize, the byte size is" + arrlist.size());
		return Byte_arr2byte_arr(arrlist);
	}
	
	public static byte[] serializeCT1(Element e) {
		ArrayList<Byte> arrlist = new ArrayList<Byte>();
		serializeElement(arrlist, e);
		return Byte_arr2byte_arr(arrlist);

	}
	
	public static Element unserializeCT1(PublicKey pk, byte[] ctBuf) {
		//ArrayList<Byte> arrlist = new ArrayList<Byte>();
		Element ct1 = pk.p.getGT().newElement();
		int offset = 0;
		offset = unserializeElement(ctBuf, offset, ct1);
		return ct1;

	}


	public static PrivateKey unserializePrivateKey(PublicKey pub, byte[] pribuf){
		
		PrivateKey pri = new PrivateKey();
		int offset = 0;
		pri.k2 = pairing.getG2().newElement();
		offset = unserializeElement(pribuf, offset, pri.k2);
		System.out.println("pri.k2 = "+ pri.k2);

		System.out.println("offset ="+offset);
		return pri;
	}
	
	public static AttributeKey unserializeAttributeKey(PublicKey pub, byte[] attbuf){
		
		AttributeKey attKey = new AttributeKey();
		StringBuffer sb = new StringBuffer();
		int offset = 0;
		int size =0 , index=0, attListStrSize =0;
		Element k;
		
		attKey.k1 = pairing.getG2().newElement();
		attKey.k2 = pairing.getG2().newElement();
		attKey.l = pairing.getG2().newElement();
		
		attListStrSize = unserializeUint82(attbuf, offset);
		System.out.println("attListStrSize "+attListStrSize);
		offset +=4;
		offset = unserializeAttList(attbuf, offset, sb);
		System.out.println("351offset ="+offset);
		size = unserializeUint8(attbuf, offset);
		attKey.setAttStr(sb.substring(0, attListStrSize));
		System.out.println("pri.attStr = "+attKey.getAttStr() + " :end");
		attKey.setSize(size);
		offset += 5;
		offset = unserializeElement(attbuf, offset, attKey.k1);
		System.out.println("pri.k1 = "+ attKey.k1);
		offset = unserializeElement(attbuf, offset, attKey.l);
		System.out.println("pri.l = "+ attKey.l);
		System.out.println("offset ="+offset);
		
		for(int i=0; i<size; i++){
			k = pairing.getG2().newElement();
			offset = SerializeUtilsFixed.unserializeElement(attbuf, offset, k);
			attKey.kx.add(k);
			System.out.println("kx"+ i +"="+attKey.kx.get(i));
		}
		
		System.out.println("kx size ="+attKey.kx.size());
		System.out.println("offset ="+offset);
		return attKey;
	}
	
	/**
	 * 
	 * @param cph
	 * @return
	 */
	public static byte[] bswabeCphSerialize(CipherText ct) {
		
		ArrayList<Byte> arrlist = new ArrayList<Byte>();
		int size=0, type=2, sessionSize = 16;
		String typeStr, sessionSizeStr, sizeStr, policySizeStr;
		byte[] len={0,0,0,0};
		
		System.out.println(arrlist.size());
		//serializeUint32(arrlist, attKey.attList.size());
		size = ct.policy.attList.size();
		typeStr = String.valueOf(type);
		sessionSizeStr = String.valueOf(sessionSize);
		sizeStr = String.valueOf(size);
		policySizeStr = String.valueOf(ct.policy.getPolicy().length());
		
		System.out.println("type = "+typeStr+"  "+Byte.valueOf(typeStr));
		//ct type
		arrlist.add(Byte.valueOf(typeStr));
		for(int i=0; i<3; i++)
		arrlist.add(len[1]);
		//sessionkey size
		arrlist.add(Byte.valueOf(sessionSizeStr));
		for(int i=0; i<3; i++)
			arrlist.add(len[1]);	
		// att num
		arrlist.add(Byte.valueOf(sizeStr));
		for(int i=0; i<3; i++)
			arrlist.add(len[1]);
		// policy
//		arrlist.add(Byte.valueOf(policySizeStr));
//		for(int i=0; i<3; i++)
//			arrlist.add(len[1]);
		serializeUint32(arrlist,ct.policy.getPolicy().length());
		System.out.println(" writing policy ...."+ct.policy.getPolicy());
		byteArrListAppend(arrlist, ct.policy.getPolicy().getBytes());
		//cprimeone
		serializeElement(arrlist, ct.c);
		
		for (int i = 0; i < ct.policy.attList.size(); i++) {
			serializeElement(arrlist, ct.policy.attList.get(i).attHash);
			serializeElement(arrlist, ct.ci.get(i));
			serializeElement(arrlist, ct.di.get(i));
		}
		System.out.println(" after serialize, the byte size is" + arrlist.size());
		return Byte_arr2byte_arr(arrlist);
		
	}
	
	public static byte[] SerializeCph1(CipherText ct) {
		
		ArrayList<Byte> arrlist = new ArrayList<Byte>();
		int size=0, type=2, sessionSize = 16;
		String typeStr, sessionSizeStr, sizeStr, policySizeStr;
		byte[] len={0,0,0,0};
		
		System.out.println(arrlist.size());
		System.out.println("the policy in ct "+ct.policy.getPolicy());
		//serializeUint32(arrlist, attKey.attList.size());
		size = ct.policy.attList.size();
		typeStr = String.valueOf(type);
		sessionSizeStr = String.valueOf(sessionSize);
		sizeStr = String.valueOf(size);
		policySizeStr = String.valueOf(ct.policy.getPolicy().length());

		System.out.println("type = "+typeStr+"  "+Byte.valueOf(typeStr));

		// att num
		arrlist.add(Byte.valueOf(sizeStr));
		for(int i=0; i<3; i++)
			arrlist.add(len[1]);
		// policy
		arrlist.add(Byte.valueOf(policySizeStr));
		for(int i=0; i<3; i++)
			arrlist.add(len[1]);
		System.out.println(" writing policy ...."+ct.policy.getPolicy());
		byteArrListAppend(arrlist, ct.policy.getPolicy().getBytes());
		//cprimeone
		serializeElement(arrlist, ct.c);
		System.out.println(" share ...."+ct.m);
		serializeElement(arrlist, ct.m);
		System.out.println(" after serialize, the byte size is" + arrlist.size());
		return Byte_arr2byte_arr(arrlist);
		
	}
	
	/**
	 * 
	 * @Title: unserializeFenattList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param offset
	 * @param: @param cphBuf
	 * @param: @param p
	 * @param: @return    
	 * @return: LinkedList<FencAttribute>    
	 * @throws
	 */
//	private static LinkedList<FencAttribute> unserializeFenattList(int offset, 
//			byte[] cphBuf, Pairing p){
//		LinkedList<FencAttribute> list = new LinkedList<FencAttribute>();
//		int size = 0;
//		StringBuffer sb;
//		FencAttribute att;
//
//		size = SerializeUtilsFixed.unserializeUint32(cphBuf, offset);
//		offset += 4;
//		
//		for(int i=0; i<size; i++){
//			att = new FencAttribute();
//			sb = new StringBuffer("");
//			//System.out.println("i= "+i);
//			offset = SerializeUtilsFixed.unserializeString(cphBuf, offset, sb);
//			att.att = sb.substring(0);
//			att.attHash = p.getZr().newElement();
//			att.share = p.getZr().newElement();
//			offset = SerializeUtilsFixed.unserializeElement(cphBuf, offset, att.attHash);
//			att.isHashed = true;
//			offset = SerializeUtilsFixed.unserializeElement(cphBuf, offset, att.share);
//			att.containShare = true;
//			list.add(att);
//		}
//		return list;
//	}
//	
	/**
	 * 
	 * @Title: unserializePolicy 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param cphBuf
	 * @param: @param offset
	 * @param: @return    
	 * @return: AttSubtree    
	 * @throws
	 */
	private static AttSubtree unserializePolicy(byte[] cphBuf, int[] offset){
		int n = 0, type = 1;
		AttSubtree tree = new AttSubtree();
		AttSubtree subtree = new AttSubtree();
		
		n = unserializeUint32(cphBuf, offset[0]);
		offset[0] += 4;
		if(n == 0){
			tree.setThreshold(0);
			tree.subtrees = null;
			StringBuffer sb = new StringBuffer();
			offset[0] = unserializeString(cphBuf, offset[0], sb);
			tree.att = sb.substring(0);
			type = unserializeUint32(cphBuf, offset[0]);
			offset[0] += 4;
			tree.setType(NodeType.values()[type]);
		}else{
			tree.subtrees = new LinkedList<AttSubtree>();
			tree.setThreshold(unserializeUint32(cphBuf, offset[0]));
			offset[0] += 4;
			type = unserializeUint32(cphBuf, offset[0]);
			offset[0] += 4;
			tree.setType(NodeType.values()[type]);
			for (int i = 0; i < n; i++){
				subtree = new AttSubtree();
				subtree = unserializePolicy(cphBuf, offset);
				tree.subtrees.add(subtree);
			}
		}
		
		return tree;
	}
	
	public static void serializeList(ArrayList<Byte> arrlist, 
			LinkedList<Element> list){
		
		for(int i=0; i<list.size(); i++){
			SerializeUtilsFixed.serializeElement(arrlist, list.get(i));
		}
	}

	/**
	 * 
	 * @param ctByte
	 * @return
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
	 * read from byte[],write into ciphertext
	 * @param ctByte
	 * @param ctext
	 */
	static public void writeCT(String ctByte, CipherText ctext){
		ArrayList<Byte> arrlist = new ArrayList<Byte>();
		String policy;
		AttributeUtil util = new AttributeUtil();
		System.out.println("ct = "+ctByte);
		byte[] abeByte;
		Element e1, e2;
		int type = 0, offset = 0, size = 0, index =0, length = 0;
		StringBuffer sb = new StringBuffer();
		FencAttribute att = new FencAttribute();
		
		try {
			abeByte = Base64.decode(ctByte);
			System.out.println("abeByte size = "+abeByte.length);
			System.out.println("####This is ct format ");
			/* ciphertext type	*/
			type = unserializeUint82(abeByte, offset);
			offset +=4;
			System.out.println("type = "+type);
			/* KEM session key size */
			type = unserializeUint82(abeByte, offset);
			offset +=4;
			System.out.println("type = "+type);
			/* num_attributes	*/
			size = unserializeUint82(abeByte, offset);
			offset +=4;
			System.out.println("999size = "+size);
			/* policy length*/
			length = unserializeUint32(abeByte, offset);
			//length = unserializeUint82(abeByte, offset);
			offset +=4;
			unserializeString(length, abeByte, offset, sb);
			offset +=length;
			//index = sb.substring(0).lastIndexOf(")");
			System.out.println(" policy: length = "+length);
			policy = sb.substring(0);
			System.out.println(" policy: policy1 = "+policy);
			System.out.println("offset = "+offset);
			policy =  util.judgeBracket(policy);	//format the policy
			System.out.println("sb = "+policy);
			ctext.setPol(policy);
			//nanio : need to be fixed
			//transToAttlist(policy, ctext.policy.attList);
			ctext.policy.attList = util.policyToListOrder(policy);
			System.out.println(" policy: policy2 = "+policy);
			ctext.c = pairing.getG1().newElement();
			//nanio
			//offset +=2;
			offset = unserializeElement(abeByte, offset, ctext.c);
			System.out.println("cprimeone = "+ctext.c);
			
			for(int i=0; i<size; i++){
				att = new FencAttribute();
				att.attHash = pairing.getZr().newElement();
				e1 = pairing.getG1().newElement();
				e2 = pairing.getG2().newElement();
				offset = unserializeElement(abeByte, offset, att.attHash);
				
				att.att = ctext.policy.attList.get(i).att;
				attList.add(att);
				offset = unserializeElement(abeByte, offset, e1);
				offset = unserializeElement(abeByte, offset, e2);
				ctext.ci.add(e1);
				ctext.di.add(e2);
				System.out.println("att"+i+" = "+att.att);
				System.out.println("hash"+i+" = "+att.attHash);
				if(!att.attHash.isZero()){
					serializeElement(arrlist, att.attHash);
				}
				System.out.println("c"+i+" = "+e1);
				System.out.println("d"+i+" = "+e2);
			}
			ctext.policy.attList = attList;
//			for(int i=0; i<ctext.policy.attList.size(); i++){
//				System.out.println("####policy test ");
//				System.out.println("policy test "+ctext.policy.attList.get(i).att);
//				System.out.println("policy test "+ctext.policy.attList.get(i).att.length());
//			}
			//writeHash(arrlist);
			System.out.println("abeByte end size = "+offset);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 
	static public void writeCT1(String ctByte, CipherText ctext){
		ArrayList<Byte> arrlist = new ArrayList<Byte>();
		String policy;
		AttributeUtil util = new AttributeUtil();
		System.out.println("ct = "+ctByte);
		byte[] abeByte;
		Element e1, e2;
		int type = 0, offset = 0, size = 0, index =0, length = 0;
		StringBuffer sb = new StringBuffer();
		FencAttribute att = new FencAttribute();
		
		try {
			abeByte = Base64.decode(ctByte);
			System.out.println("abeByte size = "+abeByte.length);
			System.out.println("####This is ct format ");
//			/* ciphertext type	*/
//			type = unserializeUint82(abeByte, offset);
//			offset +=4;
//			System.out.println("type = "+type);
//			/* KEM session key size */
//			type = unserializeUint82(abeByte, offset);
//			offset +=4;
//			System.out.println("type = "+type);
			/* num_attributes	*/
			size = unserializeUint82(abeByte, offset);
			offset +=4;
			System.out.println("999size = "+size+" offset ="+offset);
			/* policy length*/
			length = unserializeUint82(abeByte, offset);
			System.out.println("999size = "+length+" offset ="+offset);
			offset +=4;
			unserializeString(length, abeByte, offset, sb);
			offset +=length;
			//index = sb.substring(0).lastIndexOf(")");
			System.out.println(" policy: length = "+length);
			policy = sb.substring(0);
			System.out.println(" policy: policy1 = "+policy);
			System.out.println("offset = "+offset);
			policy =  util.judgeBracket(policy);	//format the policy
			System.out.println("sb = "+policy);
			ctext.setPol(policy);
			//nanio : need to be fixed
			//transToAttlist(policy, ctext.policy.attList);
			ctext.policy.attList = util.policyToListOrder(policy);
			System.out.println(" policy: policy2 = "+policy);
			ctext.c = pairing.getG1().newElement();
			ctext.m = pairing.getZr().newElement();
			//nanio
			//offset +=2;
			offset = unserializeElement(abeByte, offset, ctext.c);
			System.out.println("cprimeone = "+ctext.c);
			offset = unserializeElement(abeByte, offset, ctext.m);
			System.out.println("share = "+ctext.m);
			//ctext.policy.attList = attList;
//			for(int i=0; i<ctext.policy.attList.size(); i++){
//				System.out.println("####policy test ");
//				System.out.println("policy test "+ctext.policy.attList.get(i).att);
//				System.out.println("policy test "+ctext.policy.attList.get(i).att.length());
//			}
			//writeHash(arrlist);
			System.out.println("abeByte end size = "+offset);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
//	static public void writeCT2(String ctByte, CipherText ctext){
//		ArrayList<Byte> arrlist = new ArrayList<Byte>();
//		String policy;
//		AttributeUtil util = new AttributeUtil();
//		System.out.println("ct = "+ctByte);
//		byte[] abeByte;
//		Element e1, e2;
//		int type = 0, offset = 0, size = 0, index =0, length = 0;
//		StringBuffer sb = new StringBuffer();
//		FencAttribute att = new FencAttribute();
//		
//		try {
//			abeByte = Base64.decode(ctByte);
//			System.out.println("abeByte size = "+abeByte.length);
//			System.out.println("####This is ct format ");
//			/* ciphertext type	*/
//			type = unserializeUint82(abeByte, offset);
//			offset +=4;
//			System.out.println("type = "+type);
//			/* KEM session key size */
//			type = unserializeUint82(abeByte, offset);
//			offset +=4;
//			System.out.println("type = "+type);
//			/* num_attributes	*/
//			size = unserializeUint82(abeByte, offset);
//			offset +=4;
//			System.out.println("999size = "+size);
//			/* policy length*/
//			length = unserializeUint82(abeByte, offset);
//			offset +=4;
//			unserializeString(abeByte, offset, sb);
//			offset +=length;
//			index = sb.substring(0).lastIndexOf(")");
//			System.out.println(" policy: length = "+length);
//			policy = sb.substring(0, length);
//			System.out.println(" policy: policy1 = "+policy);
//			System.out.println("offset = "+offset);
//			policy =  util.judgeBracket(policy);	//format the policy
//			System.out.println("sb = "+policy);
//			ctext.setPol(policy);
//			//nanio : need to be fixed
//			//transToAttlist(policy, ctext.policy.attList);
//			ctext.policy.attList = util.policyToListOrdered(policy);
//			System.out.println(" policy: policy2 = "+policy);
//			ctext.c = pairing.getG1().newElement();
//			//nanio
//			//offset +=2;
//			offset = unserializeElement(abeByte, offset, ctext.c);
//			System.out.println("cprimeone = "+ctext.c);
//			
//			for(int i=0; i<size; i++){
//				att = new FencAttribute();
//				att.attHash = pairing.getZr().newElement();
//				e1 = pairing.getG1().newElement();
//				e2 = pairing.getG2().newElement();
//				offset = unserializeElement(abeByte, offset, att.attHash);
//				
//				att.att = ctext.policy.attList.get(i).att;
//				attList.add(att);
//				offset = unserializeElement(abeByte, offset, e1);
//				offset = unserializeElement(abeByte, offset, e2);
//				ctext.ci.add(e1);
//				ctext.di.add(e2);
//				System.out.println("att"+i+" = "+att.att);
//				System.out.println("hash"+i+" = "+att.attHash);
//				if(!att.attHash.isZero()){
//					serializeElement(arrlist, att.attHash);
//				}
//				System.out.println("c"+i+" = "+e1);
//				System.out.println("d"+i+" = "+e2);
//			}
//			ctext.policy.attList = attList;
////			for(int i=0; i<ctext.policy.attList.size(); i++){
////				System.out.println("####policy test ");
////				System.out.println("policy test "+ctext.policy.attList.get(i).att);
////				System.out.println("policy test "+ctext.policy.attList.get(i).att.length());
////			}
//			//writeHash(arrlist);
//			System.out.println("abeByte end size = "+offset);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//	static private void writeHash(ArrayList<Byte> arrlist){
//		String file = "test";
//		byte[] input;
//		try {
//			input = Byte_arr2byte_arr(arrlist);
//			Common.spitFile(file, Base64.encodeBytes(input).getBytes());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//	}	

	 /*Method has been test okay 
	 potential problem: the number to be serialize is less than 2^31 */
	private static void serializeUint32(ArrayList<Byte> arrlist, int k) {
		int i;
		byte b;
	
		for (i = 3; i >= 0; i--) {
			b = (byte) ((k & (0x000000ff << (i * 8))) >> (i * 8));
			arrlist.add(Byte.valueOf(b));
		}
	}
	
/*
	
	 * Usage:
	 * 
	 * You have to do offset+=4 after call this method
	 
	 Method has been test okay 
	 */
	private static int unserializeUint32(byte[] arr, int offset) {
		int i;
		int r = 0;
	
		for (i = 3; i >= 0; i--)
			r |= (byte2int(arr[offset++])) << (i * 8);
			
		return r;
	}
	
	private static int unserializeUint8(byte[] arr, int offset) {
		int i;
		int r = 0;
	
		for (i = 3; i >= 0; i--){
			offset++;
			
			if(arr[offset] == 0)
				continue;
			else 
			r = (byte2int(arr[offset]));			
		}
		//System.out.println("111offset  = "+offset);
		return r;
	}
	
	private static int unserializeUint82(byte[] arr, int offset) {
		int i;
		int r = 0;
	
		for (i = 3; i >= 0; i--){
			if(arr[offset] == 0)
				continue;
			else 
			r = (byte2int(arr[offset]));
			offset++;
		}
		return r;
	}
	
	private static int byte2int(byte b) {
		if (b >= 0)
			return b;
		return (256 + b);
	}

	private static void byteArrListAppend(ArrayList<Byte> arrlist, byte[] b) {
		int len = b.length;
		for (int i = 0; i < len; i++)
			arrlist.add(Byte.valueOf(b[i]));
	}

	private static byte[] Byte_arr2byte_arr(ArrayList<Byte> B) {
		int len = B.size();
		byte[] b = new byte[len];
	
		for (int i = 0; i < len; i++)
			b[i] = B.get(i).byteValue();
	
		return b;
	}
	
	public static void main(String[] args){
		ArrayList<Byte> arrlist = new ArrayList<Byte>();
		int i = 10, output = 0, offset = 0;
		byte[] in, out, out1;
		String str;
		
		serializeUint32(arrlist, i);
		serializeUint32(arrlist, 11);
		in = Byte_arr2byte_arr(arrlist);
		str = Base64.encodeBytes(in);
		try {
			Common.spitFile("test", str.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			out = Common.suckFile("test");
			out1 = Base64.decode(out);
			output = unserializeUint32(out1, offset);
			offset+=4;
			System.out.println(output);
			output = unserializeUint32(out1, offset);
			System.out.println(output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
