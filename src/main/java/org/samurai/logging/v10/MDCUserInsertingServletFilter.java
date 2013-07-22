package org.samurai.logging.v10;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Jim Hazen
 * Date: 10/29/12
 * Time: 1:13 PM
 */
@Component
public class MDCUserInsertingServletFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(MDCUserInsertingServletFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                                     ServletException {
        try {

            String user = ((HttpServletRequest)request).getParameter("user");
            user = user != null?user:"<unknown>";
            log.debug("Setting user: {}", user);

            MDC.put("user", user);
            MDC.put("req.requestURI", ((HttpServletRequest) request).getRequestURI());
            MDC.put("req.queryString", ((HttpServletRequest) request).getQueryString());
            MDC.put("req.method", ((HttpServletRequest) request).getMethod());
        } catch (Exception e) {
            log.warn("Unexpected problem putting user information into the MDC", e);
        } finally {
            chain.doFilter(request, response);
            MDC.remove("user");
            MDC.remove("req.requestURI");
            MDC.remove("req.queryString");
            MDC.remove("req.method");
        }
    }

    @Override
    public void destroy() {
        //do nothing
    }
}
