package com.example.myq.util;

/**
 *@author coolszy
 *@date 2012-3-26
 *@blog http://blog.92coding.com
 */
public class StringUtil
{
	/**
	 * 从字符串转换成整形
	 * @param str 待转换字符串 
	 * @return
	 */
	public static int String2Int(String str)
	{
		try
		{
			int value = Integer.valueOf(str);
			return value;
		} catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
}
