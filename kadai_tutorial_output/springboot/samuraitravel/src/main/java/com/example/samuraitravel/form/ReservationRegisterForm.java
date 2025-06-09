package com.example.samuraitravel.form; // フォーム関連のクラスを含むパッケージ

// ReservationRegisterForm クラスは宿泊予約の登録フォームを管理するクラス
// ユーザーが予約情報を確定し、データベースに保存するときに使用

import lombok.AllArgsConstructor; // Lombokのアノテーション（すべてのフィールドを引数に持つコンストラクタを自動生成）
import lombok.Data; // Lombokのアノテーション（ゲッター・セッターを自動生成）

@Data // Lombokを使用して、ゲッター・セッター・toString()などを自動生成（手動で書く必要なし）
@AllArgsConstructor // すべてのフィールドを引数に持つコンストラクタを自動生成（簡潔なコードにできる）
public class ReservationRegisterForm {    
    private Integer houseId; // 宿泊施設のID（予約する宿泊施設を識別）

    private Integer userId; // ユーザーID（予約者を識別）

    private String checkinDate; // チェックイン日（宿泊開始日）

    private String checkoutDate; // チェックアウト日（宿泊終了日）

    private Integer numberOfPeople; // 宿泊人数

    private Integer amount; // 予約金額（宿泊料金）
}
