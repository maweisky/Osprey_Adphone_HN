package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import p2p.cellcom.com.cn.bean.Device;

@Root
public class ZyzPrizeNewList implements Comparable{
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
	@Override
	public int compareTo(Object another) {
		// TODO Auto-generated method stub
		ZyzPrizeNewList zyzPrizeNewList = (ZyzPrizeNewList) another;
		if (zyzPrizeNewList.getIdx() < this.getIdx()) {
			return 1;
		} else if (zyzPrizeNewList.getIdx() > this.getIdx()) {
			return -1;
		} else {
			return 0;
		}
	}
	
}
