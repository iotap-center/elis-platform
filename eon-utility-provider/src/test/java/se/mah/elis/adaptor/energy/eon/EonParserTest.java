package se.mah.elis.adaptor.energy.eon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.energy.eon.internal.EonParser;
import se.mah.elis.adaptor.energy.eon.internal.devices.EonMainPowerMeter;
import se.mah.elis.adaptor.energy.eon.internal.devices.EonPowerSwitchMeter;
import se.mah.elis.adaptor.energy.eon.internal.devices.EonThermometer;

public class EonParserTest {

	
	@Test
	public void testParseToken() {
		String token = "{\"ewp-auth-key\":\"635132945923697159,marcus.ljungblad@mah.se,medeamah2012\"}";
		try {
			assertTrue(EonParser.parseToken(token).contains("marcus.ljungblad"));
		} catch (ParseException e) {
			fail();
		}
	}
	
	@Test
	public void testGetGateway() {
		String gwresponse = "[{\"BuildingId\":0,\"EnergyClassId\":1,"
				+ "\"EwpPanelId\":134,\"IsEnergyOptimisable\":true,\"IsOnline\":true,"
				+ "\"IsOwner\":true,\"Name\":\"MedeaPanel\",\"WeatherLocationId\":0}]";
		try {
			assertTrue(EonParser.parseGateway(gwresponse).containsKey("Name"));
			assertTrue(EonParser.parseGateway(gwresponse).containsKey("EwpPanelId"));
		} catch (Exception ignore) {
			fail();
		}
	}
	
	@Test
	public void testGetDeviceList() {
		String response = "[" + SAMPLE_POWERSWITCH + "]";
		try {
			assertEquals(1, EonParser.parseDeviceList(response).size());
		} catch (Exception ignore) {
			fail();
		}
	}
	
	@Test
	public void testGetDeviceListWithMainPowerMeter() {
		String response = "[" + SAMPLE_POWERSWITCH + ", " +
				SAMPLE_TERMOMETER + ", " +
				SAMPLE_MAIN_POWERMETER + "]";
		List<Device> devices = null;
		EonMainPowerMeter meter = null;
		
		try {
			devices = EonParser.parseDeviceList(response);
			meter = (EonMainPowerMeter) get(devices, EonMainPowerMeter.class);
		} catch (Exception ignore) {
			fail();
		}
		assertEquals(3, devices.size());
		assertTrue(contains(devices, EonPowerSwitchMeter.class));
		assertTrue(contains(devices, EonThermometer.class));
		assertTrue(contains(devices, EonMainPowerMeter.class));
		assertEquals(2, meter.size());
		assertTrue(contains(devices, EonPowerSwitchMeter.class));
		assertTrue(contains(devices, EonThermometer.class));
	}
	
	@Test
	public void testGetDeviceListWithEmptyList() {
		String response = "[]";
		try {
			assertEquals(0, EonParser.parseDeviceList(response).size());
		} catch (Exception ignore) { fail(); }
	}
	
	@Test
	public void testGetDeviceStatusForPowerSwitchDevice() {
		String response = "["
				   + "{"
				   +    "\"CurrentKwh\": 0,"
				   +    "\"CurrentOn\": false,"
				   +    "\"CurrentPrice\": 0,"
				   +    "\"DeviceId\": \"ab62ec3d-f86d-46bc-905b-144ee0511a25\""
				   + "}"
				+ "]";
		try {
			assertTrue(EonParser.parseDeviceStatus(response).containsKey("DeviceId"));
		} catch (Exception ignore) {
			fail();
		}
	}
	
	@Test
	public void testGetDeviceStatusForPowerMeterDevice() {
		String response = "["
				   + "{"
				   +    "\"CurrentKwh\": 24.0"
				   +    "\"CurrentOn\": false,"
				   +    "\"CurrentPrice\": 0,"
				   +    "\"DeviceId\": \"1c167952-2941-479d-b8ab-898f05fea5da\""
				   + "}"
				+ "]";
		try {
			assertEquals(24.0, EonParser.parsePowerMeterValue(response), 0.01);
		} catch (Exception ignore) { ignore.printStackTrace(); fail(); }
	}
	
