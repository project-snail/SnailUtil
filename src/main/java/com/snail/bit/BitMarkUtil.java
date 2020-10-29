package com.snail.bit;

import java.util.Collection;

/**
 * @version V1.0
 * @author: csz
 * @Title
 * @Package: com.snail.bit
 * @Description: 适合标记位不多的情况，最多32位
 * @date: 2020/10/29
 */
public enum BitMarkUtil {
    ;

    /**
     * 创建一个mark数字
     *
     * @param enums 枚举数组
     * @param <E>   枚举类型
     * @return mark
     */
    public static <E extends Enum> int createMark(Collection<E> enums) {

        if (enums == null || enums.size() == 0) {
            return 0;
        }

        int mark = 0;

        for (E next : enums) {
            if (next == null) {
                continue;
            }
            mark |= (1 << next.ordinal());
        }

        return mark;

    }

    /**
     * 是否为true的标记位
     *
     * @param mark     标记
     * @param markEnum 标记枚举
     */
    public static boolean isMark(int mark, Enum markEnum) {
        return markEnum != null && (mark & (1 << markEnum.ordinal())) != 0;
    }

    /**
     * 删除标记
     *
     * @param mark     标记
     * @param markEnum 标记枚举
     */
    public static int delMark(int mark, Enum markEnum) {
        if (markEnum == null) {
            return mark;
        }
        return mark & (~(1 << markEnum.ordinal()));
    }

    /**
     * 添加标记
     *
     * @param mark     标记
     * @param markEnum 标记枚举
     */
    public static int addMark(int mark, Enum markEnum) {
        if (markEnum == null) {
            return mark;
        }
        return mark | (1 << markEnum.ordinal());
    }

    /**
     * 生成位标记方法工具
     * @param enums 枚举列表
     */
    public static <E extends Enum> BitMarkFunction generateBitMarkFunction(Collection<E> enums) {
        return generateBitMarkFunction(createMark(enums));
    }

    /**
     * 生成位标记方法工具
     * @param mark 标记
     */
    public static BitMarkFunction generateBitMarkFunction(int mark) {
        return new BitMarkFunction(mark);
    }

    public static final class BitMarkFunction {

        private int mark;

        public BitMarkFunction(int mark) {
            this.mark = mark;
        }

        public boolean isMark(Enum markEnum) {
            return BitMarkUtil.isMark(mark, markEnum);
        }

        public int addMark(Enum markEnum) {
            this.mark = BitMarkUtil.addMark(mark, markEnum);
            return this.mark;
        }

        public int delMark(Enum markEnum) {
            this.mark = BitMarkUtil.delMark(mark, markEnum);
            return this.mark;
        }

        public int getMark() {
            return mark;
        }
    }

}
