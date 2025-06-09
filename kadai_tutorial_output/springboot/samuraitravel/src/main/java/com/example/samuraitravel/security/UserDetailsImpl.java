package com.example.samuraitravel.security; // セキュリティ関連のクラスを含むパッケージ

// UserDetailsImpl クラスは、Spring Security の認証機能を実装するクラス
// ユーザー情報を Spring Security に提供するために UserDetails インターフェースを実装

import java.util.Collection; // ユーザーの権限（ロール）を管理するためのコレクション

import org.springframework.security.core.GrantedAuthority; // ユーザーの権限（ロール）を表すクラス
import org.springframework.security.core.userdetails.UserDetails; // Spring Security のユーザー情報を管理するインターフェース

import com.example.samuraitravel.entity.User; // User（ユーザー）のエンティティ（データベースと対応するクラス）

// UserDetails インターフェースを実装して、Spring Security にユーザー情報を提供
public class UserDetailsImpl implements UserDetails {
    private final User user; // ユーザー情報を保持
    private final Collection<GrantedAuthority> authorities; // ユーザーの権限（ロール）を保持

    // コンストラクタ（クラスの初期化時にユーザー情報と権限を受け取る）
    public UserDetailsImpl(User user, Collection<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    // ユーザー情報を取得するメソッド
    public User getUser() {
        return user;
    }

    // ハッシュ化済みのパスワードを返す（Spring Security が認証時に使用）
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // ログイン時に利用するユーザー名（メールアドレス）を返す
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // ユーザーの権限（ロール）を返す
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // アカウントが期限切れでなければ true を返す（常に true を返す設定）
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // ユーザーがロックされていなければ true を返す（常に true を返す設定）
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }    

    // ユーザーのパスワードが期限切れでなければ true を返す（常に true を返す設定）
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // ユーザーが有効であれば true を返す（データベースの enabled フィールドを参照）
    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
