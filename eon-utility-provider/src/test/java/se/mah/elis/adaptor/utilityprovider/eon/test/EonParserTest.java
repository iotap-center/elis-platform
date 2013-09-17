package se.mah.elis.adaptor.utilityprovider.eon.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import se.mah.elis.adaptor.utilityprovider.eon.internal.EonParser;

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
}
