package com.keigo.practice.external;


// 外部依存のインタフェース。例えば外部APIやDBなど、
// テスト対象ではないロジックを抽象化して切り離すために使う。
public interface ExternalService {
    // 入力値を使って何らかの値を返す処理（例：DB参照）
    int fetch(int input);

    // 割り算処理（例：外部APIで計算）
    int divide(int a, int b);
}

