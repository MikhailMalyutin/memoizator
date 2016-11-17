package ru.msm.memoizer;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;

public class DeepEqualsBuilderTest extends UnitilsTestNG {
    @Test
    public void testAll() {
        Object object1 = DataFactory.getTestObject();
        DataFactory.TestObject1 object2 = (DataFactory.TestObject1) DataFactory.getTestObject();
        Assert.assertNotSame(object1, object2);

        boolean result = DeepEqualsBuilder.reflectionEquals(object1, object2);
        Assert.assertTrue(result);

        object2.getNested().setField2(object2.getNested().getField2() + 1);
        result = DeepEqualsBuilder.reflectionEquals(object1, object2);
        Assert.assertFalse(result);
    }
}
