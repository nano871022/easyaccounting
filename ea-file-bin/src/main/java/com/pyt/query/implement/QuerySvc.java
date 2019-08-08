package com.pyt.query.implement;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.Compare;
import org.pyt.common.common.Log;
import org.pyt.common.exceptions.FileBinException;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.validates.ValidateValueException;
import org.pyt.common.interfaces.IDelClass;
import org.pyt.common.interfaces.IUpdClass;
import org.pyt.common.reflection.ReflectionUtils;
import org.pyt.common.validates.ValidateValues;

import com.pyt.query.interfaces.IQuerySvc;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;
import co.com.japl.ea.dto.system.UsuarioDTO;

public class QuerySvc implements IQuerySvc {
	private FileBin fb;
	private Log logger = Log.Log(this.getClass());

	public QuerySvc() {
		fb = new FileBin();
	}

	@Override
	public <T extends ADto> List<T> gets(T obj, Integer init, Integer end) throws QueryException {
		List<T> lista = gets(obj);
		if (lista == null || lista.size() == 0) {
			return lista;
		}
		if (init > 0) {
			init = init + 1;
		}
		if (lista.size() > init && lista.size() > init + end) {
			return lista.subList(init, init + end);
		}
		if (lista.size() > init && lista.size() < (init + end)) {
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
				list.stream()
				.filter(filter->(new Compare<T>(filter)).to(obj))
				.forEach(row->lista.add(row));
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
		Boolean nuevo = false;
		if (obj == null) {
			throw new QueryException("No se suministro el objeto a configurar.");
		}
		if (user == null)
			throw new QueryException("No se suministro el usuario.");
		T o = null;
		nuevo = StringUtils.isBlank(obj.getCodigo());
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
		} else if (StringUtils.isBlank(obj.getCodigo()) || obj.getCodigo().length() == 0) {
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
				if (!nuevo)
					getDelUpd(obj, UpdClass.class, user.getNombre());
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
		String cadena = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
		Integer max = 15;
		String name = dto.getSimpleName();
		Integer lenSize = size.toString().length();
		name = name.replace("DTO", "");
		Integer length = name.length();
		if (length > max) {
			name = name.substring(0, 7);
			length = name.length();
		}
		StringBuilder sb = new StringBuilder();
		LocalDateTime fecha = LocalDateTime.now();
		sb.append(name);
		sb.append(fecha.getYear());
		sb.append(fecha.getMonthValue());
		sb.append(fecha.getDayOfMonth());
		sb.append(fecha.getHour());
		sb.append(fecha.getMinute());
		sb.append(fecha.getSecond());
		Random aleatorio = new Random();
		for (int i = 0; i < max - length - lenSize; i++) {
			Double valor = aleatorio.nextDouble() * (cadena.length() - 1 + 0);
			sb.append(cadena.charAt(valor.intValue()));
		}
		sb.append(size);
		return sb.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	private <T extends ADto, S extends ADto> void getDelUpd(T obj, Class annotation, String user) {
		if (annotation == null)
			return;
		if (obj == null)
			return;
		try {
			if (annotation == DelClass.class) {
				DelClass del = obj.getClass().getDeclaredAnnotation(DelClass.class);
				if (del != null && (del.clase() != null || StringUtils.isNotBlank(del.nombre()))) {
					Class clazz = null;
					if (StringUtils.isNotBlank(del.nombre())) {
						try {
							clazz = obj.getClass().forName(del.nombre());
						} catch (ClassNotFoundException e) {
							logger.logger(e);
							return;
						}
					} else {
						clazz = del.clase();
					}
					S bk = (S) ReflectionUtils.instanciar().copy(obj, clazz);
					((IDelClass) bk).setFechaElimina(LocalDateTime.now());
					((IDelClass) bk).setUsuarioElimina(user);
					List<S> list = fb.loadRead(clazz);
					if (list == null) {
						list = new ArrayList<S>();
					}
					list.add(bk);
					fb.write(list, clazz);
				}
			}
			if (annotation == UpdClass.class) {
				UpdClass upd = obj.getClass().getDeclaredAnnotation(UpdClass.class);
				if (upd != null && (upd.clase() != null || StringUtils.isNotBlank(upd.nombre()))) {
					Class clazz = null;
					if (StringUtils.isNotBlank(upd.nombre())) {
						try {
							clazz = obj.getClass().forName(upd.nombre());
						} catch (ClassNotFoundException e) {
							logger.logger(e);
							return;
						}
					} else {
						clazz = upd.clase();
					}
					S bk = (S) ReflectionUtils.instanciar().copy(obj, upd.clase());
					if (bk != null) {
						((IUpdClass) bk).setFechaActualizado(LocalDateTime.now());
						((IUpdClass) bk).setUsuarioActualizo(user);
						List<S> list = fb.loadRead(upd.clase());
						if (list == null) {
							list = new ArrayList<S>();
						}
						list.add(bk);
						fb.write(list, upd.clase());
					}else {
						logger.error("Se encontro problema con copia de "+obj.getClass().getName());
					}
				}
			}
		} catch (FileBinException | SecurityException e) {
			logger.logger(e);
		}
	}

	@SuppressWarnings({ "unchecked" })
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
			getDelUpd(obj, DelClass.class, user.getNombre());
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
