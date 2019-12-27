package com.godaddy.uk.productsapi.utilities;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MoneyUtilitiesTest {

    @Test
    public void testSumMoneyValues() {
        // given
        List<TestObject> testObjects = List.of(
                new TestObject(12.0),
                new TestObject(151.31),
                new TestObject(2.03)
        );

        // when
        double result = MoneyUtilities.sumMoneyValues(testObjects, TestObject::getValue);

        // then
        assertEquals(165.34D, result, 0);
    }

    @Test
    public void testSumMoneyValues_nullList() {
        // given
        List<TestObject> testObjects = null;

        // when
        double result = MoneyUtilities.sumMoneyValues(testObjects, TestObject::getValue);

        // then
        assertEquals(0D, result, 0);
    }

    @Test
    public void testSumMoneyValues_emptyList() {
        // given
        List<TestObject> testObjects = List.of();

        // when
        double result = MoneyUtilities.sumMoneyValues(testObjects, TestObject::getValue);

        // then
        assertEquals(0D, result, 0);
    }

    public static class TestObject {

        private final double value;

        public TestObject(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

    }

}