package co.com.japl.services.privates.utils;

import static org.pyt.common.constants.languages.Login.CONST_LOGIN_NOT_FOUND;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.Log;
import org.pyt.common.constants.InsertResourceConstants;
import org.pyt.common.exceptions.GenericServiceException;
import org.pyt.common.reflection.Reflection;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.japl.ea.dto.system.LoginDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;

public final class LoginUtil implements Reflection{
	private final Log logger = Log.Log(LoginUtil.class);
	private Map<String,LoginDTO> cacheUsuarios;
	private static LoginUtil loginUtil;
	@Inject(resource = InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_GENERIC_SERVICE)
	private IGenericServiceSvc<LoginDTO> loginSvc;
	
	private LoginUtil() {
		cacheUsuarios = new HashMap<String, LoginDTO>();
	}
	
	public final static LoginUtil INSTANCE() {if(loginUtil == null) {loginUtil= new LoginUtil();}return loginUtil;}

	public void loadLogin(LoginDTO login) {
		cacheUsuarios.put(login.getUsuario().getCodigo(), login);
	}
	
	public void removeLogin(LoginDTO login) {
		cacheUsuarios.remove(login.getUsuario().getCodigo());
	}

	public void validLogin(UsuarioDTO usuario) {
		try {
			LoginDTO login = cacheUsuarios.getOrDefault(usuario.getCodigo(), new LoginDTO());
			if (!StringUtils.isNotBlank(login.getCodigo())) {
				login = searchLogin(usuario);
				if (login == null) {
					throw new RuntimeException(CONST_LOGIN_NOT_FOUND);
				}
			}
			if (!login.getIpPublica().contains(remoteAddr())) {
				throw new RuntimeException(CONST_LOGIN_NOT_FOUND);
			}
		} catch (UnknownHostException e) {
			logger.logger(e);
		}
	}
	
	private LoginDTO searchLogin(UsuarioDTO usuario) {
		try {
		var login = new LoginDTO();
		login.setUsuario(usuario);
		login.setIpPublica(remoteAddr());
		var list = loginSvc.getAll(login);
		list.stream()
		.filter(row -> row.getFechaEliminacion() == null && row.getFechaFin().compareTo(LocalDate.now())<=0)
		.forEach(row -> cacheUsuarios.put( String.valueOf(row.getUsuario().getCodigo()) , row));
			return cacheUsuarios.getOrDefault(usuario.getCodigo(), null);
		} catch (UnknownHostException e) {
			logger.logger(e);
		} catch (GenericServiceException e) {
			logger.logger(e);
		}
		return null;
	}
	
	public static final String remoteAddr() throws UnknownHostException {
		// return getContext().getRemoteAddr();
		return InetAddress.getLocalHost().getHostAddress();
	}

	@Override
	public Log logger() {
		return logger;
	} 
}
