package com.model;



/**
 *@author coolszy
 *@date Feb 19, 2012
 *@blog http://blog.92coding.com
 *
 *新闻Model
 */
public class News
{
	private int nid;
	private int cid;
	private String title;
	private String digest;
	private String body;
	private String source;
	private int commentCount;
	private String ptime;
	private String imgSrc;
	private boolean deleted;

	public News()
	{
		super();
	}

	public News(int nid, int cid, String title, String digest, String body, String source, int commentCount, String ptime, String imgSrc, boolean deleted)
	{
		super();
		this.nid = nid;
		this.cid = cid;
		this.title = title;
		this.digest = digest;
		this.body = body;
		this.source = source;
		this.commentCount = commentCount;
		this.ptime = ptime;
		this.imgSrc = imgSrc;
		this.deleted = deleted;
	}

	public int getNid()
	{
		return nid;
	}

	public void setNid(int nid)
	{
		this.nid = nid;
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

	public String getDigest()
	{
		return digest;
	}

	public void setDigest(String digest)
	{
		this.digest = digest;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public int getReplyCount()
	{
		return commentCount;
	}

	public void setCommentCount(int replyCount)
	{
		this.commentCount = replyCount;
	}

	public String getPtime()
	{
		return ptime;
	}

	public void setPtime(String ptime)
	{
		this.ptime = ptime;
	}

	public String getImgSrc()
	{
		return imgSrc;
	}

	public void setImgSrc(String imgSrc)
	{
		this.imgSrc = imgSrc;
	}

	public boolean isDeleted()
	{
		return deleted;
	}

	public void setDeleted(boolean deleted)
	{
		this.deleted = deleted;
	}
}
