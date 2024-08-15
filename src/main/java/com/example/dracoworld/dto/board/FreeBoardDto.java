package com.example.dracoworld.dto.board;

import com.example.dracoworld.domain.Member;
import com.example.dracoworld.domain.baseentity.Updatable;
import com.example.dracoworld.domain.board.FreeBoard;
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
public class FreeBoardDto implements Updatable {

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

	public static FreeBoardDto convertToDto(FreeBoard freeBoard) {
		return FreeBoardDto.builder()
			.id(freeBoard.getId())
			.memberId(freeBoard.getMember().getMemberId())
			.authorName(freeBoard.getMember().getNickname())
			.authorEmail(freeBoard.getMember().getEmail())
			.title(freeBoard.getTitle())
			.content(freeBoard.getContent())
			.postStatus(freeBoard.getStatus())
			.createdAt(freeBoard.getCreatedAt())
			.updatedAt(freeBoard.getUpdatedAt())
			.deletedAt(freeBoard.getDeletedAt())
			.build();
	}

	public static FreeBoard convertToEntity(FreeBoardDto freeBoardDto, Member member) {
		return FreeBoard.builder()
			.id(freeBoardDto.getId())
			.member(member)
			.title(freeBoardDto.getTitle())
			.content(freeBoardDto.getContent())
			.status(freeBoardDto.getPostStatus())
			.createdAt(freeBoardDto.getCreatedAt())
			.updatedAt(freeBoardDto.getUpdatedAt())
			.deletedAt(freeBoardDto.getDeletedAt())
			.build();
	}
}
