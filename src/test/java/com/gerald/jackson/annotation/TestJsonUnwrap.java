package com.gerald.jackson.annotation;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 测试了{@link JsonUnwrapped @JsonUnwrapped}标签。这个标签用于将一个类属性的子属性unwrap
 * 出来。
 * <p>
 * 注意，这个属性有一个副作用。当被标注的属性为null时，对应的属性及其子属性将被过滤。
 * </p>
 */
public class TestJsonUnwrap {
    public static class Person {
        private Name name;
        
        private String id;

        @JsonUnwrapped
        public Name getName() {
            return name;
        }

        public void setName(Name name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
    
    public static class Name {
        private String firstName;
        
        private String lastName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }
    
    @Test
    public void test() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        
        Person p = new Person();
        
        p.setId("123");
        Name n = new Name();
        n.setFirstName("yang");
        n.setLastName("zongyuan");
        p.setName(n);
        
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(p));
    }
}
