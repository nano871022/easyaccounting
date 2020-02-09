package co.com.japl.ea.utls;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.CacheUtil;
import org.pyt.common.common.Log;
import org.pyt.common.exceptions.GenericServiceException;
import org.pyt.common.reflection.Reflection;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.japl.ea.dto.system.MenuDTO;
import co.com.japl.ea.dto.system.MenuPermUsersDTO;
import co.com.japl.ea.dto.system.PermissionDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;

public class PermissionUtil implements Reflection {
	private Log logger = Log.Log(this.getClass());
	private static PermissionUtil permission;
	private MultiValuedMap<String, PermissionDTO> cachePerm;
	private MultiValuedMap<String, MenuDTO> cacheMenu;
	@Inject
	private IGenericServiceSvc<MenuPermUsersDTO> groupsSvc;
	private final static String CONST_CACHE_PERM_UTIL = "PermissionUtil";

	private PermissionUtil() {
		cachePerm = new ArrayListValuedHashMap<>();
		cacheMenu = new ArrayListValuedHashMap<>();
		inject();
	}

	public static PermissionUtil INSTANCE() {
		if (CacheUtil.INSTANCE().isRefresh(CONST_CACHE_PERM_UTIL)) {
			permission = new PermissionUtil();
			CacheUtil.INSTANCE().load(CONST_CACHE_PERM_UTIL);
		}
		return permission;
	}

	private void loadData(UsuarioDTO usuario) {
		try {
			if (CacheUtil.INSTANCE().isRefresh(CONST_CACHE_PERM_UTIL)) {
				MenuPermUsersDTO mpu = new MenuPermUsersDTO();
				mpu.setGroupUsers(usuario.getGrupoUser());
				var list = groupsSvc.getAll(mpu);
				list.forEach(row -> {
					cachePerm.put(usuario.getNombre(), row.getPerm());
					cacheMenu.put(usuario.getNombre(), row.getMenu());
				});
				CacheUtil.INSTANCE().load(CONST_CACHE_PERM_UTIL);
			}
		} catch (GenericServiceException e) {
			logger.DEBUG(e);
		}
	}

	public Boolean havePerm(String nombre, UsuarioDTO usuario) {
		loadData(usuario);
		try {
			return cachePerm.get(usuario.getNombre()).stream().filter(row -> nombre.contentEquals(row.getName()))
					.findAny().isPresent();
		} catch (NullPointerException e) {
			logger.logger(e);
		}
		return false;
	}

	public Boolean haveMenu(String url, UsuarioDTO usuario) {
		loadData(usuario);
		try {
			return cacheMenu.get(usuario.getNombre()).stream().filter(row -> url.contentEquals(row.getUrl())).findAny()
					.isPresent();
		} catch (NullPointerException e) {
			logger.logger(e);
		}
		return false;
	}

	@Override
	public Log logger() {
		return logger;
	}

}
