package com.gerald.jackson.annotation;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 本例测试了{@link JsonCreator @JsonCreator}。
 * <p>
 * 默认情况下，Jackson使用默认构造器创建对象，然后使用使用setter访问属性。在某些情况下，类没有
 * 默认构造器，例如反序列化<code>enum</code>对象或者不可变对象(例如项目中的ObjectResp)。此时
 * 我们需要提供构造器或者工厂方法(对于<code>enum</code>，例如在{@link TestJsonValue}案例)
 * </p>
 * 
 * <p>
 * jackson提供了{@code @JsonCreator}，用以标记反序列化使用的构造器或者工厂方法。当构造器(工厂方法)
 * 只有一个参数时，jackson将数据反序列化为一个Map，提供给这个构造器；当构造器有多个参数时，需要使用
 * {@code @JsonProperty}来标注该参数映射到哪一个属性。
 * </p>
 * 
 * <p>
 * 当提供多个参数时，必须提供所有的属性。如果遗漏了属性，会得到异常，暂时没有找到方法忽略属性。
 * </p>
 */
public class TestCreator {
    public static class Resp {
        private int code;
        
        private String msg;
        
        @JsonCreator
        public Resp(@JsonProperty(value = "code") int code, 
                    /*  */
                    @JsonProperty(value = "msg", required = true) String msg) {
            this.code = code;
            this.msg = msg;
        }
        
//        @JsonCreator
        public Resp(@JsonProperty(value = "code") int code) {
            this.code = code;
        }
        
//        @JsonCreator
        public Resp(Map<String, Object> map) {
            this.code = (Integer)map.get("code");
            this.msg = (String)map.get("msg");
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
        
        @Override
        public String toString() {
            return "code = " + code + ", message = " + msg;
        }
    }
    
    @Test
    public void serialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        Resp resp = new Resp(1000, "sucess");
        
        String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resp);
        
        System.out.println(str);
    }
    
    @Test
    public void deserializeWithMsg() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        String str = "{\"code\" : 1000, \"msg\" : \"success\"}";
        
        System.out.println(mapper.readValue(str, Resp.class));
    }
    
    @Test
    public void deserializeMissingMsg() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        String str = "{\"code\" : 1000}";
        
        System.out.println(mapper.readValue(str, Resp.class));
    }
}
