package com.example.samuraitravel.repository; // リポジトリ関連のクラスを含むパッケージ

// VerificationTokenRepository クラスは、ユーザーの認証トークンを管理するリポジトリ
// JpaRepository を継承することで、基本的な CRUD 操作（作成・読み取り・更新・削除）が簡単に行える

import org.springframework.data.jpa.repository.JpaRepository; // Spring Data JPAのリポジトリ機能を利用するためのクラス

import com.example.samuraitravel.entity.VerificationToken; // VerificationToken（認証トークン）のエンティティ（データベースと対応するクラス）

// JpaRepository を継承して認証トークンのデータ操作を行う
// JpaRepository<エンティティのクラス型, 主キーのデータ型>
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

    // 指定されたトークンを持つ VerificationToken エンティティを検索
    public VerificationToken findByToken(String token);
}

// JpaRepository を継承することで、基本的な CRUD 操作（作成・読み取り・更新・削除）が可能
// 例えば findAll() を使うことで、データベース内のすべての認証トークン情報を取得できる
