package com.yjh.spring_3.core;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;



public class ExceptionUtils {
	/**
	 * 输出堆栈信息到日志中
	 * @param e 堆栈信息
	 * @param className 类名（当前类名：this.getClass().getName()）
	 */
	public static void getExceptionMessage(Exception e,String className) {
		Logger logger = Logger.getLogger(className);
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			// 将出错的栈信息输出到printWriter中
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (pw != null) {
				pw.close();
			}
		}
		logger.info("错误异常信息日志：{  "+sw.toString()+"  }");
	}
}
