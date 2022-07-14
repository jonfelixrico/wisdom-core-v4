package com.wisdom.quote.controller;

import java.time.Instant;

/**
 * 
 * @author Felix
 *
 */
public class QuoteReceiveReqDto {
	/**
	 * Id of the user who received the quote
	 */
	private String receivingUserId;
	
	/**
	 * Id of the channel where the receive happened
	 */
	private String receivingChannelId;
	
	/**
	 * The timestamp where the quote got received
	 */
	private Instant receiveDt;
	
	public QuoteReceiveReqDto(String receivingUserId, String receivingChannelId, Instant receiveDt) {
		this.receivingUserId = receivingUserId;
		this.receivingChannelId = receivingChannelId;
		this.receiveDt = receiveDt;
	}

	public String getReceivingUserId() {
		return receivingUserId;
	}

	public Instant getReceiveDt() {
		return receiveDt;
	}

	public String getReceivingChannelId() {
		return receivingChannelId;
	}	
}
