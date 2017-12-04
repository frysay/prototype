package com.prototype.integration.dao.mock;

import java.io.Serializable;

import org.springframework.context.annotation.Profile;

import com.prototype.data.BasicObject;
import com.prototype.data.dao.BasicObjectDao;

@Profile("mock")
public class BasicObjectDaoMock implements BasicObjectDao {

	private BasicObject lastBasicObject;
	
	@Override
	public boolean wasAlreadySaved(BasicObject basicObject) {
		if(lastBasicObject != null && basicObject.equals(lastBasicObject)) {
			return true;
		}
		lastBasicObject = basicObject;
		return false;
	}

	@Override
	public <S extends BasicObject> S save(S entity) {
		return entity;
	}

	@Override
	public <S extends BasicObject> Iterable<S> save(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BasicObject findOne(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(Serializable id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<BasicObject> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<BasicObject> findAll(Iterable<Serializable> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(Serializable id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(BasicObject entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Iterable<? extends BasicObject> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}
}
