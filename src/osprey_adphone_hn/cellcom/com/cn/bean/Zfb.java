package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Zfb {
	@Element(required = false)
	private String orderno;
	@Element(required = false)
	private String pid;
	@Element(required = false)
	private String sellermail;
	@Element(required = false)
	private String privatekey;
	@Element(required = false)
	private String notifyurl;
	@Element(required = false)
	private String moneynum;
  public String getOrderno() {
    return orderno;
  }
  public void setOrderno(String orderno) {
    this.orderno = orderno;
  }
  public String getPid() {
    return pid;
  }
  public void setPid(String pid) {
    this.pid = pid;
  }
  public String getSellermail() {
    return sellermail;
  }
  public void setSellermail(String sellermail) {
    this.sellermail = sellermail;
  }
  public String getPrivatekey() {
    return privatekey;
  }
  public void setPrivatekey(String privatekey) {
    this.privatekey = privatekey;
  }
  public String getNotifyurl() {
    return notifyurl;
  }
  public void setNotifyurl(String notifyurl) {
    this.notifyurl = notifyurl;
  }
  public String getMoneynum() {
    return moneynum;
  }
  public void setMoneynum(String moneynum) {
    this.moneynum = moneynum;
  }

}
