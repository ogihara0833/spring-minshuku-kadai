package com.example.samuraitravel.entity; // エンティティ（データベースのテーブルと対応するモデル）のパッケージ

// Reservation クラスは予約情報を管理するエンティティ（データベースのテーブルと対応）

import java.sql.Timestamp; // 日付・時間（作成日時・更新日時など）を扱うためのクラス
import java.time.LocalDate; // チェックイン・チェックアウトの日付を扱うためのクラス

import jakarta.persistence.Column; // データベースのカラムを表すためのアノテーション
import jakarta.persistence.Entity; // クラスをデータベースのテーブルと対応付けるためのアノテーション
import jakarta.persistence.GeneratedValue; // 主キーの自動採番を指定するアノテーション
import jakarta.persistence.GenerationType; // 主キーの値の生成戦略を指定するためのクラス
import jakarta.persistence.Id; // 主キー（識別ID）を表すアノテーション
import jakarta.persistence.JoinColumn; // 外部キーのカラムを指定するアノテーション
import jakarta.persistence.ManyToOne; // 多対一（複数の予約が 1つの宿泊施設、1人のユーザーに紐づく）を表すアノテーション
import jakarta.persistence.Table; // テーブル名を指定するアノテーション

import lombok.Data; // Lombok を使用してゲッター・セッターを自動生成（コードを簡潔にできる）

@Entity // このクラスはデータベースのテーブルと対応していることを示す
@Table(name = "reservations") // 対応するテーブル名を指定（このクラスは "reservations" テーブルと紐づいている）
@Data // ゲッター、セッター、toString() などを自動生成（手動で書く必要なし）
public class Reservation {
    @Id // このフィールドを主キーにする（各レコードを識別するための一意な ID）
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主キーの値を自動採番（データベースの AUTO_INCREMENT 機能を利用）
    @Column(name = "id") // データベースの "id" カラムに対応
    private Integer id;

    @ManyToOne // 1つの宿泊施設（House）に対して複数の予約（Reservation）がある関係
    @JoinColumn(name = "house_id") // 宿泊施設 ID（外部キー）を設定
    private House house;

    @ManyToOne // 1人のユーザー（User）が複数の予約（Reservation）を持てる関係
    @JoinColumn(name = "user_id") // ユーザー ID（外部キー）を設定
    private User user;

    @Column(name = "checkin_date") // 宿泊開始日（チェックイン日）
    private LocalDate checkinDate;

    @Column(name = "checkout_date") // 宿泊終了日（チェックアウト日）
    private LocalDate checkoutDate;

    @Column(name = "number_of_people") // 宿泊人数
    private Integer numberOfPeople;

    @Column(name = "amount") // 予約金額（宿泊料金）
    private Integer amount;

    @Column(name = "created_at", insertable = false, updatable = false) // 作成日時（データベース側で自動設定）
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false) // 更新日時（データベース側で自動設定）
    private Timestamp updatedAt;
}
