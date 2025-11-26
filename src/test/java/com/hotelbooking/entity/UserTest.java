package com.hotelbooking.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testDefaultConstructor() {
        User user = new User();
        assertNotNull(user);
        user.setUserId(1);
        user.setUsername("alice");
        user.setPassword("123456");
        user.setEmail("a@b.com");
        user.setFullName("Alice Wonderland");
        user.setRole("ADMIN");

        assertEquals(1, user.getUserId());
        assertEquals("alice", user.getUsername());
        assertEquals("123456", user.getPassword());
        assertEquals("a@b.com", user.getEmail());
        assertEquals("Alice Wonderland", user.getFullName());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    public void testCustomConstructor() {
        User user = new User("bob", "pwd", "b@c.com", "Bob Marley");

        assertEquals("bob", user.getUsername());
        assertEquals("pwd", user.getPassword());
        assertEquals("b@c.com", user.getEmail());
        assertEquals("Bob Marley", user.getFullName());
        // 默认 role
        assertEquals("CUSTOMER", user.getRole());
    }

    @Test
    public void testToString() {
        User user = new User();
        user.setUserId(5);
        user.setUsername("mike");
        user.setRole("ADMIN");

        String txt = user.toString();
        assertTrue(txt.contains("userId=5"));
        assertTrue(txt.contains("mike"));
        assertTrue(txt.contains("ADMIN"));
    }
}
