package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class DemoApplicationTests {

    Calculator underTest = new Calculator();


    @Test
    void itShouldAddTwoNumbers() {
        // given (inputs)
        int numOne = 20;
        int numTwo = 30;

        // when (method that we want to test)
        int result = underTest.add(numOne, numTwo);

        // then (testing)
        assertThat(result).isEqualTo(50);
    }

    class Calculator {
        int add(int a, int b) {
            return a + b;
        }
    }

}
