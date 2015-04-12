package com.tmind.kite.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tmind.kite.webservice.QueryLongKiteStatusRestService;

public class QueryLongKiteStatusRestServiceTest {
	private QueryLongKiteStatusRestService  queryLongKiteStatus = new QueryLongKiteStatusRestService();
	@Test
	public void testRetreiveLongKiteServiceStatusTest(){
		String result = queryLongKiteStatus.retreiveLongKiteServiceStatus("13851483034", "1234", "0");
		assertNotNull(result);
	}
}
