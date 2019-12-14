

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import gr.csd.uoc.cs359.winter2019.logbook.db.UserDB;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONResponse;
import gr.csd.uoc.cs359.winter2019.logbook.model.User;

/**
 * Servlet implementation class test
 */
@WebServlet("/test")
public class test extends HttpServlet {
	private static final long serialVersionUID = 1L;
     

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		 response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String name = request.getParameter("name");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		
		try {
			if(!UserDB.checkValidUserName(name)) {
				User user1 =  UserDB.getUser(name);
				JSONResponse resp1 = new JSONResponse("ta gamhsa ola",200,user1);
				out.print(gson.toJson(resp1));
				
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
