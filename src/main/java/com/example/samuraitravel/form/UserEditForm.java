package com.example.samuraitravel.form; // フォーム関連のクラスを含むパッケージ

// UserEditForm クラスはユーザー情報の編集フォームを管理するクラス
// ユーザーが自身の情報を更新するときに使用

import jakarta.validation.constraints.NotBlank; // 空の値を禁止するためのアノテーション（文字列用）
import jakarta.validation.constraints.NotNull; // nullを禁止するためのアノテーション（数値・オブジェクト用）

import lombok.AllArgsConstructor; // Lombokのアノテーション（すべてのフィールドを引数に持つコンストラクタを自動生成）
import lombok.Data; // Lombokのアノテーション（ゲッター・セッターを自動生成）

@Data // Lombokを使用して、ゲッター・セッター・toString()などを自動生成（手動で書く必要なし）
@AllArgsConstructor // すべてのフィールドを引数に持つコンストラクタを自動生成（簡潔なコードにできる）
public class UserEditForm {
    @NotNull // IDはnull禁止（必須項目）
    private Integer id;

    @NotBlank(message = "氏名を入力してください。") // 氏名は必須（空欄禁止）
    private String name;

    @NotBlank(message = "フリガナを入力してください。") // フリガナは必須（空欄禁止）
    private String furigana;

    @NotBlank(message = "郵便番号を入力してください。") // 郵便番号は必須（空欄禁止）
    private String postalCode;

    @NotBlank(message = "住所を入力してください。") // 住所は必須（空欄禁止）
    private String address;

    @NotBlank(message = "電話番号を入力してください。") // 電話番号は必須（空欄禁止）
    private String phoneNumber;

    @NotBlank(message = "メールアドレスを入力してください。") // メールアドレスは必須（空欄禁止）
    private String email;
}
