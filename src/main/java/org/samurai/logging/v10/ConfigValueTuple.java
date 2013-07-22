package org.samurai.logging.v10;

/**
 * User: Jim Hazen
 * Date: 5/28/13
 * Time: 1:23 PM
 */
public class ConfigValueTuple implements Comparable<ConfigValueTuple>

{
    private String value;
    private String level;

    public ConfigValueTuple(String value, String level)
    {
        this.value = value;
        this.level = level;
    }

    public String getValue()
    {
        return value;
    }

    public String getLevel()
    {
        return level;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ConfigValueTuple)) return false;

        ConfigValueTuple that = (ConfigValueTuple) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return value.hashCode();
    }

    @Override
    public int compareTo(ConfigValueTuple o)
    {
        return value.compareTo(o.getValue());
    }
}