	@Test
	public void testGetActionObject() {
		String response = "{\"Id\":15959278,\"Message\":null,\"StatusId\":1}";
		try {
			assertTrue(EonParser.parseActionObject(response).containsKey("Id"));
		} catch (Exception ignore) { fail(); }
	}
	
	@Test
	public void testParseTemperature() {
		String response = "{\"Temperature\":-1}";
		try{
			assertTrue(EonParser.parseTemperatureValue(response) == -1); // TODO: this will break when temp = -1.1 (floats)
		} catch (Exception ignore) { fail(); }
	}
	
	@Test
	public void testParseSummaryStats() {
		try {
			List<Map<String, Object>> summaries = EonParser.parseSummaryStats(SAMPLE_SUMMARYSTATS);
			Map<String, Object> summary = summaries.get(0);
			assertEquals("4401bdf2-6077-452c-ac73-b65a04e96b80", (String) summary.get("DeviceId"));
			assertEquals(234.29438725997019d, (Double) summary.get("AverageConsumptionLastYear"), 0.001d);
			assertEquals(0, (Double) summary.get("AverageConsumptionLastMonth"), 0.001d);
			assertEquals(0, (Double) summary.get("AverageConsumptionLastWeek"), 0.001d);
			assertEquals(0, (Double) summary.get("AverageConsumptionThisYear"), 0.001d);
			assertEquals(0, (Double) summary.get("AverageConsumptionThisMonth"), 0.001d);
			assertEquals(0, (Double) summary.get("AverageConsumptionThisWeek"), 0.001d);
			assertEquals(0, (Double) summary.get("AverageConsumptionThisDay"), 0.001d);
			assertEquals(0, (Double) summary.get("SumConsumptionThisDay"), 0.0d);
		} catch (ParseException e) {
			fail("Could not parse summary stats");
		}
	}
	
	@Test
	public void testParseStatData() {
		try {
			List<Map<String, Object>> datas = EonParser.parseStatData(SAMPLE_STATDATA);
			assertEquals(2, datas.size());
			Map<String, Object> data = datas.get(0);
			assertEquals("0001-01-01 00:00", data.get("Key"));
			assertEquals(37805.423369955635d, data.get("Value"));
			assertEquals(30114.022641708878d, data.get("ValueCost"));
		} catch (ParseException e) {
			fail("Could not parse stat data");
		}
	}
	
	private boolean contains(List<Device> devices, Class c) {
		for (Device device : devices) {
			if (c.isInstance(device)) {
				return true;
			}
		}
		
		return false;
	}
	
	private Device get(List<Device> devices, Class c) {
		for (Device device : devices) {
			if (c.isInstance(device)) {
				return device;
			}
		}
		
		return null;
	}
	
	public static final String SAMPLE_STATDATA = ""
			+ "[{\"Key\":\"0001-01-01 00:00\",\"Value\":37805.423369955635,\"ValueCost\":30114.022641708878}"
			+ ",{\"Key\":\"0001-01-01 01:00\",\"Value\":20857.415310303728,\"ValueCost\":16159.1836757256}]";
	
