package cn.edu.sdjzu.xg.bysj.Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Filter 10",urlPatterns = "/*")
public class Filter10 implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //轻质类型转换
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;
        HttpSession session=request.getSession();
        Object currentUser=session.getAttribute("currentUser");
        chain.doFilter(req, resp);//执行其他过滤器，如果过滤器已经执行完毕，则执行原请求
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
