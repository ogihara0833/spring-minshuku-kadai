package com.example.samuraitravel.repository; // リポジトリ関連のクラスを含むパッケージ

// UserRepository クラスはユーザー情報を管理するリポジトリ
// JpaRepository を継承することで、基本的な CRUD 操作（作成・読み取り・更新・削除）が簡単に行える

import org.springframework.data.domain.Page; // ページネーション（ページ分割表示）のためのクラス
import org.springframework.data.domain.Pageable; // ページ情報（ページ番号、サイズなど）を扱うクラス
import org.springframework.data.jpa.repository.JpaRepository; // Spring Data JPAのリポジトリ機能を利用するためのクラス

import com.example.samuraitravel.entity.User; // User（ユーザー）のエンティティ（データベースと対応するクラス）

// JpaRepository を継承してユーザーデータの操作を行う
// JpaRepository<エンティティのクラス型, 主キーのデータ型>
public interface UserRepository extends JpaRepository<User, Integer> {

    // 指定されたメールアドレスを持つユーザーを検索
    public User findByEmail(String email);

    // 氏名またはフリガナに特定の文字が含まれるユーザーを検索（ページネーション対応）
    public Page<User> findByNameLikeOrFuriganaLike(String nameKeyword, String furiganaKeyword, Pageable pageable);
}

// JpaRepository を継承することで、基本的な CRUD 操作（作成・読み取り・更新・削除）が可能
// 例えば findAll() を使うことで、データベース内のすべてのユーザー情報を取得できる
// メソッド名に Like を含めることで、SQL の LIKE 句と同様の部分一致検索が可能
