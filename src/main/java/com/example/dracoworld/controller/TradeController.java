package com.example.dracoworld.controller;

import com.example.dracoworld.domain.Category;
import com.example.dracoworld.dto.board.NoticeDto;
import com.example.dracoworld.dto.board.TradeDto;
import com.example.dracoworld.service.NoticeService;
import com.example.dracoworld.service.TradeService;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dracoworld/trade")
public class TradeController {

	private final TradeService tradeService;
	private final NoticeService noticeService;

	public TradeController(TradeService tradeService, NoticeService noticeService) {
		this.tradeService = tradeService;
		this.noticeService = noticeService;
	}

	// 거래 게시판 글 목록 조회
	@GetMapping
	public String getAllTradeList(Model model) {
		List<TradeDto> tradeDto = tradeService.getAllTrades()
			.orElse(Collections.emptyList());
		List<NoticeDto> noticeDto = noticeService.getNoticeListByCategory(Category.TRADE)
			.orElse(Collections.emptyList());

		model.addAttribute("trades", tradeDto);
		model.addAttribute("tradeNotice", noticeDto);
		return "trade/list";
	}
}
