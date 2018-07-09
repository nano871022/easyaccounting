package com.pyt.query.implement;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.ADto;
import org.pyt.common.common.Compare;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.common.ValidateValues;
import org.pyt.common.exceptions.FileBinException;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.ValidateValueException;

import com.pyt.query.interfaces.IQuerySvc;

public class QuerySvc implements IQuerySvc {
	private FileBin fb;

	public QuerySvc() {
		fb = new FileBin();
	}

	@Override
	public <T extends ADto> List<T> gets(T obj, Integer init, Integer end) throws QueryException {
		List<T> lista = gets(obj);
		if (lista == null || lista.size() == 0) {
			return lista;
		}
		if(init > 0) {
			init = init + 1;
		}
		if (lista.size() > init && lista.size() > init + end) {
			return lista.subList(init, init + end);
		}
		if(lista.size()>init && lista.size() < (init +end)){
			return lista.subList(init, lista.size());
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto> List<T> gets(T obj) throws QueryException {
		if (obj == null)
			throw new QueryException("El dto se encuentra vacio.");
		List<T> lista = new ArrayList<T>();
		try {
			List<T> list = (List<T>) fb.loadRead(obj.getClass());
			if (list != null) {
				for (T dto : list) {
					if ((new Compare<T>(dto)).to(obj)) {
						lista.add(dto);
					}
				}
			} else {
				list = new ArrayList<T>();
			}
		} catch (FileBinException e) {
			throw new QueryException("Se presento un problema con el archivo binario.", e);
		}
		return lista;
	}

	@Override
	public <T extends ADto> T get(T obj) throws QueryException {
		List<T> lista = gets(obj);
		if (lista != null && lista.size() == 1) {
			return lista.get(0);
		} else if (lista != null && lista.size() > 1) {
			throw new QueryException("Se encontraron varios registros.");
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public <T extends ADto> T set(T obj, UsuarioDTO user) throws QueryException {
		if (obj == null) {
			throw new QueryException("No se suministro el objeto a configurar.");
		}
		if (user == null)
			throw new QueryException("No se suministro el usuario.");
		T o = null;
		List<T> lista = null;
		try {
			o = (T) obj.getClass().getConstructor().newInstance();
			lista = gets(o);
		} catch (InstantiationException | IllegalAccessException e1) {
			lista = new ArrayList<T>();
		} catch (IllegalArgumentException e) {
			lista = new ArrayList<T>();
		} catch (InvocationTargetException e) {
			lista = new ArrayList<T>();
		} catch (NoSuchMethodException e) {
			lista = new ArrayList<T>();
		} catch (SecurityException e) {
			lista = new ArrayList<T>();
		}
		if (StringUtils.isNotBlank(obj.getCodigo())) {
			T dto = null;
			obj.setActualizador(user.getNombre());
			obj.setFechaActualizacion(new Date());
			if (lista.size() != 0) {
				for (int i = 0; i < lista.size(); i++) {
					dto = lista.get(i);
					try {
						if (new ValidateValues().validate(dto.getCodigo(), obj.getCodigo())) {
							lista.set(i, obj);
						}
					} catch (ValidateValueException e) {
						lista.add(obj);
					}
				}
			} else {
				if (lista == null) {
					lista = new ArrayList<T>();
				}
				lista.add(obj);
			}
		} else if (obj.getCodigo() == null || obj.getCodigo().length() == 0) {
			obj.setCreador(user.getNombre());
			obj.setFechaCreacion(new Date());
			if (lista == null) {
				lista = new ArrayList<T>();
			}
			obj.setCodigo(genConsecutivo(obj.getClass(), lista.size()));
			lista.add(obj);
		}
		if (lista != null && lista.size() > 0) {
			try {
				fb.write(lista, (Class<T>) obj.getClass());
			} catch (FileBinException e) {
				throw new QueryException("Se presento problem aen el almacenamieto del registro.", e);
			}
		}
		return obj;
	}

	/**
	 * Se encarga de generar el consecutivo
	 * 
	 * @param dto
	 * @return
	 */
	private final <T extends ADto> String genConsecutivo(Class<T> dto, Integer size) {
		String cadena = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnÑñOoPpQqRrSsTtUuVvWwXxYyZz1234567890*+{[]}-_.:,;/%$#!|?¡¿";
		Integer max = 15;
		String name = dto.getSimpleName();
		Integer lenSize = size.toString().length();
		name = name.replace("DTO", "");
		Integer length = name.length();
		if (length > max) {
			name = name.substring(0, 7);
			length = name.length();
		}
		Random aleatorio = new Random();
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		for (int i = 0; i < max - length - lenSize; i++) {
			Double valor = aleatorio.nextDouble() * (cadena.length() - 1 + 0);
			sb.append(cadena.charAt(valor.intValue()));
		}
		sb.append(size);
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto> void del(T obj, UsuarioDTO user) throws QueryException {
		try {
			T cp = (T) obj.getClass().getConstructor().newInstance();
			List<T> lista = gets(cp);
			if (lista != null && lista.size() > 0) {
				Integer count = lista.size();
				for (int i = 0; i < count; i++) {
					T dto = lista.get(i);
					if ((new Compare<T>(dto)).to(obj)) {
						lista.remove(dto);
						i = 0;
						count = lista.size();
					}
				}
			} else if (lista == null || lista.size() == 0) {
				throw new QueryException("No se encontraron registros.");
			}
			fb.write(lista, (Class<T>) cp.getClass());
		} catch (InstantiationException e) {
			throw new QueryException("Problema de instanciacion.", e);
		} catch (IllegalAccessException e) {
			throw new QueryException("Problema con acceso ilegal.", e);
		} catch (IllegalArgumentException e) {
			throw new QueryException("problema Argumento ilegal ", e);
		} catch (InvocationTargetException e) {
			throw new QueryException("Problema en invocacion", e);
		} catch (NoSuchMethodException e) {
			throw new QueryException("No se encontro metodo.", e);
		} catch (SecurityException e) {
			throw new QueryException("Problema de seguridad.", e);
		} catch (FileBinException e) {
			throw new QueryException("Problema en la eliminacion del registro.", e);
		}
	}

	@Override
	public <T extends ADto> Integer countRow(T obj) throws QueryException {
		List<T> lista = gets(obj);
		return lista.size();
	}

}
