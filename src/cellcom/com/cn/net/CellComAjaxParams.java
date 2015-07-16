package cellcom.com.cn.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import net.tsz.afinal.http.AjaxParams;

import org.apache.http.HttpEntity;

public class CellComAjaxParams {
	private AjaxParams ajaxParams;
	public CellComAjaxParams(){
		ajaxParams=new AjaxParams();
	}
	
	public AjaxParams getAjaxParams() {
		return ajaxParams;
	}
	
	public CellComAjaxParams(Map<String, String> source){
		ajaxParams=new AjaxParams(source);
	}
	
	public CellComAjaxParams(String key, String value){
		ajaxParams=new AjaxParams(key,value);
	}
	
	public CellComAjaxParams(Object... keysAndValues){
		ajaxParams=new AjaxParams(keysAndValues);
	}
	
	public void put(String key, String value){
		ajaxParams.put(key, value);
	}
	
	public void put(String key, File file) throws FileNotFoundException{
		ajaxParams.put(key, file);
	}
	
	public void put(String key, InputStream stream){
		ajaxParams.put(key, stream);
	}
	
	public void put(String key, InputStream stream, String fileName){
		ajaxParams.put(key, stream,fileName);
	}
	
	 /**
     * 添加 inputStream 到请求中.
     * @param key the key name for the new param.
     * @param stream the input stream to add.
     * @param fileName the name of the file.
     * @param contentType the content type of the file, eg. application/json
     */
    public void put(String key, InputStream stream, String fileName, String contentType) {
    	ajaxParams.put(key, stream,fileName,contentType);
    }
    
    public void remove(String key){
    	ajaxParams.remove(key);
    }
    
    public String toJson(){
    	return ajaxParams.toJson();
    }
    
    public HttpEntity getEntity(){
    	return ajaxParams.getEntity();
    }

    public String getParamString(){
    	return ajaxParams.getParamString();
    }
}
