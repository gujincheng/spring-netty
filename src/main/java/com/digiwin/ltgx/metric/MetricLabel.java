package com.digiwin.ltgx.metric;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;


public class MetricLabel {
    private String key;
    private String value;

    public MetricLabel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(StringUtils.upperCase(key), value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MetricLabel)) {
            return false;
        }

        MetricLabel other = (MetricLabel) obj;
        if (this.key.equalsIgnoreCase(other.key) && value.equals(other.getValue())) {
            return true;
        }
        return false;
    }
}
