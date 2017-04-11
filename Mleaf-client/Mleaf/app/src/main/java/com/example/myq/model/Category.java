package com.example.myq.model;


/**
 *@author coolszy
 *@date 2012-3-26
 *@blog http://blog.92coding.com
 */
public class Category
{
	//类型编号
	private int cid;
	//类型名称
	private String title;
	//类型次数
	private int sequnce;

	public Category()
	{
		super();
	}

	public Category(int cid, String title)
	{
		super();
		this.cid = cid;
		this.title = title;
	}

	public Category(int cid, String title, int sequnce)
	{
		super();
		this.cid = cid;
		this.title = title;
		this.sequnce = sequnce;
	}

	public int getCid()
	{
		return cid;
	}

	public void setCid(int cid)
	{
		this.cid = cid;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public int getSequnce()
	{
		return sequnce;
	}

	public void setSequnce(int sequnce)
	{
		this.sequnce = sequnce;
	}

	@Override
	public String toString()
	{
		return title;
	}
}
