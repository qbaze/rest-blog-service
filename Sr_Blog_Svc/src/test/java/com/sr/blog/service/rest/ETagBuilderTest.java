package com.sr.blog.service.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.EntityTag;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.sr.blog.model.AbstractResource;

@RunWith(MockitoJUnitRunner.class)
public class ETagBuilderTest {
	@Mock
	AbstractResource resource;

	@Test
	public void testCalculateETag() {
		when(resource.getId()).thenReturn("1");
		
		DateTime dt = new DateTime(1);
		when(resource.getUpdated()).thenReturn(dt);
		
		EntityTag eTag = EntityTagBuilder.calculateETag(resource);
		
		assertNotNull(eTag);
		assertEquals(eTag.getValue(), "1_1");
	}

	@Test
	public void testCalculateETag_no_update() {
		when(resource.getId()).thenReturn("1");
		when(resource.getUpdated()).thenReturn(null);
		
		EntityTag eTag = EntityTagBuilder.calculateETag(resource);
		
		assertNotNull(eTag);
		assertEquals(eTag.getValue(), "1");
	}
}
