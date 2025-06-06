package com.example.samuraitravel.service; // サービス関連のクラスを含むパッケージ

// VerificationTokenService クラスは、ユーザーの認証トークンを管理するサービスクラス
// ユーザーのメール認証やアカウント有効化をサポートする

import org.springframework.stereotype.Service; // Spring のサービスとして登録するためのアノテーション
import org.springframework.transaction.annotation.Transactional; // トランザクション管理を行うためのアノテーション

import com.example.samuraitravel.entity.User; // User（ユーザー）のエンティティ（データベースと対応するクラス）
import com.example.samuraitravel.entity.VerificationToken; // VerificationToken（認証トークン）のエンティティ（データベースと対応するクラス）
import com.example.samuraitravel.repository.VerificationTokenRepository; // 認証トークンのデータを管理するリポジトリ

@Service // Spring のサービスとして登録（DIコンテナに登録される）
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository; // 認証トークンのデータを管理するリポジトリ

    // コンストラクタ（クラスの初期化時にリポジトリを受け取る）
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {        
        this.verificationTokenRepository = verificationTokenRepository;
    } 

    // ユーザーの認証トークンを作成する処理
    @Transactional // データベースの更新処理をトランザクション管理する
    public void create(User user, String token) {
        VerificationToken verificationToken = new VerificationToken();

        // ユーザー情報と認証トークンを設定
        verificationToken.setUser(user);
        verificationToken.setToken(token);        

        // データベースに保存
        verificationTokenRepository.save(verificationToken);
    }    

    // トークンの文字列で検索し、該当する VerificationToken を取得する
    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }    
}
