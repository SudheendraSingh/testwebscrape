package com.sainsbury.scrape.model;

import java.util.Collections;
import java.util.List;

public class Result {
	List<RipeFruit> results = Collections.emptyList();
	Double total;

	public List<RipeFruit> getResults() {
		return results;
	}

	public void setResults(List<RipeFruit> results) {
		this.results = results;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

}
