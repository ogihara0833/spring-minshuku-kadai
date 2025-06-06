package com.example.samuraitravel.form; // フォーム関連のクラスを含むパッケージ

// SignupForm クラスはユーザーの会員登録フォームを管理するクラス
// ユーザーが新規登録するときに使用

import jakarta.validation.constraints.Email; // メールアドレスの形式をチェックするためのアノテーション
import jakarta.validation.constraints.NotBlank; // 空の値を禁止するためのアノテーション（文字列用）

import org.hibernate.validator.constraints.Length; // 文字列の長さを制限するためのアノテーション

import lombok.Data; // Lombokのアノテーション（ゲッター・セッターを自動生成）

@Data // Lombokを使用して、ゲッター・セッター・toString()などを自動生成（手動で書く必要なし）
public class SignupForm {    
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
    @Email(message = "メールアドレスは正しい形式で入力してください。") // メールアドレスの形式をチェック
    private String email;

    @NotBlank(message = "パスワードを入力してください。") // パスワードは必須（空欄禁止）
    @Length(min = 8, message = "パスワードは8文字以上で入力してください。") // パスワードの最小文字数を8に設定
    private String password;    

    @NotBlank(message = "パスワード（確認用）を入力してください。") // 確認用パスワードは必須（空欄禁止）
    private String passwordConfirmation;    
}
