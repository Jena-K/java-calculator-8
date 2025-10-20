package calculator.service;

import calculator.service.StringCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

// 예외 테스트 시 static org.assertj.core.api.Assertions.assertThat; 를 사용

public class StringCalculatorTest {

    private StringCalculator calculator;

    // 각 테스트 메소드가 실행되기 전 호출되어 테스트에 필요한 객체를 준비.
    @BeforeEach
    void setUp() {
        // Service 계층의 실제 구현 객체를 생성
        calculator = new StringCalculator();

    }

    // 빈 문자열 처리 기능 테스트

    @Test
    @DisplayName("빈 문자열을 입력하면 0을 반환해야 한다.")
    void empty_should_return_zero() {
        // given
        String input = "";

        // when
        int result = calculator.add(input);

        // then
        // 입력값이 빈 문자열("")일 경우 0을 반환한다.
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("숫자 하나를 입력하면 해당 숫자를 반환해야 한다.")
    void single_number_should_return_itself() {
        // given
        String input = "5";

        // when
        int result = calculator.add(input); // 현재 add()는 "5"에 대해 0을 반환. 테스트 실패 유도

        // then
        assertThat(result).isEqualTo(5);
    }

    @ParameterizedTest
    @CsvSource(value = {"1,2=3", "1:2:3=6", "1,2:3=6", "10,20=30"}, delimiter = '=')
    @DisplayName("쉽표나 콜론을 구분자로 사용해 합을 구해야 한다.")
    void default_delimiters_should_return_sum(String input, int expected) {
        // given (입력과 기대값은 CsvSource에서 제공)

        //when
        int result = calculator.add(input); // 현재 add()는 0을 반환하므로 테스트 실패 유도

        //then
        assertThat(result).isEqualTo(expected);
    }
    @Test
    @DisplayName("커스텀 구분자 세미콜론(;)으로 분리하여 합을 구해야 한다.")
    void custom_delimiter_semicolon_should_return_sum() {
        // given
        String input = "//;\n1;2;3";
        int expected = 6;

        // when
        int result = calculator.add(input);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("커스텀 구분자 샵(#)으로 분리하여 합을 구해야 한다.")
    void custom_delimiter_hash_should_return_sum() {
        // given
        String input = "//#\n4#5#6";
        int expected = 15;

        // when
        int result = calculator.add(input);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("커스텀 구분자와 기본 구분자를 함께 사용할 수 있어야 한다.")
    void custom_and_default_delimiters_should_work_together() {
        // given
        String input = "//-\n5-6,7";
        int expected = 18;

        // when
        int result = calculator.add(input);

        //then
        assertThat(result).isEqualTo(expected);
    }

    // 예외 처리 테스트
    @Test
    @DisplayName("영문자를 커스텀 구분자로 사용하면 예외가 발생해야 한다.")
    void alphabetic_custom_delimiter_should_throw_exception() {
        // given
        String input = "//a\n1a2";

        // when & then
        assertThat(org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> calculator.add(input)
        )).hasMessageContaining("구분자");
    }

    @Test
    @DisplayName("2글자 커스텀 구분자를 사용하면 예외가 발생해야 한다.")
    void two_char_custom_delimiter_should_throw_exception() {
        // given
        String input = "//;;\n1;2";

        // when & then
        assertThat(org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> calculator.add(input)
        )).hasMessageContaining("구분자");
    }

    @Test
    @DisplayName("맨 뒤에 구분자가 있으면 예외가 발생해야 한다.")
    void trailing_delimiter_should_throw_exception() {
        // given
        String input = "1,2,";

        // when & then
        assertThat(org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> calculator.add(input)
        )).hasMessageContaining("구분자");
    }

    @Test
    @DisplayName("10000 이상의 숫자가 있으면 예외가 발생해야 한다.")
    void number_over_10000_should_throw_exception() {
        // given
        String input = "10000,1";

        // when & then
        assertThat(org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> calculator.add(input)
        )).hasMessageContaining("9999");
    }

    @Test
    @DisplayName("숫자가 아닌 문자가 포함되면 예외가 발생해야 한다.")
    void non_numeric_character_should_throw_exception() {
        // given
        String input = "1,a,3";

        // when & then
        assertThat(org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> calculator.add(input)
        )).hasMessageContaining("숫자");
    }

    @Test
    @DisplayName("int 범위를 초과하는 숫자(2147483648)가 있으면 예외가 발생해야 한다.")
    void number_exceeding_int_max_should_throw_exception() {
        // given
        String input = "2147483648,1"; // Integer.MAX_VALUE + 1

        // when & then
        assertThat(org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> calculator.add(input)
        )).hasMessageContaining("9999");
    }

    @Test
    @DisplayName("앞에 0이 있는 숫자는 정상적으로 처리되어야 한다.")
    void leading_zeros_should_be_handled() {
        // given
        String input = "0001,0002,0003";
        int expected = 6;

        // when
        int result = calculator.add(input);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("000 같은 모든 0으로 구성된 숫자는 0으로 처리되어야 한다.")
    void all_zeros_should_be_treated_as_zero() {
        // given
        String input = "000,1,2";
        int expected = 3;

        // when
        int result = calculator.add(input);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("정확히 999개의 숫자는 정상적으로 처리되어야 한다.")
    void exactly_999_numbers_should_work() {
        // given - 999개의 1을 생성
        String input = String.join(",", java.util.Collections.nCopies(999, "1"));
        int expected = 999;

        // when
        int result = calculator.add(input);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("1000개 이상의 숫자가 입력되면 예외가 발생해야 한다.")
    void more_than_999_numbers_should_throw_exception() {
        // given - 1000개의 숫자 생성
        String input = String.join(",", java.util.Collections.nCopies(1000, "1"));

        // when & then
        assertThat(org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> calculator.add(input)
        )).hasMessageContaining("999");
    }

}
