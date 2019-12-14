import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/master")
@MultipartConfig
public class masterServlet extends HttpServlet {
	
	private String[] getRoutes = {"checkExistence","logout","post","signin","user"};
	private String[] deleteRoutes = {"post","user"};
	private String[] postRoutes = {"getUsers","post","signin","SignUpServlet","updateUser"};
	
	private boolean includesIn(String[] arr, String item) {
		for(int i = 0; i<arr.length; i++) {
			if(arr[i].equals(item)) return true;
		}
		return false;
	};
	
	private void gotoPage(String address, HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(address);
		dispatcher.forward(request, response);	
	}
	
	
	
	
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redirect = req.getParameter("redirect");
        if(redirect == null) return;

        if(includesIn(getRoutes,redirect)) {
            gotoPage("/"+redirect, req, resp);
        }    
    }


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String redirect = req.getParameter("redirect");
		if(redirect == null) return;

		if(includesIn(postRoutes,redirect)) {
			gotoPage("/"+redirect, req, resp);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String redirect = req.getParameter("redirect");
		if(redirect == null) return;

		if(includesIn(deleteRoutes,redirect)) {
			gotoPage("/"+redirect, req, resp);
		}
	}
}
