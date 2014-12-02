/**
 * FileName:     NodeType.java
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
package func.lsss.object;
public enum NodeType {
	
	FENC_ATTRIBUTE_POLICY_NODE_NULL,
	FENC_ATTRIBUTE_POLICY_NODE_LEAF,
	FENC_ATTRIBUTE_POLICY_NODE_AND,
	FENC_ATTRIBUTE_POLICY_NODE_OR,
	FENC_ATTRIBUTE_POLICY_NODE_THRESHOLD;
	
	public static NodeType trans(String type){
		NodeType nodeType = FENC_ATTRIBUTE_POLICY_NODE_NULL;
		
		if(type == null || type.equals("")){
			return nodeType;
		}
		if(type.equals("and")){
			nodeType =  FENC_ATTRIBUTE_POLICY_NODE_AND;
		}else if(type.equals("or")){
			nodeType =  FENC_ATTRIBUTE_POLICY_NODE_OR;
		}else if(type.equals("of")){
			nodeType =  FENC_ATTRIBUTE_POLICY_NODE_THRESHOLD;
		}else
			nodeType =  FENC_ATTRIBUTE_POLICY_NODE_LEAF;
		return 	nodeType;
	}
}
