
package cellcom.com.cn.parse;

import java.util.List;

import org.xml.sax.ContentHandler;

import android.util.Xml;
import cellcom.com.cn.util.LogMgr;

/**
 * 
 * @author WangZhongjie
 *
 */
public abstract class BaseParser {//extends AsyncTask<String, Integer, Info[]>{

	/*private InputStream data;
	public BaseParser(InputStream data) {
		this.data = data;
	}
	InputStream getData(){
		if(data == null ){
			throw new IllegalStateException("Input stream is null, please set that before parsing");
		}
		return data;
	}
	
	abstract ContentHandler newHandler(String[] params,final List<?> list, StringBuffer state, StringBuffer errorcode);
	
	void parse(ContentHandler handler) throws Exception{
		try{	
			Xml.parse(getData(), Xml.Encoding.UTF_8, handler);
		} catch (Exception e){
			LogMgr.showLog("Exception getting XML data:"+e);
			throw e;
		}
	}*/
	
	private String xml;
	
	public BaseParser(String xml) {
		this.xml = xml;
	}
	String getDataXML(){
		if(xml == null ){
			throw new IllegalStateException("return xml  is null, please set that before parsing");
		}
		return xml;
	}
	
	abstract ContentHandler newHandler(String[] params,final List<?> list, StringBuffer state, StringBuffer errorcode,StringBuffer msg);
	
	void parse(ContentHandler handler) throws Exception{
		try{	
			Xml.parse(getDataXML(),handler);
		} catch (Exception e){
			LogMgr.showLog("Exception getting XML data:"+e);
			throw e;
		}
	}
	
}
