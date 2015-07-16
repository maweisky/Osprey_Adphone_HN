package cellcom.com.cn.parse;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;



public class CellComAjaxResultParseXML extends CellComAjaxResultParse{
	public CellComAjaxResultParseXML(String result) {
		// TODO Auto-generated constructor stub
		this.result=result;
	}
	 
	public <T> T read(Class<T> clazz) {
		// TODO Auto-generated method stub
		Serializer serializer = new Persister();
		T a=null;
		try {
			a = serializer.read(clazz, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}
	
	public Object[] readOnlyLayer(String[] xmlparams) {
		// TODO Auto-generated method stub
		return new XmlParser(result).doInBackground(xmlparams);
	}
}
