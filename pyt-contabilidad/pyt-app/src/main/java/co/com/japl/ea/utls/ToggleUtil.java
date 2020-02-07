package co.com.japl.ea.utls;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.CacheUtil;
import org.pyt.common.common.Log;
import org.pyt.common.exceptions.GenericServiceException;
import org.pyt.common.reflection.Reflection;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.japl.ea.dto.system.ToggleDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;

public class ToggleUtil implements Reflection {
	@Inject
	private IGenericServiceSvc<ToggleDTO> toggleSvc;
	private Map<String, ToggleDTO> cache;
	private static ToggleUtil toggle;
	private Log logger = Log.Log(this.getClass());

	private ToggleUtil() {
		inject();
		cache = new TreeMap<>();
	}

	public static final ToggleUtil INSTANCE() {
		if (CacheUtil.INSTANCE().isRefresh("ToggleUtil")) {
			toggle = new ToggleUtil();
			CacheUtil.INSTANCE().load("ToggleUtil");
		}
		return toggle;
	}

	public Boolean isActive(String name) {
		if (cache.containsKey(name)) {
			return cache.get(name).isActivado();
		} else {
			try {
				var toggle = new ToggleDTO();
				toggle.setNombre(name);
				toggle = toggleSvc.get(toggle);
				if (StringUtils.isNotBlank(toggle.getCodigo())) {
					cache.put(name, toggle);
					return toggle.isActivado();
				}
			} catch (GenericServiceException e) {
				logger.DEBUG(name, e);
			}
		}
		try {
			var usuario = new UsuarioDTO();
			usuario.setNombre("toggleUser");
			var toggle = new ToggleDTO();
			toggle.setNombre(name);
			toggle.setActivado(false);
			toggle = toggleSvc.insert(toggle, usuario);
			cache.put(name, toggle);
		} catch (GenericServiceException e) {
			logger.DEBUG(name, e);
		}
		return false;
	}

	@Override
	public Log logger() {
		return logger;
	}

}
