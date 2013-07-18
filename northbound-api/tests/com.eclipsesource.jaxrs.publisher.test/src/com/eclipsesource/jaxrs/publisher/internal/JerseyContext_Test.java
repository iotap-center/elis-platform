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
package com.eclipsesource.jaxrs.publisher.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.eclipsesource.jaxrs.publisher.internal.JerseyContext;
import com.eclipsesource.jaxrs.publisher.internal.RootApplication;


@RunWith( MockitoJUnitRunner.class )
public class JerseyContext_Test {
  
  private JerseyContext jerseyContext;
  @Mock
  private ServletContainer servletContainer;
  @Mock
  private HttpService httpService;
  @Mock
  private RootApplication rootApplication;

  @Before
  public void setUp() {
    JerseyContext original = new JerseyContext( httpService, "/test" );
    jerseyContext = spy( original );
    doReturn( servletContainer ).when( jerseyContext ).getServletContainer();
    doReturn( rootApplication ).when( jerseyContext ).getRootApplication();
  }
  
  @Test
  public void testAddResource() throws ServletException, NamespaceException {
    Object resource = new Object();
    
    jerseyContext.addResource( resource );
    
    verify( rootApplication ).addResource( resource );
    verify( httpService ).registerServlet( "/test", servletContainer, null, null );
  }
  
  @Test
  public void testRemoveSingleResource() throws ServletException, NamespaceException {
    Object resource = new Object();
    
    jerseyContext.addResource( resource );
    jerseyContext.removeResource( resource );
    

    verify( rootApplication ).addResource( resource );
    verify( rootApplication ).removeResource( resource );
    verify( httpService ).registerServlet( "/test", servletContainer, null, null );
    verify( httpService ).unregister( "/test" );
  }
  
  @Test
  public void testRemoveResource() throws ServletException, NamespaceException {
    when( rootApplication.hasResources() ).thenReturn( true );
    Object resource = new Object();
    Object resource2 = new Object();
    
    jerseyContext.addResource( resource );
    jerseyContext.addResource( resource2 );
    jerseyContext.removeResource( resource );
    
    
    verify( rootApplication ).addResource( resource );
    verify( rootApplication ).addResource( resource2 );
    verify( rootApplication ).removeResource( resource );
    verify( httpService ).registerServlet( "/test", servletContainer, null, null );
    verify( httpService, never() ).unregister( "/test" );
  }
  
  
  @Test
  public void testEliminate() throws ServletException, NamespaceException {
    Object resource = new Object();
    Set<Object> list = new HashSet<Object>();
    list.add( resource );
    when( rootApplication.getSingletons() ).thenReturn( list );
    jerseyContext.addResource( resource );
    
    List<Object> resources = jerseyContext.eliminate();
    
    verify( rootApplication ).addResource( resource );
    verify( httpService ).unregister( "/test" );
    verify( servletContainer ).destroy();
    assertEquals( 1, resources.size() );
    assertEquals( resource, resources.get( 0 ) );
  }
  
  @Test( expected = IllegalStateException.class )
  public void testConvertsServletException() throws ServletException, NamespaceException {
    doThrow( new ServletException() ).when( httpService ).registerServlet( anyString(), 
                                                                           any( Servlet.class ), 
                                                                           any( Dictionary.class ), 
                                                                           any( HttpContext.class ) );
    
    jerseyContext.addResource( new Object() );
  }
  
  @Test( expected = IllegalStateException.class )
  public void testConvertsNamespaceException() throws ServletException, NamespaceException {
    doThrow( new NamespaceException( "test" ) )
      .when( httpService ).registerServlet( anyString(), 
                                            any( Servlet.class ), 
                                            any( Dictionary.class ), 
                                            any( HttpContext.class ) );
    
    jerseyContext.addResource( new Object() );
  }
  
}
