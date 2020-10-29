package com.snail.bit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

/**
 * @version V1.0
 * @author: csz
 * @Title
 * @Package: com.snail.bit
 * @Description:
 * @date: 2020/10/29
 */
@RunWith(JUnit4.class)
public class BitMarkUtilTest {

    @Test
    public void testBitMarkUtil() {
        int mark = BitMarkUtil.createMark(
            Arrays.asList(
                TestEnum.ONE,
                TestEnum.THREE
            )
        );
        printBit(mark);
        mark = BitMarkUtil.delMark(mark, TestEnum.ONE);
        printBit(mark);
        printBit(BitMarkUtil.addMark(mark, TestEnum.TWO));
        System.out.println((BitMarkUtil.isMark(mark, TestEnum.THREE)));

    }

    @Test
    public void testBitMarkFun() {
        BitMarkUtil.BitMarkFunction bitMarkFunction = BitMarkUtil.generateBitMarkFunction(
            Arrays.asList(
                TestEnum.ONE,
                TestEnum.THREE
            )
        );
        printBit(bitMarkFunction.getMark());
        printBit(bitMarkFunction.delMark(TestEnum.ONE));
        printBit(bitMarkFunction.addMark(TestEnum.TWO));
        System.out.println((bitMarkFunction.isMark(TestEnum.ONE)));

    }

    private void printBit(int num) {
        System.out.println(Integer.toBinaryString(num));
    }

    enum TestEnum {
        ONE,
        TWO,
        THREE
    }

}
