package com.iu.course_organizer.data.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoggedInUnitTest {
    @Test
    public void testProperties() {
        Integer userId = 42;
        String displayName = "Jane Doe";

        LoggedInUser loggedInUser = new LoggedInUser(userId, displayName);
        assertEquals(userId, loggedInUser.getUserId());
        assertEquals(displayName, loggedInUser.getDisplayName());
    }
}