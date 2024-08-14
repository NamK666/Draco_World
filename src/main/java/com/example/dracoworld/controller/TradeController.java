package com.example.dracoworld.controller;

import com.example.dracoworld.dto.TradeDto;
import com.example.dracoworld.service.TradeService;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dracoworld/trade")
public class TradeController {

	private final TradeService tradeService;

	public TradeController(TradeService tradeService) {
		this.tradeService = tradeService;
	}

	// 거래 게시판 글 목록 조회
	@GetMapping
	public String getAllTradeList(Model model) {
		List<TradeDto> tradeDto = tradeService.getAllTrades()
			.orElse(Collections.emptyList());

		model.addAttribute("trades", tradeDto);
		return "trade/list";
	}
}
