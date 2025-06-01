package com.keigo.practice.first;

import com.keigo.practice.external.ExternalService;
import org.apache.commons.lang3.StringUtils;

public class Calculator {
    private final ExternalService externalService;

    public String execute(String a, String b) throws IllegalAccessException {
        check(a, b);
        String message = call(a, b);
        return message;
    }

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

    private void check(String a, String b) {
        if(StringUtils.isNotBlank(a) && StringUtils.isNotBlank(b)) {
            throw new NullPointerException("両方に値が入っています");
        }
        if(!StringUtils.isNotBlank(a) && !StringUtils.isNotBlank(b)) {
            throw new NullPointerException("どちらにも値がありません");
        }
    }

    private String call(String a, String b) throws IllegalAccessException {
        String key = null;
        if(StringUtils.isNotEmpty(a)) {
            key = a;
        }
        else if(StringUtils.isNotEmpty(b)) {
            key = b;
        }
        else {
            throw new IllegalAccessException("aとbの両方が空です");
        }
        return serch(key);
    }

    private String serch(String key) {
        return String.format("これは秘密の%sだ", key);
    }

}
