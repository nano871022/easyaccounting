package co.com.japl.ea.utls;

import java.util.Map;
import java.util.TreeMap;

import org.pyt.common.annotations.Inject;
import org.pyt.common.common.CacheUtil;
import org.pyt.common.common.Log;
import org.pyt.common.reflection.Reflection;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.japl.ea.dto.system.PermissionDTO;

public class PermissionUtil implements Reflection {
	private Log logger = Log.Log(this.getClass());
	private static PermissionUtil permission;
	private Map<String, PermissionDTO> cache;
	@Inject
	private IGenericServiceSvc<PermissionDTO> permissionSvc;

	private PermissionUtil() {
		cache = new TreeMap<>();
		inject();
	}

	public static PermissionUtil INSTANCE() {
		if (CacheUtil.INSTANCE().isRefresh("PermissionUtil")) {
			permission = new PermissionUtil();
			CacheUtil.INSTANCE().load("PermissionUtil");
		}
		return permission;
	}

	public Boolean have() {
		PermissionDTO permission = new PermissionDTO();
		permission.setName(nombre);
		permission.setState(1);
		permission = permissionSvc.get(permission);
		return false;
	}

	@Override
	public Log logger() {
		return logger;
	}

}
