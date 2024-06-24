package com.devsuperior.dsmovie.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {

	@InjectMocks
	private MovieService service;

	@Mock
	private MovieRepository repository;

	private MovieEntity movie;
	private MovieDTO movieDTO;
	private Long existingId, nonExistingId, depedentExistingId;
	private PageImpl<MovieEntity> page;

	@BeforeEach
	void setUp() throws Exception {

		movie = MovieFactory.createMovieEntity();
		movieDTO = new MovieDTO(movie);
		existingId = 1l;
		nonExistingId = 2L;
		depedentExistingId = 3L;
		page = new PageImpl<>(List.of(movie));

		Mockito.when(repository.searchByTitle(any(), (Pageable) any())).thenReturn(page);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(movie));
		Mockito.when(repository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		Mockito.when(repository.save(any())).thenReturn(movie);
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(movie);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(depedentExistingId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(depedentExistingId);
		Mockito.doNothing().when(repository).deleteById(existingId);

	}

	@Test
	public void findAllShouldReturnPagedMovieDTO() {

		Pageable pageable = PageRequest.of(0, 12);
		String title = "Test Movie";

		Page<MovieDTO> result = service.findAll(title, pageable);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getSize(), 1);
		Assertions.assertEquals(result.iterator().next().getTitle(), title);

	}

	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {

		MovieDTO result = service.findById(existingId);
		Assertions.assertEquals(result.getId(),existingId);

	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));

	}

	@Test
	public void insertShouldReturnMovieDTO() {

		MovieDTO result = service.insert(movieDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), 1l);

	}

	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {

		MovieDTO result = service.update(existingId, movieDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getTitle(), movieDTO.getTitle());

	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, movieDTO);
		});

	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> service.delete(existingId));
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {

		Assertions.assertThrows(DatabaseException.class, () -> service.delete(depedentExistingId));
	}
}
