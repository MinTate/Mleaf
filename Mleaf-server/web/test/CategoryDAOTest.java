

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.dao.CategoryDAO;
import com.model.Category;

/**
 *@author coolszy
 *@date Feb 19, 2012
 *@blog http://blog.92coding.com
 */
public class CategoryDAOTest
{
	CategoryDAO categoryDAO;
	
	@Before
	public void Init() throws IOException, ClassNotFoundException
	{
		categoryDAO = new CategoryDAO();
	}
	
	@Test
	public void testGetTypes() throws SQLException
	{
		ArrayList<Category> list = new ArrayList<Category>();
		list = categoryDAO.getTypes(0, 5);
		for (Category category : list)
		{
			System.out.println("cid="+category.getCid()+";title="+category.getTitle());
		}
	}

}
