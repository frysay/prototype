package com.prototype.data.dao;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prototype.data.BasicObject;

@Profile("!mock")
public interface BasicObjectDao extends JpaRepository<BasicObject, Long> {
	
	@Query("select case when count(*) > 0 then true else false end from BasicObject b where b.userId = :#{#basicObject.userId} and b.personalInfo = :#{#basicObject.personalInfo}")
	public boolean wasAlreadySaved(@Param("basicObject") BasicObject basicObject);
}
