package cc.icoc.javaxu.util;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import cc.icoc.javaxu.bean.EnglishTranslateBean;

public class SaxParse extends DefaultHandler {

	List<EnglishTranslateBean> beans;
	EnglishTranslateBean bean;
	String tagName;
	int flag;
	String content = "";
	String translate = "";
	String explains = "";
	
	public List<EnglishTranslateBean> getDatas() 
	{
		return beans;
	}
	
	//测试
	public EnglishTranslateBean getData() 
	{
		return bean;
	}
	
	public void startDocument() throws SAXException 
	{
		beans = new ArrayList<EnglishTranslateBean>();
		bean = new EnglishTranslateBean();
		super.startDocument();
	}

	public void endDocument() throws SAXException {
		super.endDocument();
		System.out.println("content============"+content);
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		//音标
		if(localName.equals("phonetic"))
		{
			flag = 1;
		}
		//有道词典基本释义
		else if(localName.equals("ex"))
		{
			flag = 2;
		}
		//有道翻译
		else if(localName.equals("paragraph"))
		{
			flag = 3;
		}
		else if(localName.equals("explains"))
		{
			flag = 3;
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		beans.add(bean);
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		String s = new String(ch, start, length);
		content += s;
		
		
		if(flag == 1)
		{
			bean.setPhoetic(s);
		}
		else if(flag == 2)
		{
			explains += s;
			bean.setExplains(explains);
		}
		else if(flag == 3)
		{
			translate += s;
				bean.setParagraph(translate);
		}
	}
	
}
