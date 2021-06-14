package com.api.rest.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.api.rest.model.Tutorial;

public interface TutorialRepository extends MongoRepository<Tutorial, String> {

	//config from database conection
	
	List<Tutorial> findByTitleContaining(String title);
	List<Tutorial> findByPublished(boolean pubished);
	
}
