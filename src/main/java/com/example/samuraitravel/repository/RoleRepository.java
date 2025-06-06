package com.example.samuraitravel.repository; // リポジトリ関連のクラスを含むパッケージ

// RoleRepository クラスはユーザーの役割（Role）を管理するリポジトリ
// JpaRepository を継承することで、基本的な CRUD 操作（作成・読み取り・更新・削除）が簡単に行える

import org.springframework.data.jpa.repository.JpaRepository; // Spring Data JPAのリポジトリ機能を利用するためのクラス

import com.example.samuraitravel.entity.Role; // Role（役割）のエンティティ（データベースと対応するクラス）

// JpaRepository を継承して役割データの操作を行う
// JpaRepository<エンティティのクラス型, 主キーのデータ型>
public interface RoleRepository extends JpaRepository<Role, Integer> {

    // 指定された役割名（name）を持つ Role エンティティを検索
    public Role findByName(String name);
}

// JpaRepository を継承することで、基本的な CRUD 操作（作成・読み取り・更新・削除）が可能
// 例えば findAll() を使うことで、データベース内のすべての役割情報を取得できる
