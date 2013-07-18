/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.consumer;

import static com.eclipsesource.jaxrs.consumer.test.TestUtil.createResource;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.junit.Test;


public class ExceptionalFlowTests {
  
  
  @Path( "/test" )
  private interface ResourceWithNotReplacedPath {
    @GET
    @Path( "/{foo}/{foo2}" )
    String getContent( @PathParam( "foo" ) String foo );
  }
  
  @Test( expected = IllegalStateException.class )
  public void testNotProvidedPathParam() {
    ResourceWithNotReplacedPath resource = createResource( ResourceWithNotReplacedPath.class, "http://localhost" );
    
    assertEquals( "get", resource.getContent( "bar" ) );
  }
  
  @Path( "/test" )
  private interface ResourceWithGetWithFormParams {
    @GET
    String getContent( @FormParam( "foo" ) String foo );
  }
  
  @Test( expected = IllegalStateException.class )
  public void testGetWithFormParam() {
    ResourceWithGetWithFormParams resource = createResource( ResourceWithGetWithFormParams.class, "http://localhost" );
    
    assertEquals( "get", resource.getContent( "bar" ) );
  }
}
