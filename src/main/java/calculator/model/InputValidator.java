package calculator.model;

import java.util.Objects;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

// 모든 유효성 검사 로직을 담는 클래스
public class InputValidator {

    private static final Pattern CUSTOM_DELIMITER_PATTERN = Pattern.compile("^//([^0-9a-zA-Z\\s])\\n(.*)$", Pattern.DOTALL);

    // 입력 문자열 전체 구조 검증
    public void validateInputStructure(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }

        // 커스텀 구분자 형식인지 확인
        if (text.startsWith("//")) {
            validateCustomDelimiterStructure(text);
        } else {
            validateBasicStructure(text);
        }
    }

    // 커스텀 구분자 형식 검증
    private void validateCustomDelimiterStructure(String text) {
        if (!CUSTOM_DELIMITER_PATTERN.matcher(text).matches()) {
            throw new IllegalArgumentException("커스텀 구분자 형식이 올바르지 않습니다. 형식: //[구분자]\\n[숫자]");
        }

        // 커스텀 구분자 추출 및 검증
        String delimiter = text.substring(2, 3);
        if (delimiter.matches("[0-9a-zA-Z\\s]")) {
            throw new IllegalArgumentException("구분자는 특수문자만 허용됩니다. 입력된 구분자: " + delimiter);
        }

        String numbersString = text.substring(4); // "//;\n" 이후 문자열

        // 맨 앞/뒤는 반드시 숫자여야 함 (어떤 특수문자든 불가)
        if (!numbersString.matches("^\\d.*\\d$") && !numbersString.matches("^\\d$")) {
            throw new IllegalArgumentException("문자열의 맨 앞이나 맨 뒤에 구분자가 올 수 없습니다.");
        }

        // 연속된 구분자 검증 (기본 구분자 + 커스텀 구분자 모든 조합)
        String allDelimiters = "[" + Pattern.quote(delimiter) + ",:]";
        if (numbersString.matches(".*" + allDelimiters + "{2,}.*")) {
            throw new IllegalArgumentException("연속된 구분자는 허용되지 않습니다.");
        }
    }

    // 기본 구분자 형식 검증
    private void validateBasicStructure(String text) {
        validateNumbersStructure(text, "[,:]");
    }

    // 숫자 문자열 구조 검증 (구분자 위치, 연속된 구분자 등)
    private void validateNumbersStructure(String text, String delimiterRegex) {
        // 맨 앞이나 맨 뒤에 구분자가 있는지 검증
        if (text.matches("^" + delimiterRegex + ".*") || text.matches(".*" + delimiterRegex + "$")) {
            throw new IllegalArgumentException("문자열의 맨 앞이나 맨 뒤에 구분자가 올 수 없습니다.");
        }

        // 연속된 구분자 검증
        if (text.matches(".*" + delimiterRegex + "{2,}.*")) {
            throw new IllegalArgumentException("연속된 구분자는 허용되지 않습니다.");
        }
    }

    // 숫자로 분리된 문자열 목록을 받아 유효성 검사
    public void validateNumbers(List<String> numbers) {
        for (String number : numbers) {
            validateNumber(number);
        }
    }

    public void validateNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            throw new IllegalArgumentException("빈 숫자는 허용되지 않습니다.");
        }

        // 앞의 0 제거 (예: "0001" -> "1", "000" -> "")
        String trimmedNumber = number.replaceFirst("^0+", "");

        // 모두 0이었던 경우 빈 문자열이 되므로 "0"으로 처리
        if (trimmedNumber.isEmpty()) {
            trimmedNumber = "0";
        }

        // 문자열 길이가 5 이상이면 10000 이상의 수이므로 범위 초과
        if (trimmedNumber.length() >= 5) {
            throw new IllegalArgumentException("숫자는 9999를 초과할 수 없습니다. 입력된 값: " + number);
        }

        try {
            int num = Integer.parseInt(number);

            // 9999 초과 검증 (문자열 길이로 대부분 걸러지지만 이중 체크)
            if (num > 9999) {
                throw new IllegalArgumentException("숫자는 9999를 초과할 수 없습니다. 입력된 값: " + number);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("유효하지 않은 숫자 형식입니다. 입력된 값: " + number);
        }
    }
}
