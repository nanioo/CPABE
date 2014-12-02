/**
 * FileName:     AttSubtree.java
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

import java.util.LinkedList;

public class AttSubtree{
	
	private Boolean isRoot = false;		//�ж��Ƿ�Ϊ��ڵ�
	public NodeType type = NodeType.FENC_ATTRIBUTE_POLICY_NODE_LEAF;		//�ڵ�����
	public int threshold;		//��ֵ
	public String att;
	public FencAttribute attribute;
	public boolean useSubnode = false;      //for dec
	public LinkedList<AttSubtree> subtrees = new LinkedList<AttSubtree>();	//�ӽڵ���Ϣ
	
	public Boolean getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(Boolean isRoot) {
		this.isRoot = isRoot;
	}

	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public String getAtt() {
		return att;
	}

	public void setAtt(String att) {
		this.att = att;
	}

	public boolean isUseSubnode() {
		return useSubnode;
	}

	public void setUseSubnode(boolean useSubnode) {
		this.useSubnode = useSubnode;
	}

	/**
	 * 
	 * @Title: treeToString 
	 * @Description: TODO() 
	 * @param:     
	 * @return: void    
	 * @throws
	 */
	public void treeToString(){
		System.out.println("threshold = "+threshold);
		System.out.println("type = "+type);
		System.out.println("useSubnode = "+useSubnode);
		System.out.println("############");
		if(att == null || att.length() == 0)
			for(int i=0; i< subtrees.size(); i++){
				//System.out.println("sub size = "+tree.subtrees.get(i).subtrees.size());
				System.out.println("att = "+subtrees.get(i).att);
				
				subtrees.get(i).treeToString();
			}
	}
}
