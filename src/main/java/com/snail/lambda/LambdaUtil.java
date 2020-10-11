package com.snail.lambda;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @version V1.0
 * @author: csz
 * @Title
 * @Package: com.snail.lambda
 * @Description:
 * @date: 2020/10/11
 */
public enum LambdaUtil {
    ;

    /**
     * 转换列表
     *
     * @param sourceList 源列表
     * @param mapFun     转换时的方法
     * @param nullable   是否可为空
     * @param <R>        源列表类型
     * @param <T>        目标方法类型
     * @return 转换后的列表
     */
    public static <R, T> List<T> toList(Collection<R> sourceList, Function<R, T> mapFun, boolean nullable) {

        if (sourceList.isEmpty()) {
            return new ArrayList<>();
        }

        Stream<T> stream = sourceList.stream()
            .map(mapFun);

        if (!nullable) {
            stream = stream.filter(Objects::nonNull);
        }

        return stream.collect(Collectors.toList());

    }

    /**
     * 转换列表 nullable=false
     *
     * @param sourceList 源列表
     * @param mapFun     转换时的方法
     * @param <R>        源列表类型
     * @param <T>        目标方法类型
     * @return 转换后的列表
     */
    public static <R, T> List<T> toList(Collection<R> sourceList, Function<R, T> mapFun) {
        return toList(sourceList, mapFun, false);
    }

    /**
     * 列表转map
     *
     * @param sourceList 源列表
     * @param keyFun     生成键的方法
     * @param <R>        源类型
     * @param <K>        键类型
     * @return map
     */
    public static <R, K> Map<K, R> toMap(List<R> sourceList, Function<R, K> keyFun) {

        if (sourceList.isEmpty()) {
            return new HashMap<>();
        }

        return sourceList.stream()
            .collect(Collectors.toMap(keyFun, Function.identity(), replaceMergeFunction()));


    }


    /**
     * 使用 Collectors.toMap 时的重复键策略(保留后者)
     *
     * @param <U> 列表值类型
     * @return replaceMergeFunction
     */
    public static <U> BinaryOperator<U> replaceMergeFunction() {
        return (k1, k2) -> k2;
    }

}
