package com.keigo.practice.first;

import com.keigo.practice.external.ExternalService;

public class Calculator {
    private final ExternalService externalService;

    // コンストラクタで依存注入。モック化しやすくするため。
    public Calculator(ExternalService externalService) {
        this.externalService = externalService;
    }

    public int add(int a, int b) {
        // 外部サービスを使用して値取得、加算する
        return externalService.fetch(a) + externalService.fetch(b);
    }

    public int divide(int a, int b) {
        // 外部の除算ロジックに依存
        return externalService.divide(a, b);
    }
}
