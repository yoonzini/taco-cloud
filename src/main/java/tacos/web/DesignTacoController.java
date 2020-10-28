package tacos.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import lombok.extern.slf4j.Slf4j;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;

@Slf4j
@Controller //컨트롤러로 식별, 컴포넌트 검색을 해야함 : 스프링 애플리케이션 컨텍스트의 빈bean으로 이 클래스의 인스턴스를 자동 생성한다.
@RequestMapping("/design")  // @RequestMapping: 다목적 요청을 처리한다.
public class DesignTacoController {
	
	@PostMapping
	public String processDesign(@Valid Taco design, Errors errors) {
		
		if (errors.hasErrors()) {
			return "design";
		}
		
		// 타코 디자인(선택된 식자재 내역)을 저장한다.
		log.info("Processing design: " + design);
		
		return "redirect:/orders/current";
	}

	
	@GetMapping // spring4.3 - @GetMapping : HTTP GET을 처리한다.
	public String showDesignForm(Model model) {
		
		// 식자재 List
		List<Ingredient> ingredients = Arrays.asList(
				new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
				new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
				new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
				new Ingredient("CARN", "Carnitas", Type.PROTEIN),
				new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
				new Ingredient("LETC", "Lettuce", Type.VEGGIES),
				new Ingredient("CHED", "Cheddar", Type.CHEESE),
				new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
				new Ingredient("SLSA", "Salsa", Type.SAUCE),
				new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
				);
		
		Type[] types = Ingredient.Type.values();
		
		for (Type type : types) {
			// 식자재 유형 (고기, 치즈, 소스 등) List에서 filtering
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
		}
		
		// model 객체의 속성에 있는 데이터는
		// 뷰가 알 수 있는 서블릿 요청 속성들로 복사된다.
		model.addAttribute("taco", new Taco());
		
		return "design";
	}
	
	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		return ingredients
				.stream()
				.filter(x -> x.getType().equals(type))
				.collect(Collectors.toList());
	}
}
