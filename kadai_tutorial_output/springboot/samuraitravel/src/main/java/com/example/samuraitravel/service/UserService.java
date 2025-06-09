package com.example.samuraitravel.service; // サービス関連のクラスを含むパッケージ

// UserService クラスは、ユーザーの登録・更新・認証を管理するサービスクラス
// ユーザー情報をデータベースに保存し、認証関連の処理を行う

import org.springframework.security.crypto.password.PasswordEncoder; // パスワードのハッシュ化を行うクラス
import org.springframework.stereotype.Service; // Spring のサービスとして登録するためのアノテーション
import org.springframework.transaction.annotation.Transactional; // トランザクション管理を行うためのアノテーション

import com.example.samuraitravel.entity.Role; // Role（役割）のエンティティ（データベースと対応するクラス）
import com.example.samuraitravel.entity.User; // User（ユーザー）のエンティティ（データベースと対応するクラス）
import com.example.samuraitravel.form.SignupForm; // ユーザー登録フォーム
import com.example.samuraitravel.form.UserEditForm; // ユーザー編集フォーム
import com.example.samuraitravel.repository.RoleRepository; // 役割情報のデータを管理するリポジトリ
import com.example.samuraitravel.repository.UserRepository; // ユーザー情報のデータを管理するリポジトリ

@Service // Spring のサービスとして登録（DIコンテナに登録される）
public class UserService {
    private final UserRepository userRepository; // ユーザー情報のデータを管理するリポジトリ
    private final RoleRepository roleRepository; // 役割情報のデータを管理するリポジトリ
    private final PasswordEncoder passwordEncoder; // パスワードのハッシュ化を行うクラス

    // コンストラクタ（クラスの初期化時にリポジトリとエンコーダを受け取る）
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ユーザーの新規登録処理
    @Transactional // データベースの更新処理をトランザクション管理する
    public User create(SignupForm signupForm) {
        User user = new User();
        Role role = roleRepository.findByName("ROLE_GENERAL"); // 一般ユーザーの役割を取得

        // ユーザー情報を設定
        user.setName(signupForm.getName());
        user.setFurigana(signupForm.getFurigana());
        user.setPostalCode(signupForm.getPostalCode());
        user.setAddress(signupForm.getAddress());
        user.setPhoneNumber(signupForm.getPhoneNumber());
        user.setEmail(signupForm.getEmail());
        user.setPassword(passwordEncoder.encode(signupForm.getPassword())); // パスワードをハッシュ化
        user.setRole(role);
        user.setEnabled(false); // メール認証が完了するまで無効状態

        return userRepository.save(user);
    }

    // ユーザー情報の更新処理
    @Transactional
    public void update(UserEditForm userEditForm) {
        User user = userRepository.getReferenceById(userEditForm.getId());

        // ユーザー情報を更新
        user.setName(userEditForm.getName());
        user.setFurigana(userEditForm.getFurigana());
        user.setPostalCode(userEditForm.getPostalCode());
        user.setAddress(userEditForm.getAddress());
        user.setPhoneNumber(userEditForm.getPhoneNumber());
        user.setEmail(userEditForm.getEmail());

        userRepository.save(user);
    }

    // メールアドレスが登録済みかどうかをチェックする
    public boolean isEmailRegistered(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

    // パスワードと確認用パスワードが一致するかチェックする
    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }

    // ユーザーを有効化する（メール認証後に実行）
    @Transactional
    public void enableUser(User user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

    // メールアドレスが変更されたかどうかをチェックする
    public boolean isEmailChanged(UserEditForm userEditForm) {
        User currentUser = userRepository.getReferenceById(userEditForm.getId());
        return !userEditForm.getEmail().equals(currentUser.getEmail());
    }
}
