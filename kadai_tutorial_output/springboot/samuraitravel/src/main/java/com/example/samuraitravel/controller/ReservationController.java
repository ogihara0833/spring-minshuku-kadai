package com.example.samuraitravel.controller; // このクラスが属するパッケージ（グループ）

import java.time.LocalDate; // 日付を扱うためのクラス

import jakarta.servlet.http.HttpServletRequest; // ユーザーからのリクエスト情報を扱うクラス

import org.springframework.data.domain.Page; // ページネーション（ページ分割表示）のためのクラス
import org.springframework.data.domain.Pageable; // ページ情報（ページ番号、サイズなど）を扱うクラス
import org.springframework.data.domain.Sort.Direction; // ソートの方向（昇順・降順）を指定するクラス
import org.springframework.data.web.PageableDefault; // デフォルトのページ設定を指定するアノテーション
import org.springframework.security.core.annotation.AuthenticationPrincipal; // ログイン中のユーザー情報を取得するアノテーション
import org.springframework.stereotype.Controller; // Webアプリのコントローラーであることを示す
import org.springframework.ui.Model; // 画面にデータを渡すためのオブジェクト
import org.springframework.validation.BindingResult; // 入力チェック（バリデーション）の結果を格納するオブジェクト
import org.springframework.validation.FieldError; // フォーム入力のエラーを表すクラス
import org.springframework.validation.annotation.Validated; // フォームの入力チェックを行うためのアノテーション
import org.springframework.web.bind.annotation.GetMapping; // GETリクエスト（ページ表示）を処理するアノテーション
import org.springframework.web.bind.annotation.ModelAttribute; // フォームのデータをオブジェクトとして受け取る
import org.springframework.web.bind.annotation.PathVariable; // URLの一部（id など）を変数として受け取るアノテーション
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // リダイレクト時にデータを渡すためのクラス

import com.example.samuraitravel.entity.House; // 宿泊施設の情報を扱うクラス
import com.example.samuraitravel.entity.Reservation; // 予約情報を扱うクラス
import com.example.samuraitravel.entity.User; // ユーザー情報を扱うクラス
import com.example.samuraitravel.form.ReservationInputForm; // 予約フォーム（入力用）を扱うクラス
import com.example.samuraitravel.form.ReservationRegisterForm; // 予約フォーム（登録用）を扱うクラス
import com.example.samuraitravel.repository.HouseRepository; // 宿泊施設の情報を取得するためのリポジトリ
import com.example.samuraitravel.repository.ReservationRepository; // 予約情報を取得するためのリポジトリ
import com.example.samuraitravel.security.UserDetailsImpl; // ログイン中のユーザー情報を扱うクラス
import com.example.samuraitravel.service.ReservationService; // 予約の管理を行うサービス
import com.example.samuraitravel.service.StripeService; // 支払い処理を担当するサービス（Stripeを使用）

@Controller // このクラスがコントローラーであることを示す
public class ReservationController {
    // 予約や宿泊施設情報を管理するためのオブジェクトを定義
    private final ReservationRepository reservationRepository;   
    private final HouseRepository houseRepository;
    private final ReservationService reservationService; 
    private final StripeService stripeService;

    // コンストラクタ（クラスが作られるときに必要な情報を受け取る）
    public ReservationController(ReservationRepository reservationRepository, HouseRepository houseRepository, ReservationService reservationService, StripeService stripeService) {   
        this.reservationRepository = reservationRepository; 
        this.houseRepository = houseRepository;
        this.reservationService = reservationService;
        this.stripeService = stripeService;
    }

    // ログイン中のユーザーの予約一覧を表示する
    @GetMapping("/reservations") // 「/reservations」にアクセスしたときにこのメソッドが実行される
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) {
        User user = userDetailsImpl.getUser(); // ログイン中のユーザー情報を取得
        Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable); // ユーザーの予約履歴を取得

        model.addAttribute("reservationPage", reservationPage); // 取得した予約データを画面に渡す

        return "reservations/index"; // 予約一覧ページ（reservations/index.html）を表示
    }

    // 予約入力ページ（フォームの確認）
    @GetMapping("/houses/{id}/reservations/input")
    public String input(@PathVariable(name = "id") Integer id,
                        @ModelAttribute @Validated ReservationInputForm reservationInputForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {   
        House house = houseRepository.getReferenceById(id); // 指定された宿泊施設情報を取得
        Integer numberOfPeople = reservationInputForm.getNumberOfPeople(); // 宿泊人数を取得
        Integer capacity = house.getCapacity(); // 宿泊施設の定員を取得

        // 宿泊人数が定員を超えていないかチェック
        if (numberOfPeople != null && !reservationService.isWithinCapacity(numberOfPeople, capacity)) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "numberOfPeople", "宿泊人数が定員を超えています。");
            bindingResult.addError(fieldError);
        }

        // エラーがある場合、宿泊施設詳細ページへ戻る
        if (bindingResult.hasErrors()) {            
            model.addAttribute("house", house);
            model.addAttribute("errorMessage", "予約内容に不備があります。"); 
            return "houses/show";
        }

        redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm);           
        return "redirect:/houses/{id}/reservations/confirm"; // 予約確認ページへリダイレクト
    } 

    // 予約確認ページの表示
    @GetMapping("/houses/{id}/reservations/confirm")
    public String confirm(@PathVariable(name = "id") Integer id,
                          @ModelAttribute ReservationInputForm reservationInputForm,
                          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                          HttpServletRequest httpServletRequest,
                          Model model) {        
        House house = houseRepository.getReferenceById(id);
        User user = userDetailsImpl.getUser();

        // チェックイン日とチェックアウト日を取得
        LocalDate checkinDate = reservationInputForm.getCheckinDate();
        LocalDate checkoutDate = reservationInputForm.getCheckoutDate();

        // 宿泊料金を計算
        Integer price = house.getPrice();        
        Integer amount = reservationService.calculateAmount(checkinDate, checkoutDate, price);

        // 予約フォームの作成
        ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(house.getId(), user.getId(), checkinDate.toString(), checkoutDate.toString(), reservationInputForm.getNumberOfPeople(), amount);

        // Stripeの支払いセッションを作成
        String sessionId = stripeService.createStripeSession(house.getName(), reservationRegisterForm, httpServletRequest);

        model.addAttribute("house", house);
        model.addAttribute("reservationRegisterForm", reservationRegisterForm);
        model.addAttribute("sessionId", sessionId);

        return "reservations/confirm"; // 予約確認ページ（reservations/confirm.html）を表示
    }
}
