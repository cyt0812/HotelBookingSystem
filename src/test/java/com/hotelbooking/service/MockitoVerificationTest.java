package com.hotelbooking.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MockitoVerificationTest {

    @Mock
    private List<String> mockList;

    @Test
    void testMockitoStaticImportsWork() {
        MockitoAnnotations.openMocks(this);
        
        // 测试静态导入是否工作
        when(mockList.size()).thenReturn(10);
        
        assertEquals(10, mockList.size());
        verify(mockList, times(1)).size();
        
        System.out.println("✅ Mockito static imports are working!");
    }

    @Test
    void testMockitoAnyMethod() {
        MockitoAnnotations.openMocks(this);
        
        // 测试 any() 方法
        when(mockList.add(any(String.class))).thenReturn(true);
        
        boolean result = mockList.add("test");
        
        assertEquals(true, result);
        verify(mockList, times(1)).add(any(String.class));
        
        System.out.println("✅ Mockito any() method is working!");
    }
}