package filters;

import dataaccess.UserDB;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.User;

public class AdminFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        String email = (String) session.getAttribute("email");
        UserDB userDB = new UserDB();
        User user = userDB.get(email);

        // authentication 
        if (email == null || user == null) {
            httpResponse.sendRedirect("login");
            // always return on a redirect
            return;
        }

        // regular user authentication
        if (user.getRole().getRoleId() == 2) {
            httpResponse.sendRedirect("notes");
            return;
        }

        // unauthorized user
        if (user.getRole().getRoleId() != 1 && user.getRole().getRoleId() != 2) {
            httpResponse.sendRedirect("login");
            return;
        }

        // code before chain.doFilter will execute before the servlet
        chain.doFilter(request, response); // execute the servlet, or the next filter in the chain
        // code after chain.doFilter will execute after the servlet, during the response

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
