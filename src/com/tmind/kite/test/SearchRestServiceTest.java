package com.tmind.kite.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tmind.kite.webservice.SearchRestService;

public class SearchRestServiceTest {
	private SearchRestService search = new SearchRestService();
	
	@Test
	public void testSearchRestService(){
		String result = search.getAutoCompleteArrays("test", "13851483034", "1234", "1");
		assertNull(result);
	}
}
