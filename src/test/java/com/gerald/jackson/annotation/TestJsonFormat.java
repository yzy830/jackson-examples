package com.gerald.jackson.annotation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 本例测试了{@link JsonFormat @JsonFormat}标签。这个标签最常用于解决{@code Date}等
 * 时间类型的格式化。但是，该标签还可以用于
 * <ol>
 * <li>{@code enum}: 选择使用String还是Index进行序列化</li>
 * <li>
 *  {@code Collection}的实现类：默认序列化为数组，在实现类上标注<code>@JsonFormat(shape = Shape.OBJECT)</code>，
 *  并使用其他标签自定义序列化，可以以对象的方式处理自定义实现
 * </li>
 * <li>
 *  {@code @JsonFormat}还有一个Feature属性，用于定义序列化和反序列化中的行为。例如，可以设定，当
 *  数组或者Collection只有一个元素时，将属性处理为简单属性而非数组属性
 * </li>
 * </ol>
 */
public class TestJsonFormat {
    public static class Event {
        private String name;

        private Date date;
        
        private Page page;
        
        private EventType eventType;
        
        private List<String> infos;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Page getPage() {
            return page;
        }

        public void setPage(Page page) {
            this.page = page;
        }
        
        @JsonFormat(shape = Shape.NUMBER)
        public EventType getEventType() {
            return eventType;
        }

        public void setEventType(EventType eventType) {
            this.eventType = eventType;
        }

        @JsonFormat(with = {
                Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED,
                Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY
                })
        public List<String> getInfos() {
            return infos;
        }

        public void setInfos(List<String> infos) {
            this.infos = infos;
        }

        @Override
        public String toString() {
            return "eventType = " + eventType + "name = " + name + ", date = " + date + ", page = " + page;
        }
    }
    
    public enum EventType {
        PAY,
        CREATE
    }
    
    /**
     * 集合类型的子类，如果不使用{@code @JsonFormat}，则默认按照数组方式序列化。
     * 在使用<code>@JsonFormat(shape = Shape.OBJECT)</code>时，按照对象
     * 方式序列化。此事，可能不满足要求，可以使用{@code @JsonGetter}、
     * {@code @JsonProperty}、{@code @JsonIgnoreProperties}等定制
     * 序列化格式
     */
    @JsonFormat(shape = Shape.OBJECT)
    @JsonIgnoreProperties("empty")
    public static class Page extends ArrayList<String> {

        /**
         * 
         */
        private static final long serialVersionUID = 4665758047052615640L;
        
        private int pageNum;
        
        private int pageSize;
        
        public Page() {
            
        }
        
        @JsonCreator
        public Page(@JsonProperty("pageNum") int pageNum,
                    @JsonProperty("pageSize") int pageSize,
                    @JsonProperty("items") List<String> items) {
            super(items);
            this.pageNum = pageNum;
            this.pageSize = pageSize;
        }

        @JsonProperty("pageNum")
        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        @JsonProperty("pageSize")
        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
        
        @JsonGetter("items")
        public List<String> getItems() {
            return Collections.unmodifiableList(this);
        }
        
        @Override
        public String toString() {
            return "pageSize = " + pageSize + 
                    ", pageNum = " + pageNum + 
                    ", items = " + super.toString();
        }
    }
    
    @Test
    public void test() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        Page page = new Page();
        page.add("1");
        page.add("2");
        page.setPageNum(1);
        page.setPageSize(10);
        
        Event event = new Event();
        event.setDate(new Date());
        event.setName("yang");
        event.setPage(page);
        event.setEventType(EventType.CREATE);
        event.setInfos(Arrays.asList("str-1"));
        
        String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event);
        System.out.println(str);
        
        Event d = mapper.readValue(str, Event.class);
        System.out.println(d);
    }
}
