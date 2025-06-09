package com.example.samuraitravel.repository; // リポジトリ関連のクラスを含むパッケージ

// HouseRepository クラスは宿泊施設のデータを管理するリポジトリ（データベースへのアクセスを担当）
// JpaRepository を継承することで、基本的な CRUD 操作（作成・読み取り・更新・削除）が簡単に行える

import java.util.List; // 複数のデータ（宿泊施設の一覧）を取得するためのクラス

import org.springframework.data.domain.Page; // ページネーション（ページ分割表示）のためのクラス
import org.springframework.data.domain.Pageable; // ページ情報（ページ番号、サイズなど）を扱うクラス
import org.springframework.data.jpa.repository.JpaRepository; // Spring Data JPAのリポジトリ機能を利用するためのクラス

import com.example.samuraitravel.entity.House; // House（宿泊施設）のエンティティ（データベースと対応するクラス）

// JpaRepository を継承して宿泊施設のデータ操作を行う
// JpaRepository<エンティティのクラス型, 主キーのデータ型>
public interface HouseRepository extends JpaRepository<House, Integer> {

    // 宿泊施設名に特定の文字が含まれるものを検索（ページネーション対応）
    public Page<House> findByNameLike(String keyword, Pageable pageable);

    // 宿泊施設名または住所に特定の文字が含まれるものを「作成日時の降順」で並べて検索
    public Page<House> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable);

    // 宿泊施設名または住所に特定の文字が含まれるものを「価格の昇順」で並べて検索
    public Page<House> findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable);

    // 住所に特定の文字が含まれるものを「作成日時の降順」で並べて検索
    public Page<House> findByAddressLikeOrderByCreatedAtDesc(String area, Pageable pageable);

    // 住所に特定の文字が含まれるものを「価格の昇順」で並べて検索
    public Page<House> findByAddressLikeOrderByPriceAsc(String area, Pageable pageable);

    // 価格が指定された値以下の宿泊施設を「作成日時の降順」で検索
    public Page<House> findByPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);

    // 価格が指定された値以下の宿泊施設を「価格の昇順」で検索
    public Page<House> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable);

    // すべての宿泊施設を「作成日時の降順」で取得（ページネーション対応）
    public Page<House> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // すべての宿泊施設を「価格の昇順」で取得（ページネーション対応）
    public Page<House> findAllByOrderByPriceAsc(Pageable pageable);

    // 最新の宿泊施設を10件取得（作成日時の降順）
    public List<House> findTop10ByOrderByCreatedAtDesc();
}

// JpaRepository を継承することで、基本的な CRUD 操作（作成・読み取り・更新・削除）が可能
// 例えば findAll() を使うことで、データベース内の全ての宿泊施設を取得できる
// メソッド名に OrderBy を含めることで、SQL の ORDER BY 句と同様の並べ替えが可能
// ORDER BY は、SQL でデータを並べ替えるための構文
