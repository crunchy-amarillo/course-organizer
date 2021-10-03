package com.iu.course_organizer.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FormStateTest {

    @Test
    public void isValid() {
        assertTrue((new FormState(true)).isDataValid());
    }

    @Test
    public void isInvalid() {
        assertFalse((new FormState(false)).isDataValid());
    }
}
