package com.gerald.jackson;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestFormattedJson {
    /**
     * 使用{@link ObjectMapper#writerWithDefaultPrettyPrinter()}格式化json输出
     * 
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @Test
    public void testFormatExistedStr() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String str = "{\"name\" : \"name-test\",\"attr2\" : \"value2\", \"attr1\" : \"value1\", \"nested\":{\"prop\":1}}";
        
        // 这里，jackson生成一个LinkedHashMap
        Object obj = mapper.readValue(str, Object.class);
        
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
    }
}
