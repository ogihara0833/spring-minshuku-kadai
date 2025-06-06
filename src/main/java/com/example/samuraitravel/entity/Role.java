package com.example.samuraitravel.entity; // エンティティ（データベースのテーブルと対応するモデル）のパッケージ

// Role クラスはユーザーの役割（管理者、一般ユーザーなど）を管理するエンティティ（データベースのテーブルと対応）

import jakarta.persistence.Column; // データベースのカラムを表すためのアノテーション
import jakarta.persistence.Entity; // クラスをデータベースのテーブルと対応付けるためのアノテーション
import jakarta.persistence.GeneratedValue; // 主キーの自動採番を指定するアノテーション
import jakarta.persistence.GenerationType; // 主キーの値の生成戦略を指定するためのクラス
import jakarta.persistence.Id; // 主キー（識別ID）を表すアノテーション
import jakarta.persistence.Table; // テーブル名を指定するアノテーション

import lombok.Data; // Lombok を使用してゲッター・セッターを自動生成（コードを簡潔にできる）

@Entity // このクラスはデータベースのテーブルと対応していることを示す
@Table(name = "roles") // 対応するテーブル名を指定（このクラスは "roles" テーブルと紐づいている）
@Data // Lombok を使用して、ゲッター、セッター、toString() などを自動生成（手動で書く必要なし）
public class Role {
    @Id // このフィールドを主キーにする（各レコードを識別するための一意な ID）
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主キーの値を自動採番（データベースの AUTO_INCREMENT 機能を利用）
    @Column(name = "id") // データベースの "id" カラムに対応
    private Integer id;

    @Column(name = "name") // 役割の名前（例: "ADMIN", "USER" など）
    private String name;
}
