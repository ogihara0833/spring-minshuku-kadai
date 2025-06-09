package com.example.samuraitravel.security; // セキュリティ関連のクラスを含むパッケージ

// WebSecurityConfig クラスは、Spring Security の設定を管理するクラス
// ユーザー認証やアクセス制御を設定する

import org.springframework.context.annotation.Bean; // Spring の Bean を定義するためのアノテーション
import org.springframework.context.annotation.Configuration; // 設定クラスであることを示すアノテーション
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // メソッドレベルのセキュリティを有効化
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // HTTPリクエストのセキュリティ設定を行うクラス
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Webセキュリティを有効化
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // パスワードのハッシュ化を行うクラス
import org.springframework.security.crypto.password.PasswordEncoder; // パスワードのエンコードを管理するインターフェース
import org.springframework.security.web.SecurityFilterChain; // セキュリティフィルタの設定を行うクラス

@Configuration // このクラスが Spring の設定クラスであることを示す
@EnableWebSecurity // Webセキュリティを有効化
@EnableMethodSecurity // メソッドレベルのセキュリティを有効化
public class WebSecurityConfig {

    // セキュリティフィルタの設定
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests                
                .requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**", "/houses", "/houses/{id}", "/stripe/webhook").permitAll()  // すべてのユーザーにアクセスを許可するURL           
                .requestMatchers("/admin/**").hasRole("ADMIN")  // 管理者にのみアクセスを許可するURL
                .anyRequest().authenticated()                   // 上記以外のURLはログインが必要（会員または管理者のどちらでもOK）
            )
            .formLogin(form -> form
                .loginPage("/login")              // ログインページのURL
                .loginProcessingUrl("/login")     // ログインフォームの送信先URL
                .defaultSuccessUrl("/?loggedIn")  // ログイン成功時のリダイレクト先URL
                .failureUrl("/login?error")       // ログイン失敗時のリダイレクト先URL
                .permitAll()  // ログイン・ログアウト関連のURLが誰にでもアクセス可能
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/?loggedOut")  // ログアウト時のリダイレクト先URL
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/stripe/webhook")); // CSRFを無効にする新しい書き方
            
        return http.build();
    }
    
    // パスワードのハッシュ化を行う Bean の定義
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
