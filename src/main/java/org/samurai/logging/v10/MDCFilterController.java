package org.samurai.logging.v10;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.turbo.TurboFilter;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User: Jim Hazen
 * Date: 5/22/13
 * Time: 12:32 PM
 */
@Validated
@Controller
public class MDCFilterController
{
    Logger log = LoggerFactory.getLogger(MDCFilterController.class);

    @RequestMapping(value = "/1.0/admin/logging/mdc", method = RequestMethod.GET)
    public @ResponseBody
    Object getFilters()
    {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for (TurboFilter filter : context.getTurboFilterList())
        {
            if (DynamicMDCFilter.class.equals(filter.getClass()))
            {
                return ((DynamicMDCFilter) filter).getRegisteredFilters();
            }
        }
        return "DynamicMDCFilter not configured.  Please check logback.xml.";
    }

    @RequestMapping(value = "/1.0/admin/logging/mdc/_add", method = RequestMethod.GET)
    public @ResponseBody
    Object addFilter(@NotEmpty(message = "You must specify a 'key' param") @RequestParam(required = false) String key,
                     @NotEmpty(message = "You must specify a 'value' param") @RequestParam(required = false) String value,
                     @RequestParam(required = false, defaultValue = "trace") String level)
    {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for(TurboFilter filter : context.getTurboFilterList())
        {
            if(DynamicMDCFilter.class.equals(filter.getClass()))
            {
                ((DynamicMDCFilter)filter).addMDCMatch(key, value, level);
                return ((DynamicMDCFilter)filter).getRegisteredFilters();
            }
        }
        return "DynamicMDCFilter not configured.  Please check logback.xml.";
    }

    @RequestMapping(value = "/1.0/admin/logging/mdc/_drop", method = RequestMethod.GET)
    public @ResponseBody
    Object dropFilter(@NotEmpty(message = "You must specify a 'key' param") @RequestParam(required = false) String key,
                      @NotEmpty(message = "You must specify a 'value' param") @RequestParam(required = false) String value)
    {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for(TurboFilter filter : context.getTurboFilterList())
        {
            if(DynamicMDCFilter.class.equals(filter.getClass()))
            {
                ((DynamicMDCFilter)filter).dropMDCMatch(key, value);
                return ((DynamicMDCFilter)filter).getRegisteredFilters();
            }
        }
        return "DynamicMDCFilter not configured.  Please check logback.xml.";
    }

    @RequestMapping(value = "/1.0/admin/logging/mdc/_dropAll", method = RequestMethod.GET)
    public @ResponseBody
    Object dropAllFilter()
    {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for(TurboFilter filter : context.getTurboFilterList())
        {
            if(DynamicMDCFilter.class.equals(filter.getClass()))
            {
                ((DynamicMDCFilter)filter).clear();
                return ((DynamicMDCFilter)filter).getRegisteredFilters();
            }
        }
        return "DynamicMDCFilter not configured.  Please check logback.xml.";
    }
}
