package com.gerald.jackson.annotation;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 本例测试了{@link JsonRawValue @JsonRawValue}。这个标签，用于控制一个String属性的串行化。
 * 档String属性标记了{@code @JsonRawValue}之后，jackson将属性的值去掉引号作为json串的一部分。
 * <p>
 * 在本例中，使用了如下测试bean
 * <pre>
 * <code>
 * GeoResp resp = new GeoResp();
 * resp.setCode(10000000);
 * resp.setMsg("success");
 * resp.setResponse("{\"result\":{\"count\":9,\"data\":[]}}");
 * </code>
 * </pre>
 * </p>
 * 
 * <p>
 * 如果<em>没有使用</em>{@code @JsonRawValue}，其结果是
 * <pre>
 * <code>
 * {
 *   "code" : 10000000,
 *   "msg" : "success",
 *   "response" : "{\"result\":{\"count\":9,\"data\":[]}}"
 * }
 * </code>
 * </pre>
 * </p>
 * 
 * <p>
 * 如果<em>使用了</em>{@code @JsonRawValue}，其结果是
 * <pre>
 * <code>
 * {
 *   "code" : 10000000,
 *   "msg" : "success",
 *   "response" : {"result":{"count":9,"data":[]}}
 * }
 * </code>
 * </pre>
 * </p>
 * 
 * <p>
 * 这里需要主要的是，{@code @JsonRawValue}只控制序列化过程，对反序列化过程没有作用。
 * {@link #testDeserialize()}测试了反序列化过程，会得到异常。从现在得到的信息，只能
 * 通过自定义反序列化过程才能完成。
 * </p>
 */
public class TestRawValue {
    public static class GeoResp {
        private int code;
        
        private String msg;
        
        private String response;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @JsonRawValue
        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
        
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            
            builder.append("code = ").append(code).append("\n")
                   .append("msg = ").append(msg).append("\n")
                   .append("response = ").append(response).append("\n");
            
            return builder.toString();
        }
    }
    
    @Test
    public void testSerialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        
        GeoResp resp = new GeoResp();
        resp.setCode(10000000);
        resp.setMsg("success");
        resp.setResponse("{\"result\":{\"count\":9,\"data\":[]}}");
        
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resp));
    }
    
    @Test
    public void testDeserialize() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        String str = "{\"code\":10000000,\"msg\":\"sucess\",\"response\":{\"result\":{\"count\":9,\"data\":[]}}}";
        
        GeoResp resp = mapper.readValue(str, GeoResp.class);
        
        System.out.println(resp);
    }
}
