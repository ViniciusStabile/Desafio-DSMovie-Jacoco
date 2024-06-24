package com.devsuperior.dsmovie.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	@InjectMocks
	private ScoreService service;

	@Mock
	private UserService userService;

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private ScoreRepository scoreRepository;

	private MovieEntity movieEntity;
	private UserEntity userEntity;
	private ScoreEntity scoreEntity;
	private ScoreDTO scoreDTO;

	private Long movieIdExistente, movieIdInexistente;

	@BeforeEach
	void setUp() throws Exception {

		scoreDTO = ScoreFactory.createScoreDTO();
		scoreEntity = ScoreFactory.createScoreEntity();
		userEntity = UserFactory.createUserEntity();
		movieEntity = MovieFactory.createMovieEntity();
		movieEntity.getScores().add(scoreEntity);

		movieIdExistente = 1l;
		movieIdInexistente = 2L;

		Mockito.when(movieRepository.findById(movieIdExistente)).thenReturn(Optional.of(movieEntity));
		Mockito.when(movieRepository.findById(movieIdInexistente)).thenThrow(ResourceNotFoundException.class);
		Mockito.when(movieRepository.save(any())).thenReturn(movieEntity);

		Mockito.when(scoreRepository.saveAndFlush(any())).thenReturn(scoreEntity);

		Mockito.when(userService.authenticated()).thenReturn(userEntity);
	}

	@Test
	public void saveScoreShouldReturnMovieDTO() {

		Mockito.when(userService.authenticated()).thenReturn(userEntity);

		MovieDTO returnedMovieDTO = service.saveScore(scoreDTO);

		Assertions.assertNotNull(returnedMovieDTO);
		Assertions.assertEquals(movieEntity.getId(), returnedMovieDTO.getId());
		Assertions.assertEquals(movieEntity.getTitle(), returnedMovieDTO.getTitle());
		Assertions.assertEquals(movieEntity.getImage(), returnedMovieDTO.getImage());
		Assertions.assertEquals(movieEntity.getCount(), returnedMovieDTO.getCount());
	}

	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {

		Mockito.when(userService.authenticated()).thenReturn(userEntity);

		scoreDTO = new ScoreDTO(movieIdInexistente, 9.0);
		Assertions.assertThrows(ResourceNotFoundException.class, () -> service.saveScore(scoreDTO));

	}
}
