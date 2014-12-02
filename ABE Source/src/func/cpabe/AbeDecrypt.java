/**
 * FileName:     AbeDecrypt.java
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

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;

import java.io.IOException;
import java.util.LinkedList;

import func.cpabe.object.AttributeKey;
import func.cpabe.object.CipherText;
import func.cpabe.object.InterValue;
import func.cpabe.object.PrivateKey;
import func.cpabe.object.PublicKey;
import func.cpabe.util.AESCoder;
import func.cpabe.util.AbeError;
import func.cpabe.util.AttributeUtil;
import func.cpabe.util.Common;
import func.cpabe.util.CreateTreeV1;
import func.cpabe.util.SerializeUtilsFixed;
import func.lsss.LSSSDec;
import func.lsss.object.AbePolicy;
import func.lsss.object.AttSubtree;
import func.lsss.object.FencCoefficient;
import func.lsss.object.NodeType;

public class AbeDecrypt {
	
	//FencCoeff coeff = new FencCoeff();
	AttributeUtil attUtil = new AttributeUtil();
	//LinkedList<FencAttribute> list = attUtil.attToList();
	AbePolicy policy = new AbePolicy();
	LSSSDec lsssDec = new LSSSDec();
	LinkedList<FencCoefficient> coefList = new LinkedList<FencCoefficient>();
	PublicKey pub = new PublicKey();
	AttributeUtil util = new AttributeUtil();
	CreateTreeV1 createTree = new CreateTreeV1();
	Element tempGT, tempGT2, tempGT3, tempGT4, tempGT5;
	Element prodT, finalT, prodOne ,aesKey;
	Element tempOne, tempOne2, tempOne3;
	Element tempZ1, tempZ2, test, oneofthree;
	static Element hashA, hashB;
	static Element hashg;
	
	public AbeDecrypt(){

	}

	/**
	 * 
	 * @Title: dec 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param pubfile
	 * @param: @param attfile
	 * @param: @param encfile
	 * @param: @param outfile
	 * @param: @return    
	 * @return: String    
	 * @throws
	 */
	public String dec(String pubfile, String attfile, String prifile, String encfile, String outfile){
		
		byte[] result = null;
		String ct1Str = "", output = "";
		Element ct1;
		AbeError err;
		
		CipherText ct = new CipherText();
		AttributeKey attKey = new AttributeKey();
		PrivateKey pri = new PrivateKey();
		
		// get pk
		pub = Common.readPublicKey(pubfile);
		if(pub == null){
			return null;
		}
		//get CT
		ct = Common.readCipherText(encfile);
		if(ct == null){
			return null;
		}
		// get ak
		attKey = Common.readAttributeKey(attfile, pub);
		if(attKey == null){
			return null;
		}
		// get sk
		pri = Common.readPrivateKey(prifile, pub);
		if(pri == null){
			return null;
		}
		ct.policy.root = createTree.stringToPolicy(ct.getPol());		
		// create attList from attStr
		util.stringToList(pub ,attKey.getAttStr(), attKey.attList);				// 11-15 tested
		System.out.println("attKey attStr"+attKey.getAttStr()+attKey.attList.size());
		//ct.policy.root.treeToString();
		err = lsssDec.coeFromPolicy(ct.policy, pub.p, attKey.attList); //11-15 tested
		
		ct.policy.root.setUseSubnode(true);
		ct.policy.root.treeToString();
		if(err != AbeError.NONE_ERROR){
			System.out.println("Invalid policy");
			return null;
		}	
		ct1 = pub.p.getGT().newElement();
		System.out.println("pri kx index0 = "+attKey.kx.get(0));
		// begin to dec
		ct1 = dec1(pub.p, ct, attKey);		
		aesKey = decOperate(pub.p, ct, pri, ct1);
		
		// show the result
		try {
			result = AESCoder.decrypt(aesKey.toBytes(), ct.aes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0; i<result.length; i++){
			output += (char)result[i];
		}
		try {
			Common.spitFile(outfile, result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("plaintext is "+ output);
		return ct1Str;
	}
	
	public String dec(InterValue value){
		
		byte[] attByte = null, ctByte = null, ct1Byte, pkByte = null;
		byte[] result = null, priBase = null;
		String ct1Str = "",  output = "";
		Element ct1;
		AbeError err;
		
		CipherText ct = new CipherText();
		AttributeKey attKey = new AttributeKey();
		AttSubtree tree = new AttSubtree();
		AttSubtree tree2 = new AttSubtree();
		PrivateKey pri = new PrivateKey();
		// get public key, attribute key and private key
		try {
			pkByte = Base64.decode(value.getPubKey());
			attByte = Base64.decode(value.getAttKey());
			priBase = Base64.decode(value.getPriKey().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(pkByte.length == 0 || attByte.length == 0 
					|| priBase.length == 0){
				return null;
			}else{
				pub = SerializeUtilsFixed.unserializeBswabePub(pkByte);
				attKey = SerializeUtilsFixed.unserializeAttributeKey(pub, attByte);
				pri = SerializeUtilsFixed.unserializePrivateKey(pub, priBase);
			}
		}
		
		//get CT
		ctByte = value.getEncrypted().getBytes();
		if(ctByte.length == 0){
			return null;
		}else{
			ct = AttributeUtil.tokenizeCT(ctByte);
		}

		ct.policy.root = createTree.stringToPolicy(ct.getPol());	
		// create attList from attStr
		util.stringToList(pub ,attKey.getAttStr(), attKey.attList);				// 11-15 tested
		System.out.println("attKey attStr"+attKey.getAttStr()+attKey.attList.size());
		//ct.policy.root.treeToString();
		err = lsssDec.coeFromPolicy(ct.policy, pub.p, attKey.attList); //11-15 tested
		if(err != AbeError.NONE_ERROR){
			System.out.println("Invalid policy");
			return null;
		}
		
		ct1 = pub.p.getGT().newElement();
		System.out.println("pri kx index0 = "+attKey.kx.get(0));
		// begin to dec
		ct1 = dec1(pub.p, ct, attKey);		
		aesKey = decOperate(pub.p, ct, pri, ct1);
		// show the result
		try {
			result = AESCoder.decrypt(aesKey.toBytes(), ct.aes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0; i<result.length; i++){
			output += (char)result[i];
		}
		value.setPlaintext(output);
		return ct1Str;
	}

	public Element decOperate(Pairing p, CipherText ct, PrivateKey pri, Element ct1){
		
		Element tempGT, temp2GT, result;
		tempGT = p.getGT().newElement();
		temp2GT = p.getGT().newElement();
		result = p.getGT().newElement();
		tempGT.setToOne();
		
		/* compute: tempGT = e(SK, c') *CT'.	*/
		tempGT = p.pairing(pri.k2, ct.c);
		temp2GT = tempGT.duplicate();
		temp2GT.mul(ct1);
		result = temp2GT.duplicate();
		
		System.out.println("pri k2 = "+pri.k2);
		System.out.println("CprimeONE= = "+ct.c);
		System.out.println("e(g, g)alphas = "+result);
		System.out.println("temp2GT= "+temp2GT);
	
		return temp2GT;
	}
	/**
	 * 
	 * @Title: dec1 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param p
	 * @param: @param ct
	 * @param: @param attKey
	 * @param: @return    
	 * @return: Element    
	 * @throws
	 */
	private Element dec1(Pairing p, CipherText ct, AttributeKey attKey){

		int indexCiph = 0, indexKey = 0;
		paramInit(p);

		coefList = lsssDec.coefList;
		//System.out.println(attKey.attList.get(0).att);
		System.out.println("coefList size is "+coefList.size());
		for(int i=0; i< coefList.size(); i++){
			System.out.println(coefList.get(i).coefficient);
		}
		for(int i=0; i< coefList.size(); i++){
			//System.out.println("when att is "+coefList.get(i).attribute.att);
			if(coefList.get(i).isSet == true){
				//System.out.println("i = "+i +" value ="+coefList.get(i).coefficient);
				//indexCiph  get index from list
				indexKey = lsssDec.getIndex(ct.policy.attList.get(i), attKey.attList);
				indexCiph = i;
				if (indexCiph < 0 || indexKey < 0) {
					//System.out.println("att = "+ct.policy.attList.get(i).att);
					System.out.println("Invalid ciphertext");
					return null;
				}
				/* Compute prodONE *= CONE[index_ciph]^{coefficient[i]} */
				tempOne = ct.ci.get(indexCiph).duplicate();
				tempOne.powZn(coefList.get(i).coefficient);
				tempOne2 = tempOne.duplicate();
				tempOne2.mul(prodOne);
				prodOne = tempOne2.duplicate();
				System.out.println("attr "+coefList.get(i).attribute.att);
				System.out.println("cof "+i+" = "+coefList.get(i).coefficient);
				System.out.println("ciphertext_WatersCP.CONE"+i+" = "+ct.ci.get(indexCiph));
				System.out.println("prodOne = "+prodOne);
				/* Compute finalT = e(KXONE[index_key]^{coefficient[i]}, DTWO[index_ciph]).		*/
				tempOne = attKey.kx.get(indexKey).duplicate();
				tempOne.powZn(coefList.get(i).coefficient);
				System.out.println("key_WatersCP->KXONE"+indexKey+" = "+attKey.kx.get(indexKey));
				finalT = p.pairing(tempOne, ct.di.get(indexCiph));
				System.out.println("ciphertext_WatersCP.DTWO"+indexCiph+" = "+ct.di.get(indexCiph));
				 /* Now compute prodT *= finalT.	*/
				tempGT2 = finalT.duplicate();
				tempGT2.mul(prodT);
				prodT = tempGT2.duplicate();
				System.out.println("att = "+ct.policy.attList.get(i).att);
				System.out.println("att hash = "+ct.policy.attList.get(i).attHash);
				System.out.println("coefficient"+i+" = "+coefList.get(i).coefficient);
				System.out.println("indexCiph = "+indexCiph);
				System.out.println("indexKey = "+indexKey);
			}
		} // for end
		System.out.println("prodT = "+prodT);
		/* Now compute prodT *= e(prodONE, LTWO).	*/
		finalT = p.pairing(prodOne, attKey.l);
		System.out.println("finalT = "+finalT);
		System.out.println("k1 = "+attKey.getK1());
		tempGT2 = finalT.duplicate();
		tempGT2.mul(prodT);
		prodT = tempGT2.duplicate();
		tempGT3 = prodT.duplicate();
		tempGT3.mul(tempGT3);
		System.out.println("tempGT3 = "+tempGT3);
		/* Final computation: prodT = e(CprimeONE, KTWO) / prodT.	*/
		//nanio     fixed!!!!!!!!!!!!
		tempGT = p.pairing(ct.c, attKey.getK1());
		System.out.println("tempGT3 = "+tempGT3);
		tempGT2 = tempGT3.duplicate();
		tempGT2.invert();
		prodT = tempGT.duplicate();
		prodT.mul(tempGT2);
		
		System.out.println("CT1 = "+prodT);
		return prodT;
	}
	
	/**
	 * 
	 * @Title: paramInit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param p    
	 * @return: void    
	 * @throws
	 */
	public void paramInit(Pairing p){
		
		tempGT = p.getGT().newElement();
		tempGT2 = p.getGT().newElement();
		tempGT3 = p.getGT().newElement(); 
		tempGT4 = p.getGT().newElement();
		tempGT5 = p.getGT().newElement();
		prodT = p.getGT().newElement();
		finalT = p.getGT().newElement();
		
		prodOne = p.getG1().newElement();
		tempOne = p.getG1().newElement();
		tempOne2 = p.getG1().newElement();
		tempOne3 = p.getG1().newElement();
		hashg = p.getG1().newElement();
		aesKey = p.getG1().newElement();
		
		tempZ1 = p.getZr().newElement();
		tempZ2 = p.getZr().newElement();
		test = p.getZr().newElement();
		oneofthree = p.getZr().newElement();
		hashB = p.getZr().newElement();
		hashA = p.getZr().newElement();
		
		oneofthree.set(3);
		oneofthree.invert();
		prodT.setToOne();
		prodOne.setToOne();
	}
	
	/**
	 * 
	 * @Title: main 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: @param args    
	 * @return: void    
	 * @throws
	 */
	public static void main(String[] args){
		AbeDecrypt dec = new AbeDecrypt();
		String result = dec.dec("pk.key", "ak.key", "pri.key", "encfile1", "decfile");
		System.out.println(result);
	}
}

/*
 * attr c
cof 1 = 2
ciphertext_WatersCP.CONE1 = 1459022802174769846915707662258272632552230256914521023467072152222138570408984900687507261248559383991284116775893755536615101750659779143051102773405665,3424681731058658292794978337660983606016371056495482393941657837653352337572188152162124104684258012817144985451987883963539378184261347235883430117839138,0
prodOne = 7524775785837498638368944068509747277538611124238804739669324710565741638332828494595764528144845707773481365757073635661877344570858106751664130487294504,3244332925114053651661549182115714003715764456403723658767287769767548020375848543496629454551017014273048001506649138905583446736767554168618075372602109,0
key_WatersCP->KXONE3 = 2620050233272952951683611430883248836900091303513742747871695999365270581047110700529359125419978790716195541113412664031532188043103416678120750848277669,289833035980021777194483777298459995440755712307019737327862933278558526404260834834049059685443053653807211310439833414706626247286724508660366400563218,0
ciphertext_WatersCP.DTWO1 = 7310275870591915462553868798049257567336330898696482017780537204907546551754525738648665182193114515661551716854960434030127342780229437809479073475143959,2500120644009054892476065198797707647926022717544881464507153679407740162307859729875239024200917072269862538143025389578862345912978615922375730039768487,0
att = c
att hash = 378634183065413026726743154949172395384334675418
coefficient1 = 2
indexCiph = 1
indexKey = 3
att ???b
att ???b
att ???b
attr b
cof 2 = 730750818665451621361119245571504901405976559615
ciphertext_WatersCP.CONE2 = 2141717318433209699623884918296796808505159273212461513478652005089901979582074504001088349590191403785629345310937226470490095757371119544594899801929171,244996898289959036660631630317386562498541764386382157215391522373945055239401450784687467421231562759231781159319486031844343901594772774257144226370089,0
prodOne = 8129452505310263947651196602429771928149270381087497056345495717410615406042887726563571675037448194411202140049466472422031147034876332163253285201533436,2976290501800771732372141250334519394097156763396654636754025517269952550803096803595427182389057986236156936886022476659043241741331784890591401553085352,0
key_WatersCP->KXONE2 = 972590485517219355451430553710145876931953738421937257684184512551823911039770399421199894046477861537759276548859403993748306955594672254238837972524324,5110647547309238254938742205044390105135825546984679378972366471329463135389125062323226061148276323502679491820111564973488584968092429155361971310606190,0
ciphertext_WatersCP.DTWO2 = 7341967568313859358717417460584948840513272163923247418527143834636051020492381802938851494455662396732907922144535726378020944177022770722095346744187011,7002645458559699881566052658542257375255210906930450886994095243407797882828687052159203078063805482393465188101247802406023961771428563606654733397910006,0
att = b
att hash = 667496121297570654179642826274263315845073651660
coefficient2 = 730750818665451621361119245571504901405976559615
indexCiph = 2
indexKey = 2
att ???mytech
attr mytech
cof 3 = 2
ciphertext_WatersCP.CONE3 = 7786824866974242761428794554344605779937597600708519147029358654027948358206496324140985764162371547738937275710151840440430049027442437225774549134638787,3076121003843922146034250290844242463523889563680724050221077263644900335205770861209974039641896673687738608540058085200857044284620169212480268977915763,0
prodOne = 2339523599399110298832273806481184957966427794535225080932960737652057549752662263419173689726452448110396293451807849353910709496391415117730974741826066,5343289847563305159769476804789255519399051619554097539056923678861187241067122880783936618703325456845117087245033090935051964470519264934647951235536297,0
key_WatersCP->KXONE0 = 1645238146309290241846615803517368152533671641917847874555449310791130793036913267141200645205223101077553257699820487740560812477874008711141401603662072,1196587394626362155490247851732567511032956566412994894416297737642448786613378572627795100159367012533926912610750477227651705868018987179115794347041329,0
ciphertext_WatersCP.DTWO3 = 1198886238571602295233613893065490881443235739646037909881338072765558986135686906953528833445957234035661464031376954089018383631575316375791982149862719,3634736677556898297966481666959390346543597818308057956977508199115837408233350029373426901949406504588148353094507005899841278526211717531209975185155605,0
att = mytech
att hash = 39792882176878507315002642944720679440611853675
coefficient3 = 2
indexCiph = 3
indexKey = 0
att ???a
att ???a
attr a
cof 4 = 730750818665451621361119245571504901405976559616
ciphertext_WatersCP.CONE4 = 692436724140229663439051050718010433756373726193069539709154669485951126715176151601196125302064867955125297068284952109631392624623240511689513154595471,5011130934648720340892262302111471952908907904779874664657166962260968084555090643624713096531725805928594322745469568064394178271965284649234619278273847,0
prodOne = 1192930689353514171469438306712066592686831002412116224391089715283381998884785888675534693817279403919423271244022783509007739878495745251091327626504135,6447651628707581560350413466487240660434834807228663739346891138641674296717352823170626582087124331828201715626655335240331453555871947831942653293958631,0
key_WatersCP->KXONE1 = 2517798255384636213400831832208871115870115379578461883782976977820166980718397428965913728664363410979417999120418949460842225872361598450129857264732238,2828553593931408487408851457206680109683884719687796471815676248167276078866689184575841794495725658553382035605147633144529870198660634166297993399044876,0
ciphertext_WatersCP.DTWO4 = 8571304083545440172921851512488180111478230323709531480101980601463348515589107786080854558187038584587782745386652470997757450156628290323150621704375845,6204032122218544707702878396446171976058343311542910225307798362530883855490547908662257545550937723880585513305802465720691056540931578158579851167772090,0
*/
 