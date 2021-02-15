package com.peter.flink.java.entity;

import java.util.Date;

public class ZhiboLog {
    public String level;
    public long ds;
    public String domain;
    public long traffic;

    public ZhiboLog(String level, long ds, String domain, long traffic) {
        this.level = level;
        this.ds = ds;
        this.domain = domain;
        this.traffic = traffic;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getDs() {
        return ds;
    }

    public void setDs(long ds) {
        this.ds = ds;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public long getTraffic() {
        return traffic;
    }

    public void setTraffic(long traffic) {
        this.traffic = traffic;
    }

    @Override
    public String toString() {
        return "ZhiboLog{" +
                "level='" + level + '\'' +
                ", ds=" + ds +
                ", domain='" + domain + '\'' +
                ", traffic=" + traffic +
                '}';
    }
}
