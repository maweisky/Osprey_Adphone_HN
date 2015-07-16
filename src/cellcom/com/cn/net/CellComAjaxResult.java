package cellcom.com.cn.net;

import cellcom.com.cn.parse.CellComAjaxResultParse;
import cellcom.com.cn.parse.CellComAjaxResultParseGSON;
import cellcom.com.cn.parse.CellComAjaxResultParseXML;


public class CellComAjaxResult {
	public enum ParseType{
		GSON,XML
	}
	private CellComAjaxResultParse cellComAjaxResultParse;
	private String result;
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public CellComAjaxResult(String result) {
		// TODO Auto-generated constructor stub
		this.result=result;
	}
	
	/**
	 * 
	 * @param <T> 
	 * @param clazz 返回数据的类型
	 * @param parseType 解析的方式  CellComAjaxResult.ParseType.GSON,CellComAjaxResult.ParseType.XML
	 * @return
	 */
	public <T> T read(Class<T> clazz,ParseType parseType) {
		// TODO Auto-generated method stub
		if(ParseType.GSON==parseType){
			cellComAjaxResultParse=new CellComAjaxResultParseGSON(result);
			return cellComAjaxResultParse.read(clazz);
		}else if(ParseType.XML==parseType){
			cellComAjaxResultParse=new CellComAjaxResultParseXML(result);
			return cellComAjaxResultParse.read(clazz);
		}else{
			return null;
		}
	}
	
	/**
	 * 
	 * @param xmlparams 要解析的xml key
	 * @param parseType 解析的方式  CellComAjaxResult.ParseType.GSON,CellComAjaxResult.ParseType.XML
	 * @return
	 */
	public Object[] readOnlyLayer(String[] xmlparams,ParseType parseType) {
		// TODO Auto-generated method stub
		if(ParseType.GSON==parseType){
			cellComAjaxResultParse=new CellComAjaxResultParseGSON(result);
			return cellComAjaxResultParse.readOnlyLayer(xmlparams);
		}else if(ParseType.XML==parseType){
			cellComAjaxResultParse=new CellComAjaxResultParseXML(result);
			return cellComAjaxResultParse.readOnlyLayer(xmlparams);
		}else{
			return null;
		}
	}
	
}
