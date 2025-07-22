// com/quark/app/dto/LogDayDto.java
package com.quark.app.dto;

import java.util.List;
import java.util.Map;

public class LogDayDto {
    private LogSessionDto session;
    private List<LogItemDto> items;
    private Map<Integer, List<LogWorkDto>> works;
    // 构造器、getter、setter
    public LogDayDto(LogSessionDto session,
                     List<LogItemDto> items,
                     Map<Integer, List<LogWorkDto>> works) {
        this.session = session;
        this.items   = items;
        this.works   = works;
    }
    // … getters & setters …
    public LogSessionDto getSession() {
        return session;
    }
    public void setSession(LogSessionDto session) {
        this.session = session;
    }
    public List<LogItemDto> getItems() {
        return items;
    }
    public void setItems(List<LogItemDto> items) {
        this.items = items;
    }
    public Map<Integer, List<LogWorkDto>> getWorks() {
        return works;
    }
    public void setWorks(Map<Integer, List<LogWorkDto>> works) {
        this.works = works;
    }
}
