package cellcom.com.cn.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.ContentHandler;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import cellcom.com.cn.util.LogMgr;


/**
 * 通用解析模块
 * @author WangZhongjie
 *
 *@return Object[] 
 *resultObject[0] 状态 Y成功 N失败
 *resultObject[1] 错误码 ，成功为0
 *resultObject[2] 错误描述
 *resultObject[3] 返回解析后的haspmap(成功时）失败时,是个空haspmap
 *
 *
 *返回的xml标准格式如下：
		<?xml version="1.0" encoding="UTF-8"?>                                      
		<data>                                                                      
		<state>Y</state>                                                            
		<errorcode>0</errorcode>          
		<msg>错误描述</msg>                                                    
		<parambuf>                                                                  
			<info>                                                                      
				<vid>1</vid>                                                                
				<name>长沙--橘子洲大桥东往西</name>                                         
				<number></number>                                     
			</info>                                                                     
			<info>                                                                      
				<vid>2</vid>                                                                
				<name>长沙--五一大道黄兴路口</name>                                         
				<number></number>                                        
			</info>                                                                     
			<info>                                                                      
				<vid>4</vid>                                                                
				<name>长沙--五一大道芙蓉广场（东西向）</name>                               
				<number></number>
		 	</info>                                                           
		 </parambuf>    
		 </data>        
 */

public class XmlParser extends BaseParser{
	private HashMap<String, String> currentInfo=new HashMap<String,String>();
	private int m=0;
	
	/**
	 * @param data
	 */
	public XmlParser(String data) {
		super(data);
	}

	public Object[] doInBackground(String[] params) {
		ArrayList<HashMap<String, String>> infos = new ArrayList<HashMap<String, String>>();
		StringBuffer state = new StringBuffer();
		StringBuffer errorcode = new StringBuffer();
		StringBuffer msg = new StringBuffer();
		
		try {
			parse(newHandler(params,infos, state, errorcode,msg));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			state.append("N");
			errorcode.append("-110");
			msg.append("解析数据失败");
			LogMgr.showLog("XmlParser is error:"+e.toString());
		}
		//Object[] array = new Object[infos.size()];		
		LogMgr.showLog("XmlParser.java->infos.size = "+infos.size()+",state="+state.toString()+"|errorcode="+errorcode.toString()+"|msg="+msg.toString());
		return new Object[]{state.toString(), errorcode.toString(),msg.toString(),infos};//infos.toArray(array)};
	}
	
	protected ContentHandler newHandler(final String[] params,final List infos, final StringBuffer currentState, final StringBuffer currentErrorcode ,final StringBuffer currentMsg){	
		RootElement rootData = new RootElement("data");
		Element state = rootData.getChild("state");
		state.setEndTextElementListener(
			new EndTextElementListener(){
    			public void end(String body) {
    				currentState.append(body);
    			}
    		}
		);
		
		Element errorcode = rootData.getChild("errorcode");
		errorcode.setEndTextElementListener(
			new EndTextElementListener(){
    			public void end(String body) {
    				currentErrorcode.append(body);
    			}
    		}
		);
		
		Element msg = rootData.getChild("msg");
		msg.setEndTextElementListener(
			new EndTextElementListener(){
    			public void end(String body) {
    				currentMsg.append(body);
    			}
    		}
		);
		Element parambuf = rootData.getChild("parambuf");		
		Element info = parambuf.getChild("info");		
		info.setEndElementListener(
				new EndElementListener(){
					public void end() {//单个info体结束后运行这里		
						if(currentInfo!=null && currentInfo.size()>0){
							infos.add(currentInfo);//将已保存了一个info体的hashmap存入list中	
							currentInfo=new HashMap<String,String>();//重新实例化hashmap,待保存下一个info体内容
							m=0;//xml节点参数下标清零	
						}						
					}
				}
		);
		
		
		if(params!=null && params.length>0){			
			for(int i=0;i<params.length;i++){				
				info.getChild(params[i]).setEndTextElementListener(//单个info体内参数循环
						new EndTextElementListener(){
			    			public void end(String body) {
			    				int a=m;
			    				if(body!=null && !"".equalsIgnoreCase(body.trim())){
			    					currentInfo.put(params[m], body);//将参数和值存入hashmap
				    				m=m+1;	
			    				}else{
			    					currentInfo.put(params[m], "");//将参数和值存入hashmap
			    					m=m+1;
			    				}
			    			}
			    		}
					);			
			}
		}		
		return rootData.getContentHandler();
	}
}