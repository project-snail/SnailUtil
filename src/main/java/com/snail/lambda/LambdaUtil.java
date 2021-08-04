package com.snail.lambda;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @version V1.0
 * @author: csz
 * @Title
 * @Package: com.snail.lambda
 * @Description: 带有一些基础的集合转换
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

    public static <R, M, T> List<T> toList(List<R> sourceList,  Function<R, M> mapFun, Function<List<M>, List<T>> convertFun) {

        if (sourceList.isEmpty()) {
            return new ArrayList<>();
        }

        List<M> mediumList = toList(sourceList, mapFun);

        if (mediumList.isEmpty()) {
            return new ArrayList<>();
        }

        return convertFun.apply(mediumList);

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

    public static <R, T, K> Map<K, T> toMap(List<R> sourceList, Function<List<R>, List<T>> targetListFun, Function<T, K> keyFun) {

        if (sourceList.isEmpty()) {
            return new HashMap<>();
        }

        List<T> targetList = targetListFun.apply(sourceList);

        return toMap(targetList, keyFun);

    }

    /**
     * 列表去重
     *
     * @param sourceList 源列表
     * @param keyFun     生成键的方法(使用这个键来去重)
     * @param isParallel 是否使用parallelStream
     * @param <T>        源类型
     * @param <K>        键类型
     * @return 去重后的list
     */
    public static <T, K> List<T> distinct(List<T> sourceList, Function<T, K> keyFun, boolean isParallel) {
        return (isParallel ? sourceList.parallelStream() : sourceList.stream())
            .filter(distinctPredicate(sourceList, keyFun, isParallel))
            .collect(Collectors.toList());
    }

    /**
     * 生成一个用于去重的Predicate(在filter中使用)
     * Note 会保留重复的第一个(parallelStream不一定)
     *
     * @param sourceList 源列表(用于生成去重集合的大小)
     * @param keyFun     生成键的方法(使用这个键来去重)
     * @param isParallel 是否使用在parallelStream中
     * @param <T>        源类型
     * @param <K>        源类型(请确保key类型hashCode())
     * @return Predicate
     */
    public static <T, K> Predicate<T> distinctPredicate(List<T> sourceList, Function<T, K> keyFun, boolean isParallel) {

//        用于存储去重集
        Set<Object> keySet;
//        用于null值
        Object oForNull = new Object();

//        选择去重集的具体实现类
        if (isParallel) {
            keySet = Collections.newSetFromMap(new ConcurrentHashMap<>());
        } else {
            keySet = sourceList == null ? new HashSet<>() : new HashSet<>(sourceList.size());
        }

        return (target) -> {
            K key = keyFun.apply(target);
            return keySet.add(key == null ? oForNull : key);
        };

    }

    /**
     * 生成一个用于去重的Predicate(在filter中使用)
     * Note 会保留重复的第一个，不能用于ParallelStream
     *
     * @param keyFun 生成键的方法(使用这个键来去重)
     * @param <T>    源类型
     * @param <K>    源类型(请确保key类型hashCode())
     * @return Predicate
     */
    public static <T, K> Predicate<T> distinctPredicate(Function<T, K> keyFun) {
        return distinctPredicate(null, keyFun, false);
    }

    /**
     * 列表去重
     *
     * @param sourceList 源列表
     * @param keyFun     生成键的方法(使用这个键来去重)
     * @param <T>        源类型
     * @param <K>        键类型
     * @return 去重后的list
     */
    public static <T, K> List<T> distinct(List<T> sourceList, Function<T, K> keyFun) {
        return sourceList.stream()
            .filter(distinctPredicate(sourceList, keyFun, false))
            .collect(Collectors.toList());
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
