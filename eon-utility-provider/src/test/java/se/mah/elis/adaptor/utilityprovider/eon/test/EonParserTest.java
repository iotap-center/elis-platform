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
		String response = "["
			+    "{"
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
			+    "}"
			+ "]";
		try {
			assertTrue(EonParser.parseDeviceList(response).get(0).containsKey("Id"));
		} catch (Exception ignore) {
			fail();
		}
	}
	
	@Test
	public void testGetDeviceStatus() {
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
}
