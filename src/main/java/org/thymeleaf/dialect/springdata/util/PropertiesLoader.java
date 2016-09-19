package org.thymeleaf.dialect.springdata.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesLoader {
	
	public static Properties loadProperties(String resourceName) throws IOException{
		final Properties props = new Properties();
		InputStream in = null;
		try{
			in = PropertiesLoader.class.getResourceAsStream(resourceName);
			props.load(in);
			
			return props;
		} finally {
			if( in!=null ){
				in.close();
			}
		}
	}

}
