package org.samurai.logging.v10;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * User: Jim Hazen
 * Date: 5/22/13
 * Time: 2:52 PM
 *
 * Clear the MDC in case state is left over from a prior request.  It's hard to clean up MDC when there are exceptions.
 * Turns out that clearing before each new request is easier than trying to do it at the end.  Same effect.
 */
@Component
public class MDCCleanupServletFilter implements Filter
{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                                     ServletException
    {
        //System.out.println("!!!!!!****** Clearing MDC *****!!!!!");
        MDC.clear();
        chain.doFilter(request, response);

    }

    @Override
    public void destroy()
    {
    }
}
