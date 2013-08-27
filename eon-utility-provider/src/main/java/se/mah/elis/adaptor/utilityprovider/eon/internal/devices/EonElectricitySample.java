package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import java.util.Date;
import java.util.List;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;
import se.mah.elis.auxiliaries.data.ElectricitySample;

public class EonElectricitySample implements ElectricitySample {

	private EonGateway gateway;
	private DeviceIdentifier deviceId;

	public EonElectricitySample(Gateway gateway, DeviceIdentifier id) {
		this.gateway = (EonGateway) gateway;
		this.deviceId = id;
	}

	public int getSampleLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Date getSampleTimestamp() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getTraversableMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	public double getCurrentCurrent() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getCurrentVoltage() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getCurrentPower() {
		double power = -1;
		try {
			String key = "CurrentKwh";
			power = (double) gateway.getApiBridge().getDeviceStatus(
					((long) gateway.getId()), deviceId.toString()).get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return power;
	}

	public double getTopCurrent() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getTopVoltage() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getTopPower() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getMinCurrent() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getMinVoltage() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getMinPower() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getMeanCurrent() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getMeanVoltage() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getMeanPower() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getTotalEnergyUsageInJoules() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getTotalEnergyUsageInWh() {
		// TODO Auto-generated method stub
		return 0;
	}

}
