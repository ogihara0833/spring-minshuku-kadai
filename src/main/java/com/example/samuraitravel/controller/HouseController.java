package com.example.samuraitravel.controller; // コントローラー（Webリクエストを処理するクラス）のグループを指定

import org.springframework.data.domain.Page; // ページネーション（ページ分割表示）のためのクラス
import org.springframework.data.domain.Pageable; // ページ情報（ページ番号、サイズなど）を扱うクラス
import org.springframework.data.domain.Sort.Direction; // ソートの方向（昇順・降順）を指定するクラス
import org.springframework.data.web.PageableDefault; // デフォルトのページ設定を指定するアノテーション
import org.springframework.stereotype.Controller; // Webアプリのコントローラーであることを示す
import org.springframework.ui.Model; // 画面にデータを渡すためのオブジェクト
import org.springframework.web.bind.annotation.GetMapping; // GETリクエスト（ページ表示）を処理するアノテーション
import org.springframework.web.bind.annotation.PathVariable; // URLの一部（id など）を変数として受け取るアノテーション
import org.springframework.web.bind.annotation.RequestMapping; // リクエストのパスを設定するアノテーション
import org.springframework.web.bind.annotation.RequestParam; // URLパラメータを受け取るアノテーション

import com.example.samuraitravel.entity.House; // House（宿泊施設の情報を持つ）クラスをインポート
import com.example.samuraitravel.form.ReservationInputForm; // 予約フォームを扱うクラス
import com.example.samuraitravel.repository.HouseRepository; // 宿泊施設の情報を取得するためのリポジトリ

@Controller // このクラスがコントローラーであることを示す
@RequestMapping("/houses") // 「/houses」以下のURLに対応するリクエストを処理する
public class HouseController {
    private final HouseRepository houseRepository; // 宿泊施設データを管理するリポジトリ

    // コンストラクタ（クラスの初期化時に houseRepository を受け取る）
    public HouseController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    // 宿泊施設一覧を表示する処理
    @GetMapping // 「/houses」にアクセスされたらこのメソッドを実行
    public String index(@RequestParam(name = "keyword", required = false) String keyword, // 検索キーワード（宿泊施設名や住所）
                        @RequestParam(name = "area", required = false) String area, // 地域指定の検索
                        @RequestParam(name = "price", required = false) Integer price, // 価格指定の検索
                        @RequestParam(name = "order", required = false) String order, // 並び順（価格順・作成日時順）
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, // ページネーションの設定
                        Model model) 
    {
        Page<House> housePage; // 検索結果を格納する変数

        // 条件に応じた検索を実行
        if (keyword != null && !keyword.isEmpty()) { // キーワード検索
            if ("priceAsc".equals(order)) {
                housePage = houseRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%", "%" + keyword + "%", pageable);
            } else {
                housePage = houseRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
            }
        } else if (area != null && !area.isEmpty()) { // 地域検索
            if ("priceAsc".equals(order)) {
                housePage = houseRepository.findByAddressLikeOrderByPriceAsc("%" + area + "%", pageable);
            } else {
                housePage = houseRepository.findByAddressLikeOrderByCreatedAtDesc("%" + area + "%", pageable);
            }
        } else if (price != null) { // 価格検索
            if ("priceAsc".equals(order)) {
                housePage = houseRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
            } else {
                housePage = houseRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
            }
        } else { // 条件なしの場合
            if ("priceAsc".equals(order)) {
                housePage = houseRepository.findAllByOrderByPriceAsc(pageable);
            } else {
                housePage = houseRepository.findAllByOrderByCreatedAtDesc(pageable);
            }
        }

        // 画面に渡すデータを設定
        model.addAttribute("housePage", housePage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("area", area);
        model.addAttribute("price", price);
        model.addAttribute("order", order);

        return "houses/index"; // 宿泊施設一覧ページ（houses/index.html）を表示
    }

    // 宿泊施設の詳細情報を表示する処理
    @GetMapping("/{id}") // 「/houses/{id}」のURLにアクセスされたらこのメソッドを実行
    public String show(@PathVariable(name = "id") Integer id, Model model) {
        House house = houseRepository.getReferenceById(id); // IDに対応する宿泊施設情報を取得

        model.addAttribute("house", house); // 宿泊施設情報を画面に渡す
        model.addAttribute("reservationInputForm", new ReservationInputForm()); // 予約フォームを画面に渡す

        return "houses/show"; // 宿泊施設詳細ページ（houses/show.html）を表示
    }    
}
