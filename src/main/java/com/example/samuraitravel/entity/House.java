package com.example.samuraitravel.entity; // エンティティ（データベースのテーブルと対応するモデル）のパッケージ

// House クラスは宿泊施設情報を管理するエンティティ（データベースのテーブルと対応）

import java.sql.Timestamp; // 日付・時間を扱うためのクラス

import jakarta.persistence.Column; // データベースのカラムを表すためのアノテーション
import jakarta.persistence.Entity; // クラスをデータベースのテーブルと対応付けるためのアノテーション
import jakarta.persistence.GeneratedValue; // 主キーの自動採番を指定するアノテーション
import jakarta.persistence.GenerationType; // 主キーの値の生成戦略を指定するためのクラス
import jakarta.persistence.Id; // 主キー（識別ID）を表すアノテーション
import jakarta.persistence.Table; // テーブル名を指定するアノテーション

import lombok.Data; // Lombok を使用してゲッター・セッターを自動生成（コードを簡潔にできる）

@Entity // このクラスはデータベースのテーブルと対応していることを示す
@Table(name = "houses") // 対応するテーブル名を指定（このクラスは "houses" テーブルと紐づいている）
@Data // ゲッター、セッター、toString() などを自動生成（手動で書く必要なし）
public class House {
    @Id // このフィールドを主キーにする（各レコードを識別するための一意な ID）
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主キーの値を自動採番（データベースの AUTO_INCREMENT 機能を利用）
    @Column(name = "id") // データベースの "id" カラムに対応
    private Integer id;

    @Column(name = "name") // 宿泊施設の名前を保持
    private String name;

    @Column(name = "image_name") // 画像ファイル名を保持（施設の写真など）
    private String imageName;

    @Column(name = "description") // 宿泊施設の説明文を保持
    private String description;

    @Column(name = "price") // 宿泊料金（1泊あたりの料金）を保持
    private Integer price;

    @Column(name = "capacity") // 宿泊施設の定員（最大宿泊可能人数）を保持
    private Integer capacity;

    @Column(name = "postal_code") // 宿泊施設の郵便番号を保持
    private String postalCode;

    @Column(name = "address") // 宿泊施設の住所を保持
    private String address;

    @Column(name = "phone_number") // 施設の電話番号を保持
    private String phoneNumber;

    @Column(name = "created_at", insertable = false, updatable = false) // 作成日時（データベース側で自動設定）
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false) // 更新日時（データベース側で自動設定）
    private Timestamp updatedAt;
}
