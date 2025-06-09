package com.example.samuraitravel.controller; // コントローラーのクラスが属するパッケージ（グループ）

import java.util.List; // リスト（複数のデータを格納するためのもの）を扱うためのクラス

// このクラスは「ホームページ」に関する処理を担当します。
// 主に、新しく登録された「宿泊施設（House）」の一覧を表示する役割を持っています。

import org.springframework.stereotype.Controller; // このクラスが「コントローラー」として動作することを示す
import org.springframework.ui.Model; // 画面にデータを渡すためのオブジェクト
import org.springframework.web.bind.annotation.GetMapping; // GETリクエスト（ページ表示）を処理するアノテーション

import com.example.samuraitravel.entity.House; // House（宿泊施設の情報を持つ）クラスをインポート
import com.example.samuraitravel.repository.HouseRepository; // HouseRepository（宿泊施設情報を取得するクラス）をインポート

@Controller // このクラスが「コントローラー」であることを示す
public class HomeController {
    private final HouseRepository houseRepository; // 宿泊施設データを取得するためのオブジェクト
    
    // コンストラクタ（クラスが作られるときに houseRepository を受け取る）
    public HomeController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }
    
    // ホームページ（トップページ）を表示する処理
    @GetMapping("/") // 「/」（トップページ）にアクセスしたときにこのメソッドが実行される
    public String index(Model model) {
        // 最新の宿泊施設情報を10件取得（作成日時が新しい順）
        List<House> newHouses = houseRepository.findTop10ByOrderByCreatedAtDesc();
        
        // 取得したデータを「newHouses」という名前で画面に渡す
        model.addAttribute("newHouses", newHouses);
        
        return "index"; // 「index.html」ページを表示（ホームページ）
    }
}
