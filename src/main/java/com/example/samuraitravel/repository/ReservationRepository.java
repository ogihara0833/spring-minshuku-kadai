package com.example.samuraitravel.repository; // リポジトリ関連のクラスを含むパッケージ

// ReservationRepository クラスは予約情報のデータを管理するリポジトリ（データベースへのアクセスを担当）
// JpaRepository を継承することで、基本的な CRUD 操作（作成・読み取り・更新・削除）が簡単に行える

import org.springframework.data.domain.Page; // ページネーション（ページ分割表示）のためのクラス
import org.springframework.data.domain.Pageable; // ページ情報（ページ番号、サイズなど）を扱うクラス
import org.springframework.data.jpa.repository.JpaRepository; // Spring Data JPAのリポジトリ機能を利用するためのクラス

import com.example.samuraitravel.entity.Reservation; // Reservation（予約）のエンティティ（データベースと対応するクラス）
import com.example.samuraitravel.entity.User; // User（ユーザー）のエンティティ（予約者）

// JpaRepository を継承して予約データの操作を行う
// JpaRepository<エンティティのクラス型, 主キーのデータ型>
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    // 指定したユーザーの予約一覧を取得（作成日時の降順で並び替え）
    // Pageable を使うことで、検索結果を一定の件数ごとに分割して取得可能（ページネーション対応）
    public Page<Reservation> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}

// JpaRepository を継承することで、基本的な CRUD 操作（作成・読み取り・更新・削除）が可能
// 例えば findAll() を使うことで、データベース内のすべての予約情報を取得できる
// メソッド名に OrderBy を含めることで、SQL の ORDER BY 句と同様の並べ替えが可能
