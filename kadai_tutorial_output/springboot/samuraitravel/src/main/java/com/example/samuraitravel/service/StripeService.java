package com.example.samuraitravel.service; // サービス関連のクラスを含むパッケージ

// StripeService クラスは、Stripe を使った決済処理を管理するサービスクラス
// 宿泊予約の支払い処理を Stripe API を通じて行う

import java.util.Map; // 支払い情報を管理するためのマップ（キーと値のペア）
import java.util.Optional; // Stripe のイベントデータを安全に取得するためのクラス

import jakarta.servlet.http.HttpServletRequest; // HTTPリクエスト情報を取得するためのクラス

import org.springframework.beans.factory.annotation.Value; // 設定ファイルから値を取得するためのアノテーション
import org.springframework.stereotype.Service; // Spring のサービスとして登録するためのアノテーション

import com.example.samuraitravel.form.ReservationRegisterForm; // 宿泊予約の登録フォーム
import com.stripe.Stripe; // Stripe API を扱うためのクラス
import com.stripe.exception.StripeException; // Stripe API の例外処理を行うためのクラス
import com.stripe.model.Event; // Stripe のイベントデータを扱うためのクラス
import com.stripe.model.StripeObject; // Stripe のオブジェクトデータを扱うためのクラス
import com.stripe.model.checkout.Session; // Stripe の決済セッションを扱うためのクラス
import com.stripe.param.checkout.SessionCreateParams; // Stripe の決済セッションを作成するためのクラス
import com.stripe.param.checkout.SessionRetrieveParams; // Stripe の決済セッションを取得するためのクラス

@Service // Spring のサービスとして登録（DIコンテナに登録される）
public class StripeService {
    @Value("${stripe.api-key}") // 設定ファイルから Stripe API キーを取得
    private String stripeApiKey;

    private final ReservationService reservationService; // 予約情報を管理するサービス

    // コンストラクタ（クラスの初期化時に予約サービスを受け取る）
    public StripeService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }    

    // Stripe の決済セッションを作成し、決済ページの URL を返す
    public String createStripeSession(String houseName, ReservationRegisterForm reservationRegisterForm, HttpServletRequest httpServletRequest) {
        Stripe.apiKey = stripeApiKey; // Stripe API キーを設定
        String requestUrl = new String(httpServletRequest.getRequestURL()); // 現在のリクエスト URL を取得

        // Stripe の決済セッションを作成するためのパラメータを設定
        SessionCreateParams params =
            SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD) // クレジットカード決済を許可
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()   
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(houseName) // 宿泊施設名を設定
                                        .build())
                                .setUnitAmount((long)reservationRegisterForm.getAmount()) // 宿泊料金を設定
                                .setCurrency("jpy") // 通貨を日本円に設定
                                .build())
                        .setQuantity(1L) // 数量を1に設定
                        .build())
                .setMode(SessionCreateParams.Mode.PAYMENT) // 支払いモードを設定
                .setSuccessUrl(requestUrl.replaceAll("/houses/[0-9]+/reservations/confirm", "") + "/reservations?reserved") // 支払い成功時のリダイレクト先
                .setCancelUrl(requestUrl.replace("/reservations/confirm", "")) // 支払いキャンセル時のリダイレクト先
                .setPaymentIntentData(
                    SessionCreateParams.PaymentIntentData.builder()
                        .putMetadata("houseId", reservationRegisterForm.getHouseId().toString()) // 宿泊施設 ID を設定
                        .putMetadata("userId", reservationRegisterForm.getUserId().toString()) // ユーザー ID を設定
                        .putMetadata("checkinDate", reservationRegisterForm.getCheckinDate()) // チェックイン日を設定
                        .putMetadata("checkoutDate", reservationRegisterForm.getCheckoutDate()) // チェックアウト日を設定
                        .putMetadata("numberOfPeople", reservationRegisterForm.getNumberOfPeople().toString()) // 宿泊人数を設定
                        .putMetadata("amount", reservationRegisterForm.getAmount().toString()) // 宿泊料金を設定
                        .build())
                .build();

        try {
            Session session = Session.create(params); // Stripe の決済セッションを作成
            return session.getId(); // セッション ID を返す
        } catch (StripeException e) {
            e.printStackTrace();
            return ""; // エラー発生時は空文字を返す
        }
    } 

    // Stripe の決済完了イベントを処理し、予約情報をデータベースに登録する
    public void processSessionCompleted(Event event) {
        Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
        optionalStripeObject.ifPresentOrElse(stripeObject -> {
            Session session = (Session)stripeObject;
            SessionRetrieveParams params = SessionRetrieveParams.builder().addExpand("payment_intent").build();

            try {
                session = Session.retrieve(session.getId(), params, null);
                Map<String, String> paymentIntentObject = session.getPaymentIntentObject().getMetadata();
                reservationService.create(paymentIntentObject); // 予約情報をデータベースに登録
            } catch (StripeException e) {
                e.printStackTrace();
            }
            System.out.println("予約一覧ページの登録処理が成功しました。");
        },
        () -> {
            System.out.println("予約一覧ページの登録処理が失敗しました。");
        });
    }
}
