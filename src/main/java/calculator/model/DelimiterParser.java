package calculator.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DelimiterParser {

    // 커스텀 구분자 패턴 정의 : 숫자나 영문자를 제외한 특수 문자 1글자만 허용
    private static final Pattern CUSTOM_DELIMITER_PATTERN = Pattern.compile("^//([^0-9a-zA-Z\\s])\\n(.*)$", Pattern.DOTALL);

    // 반환 타입을 DelimiterParserResult로 변경하고 구분자 추출
    public DelimiterParserResult parse(String text) {
       String numbersString = text;
       String[] customDelimiters = new String[]{};

        if (text == null  || text.isEmpty()) {
            return new DelimiterParserResult("", customDelimiters);
        }

        Matcher m = CUSTOM_DELIMITER_PATTERN.matcher(text);

        if (m.find()) {
            // 커스텀 구분자가 존재하는 경우 :
            String delimiter = m.group(1);  // 커스텀 구분자 (예: ';')
            numbersString = m.group(2); // 숫자 문자열 (예 : '1;2;3)
            customDelimiters = new String[]{delimiter};

            // 커스텀 구분자 처리 시 반환
            return new DelimiterParserResult(numbersString, customDelimiters);
        }
        // 커스텀 구분자가 없는 경우 입력 전체를 배열에 담아 반환
        return new DelimiterParserResult(numbersString, customDelimiters);
    }
}
