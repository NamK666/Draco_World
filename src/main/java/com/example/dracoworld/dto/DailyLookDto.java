package com.example.dracoworld.dto;

import com.example.dracoworld.domain.Member;
import com.example.dracoworld.domain.baseentity.Updatable;
import com.example.dracoworld.domain.board.DailyLook;
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
public class DailyLookDto implements Updatable {

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

	public static DailyLookDto convertToDto(DailyLook dailyLook) {
		return DailyLookDto.builder()
			.id(dailyLook.getId())
			.memberId(dailyLook.getMember().getMemberId())
			.authorName(dailyLook.getMember().getNickname())
			.authorEmail(dailyLook.getMember().getEmail())
			.title(dailyLook.getTitle())
			.content(dailyLook.getContent())
			.postStatus(dailyLook.getStatus())
			.createdAt(dailyLook.getCreatedAt())
			.updatedAt(dailyLook.getUpdatedAt())
			.deletedAt(dailyLook.getDeletedAt())
			.build();
	}

	public static DailyLook convertToEntity(DailyLookDto dailyLookDto, Member member) {
		return DailyLook.builder()
			.id(dailyLookDto.getId())
			.member(member)
			.title(dailyLookDto.getTitle())
			.content(dailyLookDto.getContent())
			.status(dailyLookDto.getPostStatus())
			.createdAt(dailyLookDto.getCreatedAt())
			.updatedAt(dailyLookDto.getUpdatedAt())
			.deletedAt(dailyLookDto.getDeletedAt())
			.build();
	}
}
