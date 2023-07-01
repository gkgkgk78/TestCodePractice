package com.springboot.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// 예제 7.4
public class TestLifeCycle {

    //JUnit의 생명 주기를 통하여 동작 방식을 알아보자

    @BeforeAll  //테스트 시작 하기 전에 호출되는 메서드를 정의 한다.
    //전체 테스트 동작에서 처음에만 수행이 된다.
    static void beforeAll() {
        System.out.println("## BeforeAll Annotation 호출 ##");
        System.out.println();
    }

    @AfterAll//테스트를 종료하면서 호출되는 메서드를 정의 한다.
    //전체 테스트 동작에서 마지막에 수행이 된다.
    static void afterAll() {
        System.out.println("## afterAll Annotation 호출 ##");
        System.out.println();
    }

    @BeforeEach
        //각 테스트 메서드가 실행 되기 전에 동작하는 메서드를 정의 한다.
        //@Test 가 붙은 얘들이 실행되기 전에 실행 이 됨
    void beforeEach() {
        System.out.println("## beforeEach Annotation 호출 ##");
        System.out.println();
    }

    @AfterEach
        //각 테스트 메서드가 종료 되면서 호출되는 메서드를 정의 한다.
        //@Test 가 붙은 얘들이 실행 되고 나서 실행 이 됨
    void afterEach() {
        System.out.println("## afterEach Annotation 호출 ##");
        System.out.println();
    }

    @Test
    void test1() {
        System.out.println("## test1 시작 ##");
        System.out.println();
    }

    @Test
    void test2() {
        System.out.println("## test2 시작 ##");
        System.out.println();
    }

    // Disabled Annotation : 테스트를 실행하지 않게 설정하는 어노테이션
    @Test
    @Disabled
    void test3() {
        System.out.println("## test3 시작 ##");
        System.out.println();
    }

}
