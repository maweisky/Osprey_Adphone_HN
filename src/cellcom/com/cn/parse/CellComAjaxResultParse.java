package cellcom.com.cn.parse;



public abstract class CellComAjaxResultParse {
	protected String result;
	 
	public abstract <T> T read(Class<T> clazz);
	
	public abstract Object[] readOnlyLayer(String[] xmlparams) ;
}
