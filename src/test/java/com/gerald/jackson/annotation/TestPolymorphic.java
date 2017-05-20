package com.gerald.jackson.annotation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 本例测试了{@link JsonTypeInfo @JsonTypeInfo}、{@link JsonSubTypes @JsonSubTypes}、
 * {@link JsonTypeName @JsonTypeName}标签。
 * 
 * <p>
 * 这三个标签用于处理继承关系。{@code @JsonTypeInfo}用于描述，在序列化继承体系中各种类的实例
 * 时，如何保存类型信息。最常用的案例是
 * <p>
 * <pre>
 * <code>
 * {@code @JsonTypeInfo}(use = Id.NAME, include = As.PROPERTY, property = "type")
 * </code>
 * </pre>
 * 在这种配置下，Jackson会将类型的自定义名称作为属性"type"传递给客户端。在没有自定义名称时，也可以
 * 使用{@code JsonTypeInfo.Id#CLASS}，此时jackson将会传递类型的全限定名称。但是，这种方式
 * 与实现强绑定，耦合性太强。
 * </p>
 * 
 * <p>
 * 在使用{@code JsonTypeInfo.Id#NAME}时，需要自定义类型名称。有两种方式可以定义：
 * <ol>
 * <li>使用{@link JsonSubTypes @JsonSubTypes}：在父类定义所有子类的类型别名</li>
 * <li>
 *  使用{@link JsonTypeName @JsonTypeName}：在每个子类上，各自定义自己的类型别名。
 * </li>
 * </ol>
 * 如果只是在序列化中使用，使用第二种方式最好，复合开放封闭原则。但是，如果需要反序列化，则需要
 * 使用第一种方式定义，否则反序列化会失败
 * </p>
 * </p>
 */
public class TestPolymorphic {
    @JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
//    @JsonSubTypes({
//        @Type(value = DeliveryNotification.class, name = "delivery"),
//        @Type(value = PayNotification.class, name = "pay")
//    })
    public static class Notification {
        private Long id;
        
        private Date createTime;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }
        
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            
            builder.append("id = ").append(id).append("\n")
                   .append("createTime = ").append(createTime)
                   .append(">>>>>>\n");
            
            return builder.toString();
        }
    }
    
//    @JsonTypeName("delivery")
    public static class DeliveryNotification extends Notification {
        private String deliveryNo;

        public String getDeliveryNo() {
            return deliveryNo;
        }

        public void setDeliveryNo(String deliveryNo) {
            this.deliveryNo = deliveryNo;
        }
        
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            
            builder.append("deliveryNo = ").append(deliveryNo).append("\n")
                   .append(super.toString());
            
            return builder.toString();
        }
    }
    
//    @JsonTypeName("pay")
    public static class PayNotification extends Notification {
        private String payCode;
        
        private Integer total;

        public String getPayCode() {
            return payCode;
        }

        public void setPayCode(String payCode) {
            this.payCode = payCode;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }
        
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            
            builder.append("payCode = ").append(payCode).append("\n")
                   .append("total = ").append(total).append("\n")
                   .append(super.toString());
            
            return builder.toString();
        }
    }
    
    public static class Container {
        @JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "type")
        private Notification notification;

        public Notification getNotification() {
            return notification;
        }

        public void setNotification(Notification notification) {
            this.notification = notification;
        }
    }
    
    @Test
    public void test() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        List<Notification> notifications = new ArrayList<>();
        
        DeliveryNotification delivery1 = new DeliveryNotification();
        delivery1.setCreateTime(new Date());
        delivery1.setDeliveryNo("0001");
        delivery1.setId(1L);
        
        DeliveryNotification delivery2 = new DeliveryNotification();
        delivery2.setCreateTime(new Date());
        delivery2.setDeliveryNo("0002");
        delivery2.setId(2L);
        
        PayNotification pay1 = new PayNotification();
        pay1.setCreateTime(new Date());
        pay1.setId(3L);
        pay1.setPayCode("pay-1");
        pay1.setTotal(233);
        
        notifications.add(delivery1);
        notifications.add(delivery2);
        notifications.add(pay1);
        
        Container c = new Container();
        c.setNotification(delivery1);
        
        // 由于擦除的关系，jackson无法直接从泛型变量notifications获知元素类型，并且
        // 由于@JsonTypeInfo不可继承，因此从元素的类型也无法知道其父类的配置。因此，
        // 这里构造了JavaType对象，并使用mapper#writerFor提前告诉jackson即将
        // 序列化的目标类型
        JavaType type = mapper.getTypeFactory().constructParametricType(List.class, Notification.class); 
        String str = mapper.writerFor(type).writeValueAsString(notifications);
        
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readValue(str, Object.class)));
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(c));
        
        List<Notification> d = mapper.readValue(str, type);
        
        System.out.println("==============================");
        System.out.println(d);
    }
}
