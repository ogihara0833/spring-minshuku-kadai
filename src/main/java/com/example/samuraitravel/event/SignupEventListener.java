package com.example.samuraitravel.event; // イベント関連のクラスを含むパッケージ

// SignupEventListener クラスは、ユーザーの会員登録時に認証メールを送信するためのイベントリスナー

import java.util.UUID; // 一意の認証トークンを生成するためのクラス

import org.springframework.context.event.EventListener; // Springのイベントを処理するためのアノテーション
import org.springframework.mail.SimpleMailMessage; // シンプルなメールメッセージを作成するためのクラス
import org.springframework.mail.javamail.JavaMailSender; // メール送信を行うためのクラス
import org.springframework.stereotype.Component; // Springのコンポーネントとして登録するためのアノテーション

import com.example.samuraitravel.entity.User; // ユーザー情報を扱うクラス
import com.example.samuraitravel.service.VerificationTokenService; // 認証トークンを管理するサービス

@Component // Springのコンポーネントとして登録（DIコンテナに登録される）
public class SignupEventListener {
    private final VerificationTokenService verificationTokenService; // 認証トークンを管理するサービス
    private final JavaMailSender javaMailSender; // メール送信を担当するオブジェクト

    // コンストラクタ（クラスが作られるときに必要な情報を受け取る）
    public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
        this.verificationTokenService = verificationTokenService;        
        this.javaMailSender = mailSender;
    }

    // ユーザー登録イベントが発生したときに実行される処理
    @EventListener // Springのイベントリスナー（SignupEventが発生するとこのメソッドが実行される）
    private void onSignupEvent(SignupEvent signupEvent) {
        User user = signupEvent.getUser(); // 登録されたユーザー情報を取得
        String token = UUID.randomUUID().toString(); // 一意の認証トークンを生成
        verificationTokenService.create(user, token); // ユーザーに認証トークンを紐付けて保存

        // メール送信のための情報を設定
        String recipientAddress = user.getEmail(); // ユーザーのメールアドレスを取得
        String subject = "メール認証"; // メールの件名
        String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token; // 認証用URLを作成
        String message = "以下のリンクをクリックして会員登録を完了してください。"; // メール本文

        // メールメッセージを作成
        SimpleMailMessage mailMessage = new SimpleMailMessage(); 
        mailMessage.setTo(recipientAddress); // 送信先のメールアドレスを設定
        mailMessage.setSubject(subject); // 件名を設定
        mailMessage.setText(message + "\n" + confirmationUrl); // メール本文を設定（認証リンクを含む）

        // メールを送信
        javaMailSender.send(mailMessage);
    }
}
