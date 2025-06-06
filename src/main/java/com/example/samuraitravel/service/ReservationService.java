package com.example.samuraitravel.service; // サービス関連のクラスを含むパッケージ

// ReservationService クラスは宿泊予約の管理を担当するサービスクラス
// 予約の作成・宿泊人数のチェック・料金計算を行う

import java.time.LocalDate; // チェックイン・チェックアウトの日付を扱うためのクラス
import java.time.temporal.ChronoUnit; // 日付の差分（宿泊日数の計算）を扱うためのクラス
import java.util.Map; // 支払い情報を管理するためのマップ（キーと値のペア）

import org.springframework.stereotype.Service; // Spring のサービスとして登録するためのアノテーション
import org.springframework.transaction.annotation.Transactional; // トランザクション管理を行うためのアノテーション

import com.example.samuraitravel.entity.House; // House（宿泊施設）のエンティティ（データベースと対応するクラス）
import com.example.samuraitravel.entity.Reservation; // Reservation（予約）のエンティティ（データベースと対応するクラス）
import com.example.samuraitravel.entity.User; // User（ユーザー）のエンティティ（予約者）
import com.example.samuraitravel.repository.HouseRepository; // 宿泊施設のデータを管理するリポジトリ
import com.example.samuraitravel.repository.ReservationRepository; // 予約情報のデータを管理するリポジトリ
import com.example.samuraitravel.repository.UserRepository; // ユーザー情報のデータを管理するリポジトリ

@Service // Spring のサービスとして登録（DIコンテナに登録される）
public class ReservationService {
    private final ReservationRepository reservationRepository; // 予約情報のデータを管理するリポジトリ
    private final HouseRepository houseRepository; // 宿泊施設のデータを管理するリポジトリ
    private final UserRepository userRepository; // ユーザー情報のデータを管理するリポジトリ

    // コンストラクタ（クラスの初期化時にリポジトリを受け取る）
    public ReservationService(ReservationRepository reservationRepository, HouseRepository houseRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
    }

    // 宿泊予約の作成処理
    @Transactional // データベースの更新処理をトランザクション管理する
    public void create(Map<String, String> paymentIntentObject) {
        Reservation reservation = new Reservation(); // 新しい予約情報を作成

        // 支払い情報（paymentIntentObject）から宿のIDとユーザーのIDを取得
        Integer houseId = Integer.valueOf(paymentIntentObject.get("houseId"));
        Integer userId = Integer.valueOf(paymentIntentObject.get("userId"));

        // 宿情報とユーザー情報をデータベースから取得
        House house = houseRepository.getReferenceById(houseId);
        User user = userRepository.getReferenceById(userId);

        // チェックイン日とチェックアウト日を取得（文字列を日付型に変換）
        LocalDate checkinDate = LocalDate.parse(paymentIntentObject.get("checkinDate"));
        LocalDate checkoutDate = LocalDate.parse(paymentIntentObject.get("checkoutDate"));

        // 宿泊人数と支払う料金を取得
        Integer numberOfPeople = Integer.valueOf(paymentIntentObject.get("numberOfPeople"));
        Integer amount = Integer.valueOf(paymentIntentObject.get("amount"));

        // 予約情報をセットする
        reservation.setHouse(house);  // 宿の情報を設定
        reservation.setUser(user);  // ユーザー情報を設定
        reservation.setCheckinDate(checkinDate); // チェックイン日を設定
        reservation.setCheckoutDate(checkoutDate); // チェックアウト日を設定
        reservation.setNumberOfPeople(numberOfPeople); // 宿泊人数を設定
        reservation.setAmount(amount); // 支払う料金を設定

        // データベースに予約情報を保存する
        reservationRepository.save(reservation);
    }

    // 宿泊人数が宿の定員を超えていないかチェックするメソッド
    public boolean isWithinCapacity(Integer numberOfPeople, Integer capacity) {     
        return numberOfPeople <= capacity; // 宿泊人数が定員以下なら true（OK）
    }

    // 宿泊料金を計算するメソッド
    public Integer calculateAmount(LocalDate checkinDate, LocalDate checkoutDate, Integer price) {
        // チェックイン日からチェックアウト日までの宿泊日数を計算する
        long numberOfNights = ChronoUnit.DAYS.between(checkinDate, checkoutDate); 

        // 宿泊日数 × 1泊の料金 で合計料金を計算する
        int amount = price * (int)numberOfNights;
        return amount;
    }
}
