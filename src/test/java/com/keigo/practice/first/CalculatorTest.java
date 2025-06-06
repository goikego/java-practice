package com.keigo.practice.first;

import com.keigo.practice.external.ExternalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @ParameterizedTest
    @CsvSource({
     "サッカー, , これは秘密のサッカーだ",
     " , 野球, これは秘密の野球だ"
    })
    void test_executeを呼んだ場合_パラメータに従って正常終了(String a, String b, String expected) throws IllegalAccessException {
        // When
        String actuall = calculator.execute(a, b);

        // Then
        assertEquals(expected, actuall, "メッセージが一致しない");
    }

    @ParameterizedTest
    @CsvSource({
            "サッカー, 野球",
            "abc ,wyz",
            "1, 2",
            "'', ''",
            " , ",
            "'', ",
            " , ''",
            "' ', ''",
            "'', ' '",
            "' ', ' '"
    })
    void test_executeを呼んだ場合_異常終了(String a, String b) {
        // When & Then
        assertThrows(NullPointerException.class, () ->calculator.execute(a, b));
    }

    @Test
    void test_call_両方nullの場合は例外() throws Exception {
        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
            Method method = Calculator.class.getDeclaredMethod("call", String.class, String.class);
            method.setAccessible(true);
            method.invoke(calculator, null, null);
        });
        assertTrue(ex.getCause() instanceof IllegalAccessException);        ;
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
}