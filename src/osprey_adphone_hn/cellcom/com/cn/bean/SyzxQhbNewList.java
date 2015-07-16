package osprey_adphone_hn.cellcom.com.cn.bean;

import java.text.DateFormat;  
import java.text.ParseException;  
import java.text.SimpleDateFormat;  
import java.util.Date;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.simpleframework.xml.Element;
public class SyzxQhbNewList implements Serializable {
	@Element(required = false)
	private String ggid;
	@Element(required = false)
	private String smallpic;
	@Element(required = false)
	private String largepic;
	@Element(required = false)
	private String title;
	@Element(required = false)
	private String moneytype;
	@Element(required = false)
	private String begintime;
	@Element(required = false)
	private String endtime;
	@Element(required = false)
	private String ordertype;
	@Element(required = false)
	private String state;
	@Element(required = false)
    private String ifread;
	@Element(required = false)
    private String ifwin;
	@Element(required = false)
    private String robsum;
	private long time;
  public String getGgid() {
    return ggid;
  }
  public void setGgid(String ggid) {
    this.ggid = ggid;
  }
  public String getSmallpic() {
    return smallpic;
  }
  public void setSmallpic(String smallpic) {
    this.smallpic = smallpic;
  }
  public String getLargepic() {
    return largepic;
  }
  public void setLargepic(String largepic) {
    this.largepic = largepic;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getMoneytype() {
    return moneytype;
  }
  public void setMoneytype(String moneytype) {
    this.moneytype = moneytype;
  }
  public String getBegintime() {
    return begintime;
  }
  public void setBegintime(String begintime) {
    this.begintime = begintime;
  }
  public String getEndtime() {
    return endtime;
  }
  public void setEndtime(String endtime) {
    this.endtime = endtime;
  }
  public String getOrdertype() {
    return ordertype;
  }
  public void setOrdertype(String ordertype) {
    this.ordertype = ordertype;
  }
  public String getState() {
    return state;
  }
  public void setState(String state) {
    this.state = state;
  }
  public String getIfread() {
    return ifread;
  }
  public void setIfread(String ifread) {
    this.ifread = ifread;
  }
  public String getIfwin() {
    return ifwin;
  }
  public void setIfwin(String ifwin) {
    this.ifwin = ifwin;
  }
  public String getRobsum() {
    return robsum;
  }
  public void setRobsum(String robsum) {
    this.robsum = robsum;
  }
  /**
   * 
   * @param type 1-距离开始时间倒计时,2-距离结束时间倒计时
   * @return
   */
  public String getTime(int type) {
    // TODO Auto-generated method stub
    if(type==1){      
      return getDistanceTime(begintime, getCurrentTime());
    }else{
      return getDistanceTime(endtime, getCurrentTime());
    }
  }
  public void setTime(long time) {
    this.time = time;
  }
  
  private String getCurrentTime(){
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    return df.format(new Date());// new Date()为获取当前系统时间
  }
  
  /** 
   * 两个时间相差距离多少天多少小时多少分多少秒 
   * @param str1 时间参数 1 格式：1990-01-01 12:00:00 
   * @param str2 时间参数 2 格式：2009-01-01 12:00:00 
   * @return String 返回值为：xx天xx小时xx分xx秒 
   */  
  public static String getDistanceTime(String str1, String str2) {  
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
      Date one;  
      Date two;  
      long day = 0;  
      long hour = 0;  
      long min = 0;  
      long sec = 0;  
      try {  
          one = df.parse(str1);  
          two = df.parse(str2);  
          long time1 = one.getTime();  
          long time2 = two.getTime();  
          long diff ;  
          if(time1<time2) {  
              diff = time2 - time1;  
          } else {  
              diff = time1 - time2;  
          }  
          day = diff / (24 * 60 * 60 * 1000);  
          hour = (diff / (60 * 60 * 1000) - day * 24);  
          min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
          sec = (diff/1000-day*24*60*60-hour*60*60-min*60);  
      } catch (ParseException e) {  
          e.printStackTrace();  
      }  
      return day + "天" + hour + "小时" + min + "分" + sec + "秒";  
  }  

}
