package com.example.samuraitravel.controller;
//コントローラー
//管理者（Admin）用のコントローラー

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseEditForm;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.service.HouseService;

@Controller
@RequestMapping("/admin/houses") //ルートパスの基準値を設定する
public class AdminHouseController {
//AdminHouseControllerクラスがHouseRepositoryのオブジェクトを利用している
	//AdminHouseControllerクラスがHouseRepositoryのオブジェクトに依存している。
	private final HouseRepository houseRepository;
	private final HouseService houseService;
			//finalで宣言することにより、一度初期化されたあとは変更されません。これにより、安全性が向上します。
	public AdminHouseController(HouseRepository houseRepository, HouseService houseService) {
	//コンストラクタで依存性の注入（DI）を行う（コンストラクタインジェクション）
		this.houseRepository = houseRepository;
		this.houseService = houseService;
	}
	
	@GetMapping
	//このメソッドは、Model を受け取り、ビューにデータを渡すための処理を行う
	public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
		//Mappingwoを使ってList<House>を取得している
		//取得先は/admin/houses
		
		Page<House> housePage;
		//houseRepository.findAll() を呼び出して、
		//データベースに保存されているすべての House(民宿データ) エンティティを取得している
		if (keyword != null && !keyword.isEmpty()) {
			housePage = houseRepository.findByNameLike("%" + keyword +"%", pageable);
		} else {
			housePage = houseRepository.findAll(pageable);
		}
		
		model.addAttribute("housePage", housePage);  
		model.addAttribute("keyword",keyword);
		//取得した houses を model に追加し、ビューに渡します。
		//admin/houses/index というビュー（HTMLページ）内で、
		//houses のデータを使用して一覧を表示できる
		return "admin/houses/index";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model) {
		House house = houseRepository.getReferenceById(id);
		
		model.addAttribute("house", house);
		
		return "admin/houses/show";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("houseRegisterForm", new HouseRegisterForm());
		 return "admin/houses/register";
	}
	
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated HouseRegisterForm houseRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "admin/houses/register";
		}
		
		houseService.create(houseRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "民宿を登録しました。");
		
		return "redirect:/admin/houses";
	}
	
	@GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id, Model model) {
        House house = houseRepository.getReferenceById(id);
        String imageName = house.getImageName();
        HouseEditForm houseEditForm = new HouseEditForm(house.getId(), house.getName(), null, house.getDescription(), house.getPrice(), house.getCapacity(), house.getPostalCode(), house.getAddress(), house.getPhoneNumber());
        
        model.addAttribute("imageName", imageName);
        model.addAttribute("houseEditForm", houseEditForm);
        
        return "admin/houses/edit";
		
	}
	
	@PostMapping("/{id}/update")
	public String update(@ModelAttribute @Validated HouseEditForm houseEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			return "admin/houses/edit";
		
		}
		
		houseService.update(houseEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "民宿情報を編集しました。");
		
		return "redirect:/admin/houses";
	}
	
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		houseRepository.deleteById(id);
		
		redirectAttributes.addFlashAttribute("successMessage", "民宿を削除しました。");
		return "redirect:/admin/houses";
	}
}
