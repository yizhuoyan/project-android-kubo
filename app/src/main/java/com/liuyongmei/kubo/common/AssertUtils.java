
package com.liuyongmei.kubo.common;

public class AssertUtils {
	

	public static String notBlank(String message,String s){
		if(s==null||(s=s.trim()).length()==0){
			throw  new ThisAppException(message);
		}
		return s;
	}
}
