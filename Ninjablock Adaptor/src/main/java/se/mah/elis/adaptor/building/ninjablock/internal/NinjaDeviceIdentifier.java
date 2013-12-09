package se.mah.elis.adaptor.building.ninjablock.internal;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.ninjablock.beans.DataBean;


public class NinjaDeviceIdentifier implements DeviceIdentifier {

	private int vid, did;
	private String gid,node;
	
	public NinjaDeviceIdentifier(DataBean bean) {
		consumeBean(bean);
	}

	public int getVid() {
		return vid;
	}

	public void setVid(int vid) {
		this.vid = vid;
	}

	public int getDid() {
			return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}
	
	public String getNode() {
		return node;
	}
	
	public void setNode(String node) {
		this.node = node;
	}

	public String toSemiGUID() {
		return gid + "_" + vid + "_" + did; // GID_VID_DID
	}
	
	public String toGUID(){
		return node + "_" + gid + "_" + vid + "_" + did; //SERIALNUMBER_GID_VID_DID
	}
	
	public void consumeBean(DataBean bean) {
		vid = bean.getVid();
		did = bean.getDid();
		gid = bean.getGid();
		node = bean.getNode();
	}
}
