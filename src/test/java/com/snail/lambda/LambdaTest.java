package com.snail.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @author: csz
 * @Title
 * @Package: com.snail.lambda
 * @Description:
 * @date: 2020/10/11
 */
@RunWith(JUnit4.class)
public class LambdaTest {

    @Test
    public void testLambdaUtilDistinctPredicate() {
        List<TestObj> testObjList = Arrays.asList(
            new TestObj(3, 1),
            new TestObj(4, 2),
            new TestObj(1, 3),
            new TestObj(3, 4)
        );
        System.out.println(
            testObjList.stream()
                .filter(LambdaUtil.distinctPredicate(testObjList, TestObj::getTargetId, false))
                .collect(Collectors.toList())
        );
        System.out.println(
            testObjList.stream()
                .filter(LambdaUtil.distinctPredicate(testObjList, TestObj::getTargetId, true))
                .collect(Collectors.toList())
        );
        System.out.println(
            testObjList.stream()
                .filter(LambdaUtil.distinctPredicate(TestObj::getTargetId))
                .collect(Collectors.toList())
        );
    }

    @Test
    public void testLambdaUtilDistinct() {
        List<TestObj> testObjList = Arrays.asList(
            new TestObj(3, 2),
            new TestObj(4, 1),
            new TestObj(1, 4),
            new TestObj(3, 3)
        );
        System.out.println(LambdaUtil.distinct(testObjList, (testObj -> testObj.getTargetId() + testObj.getMarkId())));

        System.out.println(LambdaUtil.distinct(testObjList, TestObj::getTargetId));
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class TestObj {
        private Integer targetId;
        private Integer markId;
    }

}
