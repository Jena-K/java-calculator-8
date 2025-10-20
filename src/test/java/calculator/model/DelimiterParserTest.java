package calculator.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy; // 예외 테스트에 사용

public class DelimiterParserTest {

    private DelimiterParser parser;

    // 각 테스트 메소드가 실행되기 전 호출되어 테스트에 필요한 객체를 준비.
    @BeforeEach
    void setUp() {
        // Model 계층의 실제 구현 객체를 생성
        parser = new DelimiterParser();
    }

    @Test
    @DisplayName("커스텀 구분자(;)가 있을 경우, 계산할 문자열만 추출해야 함")
    void custom_delimiter_semicolon_should_extract_numbers_string() {
        // given
        String input = "//;\n1;2;3";
        String expectedNumbers = "1;2;3";
        String expectedDelimiter = ";";

        // when
        DelimiterParserResult result = parser.parse(input);

        // then
        assertThat(result.getNumbersString()).isEqualTo(expectedNumbers);
        assertThat(result.getCustomDelimiters()).containsExactly(expectedDelimiter);
    }

    @Test
    @DisplayName("커스텀 구분자(?)가 있을 경우, 계산할 문자열만 추출해야 함")
    void custom_delimiter_question_should_extract_numbers_string() {
        // given
        String input = "//?\n4?5?6";
        String expectedNumbers = "4?5?6";
        String expectedDelimiter = "?";

        // when
        DelimiterParserResult result = parser.parse(input);

        // then
        assertThat(result.getNumbersString()).isEqualTo(expectedNumbers);
        assertThat(result.getCustomDelimiters()).containsExactly(expectedDelimiter);
    }



    @Test
    @DisplayName("커스텀 구분자가 없을 경우, 입력 전체를 반환해야 함")
    void no_custom_delimiter_should_return_all_input() {
        // given
        String input = "1,2:3";

        // when
        DelimiterParserResult result = parser.parse(input);

        // then
        // 현재 parse()는 입력 전체를 반환하므로, 이 테스트는 당장은 성공하지만, 이후 로직 변경 시 다시 실패(RED)할 수 있습니다.
        assertThat(result.getNumbersString()).isEqualTo(input);
        assertThat(result.getCustomDelimiters()).isEmpty();
    }
    @ParameterizedTest
    @ValueSource(strings = {"//1\\n1;2", "//a\\n1;2", "//ABC\\N1;2"})
    @DisplayName("구분자가 숫자나 영문자일 경우 파싱에 실패하고 입력 전체를 반환해야 함")
    void delimiter_should_be_special_charcter(String input) {
        // given
        // 입력값은 ValueSource에서 제공

        // when
        DelimiterParserResult result = parser.parse(input);

        // then
        assertThat(result.getNumbersString()).isEqualTo(input);
        assertThat(result.getCustomDelimiters()).isEmpty();
    }

    @Test
    @DisplayName("커스텀 구분자가 없을 경우, 입력 문자열과 빈 구분자 배열을 반환해야 함")
    void no_custom_delimiter_should_return_default_delimiters() {
        // given
        String input = "1,2:3";

        //when
        DelimiterParserResult result = parser.parse(input);

        // then
        assertThat(result.getNumbersString()).isEqualTo("1,2:3");
        assertThat(result.getCustomDelimiters()).isEmpty();
    }
    @Test
    @DisplayName("커스텀 구분자가 있을 경우, 숫자 문자열과 해당 구분자를 반환해야 함")
    void custom_delimiter_should_return_delimiter_and_numbers_string() {
        // given
        String input = "//;\n1;2;3";

        //when
        DelimiterParserResult result = parser.parse(input);

        //then
        assertThat(result.getNumbersString()).isEqualTo("1;2;3");
        assertThat(result.getCustomDelimiters()).containsExactly(";");
    }



}
