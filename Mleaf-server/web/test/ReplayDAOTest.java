import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.dao.CommentDAO;
import com.model.Comment;

/**
 * @author  coolszy
 * @time    Dec 4, 2011 6:54:45 PM
 */
public class ReplayDAOTest
{
	CommentDAO commentDAO;
	
	@Before
	public void init() throws IOException, ClassNotFoundException
	{
		commentDAO = new CommentDAO();
	}
	
	@Test
	public void testGetReplies() throws SQLException
	{
		ArrayList<Comment> list = new ArrayList<Comment>();
		list = commentDAO.getComments(1, 0, 2);
		for (Comment comment : list)
		{
			System.out.println(comment.getContent());;
		}
	}

}
