package com.example.samuraitravel.controller; // パッケージ（コードのグループ）を指定

// このクラスは、ユーザーのログインや会員登録、認証情報の管理を担当します。

import jakarta.servlet.http.HttpServletRequest; // クライアントからのリクエストを扱うためのクラス

import org.springframework.stereotype.Controller; // このクラスがWebアプリのコントローラーであることを示す
import org.springframework.ui.Model; // 画面にデータを渡すためのオブジェクト
import org.springframework.validation.BindingResult; // 入力チェック（バリデーション）の結果を格納するオブジェクト
import org.springframework.validation.FieldError; // フォーム入力のエラーを表すクラス
import org.springframework.validation.annotation.Validated; // フォームの入力チェックを行うためのアノテーション
import org.springframework.web.bind.annotation.GetMapping; // HTTPのGETリクエストを処理するアノテーション（ページを表示）
import org.springframework.web.bind.annotation.ModelAttribute; // フォームのデータをオブジェクトとして受け取る
import org.springframework.web.bind.annotation.PostMapping; // HTTPのPOSTリクエストを処理するアノテーション（フォーム送信時）
import org.springframework.web.bind.annotation.RequestParam; // URLのパラメータを受け取る
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // リダイレクト時にデータを渡すためのクラス

import com.example.samuraitravel.entity.User; // ユーザー情報を扱うクラス
import com.example.samuraitravel.entity.VerificationToken; // 認証トークン（会員登録の確認用）のクラス
import com.example.samuraitravel.event.SignupEventPublisher; // 会員登録イベントを発行するクラス
import com.example.samuraitravel.form.SignupForm; // 会員登録フォームのクラス
import com.example.samuraitravel.service.UserService; // ユーザー情報を管理するサービスクラス
import com.example.samuraitravel.service.VerificationTokenService; // 認証トークンを管理するサービスクラス

@Controller // このクラスはコントローラー（Webリクエストを処理する）として動作する
public class AuthController {
    private final UserService userService; // ユーザー管理用サービス
    private final SignupEventPublisher signupEventPublisher; // 会員登録イベントの発行を担当
    private final VerificationTokenService verificationTokenService; // 認証トークンの管理サービス
    
    // コンストラクタ（クラスが作られるときに必要な情報を受け取る）
    public AuthController(UserService userService, SignupEventPublisher signupEventPublisher, VerificationTokenService verificationTokenService) {
        this.userService = userService;
        this.signupEventPublisher = signupEventPublisher;
        this.verificationTokenService = verificationTokenService;
    }
    
    // ログインページの表示
    @GetMapping("/login")
    public String login() {
        return "auth/login"; // 「auth/login.html」ページを表示
    }
    
    // 会員登録ページの表示
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signupForm", new SignupForm()); // 空のフォームデータを画面に渡す
        return "auth/signup"; // 「auth/signup.html」ページを表示
    }
    
    // 会員登録フォームの送信処理
    @PostMapping("/signup")
    public String signup(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        // メールアドレスが既に登録されている場合、エラーを追加
        if (userService.isEmailRegistered(signupForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);
        }

        // パスワードと確認用パスワードが一致しない場合、エラーを追加
        if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。");
            bindingResult.addError(fieldError);
        }

        // 入力にエラーがあれば、会員登録ページへ戻る
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        // エラーがない場合、ユーザーを作成
        User createdUser = userService.create(signupForm);
        
        // 認証メールを送信するためのURLを取得
        String requestUrl = new String(httpServletRequest.getRequestURL());
        
        // 会員登録イベントを発行（メール送信）
        signupEventPublisher.publishSignupEvent(createdUser, requestUrl);
        
        // リダイレクト時に成功メッセージを表示
        redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");

        return "redirect:/"; // トップページへリダイレクト
    }
    
    // 認証トークンを検証（ユーザーの認証完了）
    @GetMapping("/signup/verify")
    public String verify(@RequestParam(name = "token") String token, Model model) {
        // トークンから認証情報を取得
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

        if (verificationToken != null) {
            // トークンが有効ならユーザーを認証
            User user = verificationToken.getUser();
            userService.enableUser(user);
            model.addAttribute("successMessage", "会員登録が完了しました。");
        } else {
            // 無効なトークンならエラーメッセージを表示
            model.addAttribute("errorMessage", "トークンが無効です。");
        }

        return "auth/verify"; // 認証結果のページ（auth/verify.html）を表示
    }
}
