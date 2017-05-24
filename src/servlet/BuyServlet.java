package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.net.httpserver.HttpsServer;

/**
 * Servlet implementation class BuyServlet
 */
@WebServlet("/BuyServlet")
public class BuyServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		
		if(action.equals("buy")){
			String item_code = request.getParameter("item_code");
			String quantity = request.getParameter("quantity");
			ItemDAO daoitem = new ItemDAO();
			
			if(quantity <= daoitem.findQuantity(item_code)){
				
				HttpSession session = request.getSession(false);
				
				//買った個数分減らす
				daoitem.decreseQuantity(item_code, quantity);
				
				//誰が何を何個買ったのか登録
				daoitem.insertBuyHistory(session.getAttribute("username"), item_code, quantity);
				
				//usernameの人は何を何個いつかったのか？
				ArrayList<BuyHistoryBean> list = daoitem.findBuyHistory(session.getAttribute("username"));
				request.setAttribute("BuyHistoryList", list);
				gotoPage(request, response, "/buyhistory.jsp");
				
			                                                 
				
			}else{
				
				gotoPage(request, response, "/errorBuy.jsp");
				
			}
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	void gotoPage(HttpServletRequest request, HttpServletResponse response, String page) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(page);
		rd.forward(request, response);
	}

}
