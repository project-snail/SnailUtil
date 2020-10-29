package com.snail.spel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum SpelUtil {
    ;
    private static ExpressionParser parser = new SpelExpressionParser();

    private static LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    /**
     * 解析el值 map上下文
     *
     * @param expression el表达式
     * @param context    当前上下文
     * @param valueClass 期待解析后的类型
     * @param <T>        解析后的类型
     * @return 值
     */
    public static <T> T getValue(String expression, EvaluationContext context, Class<T> valueClass) {
        Expression exp = parser.parseExpression(expression);
        try {
            return exp.getValue(context, valueClass);
        } catch (SpelEvaluationException e) {
            return null;
        }
    }

    /**
     * 解析el值 方法入参上下文
     *
     * @param expression el表达式
     * @param method     目标方法
     * @param args       目标方法的入参
     * @param valueClass 期待解析后的类型
     * @param <T>        解析后的类型
     * @return 值
     */
    public static <T> T getValue(String expression, Method method, Object[] args, Class<T> valueClass) {
        return getValue(expression, parseMethodToContext(method, args), valueClass);
    }

    /**
     * 生成一个解析器 map上下文
     *
     * @param evaluationContext 当前上下文
     * @return 解析器
     */
    public static SpelFunction generateSpelFunction(EvaluationContext evaluationContext) {
        return new SpelFunction() {
            @Override
            public <T> T getValue(String expression, Class<T> valueClass) {
                return SpelUtil.getValue(expression, evaluationContext, valueClass);
            }
        };
    }

    /**
     * 生成一个解析器 方法入参上下文
     *
     * @param method 目标方法
     * @param args   目标方法的入参
     * @return 解析器
     */
    public static SpelFunction generateSpelFunction(Method method, Object[] args) {
        return generateSpelFunction(parseMethodToContext(method, args));
    }

    /**
     * 生成一个解析器 map上下文
     *
     * @param variableMap map上下文
     * @return 解析器
     */
    public static SpelFunction generateSpelFunction(Map<String, Object> variableMap) {
        return generateSpelFunction(parseVariableMapToContext(variableMap));
    }

    /**
     * 将 方法入参上下文 转换成 map上下文
     *
     * 可以直接通过方法创建了 * com.snail.spel.SpelUtil#parseMethodToContext(java.lang.reflect.Method, java.lang.Object[])
     *
     * @param method 目标方法
     * @param args   目标方法的入参
     * @return map上下文
     */
    @Deprecated
    private static Map<String, Object> parseMethodToVariableMap(Method method, Object[] args) {

        if (method == null || args == null) {
            return Collections.emptyMap();
        }

//        获取入参名称
        String[] parameterNames = discoverer.getParameterNames(method);

//        入参列表和入参值长度不匹配 返回空map
        if (parameterNames == null || parameterNames.length != args.length) {
            return Collections.emptyMap();
        }

        HashMap<String, Object> variableMap = new HashMap<>(parameterNames.length);
        for (int i = 0; i < parameterNames.length; i++) {
            variableMap.put(parameterNames[i], args[i]);
        }
        return variableMap;
    }

    /**
     * 将map上下文转换成EvaluationContext
     *
     * @param variableMap map上下文
     * @return
     */
    private static EvaluationContext parseVariableMapToContext(Map<String, Object> variableMap) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (variableMap != null) {
            for (Map.Entry<String, Object> entry : variableMap.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
        }
        return context;
    }

    /**
     * 直接把方法和入参转换成EvaluationContext
     *
     * @param method 方法
     * @param args   入参
     * @return
     */
    private static EvaluationContext parseMethodToContext(Method method, Object[] args) {
        return new MethodBasedEvaluationContext(TypedValue.NULL, method, args, discoverer);
    }

    @FunctionalInterface
    public interface SpelFunction {
        /**
         * 解析方法
         *
         * @param expression el表达式
         * @param valueClass 期望表达式解析后的类型
         * @param <T>        类型
         * @return 值
         */
        <T> T getValue(String expression, Class<T> valueClass);

        /**
         * 从el表达式中解析值
         *
         * @param expression el表达式
         * @param supplier   如果表达式解析后为null，则使用此Supplier提供的值
         * @param valueClass 期望表达式解析后的类型
         * @param <T>        类型
         * @return 值
         */
        default <T> T tryParseValue(String expression, Supplier<T> supplier, Class<T> valueClass) {
            T value;
            if (StringUtils.isNotBlank(expression)) {
                value = getValue(expression, valueClass);
                if (value != null) {
                    return value;
                }
            }
            return supplier.get();
        }
    }

}
