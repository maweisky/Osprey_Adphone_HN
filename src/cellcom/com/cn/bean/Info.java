package cellcom.com.cn.bean;

import java.lang.reflect.Field;
import org.json.JSONException;
import org.json.JSONObject;
import cellcom.com.cn.util.LogMgr;

public class Info {
	public String deviceid;
	public String imsi;
	public long timestamp;
	public String version;
	public String os;
	public String service;
	public String os_version;
	public String devicename;
	public String screen_width;
	public String screen_height;
	public String carrier;
	public String network_connection;

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApp_version() {
		return version;
	}

	public void setApp_version(String app_version) {
		this.version = app_version;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getOs_version() {
		return os_version;
	}

	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}

	public String getDevicename() {
		return devicename;
	}

	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}

	public String getScreen_width() {
		return screen_width;
	}

	public void setScreen_width(String screen_width) {
		this.screen_width = screen_width;
	}

	public String getScreen_height() {
		return screen_height;
	}

	public void setScreen_height(String screen_height) {
		this.screen_height = screen_height;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getNetwork_connection() {
		return network_connection;
	}

	public void setNetwork_connection(String network_connection) {
		this.network_connection = network_connection;
	}

	public String toString() {
		return toString(this);
	}

	public JSONObject toJSONObject() {
		return toJSONObject(this);
	}
	protected String toString(Info info) {
		StringBuilder sb = new StringBuilder();
		Field[] fields = info.getClass().getFields();
		for (Field field : fields) {
			sb.append(field.getName());
			sb.append(":");
			try {
				sb.append(field.get(info).toString());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				LogMgr.showLog("IllegalArgumentException = "+e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				LogMgr.showLog("IllegalAccessException = "+e);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	protected JSONObject toJSONObject(Info info) {
		JSONObject descJSON = new JSONObject();
		try {
			Field[] fields = info.getClass().getFields();
			for (Field field : fields) {
				String key = field.getName();
				String value = field.get(info) == null ? "null" : field.get(
						info).toString();
				if (value.length() > 0)
					descJSON.put(key, value);
			}
		} catch (IllegalArgumentException e) {
			LogMgr.showLog("IllegalArgumentException = "+e);
		} catch (JSONException e) {
			LogMgr.showLog("JSONException = "+e);
		} catch (IllegalAccessException e) {
			LogMgr.showLog("IllegalAccessException = "+e);
		}
		return descJSON;
	}
}