	public static final String SAMPLE_SUMMARYSTATS = ""
			+ "[{"
			   + "\"AverageConsumptionAllTime\" : 161.60007067797793, "
			   + "\"AverageConsumptionAllTimeLiter\" : 0, "
			   + "\"AverageConsumptionLastMonth\" : 0, "
			   + "\"AverageConsumptionLastMonthLiter\" : 0, "
			   + "\"AverageConsumptionLastWeek\" : 0, "
			   + "\"AverageConsumptionLastWeekLiter\" : 0, "
			   + "\"AverageConsumptionLastYear\" : 234.29438725997019, "
			   + "\"AverageConsumptionLastYearLiter\" : 0, "
			   + "\"AverageConsumptionThisDay\" : 0, "
			   + "\"AverageConsumptionThisMonth\" : 0, "
			   + "\"AverageConsumptionThisMonthLiter\" : 0, "
			   + "\"AverageConsumptionThisWeek\" : 0, "
			   + "\"AverageConsumptionThisWeekLiter\" : 0, "
			   + "\"AverageConsumptionThisYear\" : 0, "
			   + "\"AverageConsumptionThisYearLiter\" : 0, "
			   + "\"AverageCostAllTime\" : 143.78045539743627, "
			   + "\"AverageTemperatureAllTime\" : 0, "
			   + "\"DeviceId\" : \"4401bdf2-6077-452c-ac73-b65a04e96b80\", "
			   + "\"FirstObservationDate\" : \"/Date(1375034400000+0200)/\", "
			   + "\"SumConsumptionAllTime\" : 882821.1861137934, "
			   + "\"SumConsumptionLastMonth\" : 0, "
			   + "\"SumConsumptionLastWeek\" : 0, "
			   + "\"SumConsumptionLastYear\" : 882821.1861137934, "
			   + "\"SumConsumptionThisDay\" : 0, "
			   + "\"SumConsumptionThisMonth\" : 0, "
			   + "\"SumConsumptionThisWeek\" : 0, "
			   + "\"SumConsumptionThisYear\" : 0, "
			   + "\"SumCostAllTime\" : 785472.62783619436, "
			   + "\"SumCostLastMonth\" : 0, "
			   + "\"SumCostLastWeek\" : 0, "
			   + "\"SumCostLastYear\" : 785472.62783619436, "
			   + "\"SumCostThisDay\" : 0, "
			   + "\"SumCostThisMonth\" : 0, "
			   + "\"SumCostThisWeek\" : 0, "
			   + "\"SumCostThisYear\" : 0 "
			+ "}]";
	
	/**
	 * May also be used to create JSON objects
	 */
	public static final String SAMPLE_POWERSWITCH = "" 
			+ "{"
			+        "\"AreaNo\": 1,"
			+        "\"ChannelNo\": 0,"
			+        "\"ControllerDeviceId\": null,"
			+        "\"Description\": null,"
			+        "\"DeviceTypeId\": 49,"
			+        "\"DistributorCode\": null,"
			+        "\"EnergyOptimisedDate\": \"\","
			+        "\"EnergyOptimisingMode\": 0,"
			+        "\"EnergyOptimisingOption\": 0,"
			+        "\"EnergyOptmisationTemporaryDisabled\": false,"
			+        "\"EnergyTypeId\": 1,"
			+        "\"IconId\": null,"
			+        "\"Id\": \"ab62ec3d-f86d-46bc-905b-144ee0511a25\","
			+        "\"IsCamera\": false,"
			+        "\"IsChargingDevice\": false,"
			+        "\"IsDimmer\": false,"
			+        "\"IsDoorSwitch\": false,"
			+        "\"IsHumidity\": false,"
			+        "\"IsPowerSwitch\": true,"
			+        "\"IsProduction\": false,"
			+        "\"IsRadon\": false,"
			+        "\"IsSummaryDevice\": false,"
			+        "\"IsTemperature\": false,"
			+        "\"IsThermostat\": false,"
			+        "\"IsUpic\": false,"
			+        "\"IsVirtualDevice\": false,"
			+        "\"Name\": \"Golvfl%C3%A4kt\","
			+        "\"PowerSwitchIsOn\": false,"
			+        "\"RoomId\": \"465f4409-6b0b-4f55-b296-ef98520bcf97\","
			+        "\"UsageAreaId\": 9,"
			+        "\"ZoneNo\": 2"
			+    "}";
	
