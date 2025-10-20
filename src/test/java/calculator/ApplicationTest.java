package calculator;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * ApplicationTest - 통합 테스트 (콘솔 입출력 테스트)
 *
 * NsTest를 상속받아 콘솔 입출력을 모의하는 통합 테스트입니다.
 *
 * 주의사항:
 * - NsTest의 run() 메서드는 Console.readLine()을 한 번만 호출하므로 한 줄 입력만 지원합니다.
 * - 커스텀 구분자(//[구분자]\n[숫자])는 실제 개행문자가 필요하지만,
 *   NsTest의 run("//;\n1;2;3")은 문자열 "\n"을 리터럴로 전달하므로 작동하지 않습니다.
 * - 따라서 커스텀 구분자 기능은 단위 테스트(StringCalculatorTest)에서만 검증합니다.
 */
class ApplicationTest extends NsTest {

    private void assertSimpleTest(Runnable test) {
        test.run();
    }

    // 기본 기능 테스트 (한 줄 입력으로 가능한 경우)

    @Test
    @DisplayName("단일 숫자 입력 시 해당 숫자를 출력해야 한다.")
    void single_number_should_return_itself() {
        assertSimpleTest(() -> {
            run("5");
            assertThat(output()).contains("결과 : 5");
        });
    }

    @Test
    @DisplayName("쉼표 구분자로 숫자를 입력하면 합계를 출력해야 한다.")
    void comma_delimiter_should_return_sum() {
        assertSimpleTest(() -> {
            run("1,2,3");
            assertThat(output()).contains("결과 : 6");
        });
    }

    @Test
    @DisplayName("콜론 구분자로 숫자를 입력하면 합계를 출력해야 한다.")
    void colon_delimiter_should_return_sum() {
        assertSimpleTest(() -> {
            run("1:2:3");
            assertThat(output()).contains("결과 : 6");
        });
    }

    @Test
    @DisplayName("쉼표와 콜론을 혼합하여 사용할 수 있어야 한다.")
    void mixed_delimiters_should_return_sum() {
        assertSimpleTest(() -> {
            run("1,2:3");
            assertThat(output()).contains("결과 : 6");
        });
    }

    // 예외 처리 테스트

    @Test
    @DisplayName("맨 뒤에 구분자가 있으면 예외가 발생해야 한다.")
    void trailing_delimiter_should_throw_exception() {
        assertSimpleTest(() ->
            assertThatThrownBy(() -> runException("1,2,"))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("10000 이상의 숫자가 있으면 예외가 발생해야 한다.")
    void number_over_10000_should_throw_exception() {
        assertSimpleTest(() ->
            assertThatThrownBy(() -> runException("10000,1"))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("숫자가 아닌 문자가 포함되면 예외가 발생해야 한다.")
    void non_numeric_should_throw_exception() {
        assertSimpleTest(() ->
            assertThatThrownBy(() -> runException("1,a,3"))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}

