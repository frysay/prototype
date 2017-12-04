package com.prototype.data.dao;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface JpaRepository<T, ID extends Serializable> extends CrudRepository<T, Serializable> {

}
