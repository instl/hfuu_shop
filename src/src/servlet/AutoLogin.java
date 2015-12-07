package src.servlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import src.dbHandle.UserDbHandle;
import src.vo.User;

/**
 * Servlet Filter implementation class AutoLogin
 */
@WebFilter("/AutoLogin")
public class AutoLogin implements Filter {

	public AutoLogin() {
		// TODO Auto-generated constructor stub
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession ses = req.getSession();
		Cookie[] cookies = req.getCookies();
		UserDbHandle userDbHandle = new UserDbHandle();
		String EmailOrUserName = "";
		String emailCookie = null;
		/*
		 * 日后修复标记：这里仅用了email作为cookie并用于验证，极不安全
		 */
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("LOGIN_EMAIL".equals(cookie.getName())) {
					emailCookie = cookie.getValue();
					try {
						if (userDbHandle.findByEmail(emailCookie) != null) {
							User user = userDbHandle.findByEmail(emailCookie);
							if(user.getName()!=null && !user.getName().equals("")){
								EmailOrUserName=user.getName();
							}else{
								EmailOrUserName=user.getEmail();
							}
							ses.setAttribute("loginUser",user);
							ses.setAttribute("EmailOrUserName", EmailOrUserName);
							ses.setAttribute("isLogined", true);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			}
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
}
