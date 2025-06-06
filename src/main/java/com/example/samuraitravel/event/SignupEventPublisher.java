package com.example.samuraitravel.event; // イベント関連のクラスを含むパッケージ

// SignupEventPublisher クラスは、ユーザーの会員登録イベントを発行するためのクラス
// これにより、ユーザー登録後に必要な処理（メール送信など）を他のクラスで処理できるようになる

import org.springframework.context.ApplicationEventPublisher; // Springのイベントを発行するためのクラス
import org.springframework.stereotype.Component; // Springのコンポーネントとして登録するためのアノテーション

import com.example.samuraitravel.entity.User; // ユーザー情報を扱うクラス

@Component // Springのコンポーネントとして登録（DIコンテナに登録される）
public class SignupEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher; // イベント発行を担当するオブジェクト
    
    // コンストラクタ（クラスの初期化時にイベント発行のオブジェクトを受け取る）
    public SignupEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;                
    }
    
    // ユーザー登録イベントを発行するメソッド
    public void publishSignupEvent(User user, String requestUrl) {
        // SignupEvent のインスタンスを作成し、イベントを発行
        applicationEventPublisher.publishEvent(new SignupEvent(this, user, requestUrl));
    }
}
