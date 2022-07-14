/**
 * 
 */
package com.wisdom.controller.quote;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Felix
 *
 */
@RestController
public class QuoteController {
	@GetMapping("/guild/{guildId}/quotes/random")
	QuoteRespDto getRandomQuote(@PathVariable String guildId) throws Exception {
		throw new Exception("noop");
	}
	
	@PostMapping("/guild/{guildId}/quotes/{quoteId}/receives")
	void receiveQuote(@RequestBody QuoteReceiveReqDto body) throws Exception {
		throw new Exception("noop");
	}
}
