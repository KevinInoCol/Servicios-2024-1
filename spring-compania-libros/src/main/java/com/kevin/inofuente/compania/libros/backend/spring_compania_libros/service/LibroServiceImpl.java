package com.kevin.inofuente.compania.libros.backend.spring_compania_libros.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kevin.inofuente.compania.libros.backend.spring_compania_libros.model.Libro;
import com.kevin.inofuente.compania.libros.backend.spring_compania_libros.model.dao.ILibroDao;
import com.kevin.inofuente.compania.libros.backend.spring_compania_libros.response.LibroResponseRest;

@Service
public class LibroServiceImpl implements ILibroService{

private static final Logger log = LoggerFactory.getLogger(LibroServiceImpl.class);
	
	@Autowired
	private ILibroDao libroDao;

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<LibroResponseRest> buscarLibros() {
		log.info("inicio metodo buscarLibros()");
		
		LibroResponseRest response = new LibroResponseRest();
		
		try {
			
			List<Libro> libro = (List<Libro>) libroDao.findAll();
			
			response.getLibroResponse().setLibro(libro);
			
			response.setMetada("Respuesta ok", "00", "Respuesta exitosa");
			
		} catch (Exception e) {
			response.setMetada("Respuesta nok", "-1", "Error al consulta libros");
			log.error("error al consultar libros: ", e.getMessage());
			e.getStackTrace();
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK); //devuelve 200
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<LibroResponseRest> buscarPorId(Long id) {
		log.info("Inicio metodo buscarPorId)");
		
		LibroResponseRest response = new LibroResponseRest();
		List<Libro> list = new ArrayList<>();
		
		try {
			
			Optional<Libro> libro = libroDao.findById(id);
			
			if (libro.isPresent()) {
				list.add(libro.get());
				response.getLibroResponse().setLibro(list);
			} else {
				log.error("Error en consultar libro");
				response.setMetada("Respuesta nok", "-1", "Libro no encontrado");
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			log.error("Error en consultar libro");
			response.setMetada("Respuesta nok", "-1", "Error al consultar libro");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.setMetada("Respuesta ok", "00", "Respuesta exitosa");
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK); //devuelve 200
	}

	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> crear(Libro libro) {
		log.info("Inicio metodo crear libro");
		
		LibroResponseRest response = new LibroResponseRest();
		List<Libro> list = new ArrayList<>();
		
		try {
			
			Libro libroGuardada = libroDao.save(libro);
			
			if( libroGuardada != null) {
				list.add(libroGuardada);
				response.getLibroResponse().setLibro(list);
			} else {
				log.error("Error en grabar libro");
				response.setMetada("Respuesta nok", "-1", "Libro no guardado");
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
			
		} catch( Exception e) {
			log.error("Error en grabar libro");
			response.setMetada("Respuesta nok", "-1", "Error al grabar libro");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.setMetada("Respuesta ok", "00", "Libro creado");
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK); //devuelve 200

	}

	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> actualizar(Libro libro, Long id) {
		log.info("Inicio metodo actualizar");
		
		LibroResponseRest response = new LibroResponseRest();
		List<Libro> list = new ArrayList<>();
		
		try {
			
			Optional<Libro> libroBuscado = libroDao.findById(id);
			
			if (libroBuscado.isPresent()) {
				libroBuscado.get().setNombre(libro.getNombre());
				libroBuscado.get().setDescripcion(libro.getDescripcion());
				libroBuscado.get().setCategoria(libro.getCategoria());
				 
				 Libro libroActualizar = libroDao.save(libroBuscado.get()); //actualizando
				 
				 if( libroActualizar != null ) {
					 response.setMetada("Respuesta ok", "00", "Libro actualizado");
					 list.add(libroActualizar);
					 response.getLibroResponse().setLibro(list);
				 } else {
					 log.error("error en actualizar libro");
					 response.setMetada("Respuesta nok", "-1", "Libro no actualizado");
					 return new ResponseEntity<LibroResponseRest>(response, HttpStatus.BAD_REQUEST);
				 }
				 
				 
			} else {
				log.error("error en actualizar libro");
				response.setMetada("Respuesta nok", "-1", "libro no actualizado");
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch ( Exception e) {
			log.error("error en actualizar libro", e.getMessage());
			e.getStackTrace();
			response.setMetada("Respuesta nok", "-1", "Libro no actualizado");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);

	}

	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> eliminar(Long id) {
		log.info("Inicio metodo eliminar libro");
		
		LibroResponseRest response = new LibroResponseRest();
		
		 try {
			 
			 //eliminamos el registro
			 libroDao.deleteById(id);
			 response.setMetada("Respuesta ok", "00", "Libro eliminado");
			 
		 } catch (Exception e) {
			log.error("error en eliminar libro", e.getMessage());
			e.getStackTrace();
			response.setMetada("Respuesta nok", "-1", "Libro no eliminado");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR); 
		 }
		 
		 return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);

	}


}