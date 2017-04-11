

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.dao.NewsDAO;


/**
 *@author coolszy
 *@date Feb 19, 2012
 *@blog http://blog.92coding.com
 */
public class NewsDAOTest
{
	NewsDAO newsDAO;
	
	@Before
	public void init() throws IOException, ClassNotFoundException
	{
		newsDAO = new NewsDAO();
	}
	
	@Test
	public void testGetNews() throws SQLException
	{
	}

	@Test
	public void testGetFontNews() throws SQLException
	{
	}
	
	@Test
	public void testGetDetails() throws SQLException
	{
	}
}
