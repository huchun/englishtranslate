package cc.icoc.javaxu.httpsoap;

import java.io.IOException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import android.app.Activity;
import cc.icoc.javaxu.bean.EnglishBean;

/**
 *	WebService类 负责和服务器端的交互
 *  封装了从服务器获取数据和向服务器发送数据的所有方法 
 * 
 */
public class WebFunction extends Activity{

	String nameSpace;
	String methodName;
	SoapObject request;
	String URL;
	WebFunction function;
	
	
	//获取服务器的英语句子及其翻译
	public EnglishBean getEnglishSentance() throws IOException, XmlPullParserException
	{
		EnglishBean bean = new EnglishBean();
		String nameSpace="http://tempuri.org/"; 

		String methodName="getEnglishStr"; 
		String URL = "http://api.qziedu.cn/english.asmx";
		String SOAP_ACTION = nameSpace + methodName; 

		SoapObject request = new SoapObject(nameSpace, methodName); 
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		envelope.dotNet = true; 
		envelope.bodyOut = request;

		HttpTransportSE hts = new HttpTransportSE(URL);
		hts.call(SOAP_ACTION, envelope);

		//获取返回的数据
		SoapObject object = (SoapObject)envelope.getResponse();
		try
		{
			object = (SoapObject)object.getProperty(1); 
			object = (SoapObject)object.getProperty(0);
		}
		catch(Exception e)
		{
			return null;
		}

		
			for(int i=0 ;i <object.getPropertyCount();i++){
				
			   SoapObject child = (SoapObject)object.getProperty(i);
			   bean.setEnglish(child.getProperty("strEnglish").toString());
			   bean.setChinese(child.getProperty("strChinese").toString());
	  }
		return bean;
	}
	

	
}
