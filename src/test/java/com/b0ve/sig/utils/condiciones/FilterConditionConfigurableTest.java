package com.b0ve.sig.utils.condiciones;

import static com.b0ve.sig.utils.condiciones.FilterConditionConfigurable.CONDITIONS.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FilterConditionConfigurableTest {
    
    @Test
    public void testFilterConditionConfigurable1() {
        FilterCondition f1 = new FilterConditionConfigurable("", STR_EQUALS, "Trucu Trucu");
        FilterCondition f2 = new FilterConditionConfigurable("", STR_DISTINCT, "Trucu Trucu");
        FilterCondition f3 = new FilterConditionConfigurable("", INT_EQUALS, "144");
        FilterCondition f4 = new FilterConditionConfigurable("", INT_DISTINCT, "144");
        FilterCondition f5 = new FilterConditionConfigurable("", INT_LESS_THAN, "144");
        FilterCondition f6 = new FilterConditionConfigurable("", INT_GREATER_THAN, "144");
        FilterCondition f7 = new FilterConditionConfigurable("", DECIMAL_LESS_THAN, "1.44");
        FilterCondition f8 = new FilterConditionConfigurable("", DECIMAL_GRATER_THAN, "1.44");
        FilterCondition f9 = new FilterConditionConfigurable("", EMPTY, "");
        FilterCondition f10 = new FilterConditionConfigurable("", NOT_EMPTY, "");
        FilterCondition f11 = new FilterConditionConfigurable("", EXISTS, "");
        FilterCondition f12 = new FilterConditionConfigurable("", NOT_EXIST, "");
        
        //STR_EQUALS
        assertTrue(f1.testValue("Trucu Trucu"));
        assertFalse(f1.testValue("Toma lacasitos"));
        
        //STR_DISTINCT
        assertTrue(f2.testValue("Toma lacasitos"));
        assertFalse(f2.testValue("Trucu Trucu"));
        
        //INTEGER_EQUALS
        assertTrue(f3.testValue("144"));
        assertFalse(f3.testValue("150"));
        
        //INTEGER_DISTINCT
        assertTrue(f4.testValue("150"));
        assertFalse(f4.testValue("144"));
        
        //INTEGER_LESS_THAN
        assertTrue(f5.testValue("140"));
        assertFalse(f5.testValue("144"));
        assertFalse(f5.testValue("150"));
        
        //INTEGER_GREATER_THAN
        assertTrue(f6.testValue("150"));
        assertFalse(f6.testValue("144"));
        assertFalse(f6.testValue("140"));
        
        //DECIMAL_LESS_THAN
        assertTrue(f7.testValue("1.40"));
        assertFalse(f7.testValue("1.44"));
        assertFalse(f7.testValue("1.50"));
        
        //DECIMAL_GRATER_THAN
        assertTrue(f8.testValue("1.50"));
        assertFalse(f8.testValue("1.44"));
        assertFalse(f8.testValue("1.40"));
        
        //EMPTY
        assertTrue(f9.testValue(""));
        assertFalse(f9.testValue("CIMAS"));
        
        //NOT_EMPTY
        assertTrue(f10.testValue("CIMAS"));
        assertFalse(f10.testValue(""));
        
        //EXISTS
        assertTrue(f11.testValue("CIMAS"));
        assertFalse(f11.testValue(null));
        
        //NOT_EXIST
        assertTrue(f12.testValue(null));
        assertFalse(f12.testValue("CIMAS"));
    }
    
}
