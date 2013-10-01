package com.sr.blog.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

@RunWith(MockitoJUnitRunner.class)
public class DataAccessObjectImpl_Mocked_Test {
	@Mock
	MongoOperations mongoOperations;
	
	@InjectMocks
	DataAccessObjectImpl<String, TestType> dao = new DataAccessObjectImpl<String, DataAccessObjectImpl_Mocked_Test.TestType>(TestType.class, "test");

	static class TestType {
		private String id;
		private DateTime created;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public DateTime getCreated() {
			return created;
		}

		public void setCreated(DateTime created) {
			this.created = created;
		}
	}

	@Test
	public void testGet() {
		String id = "1";
		Query query = query(where("id").is(id));

		TestType test = new TestType();
		
		when(mongoOperations.findOne(query, TestType.class, "test")).thenReturn(test);
		
		assertEquals(test, dao.get(id));
	}

//	@Test
//	public void testSave() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDelete() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUpdate() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUpdatePartial() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetByQuery() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDeleteByQuery() {
//		fail("Not yet implemented");
//	}

}
