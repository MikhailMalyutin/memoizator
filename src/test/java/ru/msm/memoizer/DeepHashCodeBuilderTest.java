package ru.msm.memoizer;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;
import ru.msm.memoizer.data.TestData;
import ru.msm.memoizer.impl.DeepHashCodeBuilder;

public class DeepHashCodeBuilderTest extends UnitilsTestNG {
    @Test
    public void testAll() {
        Object object1 = TestData.getTestObject();
        TestData.TestObject1 object2 = (TestData.TestObject1) TestData.getTestObject();
        Assert.assertNotSame(object1, object2);

        int object1Hash = DeepHashCodeBuilder.reflectionHashCode(object1);
        int object2Hash = DeepHashCodeBuilder.reflectionHashCode(object2);
        Assert.assertEquals(object1Hash, object2Hash);

        object2.getNested().setField2(object2.getNested().getField2() + 1);
        object2Hash = DeepHashCodeBuilder.reflectionHashCode(object2);
        Assert.assertTrue(object1Hash != object2Hash);
    }
}
