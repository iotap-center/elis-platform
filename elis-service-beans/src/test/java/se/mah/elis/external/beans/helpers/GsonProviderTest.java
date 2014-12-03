package se.mah.elis.external.beans.helpers;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;

import org.junit.Test;

import com.google.gson.Gson;


public class GsonProviderTest {

	@Test
	public void testGetSize() {
		GsonProvider<TestObject> testObjectProvider = new GsonProvider<TestObject>();

		long size = testObjectProvider.getSize( mock( TestObject.class ), null, null, null, null );

		assertThat( size ).isEqualTo( -1 );
	}

	@Test
	public void testIsWritableWithTestObject() {
		GsonProvider<TestObject> testObjectProvider = new GsonProvider<TestObject>();

		boolean writeable = testObjectProvider.isWriteable( TestObject.class, null, null, null );

		assertTrue( writeable );
	}

	@Test
	public void testIsReadableWithTestObject() {
		GsonProvider<TestObject> testObjectProvider = new GsonProvider<TestObject>();

		boolean readable = testObjectProvider.isReadable( TestObject.class, null, null, null );

		assertTrue( readable );
	}

	@Test
	public void testWritesTestObject() throws WebApplicationException, IOException {
		GsonProvider<TestObject> testObjectProvider = new GsonProvider<TestObject>();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		TestObject testObject = new TestObject( "foo" );

		testObjectProvider.writeTo( testObject, TestObject.class, null, null, null, null, stream );

		TestObject actualTestObject = new Gson().fromJson( convertToReader( stream ), TestObject.class );
		assertThat( actualTestObject ).isEqualTo( testObject );
	}

	@Test
	public void testWritesTestObjectClosesStream() throws WebApplicationException, IOException {
		GsonProvider<TestObject> testObjectProvider = new GsonProvider<TestObject>();
		OutputStream stream = mock( OutputStream.class );
		TestObject testObject = new TestObject( "foo" );

		testObjectProvider.writeTo( testObject, TestObject.class, null, null, null, null, stream );

		verify( stream ).close();
	}

	@Test
	public void testReadsTestObject() throws WebApplicationException, IOException {
		GsonProvider<TestObject> testObjectProvider = new GsonProvider<TestObject>();
		TestObject testObject = new TestObject( "foo" );
		String json = new Gson().toJson( testObject );
		ByteArrayInputStream inputStream = new ByteArrayInputStream( json.getBytes( "UTF-8" ) );

		TestObject actualTestObject = testObjectProvider.readFrom( TestObject.class, null, null, null, null, inputStream );

		assertThat( actualTestObject ).isEqualTo( testObject );
	}

	@Test
	public void testReadTestObjectClosesStream() throws WebApplicationException, IOException {
		GsonProvider<TestObject> testObjectProvider = new GsonProvider<TestObject>();
		TestObject testObject = new TestObject( "foo" );
		String json = new Gson().toJson( testObject );
		ByteArrayInputStream inputStream = spy( new ByteArrayInputStream( json.getBytes( "UTF-8" ) ) );

		testObjectProvider.readFrom( TestObject.class, null, null, null, null, inputStream );

		verify( inputStream ).close();
	}

	private BufferedReader convertToReader( ByteArrayOutputStream stream ) {
		InputStream input = new ByteArrayInputStream(stream.toByteArray());
		return new BufferedReader( new InputStreamReader( input ) );
	}

	private static class TestObject {

		private final String id;

		public TestObject( String id ) {
			this.id = id;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
			return result;
		}

		@Override
		public boolean equals( Object obj ) {
			if( this == obj )
				return true;
			if( obj == null )
				return false;
			if( getClass() != obj.getClass() )
				return false;
			TestObject other = ( TestObject )obj;
			if( id == null ) {
				if( other.id != null )
					return false;
			} else if( !id.equals( other.id ) )
				return false;
			return true;
		}

	}

}