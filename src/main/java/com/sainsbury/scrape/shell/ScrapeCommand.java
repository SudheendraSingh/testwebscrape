package com.sainsbury.scrape.shell;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sainsbury.scrape.model.Result;
import com.sainsbury.scrape.model.RipeFruit;
import com.sainsbury.scrape.service.FruitScrapeService;

@ShellComponent
public class ScrapeCommand {
	@Autowired
	private FruitScrapeService fruitScrapeService;

	@ShellMethod(value = "Scrape the url.", group = "Sainsbury Web Scraping")
	public String scrapeweb(String url) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		List<RipeFruit> ripeFruitsAfterScraping = fruitScrapeService.getRipeFruitsAfterScraping(url);
		Result result = new Result();
		result.setResults(ripeFruitsAfterScraping);
		result.setTotal(ripeFruitsAfterScraping.stream().mapToDouble(RipeFruit::getUnitPrice).sum());
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(result);
		} catch (JsonProcessingException jsonProcessingException) {
			// TODO Auto-generated catch block
			jsonProcessingException.printStackTrace();
		}
		System.out.println("jsonString :" + result);
		return jsonString;
	}
}
