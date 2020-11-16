package org.pyt.common.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Singleton;
import org.pyt.common.interfaces.IComunicacion;

import javafx.application.Platform;

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
public class Comunicacion<IC extends IComunicacion> implements Runnable {
	private Map<String, IC[]> suscriptores;
	private Map<String, Object[]> comandoValor;
	@SuppressWarnings("rawtypes")
	private static Comunicacion comunicacion;
	private Log logger = Log.Log(this.getClass());

	private Comunicacion() {
	}

	/**
	 * Es el encargado de realiar un singleton de ete objeto
	 * 
	 * @return {@link Comunicacion}
	 */
	@SuppressWarnings("rawtypes")
	public final static Comunicacion singleton() {
		if (comunicacion == null) {
			comunicacion = new Comunicacion();
			new Thread(comunicacion).start();
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
		if (comandoValor != null) {
			Set<String> comandos = comandoValor.keySet();
			List<String> comandosWorks = new ArrayList<>();
			if (comandos != null)
				if (comandos.size() > 0)
					comandos.stream()
					.filter(comando -> StringUtils.isNotBlank(comando))
					.forEach(comando -> {
						AtomicInteger count = new AtomicInteger(0);
						var result = this.suscriptores.get(comando);
						if(ListUtils.isNotBlank(result)) {
							Arrays.asList(result).stream()
							.filter(subscriptor -> subscriptor != null)
							.forEach(subscriptor -> {
								var valores = comandoValor.get(comando);
								if(ListUtils.isNotBlank(valores)) {
									Arrays.asList(valores)
									.forEach(valor->subscriptor.get(comando, valor));
									count.incrementAndGet();
								}
							});
						}
						if(count.get() > 0) {
							comandosWorks.add(comando);
							count.set(0);
						}
					});
			if(ListUtils.isNotBlank(comandosWorks)) {
				comandosWorks.forEach(comando->comandoValor.remove(comando));
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
		try {
			T[] valores = (T[])comandoValor.get(comando);
			if (ListUtils.isNotBlank(valores )) {
				List<T> lvalores = new ArrayList<>();
				for(T valors : valores) {
					lvalores.add(valors);
				}
				lvalores.add(valor);
				comandoValor.put(comando, lvalores.toArray());
			} else {
				List<T> lvalores = new ArrayList<>();
				lvalores.add(valor);
				comandoValor.put(comando, lvalores.toArray());
			}
		} catch (Exception e) {
			logger.DEBUG(e);
		}
	}

	/**
	 * se encarga de realizar la subscripcion a los comandos
	 * 
	 * @param subscriber
	 *            {@link IComunicacion} extends
	 * @param comandos
	 *            {@link String} ...
	 */
	@SuppressWarnings("unchecked")
	public final synchronized <T extends IComunicacion> void subscriber(T subscriber, String... comandos) {
		if (this.suscriptores == null) {
			this.suscriptores = new HashMap<>();
		}
		if (comandos == null || comandos.length == 0 || subscriber == null) {
			logger.error("No se suministro el subscriptor o los comandos.");
			return;
		}
		if (comandos.length > 0) {
			for (String comando : comandos) {
				T[] subscribers = (T[]) suscriptores.get(comando);
				if (subscribers == null || subscribers.length == 0) {
					List<IComunicacion> lSubscribers = new ArrayList<IComunicacion>();
					lSubscribers.add(subscriber);
					T[] ss = (T[]) lSubscribers.toArray(new IComunicacion[0]);
					suscriptores.put(comando, (IC[]) ss);
				} else if (subscribers != null || subscribers.length > 0) {
					List<T> lSubscribers = Arrays.asList(subscribers).stream().collect(Collectors.toList());
					lSubscribers.add(subscriber);
					suscriptores.put(comando, (IC[]) lSubscribers.toArray(new IComunicacion[0]));
				}
			}
		}
	}

	public void run() {

		try {
			while (true) {
				Platform.runLater(() -> {
					procesar();
				});
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			logger.logger(e);
		}
	}

}
