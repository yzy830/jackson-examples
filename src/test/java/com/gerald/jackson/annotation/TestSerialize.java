package com.gerald.jackson.annotation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * 本例测试{@link JsonSerialize @JsonSerialize}的简单用法，自定义了一个
 * {@link StdSerializer}实现，用于格式化时间
 *
 */
public class TestSerialize {
    public static class Event {
        private String name;
        
        @JsonSerialize(using = DateSerializer.class)
        private Date createTime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }
    }
    
    public static class DateSerializer extends StdSerializer<Date> {
        /**
         * 
         */
        private static final long serialVersionUID = -8581237705814593513L;

        protected DateSerializer() {
            super(Date.class);
        }
        
        private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void serialize(Date value, JsonGenerator gen,
                SerializerProvider provider) throws IOException {
            gen.writeString(format.format(value));
        }
    }
    
    @Test
    public void test() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        
        Event event = new Event();
        event.setName("event-name");
        event.setCreateTime(new Date());
        
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event));
    }
}
