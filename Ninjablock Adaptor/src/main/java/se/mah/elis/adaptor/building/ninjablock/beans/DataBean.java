package se.mah.elis.adaptor.building.ninjablock.beans;

public class DataBean {
	
	private int vid;
	private int did;
	private String device_type;
	private String default_name;
	private String tags;
	private String is_sensor;
	private String is_actuator;
	private String is_silent;
	private String has_time_series;
	private String has_subdevice_count;
	private String has_state;
	private String unit;
	private String documentation;
	private String node;
	private String guid;
	private String gid;
//	private String meta;
//	private String subDevices;
	private LastDataBean last_data;
	private String pusherChannel;

	public LastDataBean getLastData() {
		return last_data;
	}

	public String getPusherChannelValue()	{
		return pusherChannel;
	}

	public int getVid() {
		return vid;
	}

	public void setVid(int vid) throws IllegalArgumentException{
		if (vid >= 0){
			this.vid = vid;
		}
		else{
			throw new IllegalArgumentException("Input is not a positive integer.");
		}
	}

	public String getDefault_name() {
		return default_name;
	}

	public void setDefault_name(String default_name) {
		this.default_name = default_name;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		if (gid != null){
			this.gid = gid;
		}
		else{
			throw new IllegalArgumentException("Input is null.");
		}
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		if (did >= 0){
			this.did = did;
		}
		else{
			throw new IllegalArgumentException("Input is not a positive integer.");
		}
	}

	public String getGuid() {
//		String guid = node+"_"+gid+"_"+vid+"_"+did;
		return guid;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		if (node != null){
			this.node = node;
		}
		else{
			throw new IllegalArgumentException("Input is null.");
		}
	}
}
