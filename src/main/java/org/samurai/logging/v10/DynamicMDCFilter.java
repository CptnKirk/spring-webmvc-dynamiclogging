package org.samurai.logging.v10;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.MatchingFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.MDC;
import org.slf4j.Marker;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * User: Jim Hazen
 * Date: 5/22/13
 * Time: 11:11 AM
 */
public class DynamicMDCFilter extends MatchingFilter
{
    private Map<String, Set<ConfigValueTuple>> registeredMDCFilters = new ConcurrentHashMap<String, Set<ConfigValueTuple>>();

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t)
    {
        //System.out.println("** Being called "+logger+"**"+level+"**"+marker);
        for (Map.Entry<String, Set<ConfigValueTuple>> entry : registeredMDCFilters.entrySet())
        {
            String mdcValue = MDC.get(entry.getKey());
            if (mdcValue != null)
            {
                for (ConfigValueTuple valueTuple : entry.getValue())
                {
                    String rlevel = valueTuple.getLevel();
                    if (mdcValue.equals(valueTuple.getValue()))
                    {
                        if (rlevel == null)
                        {
                            return onMatch;
                        }
                        Level registeredLevel = Level.toLevel(rlevel, Level.TRACE);
                        if(level.isGreaterOrEqual(registeredLevel))
                        {
                            return onMatch;
                        }
                        else
                            return onMismatch;
                    }
                }
            }
        }
        return onMismatch;
    }

    public void addMDCMatch(String key, String value, String level)
    {
        Set<ConfigValueTuple> currentValues = registeredMDCFilters.get(key);
        ConfigValueTuple valueTuple = new ConfigValueTuple(value, level);
        if (currentValues != null)
        {
            currentValues.remove(valueTuple);
            currentValues.add(valueTuple);
            registeredMDCFilters.put(key, currentValues);
        }
        else
        {
            currentValues = new ConcurrentSkipListSet<ConfigValueTuple>();
            currentValues.remove(valueTuple);
            currentValues.add(valueTuple);
            registeredMDCFilters.put(key, currentValues);
        }
    }

    public void dropMDCMatch(String key, String value)
    {
        ConfigValueTuple toDrop = new ConfigValueTuple(value, null);
        Set<ConfigValueTuple> currentValues = registeredMDCFilters.get(key);
        if(currentValues != null)
        {
            currentValues.remove(toDrop);
            if(currentValues.size() > 0)
                registeredMDCFilters.put(key, currentValues);
            else
                registeredMDCFilters.remove(key);
        }
    }

    public Map<String, Set<ConfigValueTuple>> getRegisteredFilters()
    {
        return registeredMDCFilters;
    }

    public void clear()
    {
        registeredMDCFilters.clear();
    }
}
