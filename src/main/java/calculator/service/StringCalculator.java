package calculator.service;

import calculator.model.DelimiterParser;
import calculator.model.DelimiterParserResult;
import calculator.model.InputValidator;

import java.util.regex.Pattern;

public class StringCalculator {

    private final DelimiterParser parser;
    private final InputValidator validator;

    public StringCalculator() {
        this.parser = new DelimiterParser();
        this.validator = new InputValidator();
    }

    // 문자열을 입력받아 숫자를 추출하고 합을 반환하는 메소드
    public int add(String text) {
        // 빈 문자열 처리
        if (text == null || text.isEmpty()) {
            return 0;
        }

        // 입력 구조 검증
        validator.validateInputStructure(text);

        DelimiterParserResult parsedResult = parser.parse(text);
        return sum(parsedResult);

    }
    private int sum(DelimiterParserResult result) {
        String numbersString = result.getNumbersString();

        // 기본 구분자 정규식 (쉼표, 콜론)
        String delimiterRegex = "[,:]";

        // 커스텀 구분자가 있으면 정규식에 추가 (예: "[,:]|-")
        if (result.getCustomDelimiters().length > 0) {
            String customDelimiter = Pattern.quote(result.getCustomDelimiters()[0]);
            delimiterRegex += "|" + customDelimiter;
        }

        // 문자열을 쉼표(,) 기분으로 분리
        String[] numbers = numbersString.split(delimiterRegex);

        // 숫자 개수 제한 검증 (999개까지 허용, 1000개 이상 불가)
        if (numbers.length >= 1000) {
            throw new IllegalArgumentException("숫자는 999개까지 입력할 수 있습니다. 입력된 개수: " + numbers.length);
        }

        int totalSum = 0;
        // 분리된 문자열 배열을 순회하며 계산

        for (String number : numbers) {
            validator.validateNumber(number);
            totalSum += Integer.parseInt(number);
        }
        return totalSum;
    }
}
