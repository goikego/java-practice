package com.keigo.practice.first;

import com.keigo.practice.external.ExternalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalculatorTest {

    // モック対象
    private ExternalService externalService;
    // テスト対象
    private Calculator calculator;

    @BeforeEach
    void setup() {
        // 外部依存をモックで初期化
        externalService = mock(ExternalService.class);
        // モックを注入してCalculatorを作成
        calculator = new Calculator(externalService);
    }

    @Test
    void test_加算テスト() {
        // モックのfetchメソッドに値を返すように設定
        when(externalService.fetch(2)).thenReturn(10);
        when(externalService.fetch(3)).thenReturn(20);

        // 外部の戻り値を使用して加算
        int result = calculator.add(2, 3);

        assertEquals(30, result);
    }

    @Test
    void test_割り算テスト() {
        // doReturn は when().thenReturn と似ているが、
        // voidメソッドやfinalクラスへの使用に適している（安全に使える）
        doReturn(5).when(externalService).divide(10, 2);

        int result = calculator.divide(10, 2);

        assertEquals(5, result);
    }

    @Test
    void test_割り算テスト_0で割り切れない例外発生() {
        // doThrow を使って divide に例外を投げさせる
        doThrow(new ArithmeticException("Division by zero"))
                .when(externalService).divide(anyInt(), eq(0));

        assertThrows(ArithmeticException.class, () -> calculator.divide(5, 0));
    }

    @Test
    void testFailingCase() {
        // 故意に失敗させるテスト（GitHub Actionsなどでfail動作確認したいときに使う）
        assertEquals(1, 2, "これは意図的に失敗するテストです");
    }
}