	/**
	 * May also be used to create JSON objects
	 */
	public static final String SAMPLE_TERMOMETER = ""
			+"{"
			+"\"AreaNo\": 1,"
			+"\"ChannelNo\": 0,"
			+"\"ControllerDeviceId\": null,"
			+"\"Description\": null,"
			+"\"DeviceTypeId\": 101,"
			+"\"DistributorCode\": null,"
			+"\"EnergyOptimisedDate\": \"\","
			+"\"EnergyOptimisingMode\": 0,"
			+"\"EnergyOptimisingOption\": 0,"
			+"\"EnergyOptmisationTemporaryDisabled\": false,"
			+"\"EnergyTypeId\": null,"
			+"\"IconId\": null,"
			+"\"Id\": \"b6530784-14da-469b-8a46-36e8e2c0d684\","
			+"\"IsCamera\": false,"
			+"\"IsChargingDevice\": false,"
			+"\"IsDimmer\": false,"
			+"\"IsDoorSwitch\": false,"
			+"\"IsHumidity\": true,"
			+"\"IsPowerSwitch\": false,"
			+"\"IsProduction\": false,"
			+"\"IsRadon\": false,"
			+"\"IsSummaryDevice\": false,"
			+"\"IsTemperature\": true,"
			+"\"IsThermostat\": false,"
			+"\"IsUpic\": false,"
			+"\"IsVirtualDevice\": false,"
			+"\"Name\": \"NO%20NAME\","
			+"\"PowerSwitchIsOn\": false,"
			+"\"RoomId\": null,"
			+"\"UsageAreaId\": 10,"
			+"\"ZoneNo\": 3"
			+"}";
	
	/**
	 * May also be used to create JSON objects
	 */
	public static final String SAMPLE_POWERMETER = ""
			+"{"
			+"\"AreaNo\": 1,"
			+"\"ChannelNo\": 0,"
			+"\"ControllerDeviceId\": null,"
			+"\"Description\": null,"
			+"\"DeviceTypeId\": 51,"
			+"\"DistributorCode\": null,"
			+"\"EnergyOptimisedDate\": \"\","
			+"\"EnergyOptimisingMode\": 0,"
			+"\"EnergyOptimisingOption\": 0,"
			+"\"EnergyOptmisationTemporaryDisabled\": false,"
			+"\"EnergyTypeId\": null,"
			+"\"IconId\": null,"
			+"\"Id\": \"1c167952-2941-479d-b8ab-898f05fea5da\","
			+"\"IsCamera\": false,"
			+"\"IsChargingDevice\": false,"
			+"\"IsDimmer\": false,"
			+"\"IsDoorSwitch\": false,"
			+"\"IsHumidity\": false,"
			+"\"IsPowerSwitch\": false,"
			+"\"IsProduction\": false,"
			+"\"IsRadon\": false,"
			+"\"IsSummaryDevice\": true,"
			+"\"IsTemperature\": false,"
			+"\"IsThermostat\": false,"
			+"\"IsUpic\": false,"
			+"\"IsVirtualDevice\": true,"
			+"\"Name\": \"Elmtare elishem3\","
			+"\"PowerSwitchIsOn\": false,"
			+"\"RoomId\": null,"
			+"\"UsageAreaId\": 0,"
			+"\"ZoneNo\": 1"
			+"}";
	
	/**
	 * May also be used to create JSON objects
	 */
	public static final String SAMPLE_MAIN_POWERMETER = ""
			+"{"
			+"\"AreaNo\": 1,"
			+"\"ChannelNo\": 0,"
			+"\"ControllerDeviceId\": \"deadbeef-2941-479d-b8ab-898f05fea5da\","
			+"\"Description\": null,"
			+"\"DeviceTypeId\": 51,"
			+"\"DistributorCode\": null,"
			+"\"EnergyOptimisedDate\": \"\","
			+"\"EnergyOptimisingMode\": 0,"
			+"\"EnergyOptimisingOption\": 0,"
			+"\"EnergyOptmisationTemporaryDisabled\": false,"
			+"\"EnergyTypeId\": null,"
			+"\"IconId\": null,"
			+"\"Id\": \"1c167952-2941-479d-b8ab-898f05fea5dc\","
			+"\"IsCamera\": false,"
			+"\"IsChargingDevice\": false,"
			+"\"IsDimmer\": false,"
			+"\"IsDoorSwitch\": false,"
			+"\"IsHumidity\": false,"
			+"\"IsPowerSwitch\": false,"
			+"\"IsProduction\": false,"
			+"\"IsRadon\": false,"
			+"\"IsSummaryDevice\": true,"
			+"\"IsTemperature\": false,"
			+"\"IsThermostat\": false,"
			+"\"IsUpic\": false,"
			+"\"IsVirtualDevice\": true,"
			+"\"Name\": \"Elmtare elishem5\","
			+"\"PowerSwitchIsOn\": false,"
			+"\"RoomId\": null,"
			+"\"UsageAreaId\": 7,"
			+"\"ZoneNo\": 1"
			+"}";
	
