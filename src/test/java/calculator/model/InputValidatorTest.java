package calculator.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;  // 예외 테스트에 사용

public class InputValidatorTest {

    private InputValidator validator;

    @BeforeEach
    void setUp() {
        // Model 계층의 실제 구현 객체를 생성
        validator = new InputValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "!", "100.5", "3000000000"})
    @DisplayName("숫자 외 문자 또는 잘못된 형식 입력 시 예외를 발생시켜야 한다.")
    void non_numeric_should_throw_exception(String input) {
        // given : 입력값 ValueSource에서 제공

        // when & then
        // NumberFormatException 발생 시 RuntimeException으로 래핑하지 않아 실패 유도
        assertThatThrownBy(() -> validator.validateNumber(input))
                .isInstanceOf(RuntimeException.class);

    }
    // int 범위 초과 검증 시 assertThat를 사용해 성공 케이스 검증 예정


}