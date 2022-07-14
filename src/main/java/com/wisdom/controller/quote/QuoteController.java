/**
 * 
 */
package com.wisdom.controller.quote;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Felix
 *
 */
@RestController
public class QuoteController {
	@RequestMapping("/quotes/random")
	QuoteRespDto getRandomQuote() throws Exception {
		throw new Exception("noop");
	}
}
