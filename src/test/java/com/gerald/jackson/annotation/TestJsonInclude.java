package com.gerald.jackson.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/**
 * 本例测试了{@link JsonInclude @JsonInclude}标签。{@code @JsonInclude}过滤某些特定的值，
 * 例如<code>null</code>。
 * <p>
 * {@code @JsonInclude}具有四种类型的值，
 * <ol>
 * <li>
 *  {@link Include#NON_NULL}：过滤<code>null</code>值
 * </li>
 * <li>
 *  {@link Include#NON_ABSENT}：包括{@code Include#NON_NULL}，同时会过滤
 * 空引用，例如具有空值的Optional和AtomicReference
 * </li>
 * <li>
 *  {@link Include#NON_EMPTY}：包括{@code Include#NON_ABSENT}，同时会过滤空字符串、
 * 空集合、空数据组
 * </li>
 * <li>
 *  {@link Include#NON_DEFAULT}：
 *  <ul>
 *  <li>
 *      当将{@code @JsonInclude}用于POJO时，jackson将用默认构造器创建一个对象A，并比较每
 *      一个属性。如果属性值，与A具有相同的值，则忽略；反之，则保留。
 *  </li>
 *  <li>
 *      当将{@code @JsonInclude}用于属性时，{@code Include#NON_DEFAULT}包括
 *      {@code Include#NON_EMPTY}，并将过滤具有默认值的基本类型和具有0值的时间类型
 *      (例如new Date(0))
 *  </li>
 *  </ul>
 * </li>
 * </ol>
 * </p>
 *
 */
public class TestJsonInclude {
    @JsonInclude(value = Include.NON_EMPTY)
    public static class Nulls {
        private Integer id;

        private Optional<String> name = Optional.empty();

        private List<String> children = new ArrayList<>();
        
        private Default def1 = new Default();
        
        private boolean isOk = true;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Optional<String> getName() {
            return name;
        }

        public void setName(Optional<String> name) {
            this.name = name;
        }

        public List<String> getChildren() {
            return children;
        }

        public void setChildren(List<String> children) {
            this.children = children;
        }

        public Default getDef1() {
            return def1;
        }

        public void setDef1(Default def1) {
            this.def1 = def1;
        }

        public boolean isOk() {
            return isOk;
        }

        public void setOk(boolean isOk) {
            this.isOk = isOk;
        }
    }
    
    public static class Default {
        private int id;
        
        public Default() {
            
        }
        
        public int getId() {
            return id;
        }

        public Default(int id) {
            this.id = id;
        }
        
        @Override
        public boolean equals(Object other) {
            if(!(other instanceof Default)) {
                return false;
            }
            
            Default tmp = (Default)other;
            
            return tmp.id == this.id;
        }
    }
    
    @Test
    public void test() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        
        mapper.registerModule(new Jdk8Module());
        
        Nulls n = new Nulls();
        n.setId(2);
        
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(n));
    }
}
