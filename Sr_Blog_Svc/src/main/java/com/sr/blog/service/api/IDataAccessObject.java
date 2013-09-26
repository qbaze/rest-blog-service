package com.sr.blog.service.api;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

public interface IDataAccessObject<ID, T> {
	public T get(ID id);
	public List<T> getByQuery(Query query);
	public T save(T data);
	public T delete(ID id);
	public void deleteByQuery(Query query);
	public T update(T data);
}
