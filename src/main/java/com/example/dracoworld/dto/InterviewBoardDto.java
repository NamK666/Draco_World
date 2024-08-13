package com.example.dracoworld.dto;

import com.example.dracoworld.domain.Member;
import com.example.dracoworld.domain.baseentity.Updatable;
import com.example.dracoworld.domain.board.InterviewBoard;
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
public class InterviewBoardDto implements Updatable {

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

	public static InterviewBoardDto convertToDto(InterviewBoard interviewBoard) {
		return InterviewBoardDto.builder()
			.id(interviewBoard.getId())
			.memberId(interviewBoard.getMember().getMemberId())
			.authorName(interviewBoard.getMember().getNickname())
			.authorEmail(interviewBoard.getMember().getEmail())
			.title(interviewBoard.getTitle())
			.content(interviewBoard.getContent())
			.postStatus(interviewBoard.getStatus())
			.createdAt(interviewBoard.getCreatedAt())
			.updatedAt(interviewBoard.getUpdatedAt())
			.deletedAt(interviewBoard.getDeletedAt())
			.build();
	}

	public static InterviewBoard convertToEntity(InterviewBoardDto interviewBoardDto,
		Member member) {
		return InterviewBoard.builder()
			.id(interviewBoardDto.getId())
			.member(member)
			.title(interviewBoardDto.getTitle())
			.content(interviewBoardDto.getContent())
			.status(interviewBoardDto.getPostStatus())
			.createdAt(interviewBoardDto.getCreatedAt())
			.updatedAt(interviewBoardDto.getUpdatedAt())
			.deletedAt(interviewBoardDto.getDeletedAt())
			.build();
	}
}
