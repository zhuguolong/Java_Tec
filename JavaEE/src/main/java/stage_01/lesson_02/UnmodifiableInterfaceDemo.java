package stage_01.lesson_02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author zhugu
 * 2019/10/28 22:07
 */
public class UnmodifiableInterfaceDemo {
    public static void main(String[] args) {
        Collection<Integer> values = of(1, 2, 3, 4, 5);
        values.add(6);

        values = unmodifiable(1, 2, 3, 4, 5);
        // UnsupportedOperationException 异常，不可修改
        values.add(6);
    }

    /**
     * 返回的集合可以修改
     * @param values
     * @return
     */
    public static Collection<Integer> of(Integer... values) {
        return new ArrayList<Integer>(Arrays.asList(values));
    }

    /**
     * 返回的集合不可被修改，只读
     * @param values
     * @return
     */
    public static Collection<Integer> unmodifiable(Integer... values) {
        return Collections.unmodifiableList(Arrays.asList(values));
    }
}
