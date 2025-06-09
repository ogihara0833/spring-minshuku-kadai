package com.example.samuraitravel.controller; // このクラスが属するパッケージ（グループ）

// ユーザー情報の管理を担当するコントローラー
// ログイン中のユーザーの情報を取得、表示、編集する機能を提供

import org.springframework.security.core.annotation.AuthenticationPrincipal; // ログイン中のユーザー情報を取得するためのアノテーション
import org.springframework.stereotype.Controller; // Webアプリのコントローラーであることを示す
import org.springframework.ui.Model; // 画面にデータを渡すためのオブジェクト
import org.springframework.validation.BindingResult; // 入力チェック（バリデーション）の結果を格納するオブジェクト
import org.springframework.validation.FieldError; // フォーム入力のエラーを表すクラス
import org.springframework.validation.annotation.Validated; // フォームの入力チェックを行うためのアノテーション
import org.springframework.web.bind.annotation.GetMapping; // GETリクエスト（ページ表示）を処理するアノテーション
import org.springframework.web.bind.annotation.ModelAttribute; // フォームのデータをオブジェクトとして受け取る
import org.springframework.web.bind.annotation.PostMapping; // POSTリクエスト（データ送信）を処理するアノテーション
import org.springframework.web.bind.annotation.RequestMapping; // リクエストのパスを設定するアノテーション
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // リダイレクト時にデータを渡すためのクラス

import com.example.samuraitravel.entity.User; // ユーザー情報を扱うクラス
import com.example.samuraitravel.form.UserEditForm; // ユーザー情報の編集フォーム
import com.example.samuraitravel.repository.UserRepository; // ユーザー情報を取得するリポジトリ
import com.example.samuraitravel.security.UserDetailsImpl; // ログイン中のユーザー情報を扱うクラス
import com.example.samuraitravel.service.UserService; // ユーザー情報の管理を行うサービス

@Controller // このクラスがコントローラーであることを示す
@RequestMapping("/user") // 「/user」以下のURLに対応するリクエストを処理する
public class UserController {
    private final UserRepository userRepository; // ユーザー情報を取得するためのリポジトリ
    private final UserService userService; // ユーザー情報を管理するサービス

    // コンストラクタ（クラスが作られるときに必要な情報を受け取る）
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // ログイン中のユーザー情報を表示するページ
    @GetMapping // 「/user」にアクセスされたときにこのメソッドを実行
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
        // @AuthenticationPrincipal を使って、現在ログイン中のユーザー情報を取得
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());

        // ユーザー情報を画面に渡す
        model.addAttribute("user", user);

        return "user/index"; // ユーザー情報ページ（user/index.html）を表示
    }

    // ユーザー情報編集ページの表示
    @GetMapping("/edit") // 「/user/edit」にアクセスされたときにこのメソッドを実行
    public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
        // ログイン中のユーザー情報を取得
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());

        // 取得したユーザー情報を編集フォームに設定
        UserEditForm userEditForm = new UserEditForm(
            user.getId(), user.getName(), user.getFurigana(),
            user.getPostalCode(), user.getAddress(),
            user.getPhoneNumber(), user.getEmail()
        );

        // 編集フォームを画面に渡す
        model.addAttribute("userEditForm", userEditForm);

        return "user/edit"; // ユーザー編集ページ（user/edit.html）を表示
    }

    // ユーザー情報の更新処理
    @PostMapping("/update") // 「/user/update」にフォームを送信したときにこのメソッドを実行
    public String update(@ModelAttribute @Validated UserEditForm userEditForm,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        // メールアドレスが変更されていて、すでに登録済みならエラーを追加
        if (userService.isEmailChanged(userEditForm) && userService.isEmailRegistered(userEditForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);
        }

        // エラーがある場合、編集ページに戻る
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }

        // ユーザー情報を更新する
        userService.update(userEditForm);

        // 更新成功メッセージを設定し、リダイレクト
        redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");

        return "redirect:/user"; // ユーザー情報ページへリダイレクト
    }
}
