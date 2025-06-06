package com.example.samuraitravel.controller;
// "controller" は「操作する人・もの」という意味。
// このクラスは「Stripeの決済完了の通知(Webhook)を受け取るためのコントローラ」です！

import org.springframework.beans.factory.annotation.Value; // 設定ファイルから値を読み込むための機能
import org.springframework.http.HttpStatus; // HTTPのステータスコードを扱うための機能（成功・失敗など）
import org.springframework.http.ResponseEntity; // HTTPレスポンスを作るための機能
import org.springframework.stereotype.Controller; // このクラスが「コントローラ」だとSpringに伝える
import org.springframework.web.bind.annotation.PostMapping; // Webから「POSTリクエスト」を受け付けるための設定
import org.springframework.web.bind.annotation.RequestBody; // リクエストの「本文（payload）」を受け取るための設定
import org.springframework.web.bind.annotation.RequestHeader; // リクエストの「ヘッダー情報」を受け取るための設定

import com.example.samuraitravel.service.StripeService; // Stripe関連の処理を担当するクラスを使う
import com.stripe.Stripe; // StripeのAPIを使うためのライブラリ
import com.stripe.exception.SignatureVerificationException; // Webhookの署名が正しいかチェックするための機能
import com.stripe.model.Event; // Stripeから届く「決済完了のイベント（通知）」を表すクラス
import com.stripe.net.Webhook; // StripeのWebhook機能を使うためのライブラリ

@Controller // このクラスが「Webのリクエストを受け取る役割」であることをSpringに教える
public class StripeWebhookController { // クラスの開始
    private final StripeService stripeService; // Stripeの処理をまとめたクラスを使うための準備

    @Value("${stripe.api-key}") // 設定ファイルから「StripeのAPIキー」を読み込む
    private String stripeApiKey;

    @Value("${stripe.webhook-secret}") // 設定ファイルから「Webhookの秘密キー」を読み込む
    private String webhookSecret;

    // コンストラクタ（このクラスが作られるときに実行される）
    public StripeWebhookController(StripeService stripeService) {
        this.stripeService = stripeService; // Stripeの処理を担当するクラスを使えるようにする
    }

    // Webhook（Stripeからの決済完了通知）を受け取る
    @PostMapping("/stripe/webhook") // 「/stripe/webhook」というURLにPOSTリクエストが送られたときにこのメソッドを実行する
    public ResponseEntity<String> webhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Stripe.apiKey = stripeApiKey; // StripeのAPIキーをセット
        Event event = null; // Stripeのイベント（決済通知）を入れる変数を準備

        try {
            // Stripeから届いたデータ（payload）が正しいものかチェックする
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            // もし署名が間違っていたら「400 Bad Request（リクエストが間違っている）」を返す
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Stripeのイベントが「決済完了（checkout.session.completed）」だった場合
        if ("checkout.session.completed".equals(event.getType())) {
            stripeService.processSessionCompleted(event); // 予約を処理するメソッドを呼び出す
        }
        
        // 成功したことを知らせるレスポンス「200 OK」を返す
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
