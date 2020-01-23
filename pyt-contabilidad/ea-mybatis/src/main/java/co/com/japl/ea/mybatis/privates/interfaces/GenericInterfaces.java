package co.com.japl.ea.mybatis.privates.interfaces;

import java.util.List;

import org.pyt.common.abstracts.ADto;

import com.pyt.service.dto.ParametroGrupoDTO;

public interface  GenericInterfaces {
	
	public <T extends ADto> void insert(T dto);
	public <T extends ADto> void update(T dto);
	public <T extends ADto> void delete(T dto);
	public <T extends ADto> ParametroGrupoDTO get(T dto);
	public <T extends ADto> List<ParametroGrupoDTO> gets(T dto);
	
	
}
