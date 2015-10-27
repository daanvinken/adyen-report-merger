package com.adyen.report.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class ControllerTest {

	@Test
	public void testReadSettlementDetailReport() {
		List<String> testlist = new ArrayList();
		HashMap <String, List> test = new HashMap <String, List>();
		test.put("AAP", testlist);
		test.put("Hond", testlist);
		test.get("AAP").add("Apen");
		test.get("AAP").add("Aperne");
		
		System.out.println(test);
		
	}



}
