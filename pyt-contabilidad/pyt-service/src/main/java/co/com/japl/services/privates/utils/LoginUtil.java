package co.com.japl.services.privates.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.pyt.common.annotations.Inject;
import org.pyt.common.common.Log;
import org.pyt.common.reflection.Reflection;

import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.interfaces.IUsersSvc;

import co.com.japl.ea.dto.system.LoginDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;

public final class LoginUtil implements Reflection{
	private final Log logger = Log.Log(LoginUtil.class);
	private Map<String,LoginDTO> cacheUsuarios;
	private static LoginUtil loginUtil;
	@Inject()
	private IGenericServiceSvc<LoginDTO> loginSvc;
	
	private LoginUtil() {
		cacheUsuarios = new HashMap<String, LoginDTO>();
	}
	
	public final static LoginUtil INSTANCE() {if(loginUtil == null) {loginUtil= new LoginUtil();}return loginUtil;}

	public void loadLogin(LoginDTO login) {
		cacheUsuarios.put(login.getCodigo(),login);
	}
	
	public void removeLogin(LoginDTO login) {
		cacheUsuarios.remove(login.getCodigo());
	}
	
	public void searchLogin(UsuarioDTO usuario) {
		var login = new LoginDTO();
		login.setUsuario(usuario);
		login.setIpPublica(remoteAddr());
		var list = loginSvc.getAll(login);
		list.stream()
		.filter(row -> row.getFechaEliminacion() == null && row.getFechaFin().compareTo(LocalDate.now())<=0)
		.forEach(row -> cacheUsuarios.put( String.valueOf(row.getUsuario().getCodigo()) , row));
		
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
