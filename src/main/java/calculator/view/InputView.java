package calculator.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    // 문자열 입력 받는 메소드
    public String getInput() {
        System.out.println("덧셈할 문자열을 입력해 주세요.");
        return Console.readLine();
    }

}
