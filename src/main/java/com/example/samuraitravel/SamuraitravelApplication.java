package com.example.samuraitravel; // アプリケーションのメインパッケージ

// SamuraitravelApplication クラスは Spring Boot アプリケーションのエントリーポイント
// ここからアプリケーションが起動する

import org.springframework.boot.SpringApplication; // Spring Boot のアプリケーションを起動するためのクラス
import org.springframework.boot.autoconfigure.SpringBootApplication; // Spring Boot の設定を自動構成するためのアノテーション

@SpringBootApplication // Spring Boot アプリケーションとして認識させ、自動構成を有効化
public class SamuraitravelApplication {

    // アプリケーションのメインメソッド（プログラムの開始点）
    public static void main(String[] args) {
        SpringApplication.run(SamuraitravelApplication.class, args); // Spring Boot を起動
    }
}
