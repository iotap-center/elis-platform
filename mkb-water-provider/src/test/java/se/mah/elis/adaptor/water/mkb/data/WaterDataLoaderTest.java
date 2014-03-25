package se.mah.elis.adaptor.water.mkb.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class WaterDataLoaderTest {

	@Test
	public void testSamplesFromFileThrowsExceptionIfNoFileExists() {
		try {
			WaterDataLoader.loadFromFile("no/such/file");
			fail("loadFromFile did not throw an exception as expected");
		} catch (Exception e) {
		}
	}

	@Test
	public void testLoadSamplesFromFile() throws FileNotFoundException,
			UnsupportedEncodingException {
		List<String> data = new ArrayList<>(Arrays.asList(
				"2014-02-18 07:00:00;63408103;735999114002080222;33,764",
				"2014-02-18 07:00:00;63408104;735999114001608663;18,519"));
		writeFile("/tmp/samples.txt", data);
		testLoad("/tmp/samples.txt", data.size());
		removeFile("/tmp/samples.txt");
	}

	@Test
	public void testLoadSamplesWithZeroValueFromFile()
			throws FileNotFoundException, UnsupportedEncodingException {
		List<String> data = new ArrayList<>(Arrays.asList(
				"2014-02-18 07:00:00;0;0;0", "2014-02-18 08:00:00;1;0;0,0"));
		writeFile("/tmp/zerosamples.txt", data);
		testLoad("/tmp/zerosamples.txt", data.size());
		removeFile("/tmp/zerosamples.txt");
	}

	@Test
	public void testLoadMissingDataFromLineInFile()
			throws FileNotFoundException, UnsupportedEncodingException {
		List<String> data = new ArrayList<>(Arrays.asList(
				"2014-02-18 07:00:00;0", "2014-02-18 08:00:00;1;0;0,0"));
		writeFile("/tmp/faulty.txt", data);
		testLoad("/tmp/faulty.txt", 1);
		removeFile("/tmp/faulty.txt");
	}

	@Test
	public void testLoadFaultyDateFromFile() throws FileNotFoundException,
			UnsupportedEncodingException {
		List<String> data = new ArrayList<>(Arrays.asList(
				"2014-02-00;63408103;735999114002080222;33,764",
				"2014-02-18 07:00:00;63408104;735999114001608663;18,519"));
		writeFile("/tmp/faulty.txt", data);
		testLoad("/tmp/faulty.txt", 1);
		removeFile("/tmp/faulty.txt");
	}

	@Test
	public void testLoadUpdatedFormatFromFile() throws FileNotFoundException,
			UnsupportedEncodingException {
		List<String> data = new ArrayList<>(Arrays.asList(
				"2014-03-25 01:00:00;63408098;194-085-01-Bad-VV;113,709",
				"2014-03-25 00:00:00;63408100;194-089-01-Bad-VV;0,000"));
		writeFile("/tmp/updated.txt", data);
		testLoad("/tmp/updated.txt", 2);
		removeFile("/tmp/updated.txt");
	}

	@Test
	public void testLoadNoWaterMeasurementInFile()
			throws FileNotFoundException, UnsupportedEncodingException {
		List<String> data = new ArrayList<>(Arrays.asList(
				"2014-03-25 01:00:00;63408098;194-085-01-Bad-VV;",
				"2014-03-25 00:00:00;63408100;194-089-01-Bad-VV;20,0"));
		writeFile("/tmp/missingvalue.txt", data);
		testLoad("/tmp/missingvalue.txt", 2);
		removeFile("/tmp/missingvalue.txt");
	}

	private void testLoad(String path, int size) {
		try {
			WaterData data = WaterDataLoader.loadFromFile(path);
			assertEquals(size, data.size());
		} catch (Exception e) {
			fail("Could not load samples (" + path + ") file during test");
		}
	}

	private void removeFile(String pathname) {
		new File(pathname).delete();
	}

	private void writeFile(String filename, List<String> data)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		for (String line : data)
			writer.println(line);
		writer.close();
	}

}
