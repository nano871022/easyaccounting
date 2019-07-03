package co.com.japl.ea.gdb.privates;

import java.time.LocalDate;

import org.pyt.common.common.UsuarioDTO;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.dto.BancoDTO;
import com.pyt.service.dto.ParametroDTO;

import co.com.japl.ea.gdb.impls.QueryGDBSvc;

public class Main {
	public IQuerySvc svc;
	public Main() {
		svc = new QueryGDBSvc();
	}

	public static void main(String[] args) {
		var main = new Main();
		try {
//			main.testParametroDTO();
			main.testBancoDTO();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {System.exit(0);}
		
	}
	
	protected ParametroDTO getParametroAny()throws Exception {
		var list = svc.gets(new ParametroDTO(),0,1);
		return list.size()>0?list.get(0):null;
	}
	
	protected UsuarioDTO getUsuario() {
		var usuario = new UsuarioDTO();
		usuario.setNombre("alejanro parra");
		usuario.setPassword("asdasd");
		return usuario;
	}
	
	protected void testBancoDTO() throws Exception{
		var usuario = getUsuario();
		var parametro = getParametroAny();
		var banco = new BancoDTO();
		banco.setNombre("bancolombia");
		banco.setDescripcion("Banco de cuenta ahorros");
		banco.setEstado(parametro);
		banco.setTipoBanco(parametro);
		banco.setTipoCuenta(parametro);
		banco.setFechaApertura(LocalDate.now());
		banco.setNumeroCuenta("104567896351");
		svc.set(banco, usuario);
		var list = svc.gets(new BancoDTO());
		svc.del(banco, usuario);
		list.forEach(bank->System.out.println(bank.toStringAll()));
	}
	
	protected void testParametroDTO()throws Exception {
		var usuario = getUsuario();
		var obj = new ParametroDTO();
		var dto = new ParametroDTO();
		dto.setNombre("parametro 1");
		dto.setDescripcion("descripcion parametro");
		dto.setValor("1");
		dto.setEstado("1");
		dto.setGrupo("*");
		svc.set(dto, usuario);
		dto.setValor2("1212");
		svc.set(dto, usuario);
		svc.del(dto, usuario);
		var list = svc.gets(obj);
		list.forEach(parametro->System.out.println(parametro.toStringAll()));
		
	}

}
