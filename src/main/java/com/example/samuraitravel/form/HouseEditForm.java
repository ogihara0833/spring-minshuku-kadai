package com.example.samuraitravel.form; // フォーム関連のクラスを含むパッケージ

// HouseEditForm クラスは宿泊施設の編集フォームを管理するクラス
// ユーザーが宿泊施設の情報を編集・更新するときに使用

import jakarta.validation.constraints.Min; // 数値の最小値を制限するためのアノテーション
import jakarta.validation.constraints.NotBlank; // 空の値を禁止するためのアノテーション（文字列用）
import jakarta.validation.constraints.NotNull; // nullを禁止するためのアノテーション（数値・オブジェクト用）

import org.springframework.web.multipart.MultipartFile; // ファイルアップロードを扱うクラス（宿泊施設の画像など）

import lombok.AllArgsConstructor; // Lombokのアノテーション（すべてのフィールドを引数に持つコンストラクタを自動生成）
import lombok.Data; // Lombokのアノテーション（ゲッター・セッターを自動生成）

@Data // Lombokを使用して、ゲッター・セッター・toString()などを自動生成（手動で書く必要なし）
@AllArgsConstructor // すべてのフィールドを引数に持つコンストラクタを自動生成（簡潔なコードにできる）
public class HouseEditForm {
    @NotNull // IDはnull禁止（必須項目）
    private Integer id;    

    @NotBlank(message = "民宿名を入力してください。") // 宿泊施設名は必須（空欄禁止）
    private String name;

    private MultipartFile imageFile; // 宿泊施設の画像（アップロード用）

    @NotBlank(message = "説明を入力してください。") // 宿泊施設の説明は必須（空欄禁止）
    private String description;   

    @NotNull(message = "宿泊料金を入力してください。") // 宿泊料金は必須（null禁止）
    @Min(value = 1, message = "宿泊料金は1円以上に設定してください。") // 最小値を1円に設定
    private Integer price; 

    @NotNull(message = "定員を入力してください。") // 定員は必須（null禁止）
    @Min(value = 1, message = "定員は1人以上に設定してください。") // 最小値を1人に設定
    private Integer capacity;       

    @NotBlank(message = "郵便番号を入力してください。") // 郵便番号は必須（空欄禁止）
    private String postalCode;

    @NotBlank(message = "住所を入力してください。") // 住所は必須（空欄禁止）
    private String address;

    @NotBlank(message = "電話番号を入力してください。") // 電話番号は必須（空欄禁止）
    private String phoneNumber;
}
