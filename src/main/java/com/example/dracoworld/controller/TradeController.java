package com.example.dracoworld.controller;

import com.example.dracoworld.aop.exception.BoardExceptionHandler;
import com.example.dracoworld.domain.Category;
import com.example.dracoworld.dto.board.NoticeDto;
import com.example.dracoworld.dto.board.TradeDto;
import com.example.dracoworld.dto.comment.TradeCommentDto;
import com.example.dracoworld.service.NoticeService;
import com.example.dracoworld.service.TradeService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	// 거래 게시판 글 상세 조회
	@BoardExceptionHandler(boardType = "trade", errorRedirect = "redirect:/fashionlog/trade")
	@GetMapping("/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		Optional<TradeDto> tradeDtoOpt = tradeService.getTradeById(id);

		// Trade 정보가 없을 때 예외처리
		if (tradeDtoOpt.isEmpty()) {
			throw new IllegalArgumentException("Trade not found");
		}

		TradeDto tradeDto = tradeDtoOpt.get();
		List<TradeCommentDto> tradeCommentDto = tradeService.getCommentsByTradeId(id)
			.orElse(Collections.emptyList());

		model.addAttribute("trade", tradeDto);
		model.addAttribute("tradeComments", tradeCommentDto);
		model.addAttribute("tradeComment", new TradeCommentDto());
		return "trade/detail";
	}

	// 거래 게시판 글 작성 폼
	@GetMapping("/new")
	public String newForm(Model model) {
		model.addAttribute("trade", new TradeDto());
		return "trade/new";
	}

}
