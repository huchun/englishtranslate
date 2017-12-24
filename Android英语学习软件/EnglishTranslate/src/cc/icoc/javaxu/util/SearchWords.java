package cc.icoc.javaxu.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cc.icoc.javaxu.bean.EnglishTranslateBean;

public class SearchWords {

	private final static String URL="http://fanyi.youdao.com/openapi.do?keyfrom=api-qziedu&key=492065641&type=data&doctype=xml&version=1.1&q=";
	
	public EnglishTranslateBean transWord(String word){
		
		EnglishTranslateBean bean = null;
		InputStream inStr=null;
		List<EnglishTranslateBean> beans;
		
		try {
			URL url = new URL(URL+word);			
		    HttpURLConnection conn=(HttpURLConnection) url.openConnection(); 
		    conn.connect();
		    inStr=conn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}     
		
		InputStream in= inStr;
		
		 SAXParser parser = null;  
	        try {  
	            //构建SAXParser  
	            parser = SAXParserFactory.newInstance().newSAXParser();  
	            //实例化  DefaultHandler对象  
	            SaxParse parseXml=new SaxParse();  
	            //调用parse()方法  
	            parser.parse(in, parseXml);
	            
	            beans = parseXml.getDatas();
	            bean = beans.get(0);
	            
	        }catch (Exception e) {
	        	e.printStackTrace();
			}
		return bean;
		
	}
	
}
