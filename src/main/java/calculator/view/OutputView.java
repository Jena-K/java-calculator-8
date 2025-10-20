package calculator.view;

public class OutputView {

    // 최종 결과를 출력하는 메소드
    public void printResult(int result) {
        System.out.println("결과 : " + result);
    }

    // 예외 발생 시 오류 메시지를 출력하는 메소드
    public void printError(Exception e) {
        System.out.println("[ERROR] " + e.getMessage());
    }
}
