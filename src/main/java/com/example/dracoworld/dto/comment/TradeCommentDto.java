package com.example.dracoworld.dto.comment;

import com.example.dracoworld.domain.Member;
import com.example.dracoworld.domain.baseentity.CommentUpdatable;
import com.example.dracoworld.domain.board.Trade;
import com.example.dracoworld.domain.comment.TradeComment;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeCommentDto implements CommentUpdatable {

	private Long id;
	private Long tradeId;
	private Long memberId;

	@Size(max = 25, message = "이름은 25자를 초과할 수 없습니다.")
	private String authorName;

	@Size(max = 50, message = "이메일은 50자를 초과할 수 없습니다.")
	private String authorEmail;

	@Size(max = 65535, message = "글 내용은 65535자를 초과할 수 없습니다.")
	private String content;

	private Boolean commentStatus;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;

	@Override
	public String getContent() {
		return this.content;
	}

	/* TradeCommentDto -> TradeComment 변환 */
	public static TradeComment convertToEntity(Trade foundTrade, TradeCommentDto tradeCommentDto,
		Member member) {
		return TradeComment.builder()
			.member(member)
			.trade(foundTrade)
			.content(tradeCommentDto.getContent())
			.commentStatus(
				tradeCommentDto.getCommentStatus() != null ? tradeCommentDto.getCommentStatus()
					: true)
			.createdAt(tradeCommentDto.getCreatedAt() != null ? tradeCommentDto.getCreatedAt()
				: LocalDateTime.now())
			.updatedAt(tradeCommentDto.getUpdatedAt())
			.deletedAt(tradeCommentDto.getDeletedAt())
			.build();
	}

	/* TradeComment -> TradeCommentDto 변환 */
	public static TradeCommentDto convertToDto(TradeComment tradeComment) {
		return TradeCommentDto.builder()
			.id(tradeComment.getId())
			.memberId(tradeComment.getMember().getMemberId())
			.tradeId(tradeComment.getTrade().getId())
			.authorName(tradeComment.getMember().getNickname())
			.authorEmail(tradeComment.getMember().getEmail())
			.content(tradeComment.getContent())
			.commentStatus(tradeComment.getCommentStatus())
			.createdAt(tradeComment.getCreatedAt())
			.updatedAt(tradeComment.getUpdatedAt())
			.deletedAt(tradeComment.getDeletedAt())
			.build();
	}
}
