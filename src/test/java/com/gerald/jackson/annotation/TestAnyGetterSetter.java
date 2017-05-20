package com.gerald.jackson.annotation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestAnyGetterSetter {    
    public static class ExtendableBean {
        public String name;
        private Map<String, String> properties = new HashMap<>();
     
        /**
         * <p>
         * {@link JsonAnyGetter @JsonAnyGetter}只能用于标记类型为java.util.Map
         * 的字段。{@code JsonAnyGetter}的作用是，将Map的内容直接作为json属性。
         * </p>
         * 
         * <p>
         * 例如，properties属性具有<"attr1", "value1">，<"attr2", "value2">两个键值对。
         * </p>
         * <p>
         * 在<em>不使用</em>{@code JsonAnyGetter}时，其结果是
         * <pre>
         * <code>
         * {
         *   "name" : "name-test",
         *   "properties" : {
         *     "attr2" : "value2",
         *     "attr1" : "value1"
         *   }
         * }
         * </code>
         * </pre>
         * </p>
         * 
         * <p>
         * 在<em>使用</em>{@code JsonAnyGetter}时，其结果是
         * <pre>
         * <code>
         * {
         *   "name" : "name-test",
         *   "attr2" : "value2",
         *   "attr1" : "value1"
         * }
         * </code>
         * </pre>
         * </p>
         * 
         * @return
         */
        @JsonAnyGetter
        public Map<String, String> getProperties() {
            return properties;
        }
        
        /**
         * {@link @JsonAnySetter JsonAnySetter}和{@link JsonAnyGetter @JsonAnyGetter}
         * 应结对使用。{@code JsonAnySetter}作为一个fallback，用于存储不可识别的
         * 属性。
         * {@code JsonAnySetter}只能标记两个参数的方法，第一个参数是属性名称，第二个参数是属性值
         * 
         * @param name
         * @param value
         */
        @JsonAnySetter
        public void setProperties(String name, String value) {
            this.properties.put(name, value);
        }
    }
    
    @Test
    public void testJsonAnyGetter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        ExtendableBean bean = new ExtendableBean();
        bean.name = "name-test";
        bean.getProperties().put("attr1", "value1");
        bean.getProperties().put("attr2", "value2");
        
        String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bean);
        System.out.println(jsonStr);
        
        ExtendableBean deserialize = mapper.readValue(jsonStr, ExtendableBean.class);
        Assert.assertTrue(deserialize.name.equals(bean.name));
        Assert.assertEquals(deserialize.getProperties(), bean.getProperties());
    }
}
