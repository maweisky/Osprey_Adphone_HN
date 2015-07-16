package osprey_adphone_hn.cellcom.com.cn.bean;

public class ContactBean {

	private int contactId;
	private String preDesplayName;
	private String posdesplayName;
	private String desplayName;
	private String prePhoneNum;
	private String posphoneNum;
	private String phoneNum;
	private String sortKey;
	private Long photoId;
	private String lookUpKey;
	private int selected = 0;
	private String formattedNumber;
	private String pinyin;

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getPreDesplayName() {
		return preDesplayName;
	}

	public void setPreDesplayName(String preDesplayName) {
		this.preDesplayName = preDesplayName;
	}

	public String getPrePhoneNum() {
		return prePhoneNum;
	}

	public void setPrePhoneNum(String prePhoneNum) {
		this.prePhoneNum = prePhoneNum;
	}

	public String getPosdesplayName() {
		return posdesplayName;
	}

	public void setPosdesplayName(String posdesplayName) {
		this.posdesplayName = posdesplayName;
	}

	public String getPosphoneNum() {
		return posphoneNum;
	}

	public void setPosphoneNum(String posphoneNum) {
		this.posphoneNum = posphoneNum;
	}

	public String getDesplayName() {
		return desplayName;
	}

	public void setDesplayName(String desplayName) {
		this.desplayName = desplayName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public Long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}

	public String getLookUpKey() {
		return lookUpKey;
	}

	public void setLookUpKey(String lookUpKey) {
		this.lookUpKey = lookUpKey;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public String getFormattedNumber() {
		return formattedNumber;
	}

	public void setFormattedNumber(String formattedNumber) {
		this.formattedNumber = formattedNumber;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

}
