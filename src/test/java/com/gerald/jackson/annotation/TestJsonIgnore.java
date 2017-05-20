package com.gerald.jackson.annotation;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 本例测试{@link JsonIgnoreProperties @JsonIngnoreProperties}、
 * {@link JsonIgnore @JsonIgnore}、{@link JsonIgnoreType @JsonIgnoreType}
 * 
 * <p>
 * {@code @JsonIgnoreProperties}用于在序列化和反序列化过程中，忽略指定的属性。
 * <ol>
 * <li>
 *  在序列化和反序列化过程中，忽略指定属性。设置allowGetters，可以控制只在反序列化过程中忽略指定属性；
 *  也存在allowSetters，但是存在bug，不可用</li>
 * <li>
 *  在反序列化过程中，忽略未知属性。这个功能可以用于处理版本更新。例如在老版本中存在一个属性，并持久化了
 *  这个序列化结果，而新版本取消了这个属性。如果没有设置这个属性，则在反序列化老版本的值时，则会出现异常。
 * </li>
 * </ol>
 * </p>
 *
 * <p>
 * {@code @JsonIgnore}用于属性层面，在序列化和反序列化中，忽略某一特定属性
 * </p>
 * 
 * <p>
 * jackson会在序列化和反序列化过程中，都会忽略被{@code @JsonIgnoreType}标记的类型
 * </p>
 */
public class TestJsonIgnore {
    @JsonIgnoreProperties(ignoreUnknown = true, 
                          value = {"id","random"},
                          allowGetters = true
                          /*, allowSetters = true 存在bug，allowSetters不生效*/)
    public static class Order {
        private Long id;
        
        private Long sum;
        
        private Long goodsCount;
        
        private String address;
        
        private IgnoredType ingored;
        
        public Order() {
            
        }

        public Order(Long id) {
            this.id = id;
        }
        
        public Long getId() {
            return id;
        }

        @JsonIgnore
        public Long getSum() {
            return sum;
        }

        public void setSum(Long sum) {
            this.sum = sum;
        }

        public Long getGoodsCount() {
            return goodsCount;
        }

        public void setGoodsCount(Long goodsCount) {
            this.goodsCount = goodsCount;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
        
        public Integer getRandom() {
            return ThreadLocalRandom.current().nextInt(0, 100);
        }
        
        public void setRandom(Integer value) {
            System.out.println("setRandom is called with value = " + value);
        }
        
        public IgnoredType getIngored() {
            return ingored;
        }

        public void setIngored(IgnoredType ingored) {
            this.ingored = ingored;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            
            builder.append("id = ").append(id).append("\n")
                   .append("sum = ").append(sum).append("\n")
                   .append("goodsCount = ").append(goodsCount).append("\n")
                   .append("address = ").append(address);
            
            return builder.toString();
        }
    }
    
    @JsonIgnoreType
    public static class IgnoredType {
        private String attr1;
        
        private String attr2;
        
        public IgnoredType(String attr1, String attr2) {
            this.attr1 = attr1;
            this.attr2 = attr2;
        }

        public String getAttr1() {
            return attr1;
        }

        public void setAttr1(String attr1) {
            this.attr1 = attr1;
        }

        public String getAttr2() {
            return attr2;
        }

        public void setAttr2(String attr2) {
            this.attr2 = attr2;
        }
    }
    
    @Test
    public void testIngoreProperties() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        Order order = new Order(1L);
        order.setAddress("address-1");
        order.setGoodsCount(2L);
        order.setSum(2890L);
        order.setIngored(new IgnoredType("value-1", "value-2"));
        
        String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(order);
        
        System.out.println(str);
        
        Order d = mapper.readValue(str, Order.class);
        
        System.out.println(d);
    }
    
    /**
     * 到目前为止(version-2.8.8)，allowSetter存储bug，即使设置为true，也不能允许
     * 对忽略的属性赋值。例如在本例中，设置allowSetter = true，values = {"id", "random"}时，
     * id和random不会得到设置
     */
    @Test
    public void testIngoreSetterTrue() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        String str = "{\"id\" : 2, \"sum\" : 2890, \"goodsCount\" : 2, \"address\" : \"address-2\", \"random\" : 45}";
        
        Order d = mapper.readValue(str, Order.class);
        
        System.out.println(d);
    }
    
    /**
     * 如果将{@link JsonIgnoreProperties#ignoreUnknown}设置为false，这个测试用例不能通过
     */
    @Test
    public void testIgnoreUnknownProperties() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        String str = "{\"attr\" : 2, \"id\" : 2, \"sum\" : 2890, \"goodsCount\" : 2, \"address\" : \"address-2\", \"random\" : 45}";
        
        Order d = mapper.readValue(str, Order.class);
        
        System.out.println(d);
    }
}
