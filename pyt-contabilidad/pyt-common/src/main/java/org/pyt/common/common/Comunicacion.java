package org.pyt.common.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pyt.common.annotations.Singleton;
import org.pyt.common.interfaces.IComunicacion;

/**
 * Se encarga de ser un servicio que este en permanente comunicacion e indicar
 * cuando una informacion se reciva, un suscriptor debe subsribirse al sistema y
 * indicar que quiere escuchas, cuando desea comunicar indica que variable desea
 * comunicar
 * 
 * @author Alejandro Parra
 * @since 2018-05-24
 */
@Singleton
public class Comunicacion implements Runnable {
	private Map<String, IComunicacion[]> suscriptores;
	private Map<String, Object[]> comandoValor;
	private static Comunicacion comunicacion;
	private Thread hilo;

	private Comunicacion() {
	}

	/**
	 * Es el encargado de realiar un singleton de ete objeto
	 * 
	 * @return {@link Comunicacion}
	 */
	public final static Comunicacion singleton() {
		if (comunicacion == null) {
			comunicacion = new Comunicacion();
			comunicacion.hilo = new Thread(comunicacion);
			comunicacion.hilo.start();
		}
		return comunicacion;
	}

	/**
	 * Este metodo se encarga de realizar el procedimiento de todos los comandos
	 * recividos y enviandolos a sus subscriptores y eliminando de la lista.
	 */
	private final synchronized void procesar() {
		if (comandoValor == null) {
			comandoValor = new HashMap<>();
		}
		if (this.suscriptores == null) {
			this.suscriptores = new HashMap<>();
		}
		Integer count = 0;
		Set<String> comandos = comandoValor.keySet();
		for (String comando : comandos) {
			IComunicacion[] suscriptores = this.suscriptores.get(comando);
			if (suscriptores != null && suscriptores.length > 0) {
				for (IComunicacion suscriptor : suscriptores) {
					if (suscriptor != null) {
						Object[] valores = comandoValor.get(comando);
						if (valores != null && valores.length > 0) {
							for (Object valor : valores) {
								suscriptor.get(comando, valor);
							}
							count++;
						}
					}
				}
			}
			if (count > 0) {
				comandoValor.remove(comando);
				count = 0;
			}
		}
	}

	/**
	 * Es encargado de recibir el comando de caralogo para despues ser informado a
	 * los subscriptores de ese valor
	 * 
	 * @param comando
	 *            {@link String}
	 * @param valor
	 *            {@link Object} extends
	 */
	@SuppressWarnings("unchecked")
	public final synchronized <T extends Object> void setComando(String comando, T valor) {
		if (comandoValor == null) {
			comandoValor = new HashMap<>();
		}

		T[] valores = (T[]) comandoValor.get(comando);
		if (valores != null && valores.length > 0) {
			List<T> lvalores = List.of(valores);
			if (lvalores != null && lvalores.size() > 0) {
				lvalores.add(valor);
				comandoValor.put(comando, lvalores.toArray());
			}
		} else {
			List<T> lvalores = new ArrayList<>();
			lvalores.add(valor);
			comandoValor.put(comando, lvalores.toArray());
		}
	}

	/**
	 * se encarga de realizar la subscripcion a los comandos
	 * 
	 * @param subscriber {@link IComunicacion} extends
	 * @param comandos {@link String} ...
	 */
	@SuppressWarnings("unchecked")
	public final synchronized <T extends IComunicacion> void subscriber(T subscriber, String... comandos) {
		if (this.suscriptores == null) {
			this.suscriptores = new HashMap<>();
		}
		if (comandos == null || comandos.length == 0 || subscriber == null) {
			Log.error("No se suministro el subscriptor o los comandos.");
			return;
		}
		if (comandos.length > 0) {
			for (String comando : comandos) {
				T[] subscribers = (T[]) suscriptores.get(comando);
				if (subscribers == null || subscribers.length == 0) {
					List<T> lSubscribers = new ArrayList<>();
					lSubscribers.add(subscriber);
					suscriptores.put(comando, (IComunicacion[]) lSubscribers.toArray());
				} else if (subscribers != null || subscribers.length > 0) {
					List<T> lSubscribers = List.of(subscribers);
					lSubscribers.add(subscriber);
					suscriptores.put(comando, (IComunicacion[]) lSubscribers.toArray());
				}
			}
		}
	}

	@Override
	public void run() {

		try {
			while (true) {
				procesar();
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			Log.logger(e);
		}
	}

}
