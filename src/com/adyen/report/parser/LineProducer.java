package com.adyen.report.parser;

import java.text.ParseException;

public interface LineProducer {

	String getLine() throws ParseException;
	int getLineNo();

}
