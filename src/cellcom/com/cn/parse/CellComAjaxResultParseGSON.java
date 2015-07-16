package cellcom.com.cn.parse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class CellComAjaxResultParseGSON extends CellComAjaxResultParse {
	public CellComAjaxResultParseGSON(String result) {
		// TODO Auto-generated constructor stub
		this.result = result;
	}

	public <T> T read(Class<T> clazz) {
		// TODO Auto-generated method stub
		GsonBuilder gsonb = new GsonBuilder();
//		gsonb.registerTypeAdapter(Date.class, new DateDeserializerUtils());
		gsonb.setDateFormat("yyyy-MM-dd");
		Gson gson = gsonb.create();
		java.lang.reflect.Type type = 
			new com.google.gson.reflect.TypeToken<T>(){}.getType();
//		Gson gson = new Gson();
		T a = gson.fromJson(result, clazz);
		return a;
	}

	/**
	 * 日期序列化实用工具类
	 * @author Lupeng
	 * @date   2011-08-06
	 */
	public class DateDeserializerUtils implements JsonDeserializer<java.util.Date>{
		@Override
		public java.util.Date deserialize(JsonElement json, Type type,
				JsonDeserializationContext context) throws JsonParseException {
			String JSONDateToMilliseconds = "^\\d{4}-\\d{1,2}-\\d{1,2}$";
	        Pattern pattern = Pattern.compile(JSONDateToMilliseconds);
	        Matcher matcher = pattern.matcher(json.getAsJsonPrimitive().getAsString());
	        String result = matcher.replaceAll("$2");
	 
	          return new Date(result);
		}

	}

	public Object[] readOnlyLayer(String[] xmlparams) {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, String>> infos = new ArrayList<HashMap<String, String>>();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(result);
			String state = jsonObject.get("state").toString();
			String errorcode = jsonObject.get("errorcode").toString();
			String msg = jsonObject.get("msg").toString();
			JSONArray jsonArray = jsonObject.getJSONArray("parambuf");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject event = jsonArray.getJSONObject(i);
				Iterator keys = event.keys();
				HashMap<String, String> hashMap = new HashMap<String, String>();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = event.getString(key);
					hashMap.put(key, value);
				}
				infos.add(hashMap);
			}
			return new Object[] { state, errorcode, msg, infos };
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
