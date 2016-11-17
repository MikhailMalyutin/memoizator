package ru.msm.memoizer;

public class DataFactory {
///////////////////////////////////////////////////////////////////////////////
    public static class TestObject1 {
        private String field1;
        private TestNestedObject1 nested;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public TestNestedObject1 getNested() {
            return nested;
        }

        public void setNested(TestNestedObject1 nested) {
            this.nested = nested;
        }
    }
///////////////////////////////////////////////////////////////////////////////
    public static class TestNestedObject1 {
        private String field1;
        private int field2;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public int getField2() {
            return field2;
        }

        public void setField2(int field2) {
            this.field2 = field2;
        }
    }
///////////////////////////////////////////////////////////////////////////////

    public static Object getTestObject() {
        TestObject1 result = new TestObject1();
        result.setField1("field1");

        TestNestedObject1 nested = new TestNestedObject1();
        nested.setField1("nestedField");
        nested.setField2(1);

        result.setNested(nested);
        return result;
    }
}
