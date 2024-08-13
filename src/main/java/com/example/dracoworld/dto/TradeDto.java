package com.example.dracoworld.dto;

import com.example.dracoworld.domain.Member;
import com.example.dracoworld.domain.baseentity.Updatable;
import com.example.dracoworld.domain.board.FreeBoard;
import com.example.dracoworld.domain.board.Trade;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeDto implements Updatable {

	private Long id;
	private Long memberId;

	@Size(max = 25, message = "이름은 25자를 초과할 수 없습니다.")
	private String authorName;

	@Size(max = 50, message = "이메일은 50자를 초과할 수 없습니다.")
	private String authorEmail;

	@Size(max = 250, message = "제목은 250자를 초과할 수 없습니다.")
	private String title;

	@Size(max = 65535, message = "내용은 65535자를 초과할 수 없습니다.")
	private String content;

	private Boolean postStatus;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public String getContent() {
		return this.content;
	}

	public static TradeDto convertToDto(Trade trade) {
		return TradeDto.builder()
			.id(trade.getId())
			.memberId(trade.getMember().getMemberId())
			.authorName(trade.getMember().getNickname())
			.authorEmail(trade.getMember().getEmail())
			.title(trade.getTitle())
			.content(trade.getContent())
			.postStatus(trade.getStatus())
			.createdAt(trade.getCreatedAt())
			.updatedAt(trade.getUpdatedAt())
			.deletedAt(trade.getDeletedAt())
			.build();
	}

	public static Trade convertToEntity(TradeDto tradeDto, Member member) {
		return Trade.builder()
			.id(tradeDto.getId())
			.member(member)
			.title(tradeDto.getTitle())
			.content(tradeDto.getContent())
			.status(tradeDto.getPostStatus())
			.createdAt(tradeDto.getCreatedAt())
			.updatedAt(tradeDto.getUpdatedAt())
			.deletedAt(tradeDto.getDeletedAt())
			.build();
	}
}
