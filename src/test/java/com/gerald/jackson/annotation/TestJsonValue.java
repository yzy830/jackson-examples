package com.gerald.jackson.annotation;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link JsonValue @JsonValue}用于标记一个类的getter方法(non-void返回值，没有参数的方法)，
 * 以将这个方法的返回值作为整个类序列化的值。这个标签一般用于enum和普通类的toString方法。
 * <p>
 * 为了反序列化，需要使用{@link JsonCreator @JsonCreator}指定反序列化构造器或者工厂方法。
 * </p>
 * <p>
 * 在本例中，使用msg代替整个ErrorCode。为了反序列化，自定义了工厂方法{@link ErrorCode#deserialize(String)}
 * 将字符串映射到enum
 * </p>
 */
public class TestJsonValue {
    public static class Resp {
        private ErrorCode errorCode;

        public ErrorCode getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(ErrorCode errorCode) {
            this.errorCode = errorCode;
        }
    }
    
    public enum ErrorCode {
        SUCCESS(1000, "success"),
        FAIL(1001, "fail")
        ;
        
        private static final Map<String, ErrorCode> map;
        
        static {
            map = Arrays.stream(ErrorCode.values())
                        .collect(Collectors.toMap(ErrorCode::getMsg, 
                                                  (t)->t));
        }
        
        private ErrorCode(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
        
        private int code;
        
        private String msg;

        public int getCode() {
            return code;
        }

        /**
         * 使用msg代替整个对象的值
         */
        @JsonValue
        public String getMsg() {
            return msg;
        }
        
        /**
         * 自定义反序列化
         */
        @JsonCreator
        private static ErrorCode deserialize(String msg) {
            return map.get(msg);
        }
    }
    
    @Test
    public void test() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        Resp resp = new Resp();
        resp.setErrorCode(ErrorCode.SUCCESS);
        
        String str = mapper.writeValueAsString(resp);
        System.out.println(str);
        
        Resp d = mapper.readValue(str, Resp.class);
        
        System.out.println("error code = " + d.getErrorCode());
    }
}
