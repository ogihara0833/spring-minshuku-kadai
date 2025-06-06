package com.example.samuraitravel.service; // サービス関連のクラスを含むパッケージ

// HouseService クラスは宿泊施設の管理を担当するサービスクラス
// 宿泊施設の登録・更新・画像処理を行う

import java.io.IOException; // ファイル操作時の例外処理を行うためのクラス
import java.nio.file.Files; // ファイルのコピーや作成を行うためのクラス
import java.nio.file.Path; // ファイルのパスを扱うためのクラス
import java.nio.file.Paths; // ファイルのパスを生成するためのクラス
import java.util.UUID; // 一意の識別子（ファイル名の生成など）を扱うためのクラス

import org.springframework.stereotype.Service; // Spring のサービスとして登録するためのアノテーション
import org.springframework.transaction.annotation.Transactional; // トランザクション管理を行うためのアノテーション
import org.springframework.web.multipart.MultipartFile; // ファイルアップロードを扱うクラス（宿泊施設の画像など）

import com.example.samuraitravel.entity.House; // House（宿泊施設）のエンティティ（データベースと対応するクラス）
import com.example.samuraitravel.form.HouseEditForm; // 宿泊施設の編集フォーム
import com.example.samuraitravel.form.HouseRegisterForm; // 宿泊施設の登録フォーム
import com.example.samuraitravel.repository.HouseRepository; // 宿泊施設のデータを管理するリポジトリ

@Service // Spring のサービスとして登録（DIコンテナに登録される）
public class HouseService {
    private final HouseRepository houseRepository; // 宿泊施設のデータを管理するリポジトリ

    // コンストラクタ（クラスの初期化時にリポジトリを受け取る）
    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;        
    }    

    // 宿泊施設の新規登録処理
    @Transactional // データベースの更新処理をトランザクション管理する
    public void create(HouseRegisterForm houseRegisterForm) {
        House house = new House();        
        MultipartFile imageFile = houseRegisterForm.getImageFile();

        // 画像ファイルがアップロードされている場合、ファイル名を生成して保存
        if (!imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename(); 
            String hashedImageName = generateNewFileName(imageName);
            Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
            copyImageFile(imageFile, filePath);
            house.setImageName(hashedImageName);
        }

        // 宿泊施設の情報を設定
        house.setName(houseRegisterForm.getName());                
        house.setDescription(houseRegisterForm.getDescription());
        house.setPrice(houseRegisterForm.getPrice());
        house.setCapacity(houseRegisterForm.getCapacity());
        house.setPostalCode(houseRegisterForm.getPostalCode());
        house.setAddress(houseRegisterForm.getAddress());
        house.setPhoneNumber(houseRegisterForm.getPhoneNumber());

        // データベースに保存
        houseRepository.save(house);
    }  

    // 宿泊施設の更新処理
    @Transactional // データベースの更新処理をトランザクション管理する
    public void update(HouseEditForm houseEditForm) {
        House house = houseRepository.getReferenceById(houseEditForm.getId());
        MultipartFile imageFile = houseEditForm.getImageFile();

        // 画像ファイルがアップロードされている場合、ファイル名を生成して保存
        if (!imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename(); 
            String hashedImageName = generateNewFileName(imageName);
            Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
            copyImageFile(imageFile, filePath);
            house.setImageName(hashedImageName);
        }

        // 宿泊施設の情報を更新
        house.setName(houseEditForm.getName());                
        house.setDescription(houseEditForm.getDescription());
        house.setPrice(houseEditForm.getPrice());
        house.setCapacity(houseEditForm.getCapacity());
        house.setPostalCode(houseEditForm.getPostalCode());
        house.setAddress(houseEditForm.getAddress());
        house.setPhoneNumber(houseEditForm.getPhoneNumber());

        // データベースに保存
        houseRepository.save(house);
    }    

    // UUIDを使って生成したファイル名を返す
    public String generateNewFileName(String fileName) {
        String[] fileNames = fileName.split("\\.");                
        for (int i = 0; i < fileNames.length - 1; i++) {
            fileNames[i] = UUID.randomUUID().toString();            
        }
        String hashedFileName = String.join(".", fileNames);
        return hashedFileName;
    }     

    // 画像ファイルを指定したファイルにコピーする
    public void copyImageFile(MultipartFile imageFile, Path filePath) {           
        try {
            Files.copy(imageFile.getInputStream(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }          
    } 
}
