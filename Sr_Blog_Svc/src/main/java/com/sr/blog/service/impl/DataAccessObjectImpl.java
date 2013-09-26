package com.sr.blog.service.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import com.sr.blog.model.Comment;
import com.sr.blog.service.api.IDataAccessObject;

public class DataAccessObjectImpl<ID, T> implements IDataAccessObject<ID, T> {
	@Autowired
	private MongoOperations mongoOperations;
	
	private final String collectionName;

	private final Class<T> type;
	
	public DataAccessObjectImpl(Class<T> type, String collectionName) {
		if (type == null || collectionName == null){
			throw new IllegalArgumentException();
		}
		
		this.type = type;
		this.collectionName = collectionName;
	}

	private String getCollectionName() {
		return collectionName;
	}
	
	@Override
	public T get(ID id) {
		Query query = query(where("id").is(id));
		
		return mongoOperations.findOne(query, type, getCollectionName());
	}

	@Override
	public T save(T data) {
		mongoOperations.save(data, getCollectionName());
		
		return data;
	}

	@Override
	public T delete(ID id) {
		Query query = query(where("id").is(id));
		
		return mongoOperations.findAndRemove(query, type, getCollectionName());
	}

	@Override
	public T update(T data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> getByQuery(Query query) {
		return mongoOperations.find(query, type, getCollectionName());
	}

	@Override
	public void deleteByQuery(Query query) {
		mongoOperations.remove(query, Comment.class, getCollectionName());
	}

}