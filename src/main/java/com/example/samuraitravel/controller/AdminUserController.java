package com.example.samuraitravel.controller; // パッケージの宣言（このクラスが属するグループ）

// ユーザーが /admin/users にアクセスすると、このクラス（AdminUserController）がリクエストを受け取る

import org.springframework.data.domain.Page; // ページネーション（ページ分割）のためのクラス
import org.springframework.data.domain.Pageable; // ページ情報を扱うクラス（ページ番号やサイズなど）
import org.springframework.data.domain.Sort.Direction; // ソートの方向（昇順・降順）
import org.springframework.data.web.PageableDefault; // デフォルトのページ設定を指定するアノテーション
import org.springframework.stereotype.Controller; // Springのコントローラー（Webリクエストを処理する）であることを示す
import org.springframework.ui.Model; // 画面にデータを渡すためのオブジェクト
import org.springframework.web.bind.annotation.GetMapping; // HTTPのGETリクエストを処理するためのアノテーション
import org.springframework.web.bind.annotation.PathVariable; // URLの一部（idなど）を変数として受け取るためのアノテーション
import org.springframework.web.bind.annotation.RequestMapping; // リクエストのパスを設定するアノテーション
import org.springframework.web.bind.annotation.RequestParam; // URLパラメータを受け取るためのアノテーション

import com.example.samuraitravel.entity.User; // User（ユーザー情報のエンティティ）クラスをインポート
import com.example.samuraitravel.repository.UserRepository; // UserRepository（データ操作を担当するクラス）をインポート

@Controller // このクラスはコントローラー（Webリクエストを処理する役割）であることを示す
@RequestMapping("/admin/users") // "/admin/users" のURLに対応するリクエストを処理
public class AdminUserController {
    private final UserRepository userRepository; // ユーザー情報を扱うためのデータベースへのアクセス手段

    // コンストラクタ（クラスの初期化時に userRepository を受け取る）
    public AdminUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ユーザー一覧を表示する処理（URLに「/admin/users」が指定された場合）
    @GetMapping
    public String index(
        @RequestParam(name = "keyword", required = false) String keyword, // 検索キーワード（任意）
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, // ページネーションの設定（デフォルト：10件表示、ID昇順）
        Model model // 画面にデータを渡すためのオブジェクト
    ) {
        Page<User> userPage; // ユーザーのページ情報を格納する変数

        if (keyword != null && !keyword.isEmpty()) { 
            // キーワードが入力されていれば、氏名かフリガナにその文字が含まれるユーザーを検索
            userPage = userRepository.findByNameLikeOrFuriganaLike("%" + keyword +"%", "%" + keyword + "%", pageable);
        } else {
            // キーワードがなければ、全ユーザーを取得（ページネーション付き）
            userPage = userRepository.findAll(pageable);
        }

        model.addAttribute("userPage", userPage); // 画面に渡すデータ（ユーザー情報）
        model.addAttribute("keyword", keyword); // 検索キーワードも渡す（検索欄に反映）

        return "admin/users/index"; // ユーザー一覧画面（admin/users/index.html）を表示
    }

    // ユーザー詳細を表示する処理（URLに「/admin/users/{id}」が指定された場合）
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) { 
        // URLの「id」部分の値を受け取る（例：「/admin/users/5」なら id = 5）

        User user = userRepository.getReferenceById(id); // 指定されたIDのユーザー情報を取得

        model.addAttribute("user", user); // 画面にユーザー情報を渡す
        return "admin/users/show"; // ユーザー詳細画面（admin/users/show.html）を表示
    }
}
