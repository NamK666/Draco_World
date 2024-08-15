package com.example.dracoworld.dto.board;

import com.example.dracoworld.domain.Member;
import com.example.dracoworld.domain.baseentity.Updatable;
import com.example.dracoworld.domain.board.LookBook;
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
public class LookBookDto implements Updatable {

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

	public static LookBookDto convertToDto(LookBook lookBook) {
		return LookBookDto.builder()
			.id(lookBook.getId())
			.memberId(lookBook.getMember().getMemberId())
			.authorName(lookBook.getMember().getNickname())
			.authorEmail(lookBook.getMember().getEmail())
			.title(lookBook.getTitle())
			.content(lookBook.getContent())
			.postStatus(lookBook.getStatus())
			.createdAt(lookBook.getCreatedAt())
			.updatedAt(lookBook.getUpdatedAt())
			.deletedAt(lookBook.getDeletedAt())
			.build();
	}

	public static LookBook convertToEntity(LookBookDto lookBookDto, Member member) {
		return LookBook.builder()
			.id(lookBookDto.getId())
			.member(member)
			.title(lookBookDto.getTitle())
			.content(lookBookDto.getContent())
			.status(lookBookDto.getPostStatus())
			.createdAt(lookBookDto.getCreatedAt())
			.updatedAt(lookBookDto.getUpdatedAt())
			.deletedAt(lookBookDto.getDeletedAt())
			.build();
	}
}
