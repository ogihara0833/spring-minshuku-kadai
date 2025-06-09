package com.example.samuraitravel.entity; // エンティティ（データベースのテーブルと対応するモデル）のパッケージ

// User クラスはユーザー情報を管理するエンティティ（データベースの "users" テーブルと対応）

import java.sql.Timestamp; // 作成日時・更新日時を扱うためのクラス

import jakarta.persistence.Column; // データベースのカラムを表すためのアノテーション
import jakarta.persistence.Entity; // クラスをデータベースのテーブルと対応付けるためのアノテーション
import jakarta.persistence.GeneratedValue; // 主キーの自動採番を指定するアノテーション
import jakarta.persistence.GenerationType; // 主キーの値の生成戦略を指定するためのクラス
import jakarta.persistence.Id; // 主キー（識別ID）を表すアノテーション
import jakarta.persistence.JoinColumn; // 外部キーのカラムを指定するアノテーション
import jakarta.persistence.ManyToOne; // 多対一（複数のユーザーが1つの役割に紐づく）を表すアノテーション
import jakarta.persistence.Table; // テーブル名を指定するアノテーション

import lombok.Data; // Lombok を使用してゲッター・セッターを自動生成（コードを簡潔にできる）

@Entity // このクラスはデータベースのテーブルと対応していることを示す
@Table(name = "users") // 対応するテーブル名を指定（このクラスは "users" テーブルと紐づいている）
@Data // Lombok を使用して、ゲッター、セッター、toString() などを自動生成（手動で書く必要なし）
public class User {
    @Id // このフィールドを主キーにする（各レコードを識別するための一意な ID）
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主キーの値を自動採番（データベースの AUTO_INCREMENT 機能を利用）
    @Column(name = "id") // データベースの "id" カラムに対応
    private Integer id;

    @Column(name = "name") // ユーザーの名前を保持
    private String name;

    @Column(name = "furigana") // ユーザーのフリガナ（読み仮名）を保持
    private String furigana;

    @Column(name = "postal_code") // 郵便番号を保持
    private String postalCode;

    @Column(name = "address") // ユーザーの住所を保持
    private String address;

    @Column(name = "phone_number") // ユーザーの電話番号を保持
    private String phoneNumber;

    @Column(name = "email") // ユーザーのメールアドレスを保持
    private String email;

    @Column(name = "password") // ユーザーのパスワードを保持（暗号化が推奨される）
    private String password;

    @ManyToOne // 1つの役割（Role）に対して複数のユーザーが紐づく関係
    @JoinColumn(name = "role_id") // 役割 ID（外部キー）を設定（管理者・一般ユーザーなどの役割を管理）
    private Role role;

    @Column(name = "enabled") // アカウントが有効かどうか（true = 有効, false = 無効）
    private Boolean enabled;

    @Column(name = "created_at", insertable = false, updatable = false) // 作成日時（データベース側で自動設定）
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false) // 更新日時（データベース側で自動設定）
    private Timestamp updatedAt;
}
