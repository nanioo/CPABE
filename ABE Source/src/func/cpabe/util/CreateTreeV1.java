package func.cpabe.util;
import func.lsss.object.AttSubtree;
import func.lsss.object.NodeType;

public class CreateTreeV1
{
	public AttSubtree[] trees = new AttSubtree[1000];
	/**
	 * delete the leftmost and the rightmost blank
	 * @param str
	 * @return a string without blanks in the leftmost and rightmost
	 */
	public String deleteBlank(String str)
	{
		int i;
		int n = str.length();
		for(i=0;i<n;++i)
		{
			if(str.charAt(i) != ' ')
			{
				str = str.substring(i,n);
				break;
			}
		}
		n = str.length();
		for(int m = n-1;m>=0;--m)
		{
			if(str.charAt(m) != ' ')
			{
				str = str.substring(0,m+1);
				break;
			}
		}
		return str;
	}
	/**
	 * get Atts from  subtree
	 * @param str subtree
	 * @return A String array
	 */
	public String[] getAtt(String str)
	{
		String[] str_array = new String[1000];
		str = deleteBlank(str);
		int i = 1;
		while(true)
		{
			str = deleteBlank(str);
			int num = str.indexOf(" ");
			if(num == -1)
			{
				str_array[i] = str;
				break;
			}
			String s = str.substring(0,num);
			if(s.equals("and") || s.equals("or"))
			{
				str_array[0] = s;
			}
			else
			{
				str_array[i] = s;
				++i;
			}
			str = str.substring(num,str.length());
		}
		return str_array;
	}
	/**
	 * get the Subtree
	 * @param str
	 * @return
	 */
	public AttSubtree getSubtree(String str)
	{
		AttSubtree tr = new AttSubtree();
		String[] str_array = getAtt(str);
		int i = 1;
		tr.type = NodeType.trans(str_array[0]);
		while(str_array[i] != null)
		{
			AttSubtree tree = new AttSubtree();
			tree.att = str_array[i];
			tr.subtrees.add(tree);
			++i;
		}
		return tr;
	}
	/**
	 * judge if the string is only made by number
	 * @param str
	 * @return
	 */
	public boolean isint(String str)
	{
		boolean flag = true;
		if(str == null)
			flag = false;
		else
		{
			for(int i = 0;i<str.length();++i)
			{
				if((int)str.charAt(i)<48||(int)str.charAt(i)>57)
				{
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
	/**
	 * 
	 * @param tr
	 * @return
	 */
	public AttSubtree replaceSubtree(AttSubtree tr)
	{
		int len = tr.subtrees.size();
		for(int i = 0;i<len;++i)
		{
			if(isint(tr.subtrees.get(i).att))
			{
				int count = Integer.parseInt(tr.subtrees.get(i).att);
				tr.subtrees.remove(i);
				tr.subtrees.add(i, trees[count]);
			}
		}
		return tr;
	}
	public void threshold(AttSubtree tree)
	{
		int threshold = 0;
		if(tree.type == NodeType.FENC_ATTRIBUTE_POLICY_NODE_AND){
			threshold = tree.subtrees.size();
		}else if(tree.type == NodeType.FENC_ATTRIBUTE_POLICY_NODE_OR){
			threshold = 1;
		}
		tree.threshold = threshold;
	}
	/**
	 * 
	 * @param str
	 * @return
	 */
	public AttSubtree treeGroup(String str)
	{
		AttSubtree tr = new AttSubtree();
		int [] stack = new int[100];
		int pop = -1;
		int len = str.length();
		int num = 0;
		for(int i = 0;i<len;++i)
		{
			if(str.charAt(i) == '(')
			{
				stack[++pop] = i;
			}
			if(str.charAt(i) == ')')
			{
				String substr = str.substring(stack[pop]+1,i);
				AttSubtree subtr = getSubtree(substr);
				threshold(subtr);
				tr = replaceSubtree(subtr);
				trees[num] = subtr;
				int length = substr.length()+2;
				str = str.substring(0, stack[pop])+num+str.substring(i+1,str.length());
				i = i-length+1;
				if(num>9)
				{
					if(num>99)
						len = len - length+3;
					else
						len = len - length+2;
				}
				else
				{
					len = len - length+1;
				}
				--pop;
				++num;
			}
		}
		return tr;
	}
	public AttSubtree stringToPolicy(String str)
	{
		System.out.println("the policy :"+str+" will be formed into tree");
		CreateTreeV1 ctr = new CreateTreeV1();
		AttSubtree tree = new AttSubtree();
		tree = ctr.treeGroup(str);
		//tree.treeToString();
		return tree;
	}
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		String s2 = "((abc and fff) or (ab and B))";
		String s = "((aaa and bbb and ccc) or (ddd and eee))";
		String s3 = "(((aaa and bbb and ccc and ddd) or eee) and (gg or hh or jj))";
		String s4 = "(A and B)";
		String s5 = "((abc or de or ef) and (ab and (a or (bc and ef and fg))))";
		String s6 = "(((red and blue and green) or black or purple) or (yellow and white))";
		String s7 = "(((de and efg) and (abg or cef)) or (cn and gg))";
		String s8 = "(aaa and aaabbb and ccc and ddd and eee and fff and hhh and iii)";
		String s9 = "(((((((mytech and a) and b) and c) or ((mymoney and a) and b1)) or (mymakert and a2)) or (mybank and a3)) or (myshell and a4))";
		CreateTreeV1 ctr = new CreateTreeV1();
		
		AttSubtree tre = ctr.stringToPolicy(s9);
		
//		String[] str ;
//		str = ctr.getAtt(s2);
//		for(int i = 0;i<100;++i)
//		{
//			System.out.println(str[i]);
//		}
	}

}
