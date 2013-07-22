package org.samurai.logging.v10;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.turbo.TurboFilter;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User: Jim Hazen
 * Date: 6/11/13
 * Time: 3:58 PM
 */
@Validated
@Controller
public class LoggerFilterController
{
    @RequestMapping(value = "/1.0/admin/logging/logger", method = RequestMethod.GET)
    public @ResponseBody
    Object getFilters()
    {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for (TurboFilter filter : context.getTurboFilterList())
        {
            if (DynamicLoggerFilter.class.equals(filter.getClass()))
            {
                return ((DynamicLoggerFilter) filter).getRegisteredFilters();
            }
        }
        return "DynamicLoggerFilter not configured.  Please check logback.xml.";
    }

    @RequestMapping(value = "/1.0/admin/logging/logger/_add", method = RequestMethod.GET)
    public @ResponseBody
    Object addFilter(@NotEmpty(message = "You must specify a 'logger' param") @RequestParam(required = false) String logger,
                                          @RequestParam(required = false, defaultValue = "trace") String level)
    {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for(TurboFilter filter : context.getTurboFilterList())
        {
            if(DynamicLoggerFilter.class.equals(filter.getClass()))
            {
                for(Logger l : context.getLoggerList())
                {
                    if(l.getName().endsWith(logger))
                        ((DynamicLoggerFilter)filter).addOverride(l, level);
                }
                return ((DynamicLoggerFilter)filter).getRegisteredFilters();
            }
        }
        return "DynamicLoggerFilter not configured.  Please check logback.xml.";
    }

    @RequestMapping(value = "/1.0/admin/logging/logger/_drop", method = RequestMethod.GET)
    public @ResponseBody
    Object dropFilter(@NotEmpty(message = "You must specify a 'logger' param") @RequestParam(required = false) String logger)
    {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for(TurboFilter filter : context.getTurboFilterList())
        {
            if(DynamicLoggerFilter.class.equals(filter.getClass()))
            {
                for(Logger l : context.getLoggerList())
                {
                    if(l.getName().equals(logger))
                        ((DynamicLoggerFilter)filter).dropOverride(l);
                }
                return ((DynamicLoggerFilter)filter).getRegisteredFilters();
            }
        }
        return "DynamicLoggerFilter not configured.  Please check logback.xml.";
    }

    @RequestMapping(value = "/1.0/admin/logging/logger/_dropAll", method = RequestMethod.GET)
    public @ResponseBody
    Object dropAllFilter()
    {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for(TurboFilter filter : context.getTurboFilterList())
        {
            if(DynamicLoggerFilter.class.equals(filter.getClass()))
            {
                ((DynamicLoggerFilter)filter).clear();
                return ((DynamicLoggerFilter)filter).getRegisteredFilters();
            }
        }
        return "DynamicLoggerFilter not configured.  Please check logback.xml.";
    }
}
