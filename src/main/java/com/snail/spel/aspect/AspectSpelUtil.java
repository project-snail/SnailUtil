package com.snail.spel.aspect;


import com.snail.spel.SpelUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public enum AspectSpelUtil {
    ;

    /**
     * 从切面生成spel解析器
     *
     * @param joinPoint 切面入参
     * @return spel解析器
     */
    public static SpelUtil.SpelFunction generateSpelFunction(ProceedingJoinPoint joinPoint) {

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        Object[] args = joinPoint.getArgs();

        return SpelUtil.generateSpelFunction(method, args);

    }

    /**
     * 从切面生成spel解析器 带condition(方便判断这次el是否需要解析)
     *
     * @param joinPoint           切面入参
     * @param conditionExpression conditionEl表达式, 如果condition解析为null或false 返回值为null
     * @return spel解析器
     */
    public static SpelUtil.SpelFunction generateSpelFunctionWithCondition(
        ProceedingJoinPoint joinPoint, String conditionExpression
    ) {

        SpelUtil.SpelFunction spelFunction = generateSpelFunction(joinPoint);

        Boolean condition = spelFunction.tryParseValue(conditionExpression, () -> Boolean.FALSE, Boolean.class);

        return condition ? spelFunction : null;

    }

}
