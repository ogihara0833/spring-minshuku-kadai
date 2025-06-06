package com.example.samuraitravel.form; // フォーム関連のクラスを含むパッケージ

// ReservationInputForm クラスは宿泊予約の入力フォームを管理するクラス
// ユーザーが宿泊予約をするときに使用

import java.time.LocalDate; // チェックイン・チェックアウトの日付を扱うためのクラス

import jakarta.validation.constraints.Min; // 数値の最小値を制限するためのアノテーション
import jakarta.validation.constraints.NotBlank; // 空の値を禁止するためのアノテーション（文字列用）
import jakarta.validation.constraints.NotNull; // nullを禁止するためのアノテーション（数値・オブジェクト用）

import lombok.Data; // Lombokのアノテーション（ゲッター・セッターを自動生成）

@Data // Lombokを使用して、ゲッター・セッター・toString()などを自動生成（手動で書く必要なし）
public class ReservationInputForm {
    @NotBlank(message = "チェックイン日とチェックアウト日を選択してください。") // 宿泊日程は必須（空欄禁止）
    private String fromCheckinDateToCheckoutDate;    

    @NotNull(message = "宿泊人数を入力してください。") // 宿泊人数は必須（null禁止）
    @Min(value = 1, message = "宿泊人数は1人以上に設定してください。") // 宿泊人数の最小値を1人に設定
    private Integer numberOfPeople; 

    // チェックイン日を取得するメソッド
    public LocalDate getCheckinDate() {
        // "チェックイン日 から チェックアウト日" の形式で入力されるため、"から"で分割
        String[] checkinDateAndCheckoutDate = getFromCheckinDateToCheckoutDate().split(" から ");
        return LocalDate.parse(checkinDateAndCheckoutDate[0]); // チェックイン日を取得
    }

    // チェックアウト日を取得するメソッド
    public LocalDate getCheckoutDate() {
        // "チェックイン日 から チェックアウト日" の形式で入力されるため、"から"で分割
        String[] checkinDateAndCheckoutDate = getFromCheckinDateToCheckoutDate().split(" から ");
        return LocalDate.parse(checkinDateAndCheckoutDate[1]); // チェックアウト日を取得
    }
}
