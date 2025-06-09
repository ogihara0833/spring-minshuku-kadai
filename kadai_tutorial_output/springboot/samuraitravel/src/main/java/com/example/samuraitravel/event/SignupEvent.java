package com.example.samuraitravel.event; // イベント関連のクラスを含むパッケージ

// SignupEvent クラスは、ユーザーの会員登録時に発生するイベントを管理するクラス

import org.springframework.context.ApplicationEvent; // Springのイベントを扱うクラス

import com.example.samuraitravel.entity.User; // ユーザー情報を扱うクラス

import lombok.Getter; // Lombokを使用して、ゲッター（getter）を自動生成

@Getter // Lombokのアノテーションを使って、フィールドのゲッターを自動生成
public class SignupEvent extends ApplicationEvent { // Springのイベント機能を活用するために ApplicationEvent を継承
    private User user; // 会員登録したユーザー情報を保持
    private String requestUrl; // リクエストURL（例えば、メール認証リンクなど）を保持

    // イベントのコンストラクタ（イベント発生時に必要な情報を設定）
    public SignupEvent(Object source, User user, String requestUrl) {
        super(source); // 親クラス（ApplicationEvent）のコンストラクタを呼び出し

        this.user = user; // ユーザー情報をセット
        this.requestUrl = requestUrl; // リクエストURLをセット
    }
}