	/**
	 * May also be used to create JSON objects
	 */
	public static final String SAMPLE_DIN_POWERMETER = ""
			+"{"
			+"\"AreaNo\": 1,"
			+"\"ChannelNo\": 0,"
			+"\"ControllerDeviceId\": null,"
			+"\"Description\": null,"
			+"\"DeviceTypeId\": 49,"
			+"\"DistributorCode\": null,"
			+"\"EnergyOptimisedDate\": \"\","
			+"\"EnergyOptimisingMode\": 0,"
			+"\"EnergyOptimisingOption\": 0,"
			+"\"EnergyOptmisationTemporaryDisabled\": false,"
			+"\"EnergyTypeId\": null,"
			+"\"IconId\": null,"
			+"\"Id\": \"1c167952-2941-479d-b8ab-898f05fea5dc\","
			+"\"IsCamera\": false,"
			+"\"IsChargingDevice\": false,"
			+"\"IsDimmer\": false,"
			+"\"IsDoorSwitch\": false,"
			+"\"IsHumidity\": false,"
			+"\"IsPowerSwitch\": false,"
			+"\"IsProduction\": false,"
			+"\"IsRadon\": false,"
			+"\"IsSummaryDevice\": true,"
			+"\"IsTemperature\": false,"
			+"\"IsThermostat\": false,"
			+"\"IsUpic\": false,"
			+"\"IsVirtualDevice\": true,"
			+"\"Name\": \"Elmtare elishem5\","
			+"\"PowerSwitchIsOn\": false,"
			+"\"RoomId\": null,"
			+"\"UsageAreaId\": 7,"
			+"\"ZoneNo\": 1"
			+"}";
	
	/**
	 * May also be used to create JSON objects
	 */
	public static final String SAMPLE_THERMOSTAT = ""
			+"{"
			+"\"AreaNo\": 1,"
			+"\"ChannelNo\": 0,"
			+"\"ControllerDeviceId\": null,"
			+"\"Description\": null,"
			+"\"DeviceTypeId\": 96,"
			+"\"DistributorCode\": null,"
			+"\"EnergyOptimisedDate\": \"\","
			+"\"EnergyOptimisingMode\": 0,"
			+"\"EnergyOptimisingOption\": 0,"
			+"\"EnergyOptmisationTemporaryDisabled\": false,"
			+"\"EnergyTypeId\": 1,"
			+"\"IconId\": null,"
			+"\"Id\": \"69d0ea21-09c4-421a-8862-5edf8fd78c61\","
			+"\"IsCamera\": false,"
			+"\"IsChargingDevice\": false,"
			+"\"IsDimmer\": false,"
			+"\"IsDoorSwitch\": false,"
			+"\"IsHumidity\": false,"
			+"\"IsPowerSwitch\": false,"
			+"\"IsProduction\": false,"
			+"\"IsRadon\": false,"
			+"\"IsSummaryDevice\": false,"
			+"\"IsTemperature\": false,"
			+"\"IsThermostat\": true,"
			+"\"IsUpic\": false,"
			+"\"IsVirtualDevice\": false,"
			+"\"Name\": \"Termostat\","
			+"\"PowerSwitchIsOn\": false,"
			+"\"RoomId\": \"197f210a-58ea-483d-bd41-2248b03edbff\","
			+"\"UsageAreaId\": 9,"
			+"\"ZoneNo\": 1"
			+"}";
	
}
