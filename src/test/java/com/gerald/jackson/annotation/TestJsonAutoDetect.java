package com.gerald.jackson.annotation;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJsonAutoDetect {
    /**
     * 测试字段和属性在序列化中的默认可见性
     *
     */
    @JsonAutoDetect
    public static class PrivateBean {
        private String attr1;
        
        private String attr2;
        
        public String attr3;
        
        private Boolean isDefault;
        
        protected Boolean isOk;
        
        public PrivateBean() {}
        
        public PrivateBean(String attr1, String attr2, String attr3,
                           boolean isDefault, boolean isOK) {
            this.attr1 = attr1;
            this.attr2 = attr2;
            this.attr3 = attr3;
            this.isDefault = isDefault;
            this.isOk = isOK;
        }

        public boolean isDefault() {
            return isDefault;
        }

        private void setDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }

        protected boolean isOk() {
            return isOk;
        }

        protected void setOk(boolean isOk) {
            this.isOk = isOk;
        }

        public String getAttr1() {
            return attr1;
        }

//        private void setAttr1(String attr1) {
//            System.out.println("setAttr1 is called");
//            
//            this.attr1 = attr1;
//        }

        protected String getAttr2() {
            return attr2;
        }

        protected void setAttr2(String attr2) {
            this.attr2 = attr2;
        }
        
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            
            builder.append("attr1 = ").append(attr1).append("\n")
                   .append("attr2 = ").append(attr2).append("\n")
                   .append("attr3 = ").append(attr3).append("\n")
                   .append("isDefault = ").append(isDefault).append("\n")
                   .append("isOk = ").append(isOk);
            
            return builder.toString();
        }
    }
    
    /**
     * 从下面两个测试用例可以看出，
     * <ol>
     * <li>
     * 在序列化过程中，getter和field的默认最低可见性是public
     * </li>
     * <li>
     * 在反序列化过程中，setter和field的默认最低可见性是private
     * </li>
     * </ol>
     * 
     * @throws IOException
     */
    @Test
    public void testDefaultSerializeVisibility() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        PrivateBean bean = new PrivateBean("value-1", "value-2", "value-3", false, true);
        
        String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bean);
        System.out.println(str);
        
        PrivateBean d = mapper.readValue(str, PrivateBean.class);
        
        System.out.println(d);
    }
    
    @Test
    public void testDefaultDeserializeVisibility() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        String str = "{\"attr2\":\"value-2\", \"default\":true}";
        
        PrivateBean d = mapper.readValue(str, PrivateBean.class);
        
        System.out.println(d);
    }
}
