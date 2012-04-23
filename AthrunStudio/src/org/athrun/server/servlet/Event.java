package org.athrun.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.athrun.server.utils.ForwardPortManager;
import org.athrun.server.utils.ReservedPortExhaust;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Servlet implementation class Event
 */
public class Event extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Event() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// TODO Auto-generated method stub
		String type = request.getParameter("type");
		String x = request.getParameter("x");
		String y = request.getParameter("y");
		String action = request.getParameter("action"); // 0��down 1��up 2��move
		String keyCode = request.getParameter("keyCode");

		String metaState = "-1";
		String downTime = "10";
		String eventTime = "10";

		String cmd;
		if (type.equalsIgnoreCase("pointer")) {
			cmd = type + "/" + downTime + "/" + eventTime + "/" + action + "/"
					+ x + "/" + y + "/" + metaState;
		} else {
			if (type.equalsIgnoreCase("key")) {
				cmd = type + "/" + action + "/" + keyCode;
			} else {
				throw new NotImplementedException();
			}
		}

		String serialNumber = "SH0CKPL09389";

		try {
			Socket server;
			server = new Socket("127.0.0.1",
					ForwardPortManager.getEventPort(serialNumber));
			PrintWriter out = new PrintWriter(server.getOutputStream());
			out.print(cmd);
			out.flush();
			out.close();
			server.close();
		} catch (ReservedPortExhaust e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("finish");

	}

}
