package com.wisdom.quote.controller;

import java.time.Instant;

public class QuoteReqDto {
	/**
	 * Id of who said the quote
	 */
	private String authorId;
	
	/**
	 * The text content of the quote
	 */
	private String content;
	
	/**
	 * Timestamp of when the quote got submitted
	 */
	private Instant submitDt;
	
	/**
	 * Id of who submitted the quote
	 */
	private String submitterId;

	public QuoteReqDto(String authorId, String content, Instant submitDt, String submitterId) {
		this.authorId = authorId;
		this.content = content;
		this.submitDt = submitDt;
		this.submitterId = submitterId;
	}

	public String getAuthorId() {
		return authorId;
	}

	public String getContent() {
		return content;
	}

	public Instant getSubmitDt() {
		return submitDt;
	}

	public String getSubmitterId() {
		return submitterId;
	}
}
