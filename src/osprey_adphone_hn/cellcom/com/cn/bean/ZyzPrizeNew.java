package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ZyzPrizeNew {
	@Element(required = false)
	private int idx;
	@Element(required = false)
	private int verytype;
	@Element(required = false)
	private int verynum;
	@Element(required = false)
	private int angle;
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public int getVerytype() {
		return verytype;
	}
	public void setVerytype(int verytype) {
		this.verytype = verytype;
	}
	public int getVerynum() {
		return verynum;
	}
	public void setVerynum(int verynum) {
		this.verynum = verynum;
	}
	public int getAngle() {
		return angle;
	}
	public void setAngle(int angle) {
		this.angle = angle;
	}
}
