package co.com.japl.ea.gdb.privates;

import org.pyt.common.common.UsuarioDTO;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.dto.ParametroDTO;

import co.com.japl.ea.gdb.impls.QueryGDBSvc;

public class Main {

	public static void main(String[] args) {
		IQuerySvc svc = new QueryGDBSvc();
		var obj = new ParametroDTO();
		var dto = new ParametroDTO();
		try {
			dto.setNombre("parametro 1");
			dto.setDescripcion("descripcion parametro");
			dto.setValor("1");
			dto.setEstado("1");
			dto.setGrupo("*");
			svc.set(dto, new UsuarioDTO());
			dto.setValor2("1212");
			svc.set(dto, new UsuarioDTO());
			svc.del(dto, new UsuarioDTO());
			var list = svc.gets(obj);
			list.forEach(parametro->System.out.println(parametro.toStringAll()));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {System.exit(0);}
		
	}

}
