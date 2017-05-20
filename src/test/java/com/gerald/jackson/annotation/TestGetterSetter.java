package com.gerald.jackson.annotation;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 这里测试了{@link JsonGetter @JsonGetter}、{@link JsonSetter @JsonSetter}、
 * {@link JsonProperty @JsonProperty}三个属性。
 * 
 * 默认情况下，jackson使用属性名或者字段名作为json属性的名称。为了改变默认行为，可以使用
 * {@code @JsonProperty}标签。可以推测，Jackson在解析时，将json属性名称和bean的
 * PropertyDescriptor绑定在一起，从而完成json属性名称与属性访问方法之间的映射。
 * 
 * {@code @JsonGetter}和{@code @JsonSetter}也可以完成json属性的重命名
 *
 */
public class TestGetterSetter {
    public static class Persion {
        private String id;
        
        private String name;

        @JsonGetter("personId")
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @JsonProperty("personName")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return "id = " + id + ", name = " + name;
        }
    }
    
    @Test
    public void test() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        Persion person = new Persion();
        person.setId("1");
        person.setName("name-1");
        String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
        
        System.out.println(str);
        
        Persion d = mapper.readValue(str, Persion.class);

        System.out.println(d);
    }
}
