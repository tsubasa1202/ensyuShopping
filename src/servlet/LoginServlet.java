package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if(request.getParameter("action").equals("Login")){

			String name = request.getParameter("name");
			String pass = request.getParameter("pass");

			LoginDAO daologin = new LoginDAO();

			if(pass == daologin.findPass(name)){

				HttpSession session = request.getSession();
				session.setAttribute("username", name);

				ItemDAO daoitem = new ItemDAO();
				ArrayList<ItemBean> list = daoitem.findAll();
				request.setAttribute("ItemList", list);
				gotoPage(request, response,"/itemlist.jsp");

			}else{
				gotoPage(request,response,"/errorlogin.jsp");
			}
		}else if(request.getParameter("action").equals("Logout")){

			HttpSession session = request.getSession(false);

			if(session !=null){
				session.invalidate();
				request.setAttribute("logoutMessage", "ログアウトしました");
			}else{
				request.setAttribute("logoutMessage", "ログインしていません");
			}
			gotoPage(request,response,"/logout.jsp");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	void gotoPage(HttpServletRequest request, HttpServletResponse response, String page) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(page);
		rd.forward(request, response);
	}

}
