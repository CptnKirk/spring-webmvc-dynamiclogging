package org.samurai.logging.v10;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: Jim Hazen
 * Date: 6/11/13
 * Time: 4:03 PM
 */
public class DynamicLoggerFilter extends TurboFilter
{
    private Map<Logger, ConfigValueTuple> loggerOverrides = new ConcurrentHashMap<Logger, ConfigValueTuple>();

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t)
    {
        //System.out.println("** Being called "+logger+"**"+level+"**"+marker);
        ConfigValueTuple override = loggerOverrides.get(logger);
        if(override != null)
        {
            Level configLevel = Level.toLevel(override.getLevel());
            if(level.isGreaterOrEqual(configLevel))
                return FilterReply.ACCEPT;
            else
                return FilterReply.DENY;
        }
        else
            return FilterReply.NEUTRAL;
    }

    public void addOverride(Logger logger, String level)
    {
        loggerOverrides.put(logger, new ConfigValueTuple(logger.getName(), level));
    }

    public void dropOverride(Logger logger)
    {
        loggerOverrides.remove(logger);
    }

    public Collection<ConfigValueTuple> getRegisteredFilters()
    {
        return loggerOverrides.values();
    }

    public void clear()
    {
        loggerOverrides.clear();
    }
}
