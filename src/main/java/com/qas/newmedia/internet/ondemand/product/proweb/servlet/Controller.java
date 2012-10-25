/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Controller.java
 *
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.qas.ondemand_2011_03.QAAuthentication;


/**
 * Servlet that acts as the Command Controller.
 *
 * Uses HttpHelper as a Command factory.
 *
 */
public class Controller extends HttpServlet {
    /**  Initializes the servlet - gets the init params from the servlet context and sets them in the Constants class */
    public void init(ServletConfig config)
    throws ServletException {

        super.init(config);

        String sEndpoint = getServletContext().getInitParameter("OnDemandProWebEndpoint");
		String sUsername = getServletContext().getInitParameter("AuthenticationUsername");
		String sPassword = getServletContext().getInitParameter("AuthenticationPassword");

		Constants.ENDPOINT = sEndpoint;
		Constants.USERNAME = sUsername;
		Constants.PASSWORD = sPassword;
    }

    /** nothing to do here */
    public void destroy() {
    }

    /** Processes requests for both HTTP
     * <code>GET</code> and <code>POST</code> methods.
     * 1) constructs the relevant <code>Command</code> object (by delegating to <code>HttpHelper</code>.),
     * 2) calls <code>execute</code> on the new <code>Command</code> object
     * 3) dispatches to the page returned by the <code>Command</code> object.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, java.io.IOException {
        String sPage;
        try {

        	// Extract authentication and client information
			QAAuthentication authentication = new QAAuthentication();
			authentication.setUsername(Constants.USERNAME);
			authentication.setPassword(Constants.PASSWORD);

			request.setAttribute(Constants.QA_AUTHENTICATION, authentication);

			// Add proxy credentials to request
			String sHttpProxyHost = getServletContext().getInitParameter("HttpProxyHost");
			if (sHttpProxyHost != null) {
				request.setAttribute(Constants.HTTP_PROXY_HOST, sHttpProxyHost);
			}
			String sHttpProxyPort = getServletContext().getInitParameter("HttpProxyPort");
			if (sHttpProxyPort != null) {
				request.setAttribute(Constants.HTTP_PROXY_PORT, sHttpProxyPort);
			}
			String sHttpProxyUser = getServletContext().getInitParameter("HttpProxyUser");
			if (sHttpProxyUser != null) {
				request.setAttribute(Constants.HTTP_PROXY_USER, sHttpProxyUser);
			}
			String sHttpProxyPass = getServletContext().getInitParameter("HttpProxyPass");
			if (sHttpProxyPass != null) {
				request.setAttribute(Constants.HTTP_PROXY_PASS, sHttpProxyPass);
			}

			HttpSession session = request.getSession(true);

			// The helper extract the Command name from the request and creates the relevant object.
        	Command cmd = HttpHelper.getCommand(request);

        	// Determine the layout
        	String sLayout = null;
        	try {
        		sLayout = getServletContext().getInitParameter((String) request.getParameter(Constants.DATA_ID));
        	} catch (Exception e) {
        		// Layout not configured for specific dataset
        	}

            if (sLayout == null || sLayout.equals("")) {
                sLayout = getServletContext().getInitParameter("OnDemandProWebLayout");
            }

            session.setAttribute("COMMAND", cmd);
            session.setAttribute(Constants.LAYOUT, sLayout);
            session.setAttribute(Constants.DROPLIST, DropListHelper.initDropListHelper(this, request));

			// perform custom operation
            sPage = cmd.execute(request, response);

        } catch (Throwable e) {
            System.err.println("~~~~~~~~~ Controller:command exception ~~~~~~~~~~");
            e.printStackTrace();
            System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            throw new ServletException(e);
        }
        // dispatch control to view
        dispatch(request, response, sPage);
    }

    /** Handles the HTTP <code>GET</code> method - delegates to <code>processRequest</code>
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method - delegates to <code>processRequest</code>
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** delegates the <code>forward</code> to the servlet context's <code>RequestDispatcher</code> */
    protected void dispatch(HttpServletRequest request,
            HttpServletResponse response,
            String sPage)
            throws  javax.servlet.ServletException,
            java.io.IOException {
    	if (!sPage.startsWith("/")){
    		/**********************************
			 * Updated to allow the user to use
			 * different folder names when deploying
			 * Java sample code.  Some
			 * page names were changed to use relative names.
			 * The request dispatcher cannot handle
			 * names without a '/' in the front.
			 * Therefore, a '/' is added.
			 * Currently, only applies to 'address.jsp'
			 **********************************/
    		sPage = "/" + sPage;
        }

    	RequestDispatcher dispatcher
                = getServletContext().getRequestDispatcher(sPage);
        dispatcher.forward(request, response);
    }
}
