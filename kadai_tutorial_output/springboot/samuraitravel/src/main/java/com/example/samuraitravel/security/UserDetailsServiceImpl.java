package com.example.samuraitravel.security; // セキュリティ関連のクラスを含むパッケージ

// UserDetailsServiceImpl クラスは、Spring Security の認証機能を実装するクラス
// ユーザー情報を Spring Security に提供するために UserDetailsService インターフェースを実装

import java.util.ArrayList; // ユーザーの権限（ロール）を管理するためのリスト
import java.util.Collection; // ユーザーの権限（ロール）を管理するためのコレクション

import org.springframework.security.core.GrantedAuthority; // ユーザーの権限（ロール）を表すクラス
import org.springframework.security.core.authority.SimpleGrantedAuthority; // 権限（ロール）をシンプルな形式で扱うクラス
import org.springframework.security.core.userdetails.UserDetails; // Spring Security のユーザー情報を管理するインターフェース
import org.springframework.security.core.userdetails.UserDetailsService; // Spring Security のユーザー認証を担当するインターフェース
import org.springframework.security.core.userdetails.UsernameNotFoundException; // ユーザーが見つからない場合の例外
import org.springframework.stereotype.Service; // Spring のサービスとして登録するためのアノテーション

import com.example.samuraitravel.entity.User; // User（ユーザー）のエンティティ（データベースと対応するクラス）
import com.example.samuraitravel.repository.UserRepository; // ユーザー情報を取得するためのリポジトリ

@Service // Spring のサービスとして登録（DIコンテナに登録される）
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository; // ユーザー情報を管理するリポジトリ

    // コンストラクタ（クラスの初期化時にユーザーリポジトリを受け取る）
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;        
    }

    // ユーザー名（メールアドレス）を使ってユーザー情報を取得するメソッド
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {  
        try {
            // メールアドレスを使ってユーザー情報を取得
            User user = userRepository.findByEmail(email);

            // ユーザーの役割（ロール）を取得
            String userRoleName = user.getRole().getName();

            // 権限（ロール）を設定
            Collection<GrantedAuthority> authorities = new ArrayList<>();         
            authorities.add(new SimpleGrantedAuthority(userRoleName));

            // ユーザー情報を Spring Security に提供
            return new UserDetailsImpl(user, authorities);
        } catch (Exception e) {
            // ユーザーが見つからない場合は例外をスロー
            throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
        }
    }   
}
