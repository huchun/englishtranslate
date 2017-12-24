package cc.icoc.javaxu.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;
import cc.icoc.javaxu.bean.EnglishBean;

public class ParseDailyEnglish extends DefaultHandler {

	EnglishBean bean;
	final int English = 1;
	final int Chanese = 2;
	final int Other   = 3;
	int whatObject = 0;
	String string;
	
	public EnglishBean getBean() 
	{
		return bean;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		string = new String(ch, start, length);
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if(whatObject == English)
		{
			bean.setEnglish(string);
		}
		else if (whatObject == Chanese) 
		{
			bean.setChinese(string);
		}

	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		bean = new EnglishBean();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals("en_sentence"))
		{
			whatObject = English;
			Log.i("XU", "english");
		}
		else if (localName.equals("cn_sentence")) 
		{
			whatObject = Chanese;
			Log.i("XU", "chanese");
		}
		else 
		{
			whatObject = Other;
		}
	}

}
