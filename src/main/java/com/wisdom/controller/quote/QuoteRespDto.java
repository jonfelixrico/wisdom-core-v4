package com.wisdom.controller.quote;

/**
 * 
 * @author Felix
 *
 */
public class QuoteRespDto {
	private String id;
	private String authorId;
	private String content;
	
	public QuoteRespDto(String id, String authorId, String content) {
		this.id = id;
		this.authorId = authorId;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public String getAuthorId() {
		return authorId;
	}

	public String getContent() {
		return content;
	}	
}
