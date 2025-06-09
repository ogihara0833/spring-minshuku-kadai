package com.example.samuraitravel.entity; // エンティティ（データベースのテーブルと対応するモデル）のパッケージ

// VerificationToken クラスは、ユーザーの認証・アカウント有効化を管理するエンティティ（データベースの "verification_tokens" テーブルと対応）

import java.sql.Timestamp; // 日付・時間（作成日時・更新日時など）を扱うためのクラス

import jakarta.persistence.Column; // データベースのカラムを表すためのアノテーション
import jakarta.persistence.Entity; // クラスをデータベースのテーブルと対応付けるためのアノテーション
import jakarta.persistence.GeneratedValue; // 主キーの自動採番を指定するアノテーション
import jakarta.persistence.GenerationType; // 主キーの値の生成戦略を指定するためのクラス
import jakarta.persistence.Id; // 主キー（識別ID）を表すアノテーション
import jakarta.persistence.JoinColumn; // 外部キーのカラムを指定するアノテーション
import jakarta.persistence.OneToOne; // 1対1の関係（1人のユーザーに対して1つの認証トークン）を表すアノテーション
import jakarta.persistence.Table; // テーブル名を指定するアノテーション

import lombok.Data; // Lombok を使用してゲッター・セッターを自動生成（コードを簡潔にできる）

@Entity // このクラスはデータベースのテーブルと対応していることを示す
@Table(name = "verification_tokens") // 対応するテーブル名を指定（このクラスは "verification_tokens" テーブルと紐づいている）
@Data // Lombok を使用して、ゲッター、セッター、toString() などを自動生成（手動で書く必要なし）
public class VerificationToken {    
    @Id // このフィールドを主キーにする（各レコードを識別するための一意な ID）
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主キーの値を自動採番（データベースの AUTO_INCREMENT 機能を利用）
    @Column(name = "id") // データベースの "id" カラムに対応
    private Integer id;

    @OneToOne // 1人のユーザー（User）に対して1つの認証トークンが割り当てられる関係
    @JoinColumn(name = "user_id") // ユーザー ID（外部キー）を設定（ユーザーの認証と紐付け）
    private User user;

    @Column(name = "token") // 認証トークン（アカウント有効化リンクなどで使用）
    private String token;

    @Column(name = "created_at", insertable = false, updatable = false) // 作成日時（データベース側で自動設定）
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false) // 更新日時（データベース側で自動設定）
    private Timestamp updatedAt;
}
