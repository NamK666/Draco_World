package com.example.dracoworld.service;

public interface BoardService {

	/**
	 * 주어진 게시물의 작성자가 현재 사용자인지 확인합니다.
	 *
	 * @param postId      확인하고자 하는 게시물의 ID
	 * @param memberEmail 현재 사용자의 이메일
	 * @return 현재 사용자가 게시물의 작성자인 경우 true, 그렇지 않은 경우 false
	 */
	boolean isPostAuthor(Long postId, String memberEmail);

	/**
	 * 주어진 댓글 작성자가 현재 사용자인지 확인합니다.
	 *
	 * @param commentId   확인하고자 하는 게시물의 ID
	 * @param memberEmail 현재 사용자의 이메일
	 */
	boolean isCommentAuthor(Long commentId, String memberEmail);

}
