package co.com.japl.ea.mybatis;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.dto.DepartamentoDTO;
import com.pyt.service.dto.PaisDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.ParametroGrupoDTO;

import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.mybatis.impl.QueryMyBatisSvc;

public class Main {

	public static void main(String[] args) {
		IQuerySvc query = new QueryMyBatisSvc();
		var parametro = new ParametroDTO();
		parametro.setNombre("alejo");
		parametro.setEstado("1");
		parametro.setGrupo("*");
		var parametro2 = new ParametroDTO();
		parametro2.setNombre("alejo");
		var parametroGrupo = new ParametroGrupoDTO();
		parametroGrupo.setGrupo("MONEDA");
		var pais = new PaisDTO();
		pais.setNombre("Colombia");
		var departamento = new DepartamentoDTO();
		departamento.setNombre("Antioquia");
		try {
			query.set(parametro, new UsuarioDTO());
			pais.setEstado(parametro);
			query.set(pais, new UsuarioDTO());
			departamento.setPais(pais);
			query.set(departamento, new UsuarioDTO());
			parametroGrupo.setParametro(parametro.getCodigo());
//			query.set(parametroGrupo, new UsuarioDTO());
			var listGP = query.gets(new ParametroGrupoDTO());
			var group = query.get(parametroGrupo);
			var list = query.gets(parametro2);
			var list2 = query.gets(parametro2, 3, 3);
			var onces = query.get(parametro);
			System.out.println(list.size() + " - " + (onces != null) + " " + list2.size() + " " + (group != null) + " "
					+ listGP.size());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
		} finally {
			System.exit(1);
		}
	}

}
