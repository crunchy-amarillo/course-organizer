package com.iu.course_organizer.common.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class StringUtilsTest {
    private final String charSequence;
    private final boolean expectedResult;

    public StringUtilsTest(String charSequence, boolean expectedResult) {
        this.charSequence = charSequence;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][]{{null, false}, {"", false}, {" ", false}, {"a", true}, {"abc", true}
                });
    }

    @Test
    public void isNotEmpty() {
        assertEquals(expectedResult, StringUtils.isNotEmpty(charSequence));
    }
}
