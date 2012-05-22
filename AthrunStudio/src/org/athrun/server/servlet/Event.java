package org.athrun.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.athrun.server.service.EventServiceManager;
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
		String actionNumber = request.getParameter("an");

		String metaState = "0";
		String serialNumber = request.getParameter("serialNumber");

		String cmd;
		if (type.equalsIgnoreCase("pointer")) {
			cmd = type + "/" + action + "/" + x + "/" + y + "/" + metaState
					+ "/" + actionNumber;
			sendPointer(serialNumber, cmd, action);
		} else {
			if (type.equalsIgnoreCase("key")) {
				cmd = type + "/" + action + "/" + keyCode;
				sendKey(serialNumber, cmd);
			} else {
				throw new NotImplementedException();
			}
		}

		response.getWriter().write("finish");

	}

	private void sendKey(String serialNumber, String cmd) {
		EventServiceManager.Send(serialNumber, cmd);
	}

	private void sendPointer(String serialNumber, String cmd, String action) {
		// 根据时间，过滤不必要的请求
		if (action.equalsIgnoreCase("2")) {
			EventServiceManager.processPointerMove(serialNumber, cmd);
		} else {
			if (action.equalsIgnoreCase("1")) {
				EventServiceManager.processPointerUp(serialNumber, cmd);
			} else {
				EventServiceManager.processPointerDown(serialNumber, cmd);
			}

		}
	}

}
