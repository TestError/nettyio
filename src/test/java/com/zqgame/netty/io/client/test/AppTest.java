package com.zqgame.netty.io.client.test;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description
 * @auther peng.chen
 * @create 2018/3/15 13:18
 */
public class AppTest {
	private static Logger logger = LoggerFactory.getLogger(AppTest.class);

	/**
	 * @description
	 * @auther peng.chen
	 * @create 2018/3/14 13:29
	 */
	@Test
	public void testApp() throws UnsupportedEncodingException {
		logger.debug("test");

		Assert.assertTrue(true);

//		URLEncoder.encode();
		Pattern pattern = Pattern.compile("\\%\\S{2}");

		Matcher matcher = pattern.matcher("%2BAAAB%2AAAA%2F%2B");

		StringBuffer stringBuffer = new StringBuffer();
		while (matcher.find()) {
			String result = matcher.group();
			matcher.appendReplacement(stringBuffer, result.toLowerCase());
		}
		matcher.appendTail(stringBuffer);
		logger.debug(stringBuffer.toString());


	}
}
