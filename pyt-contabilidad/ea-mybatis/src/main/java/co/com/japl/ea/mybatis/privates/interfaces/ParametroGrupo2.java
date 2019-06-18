package co.com.japl.ea.mybatis.privates.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.pyt.service.dto.ParametroGrupoDTO;

public interface ParametroGrupo2 extends GenericInterfaces {
	
	@Select("INSERT INTO TBL_PARAMETER_GROUP ( SPARAMETER, SGROUP ,SCODE) VALUES (#{parametro},#{grupo},#{codigo} )")
	public void insert(ParametroGrupoDTO dto);
	@Select("UPDATE TBL_PARAMETER_GROUP SET SPARAMETER = #{parametro} AND SGROUP = #{grupo} WHERE SCODE = #{codigo}")
	public void update(ParametroGrupoDTO dto);
	@Select("DELETE FROM TBL_PARAMETER_GROUP WHERE SCODE = #{codigo}")
	public void delete(ParametroGrupoDTO dto);
	@Select("SELECT * FROM TBL_PARAMETER_GROUP WHERE SPARAMETER = #{parametro} AND SGROUP = #{grupo}")
	@Results(value= {
			@Result(property="scode",column="codigo")
			,@Result(property="sparameter",column="parametro")
			,@Result(property="sgroup",column="grupo")
	})
	public ParametroGrupoDTO get(ParametroGrupoDTO dto);
	@Select("SELECT * FROM TBL_PARAMETER_GROUP WHERE SPARAMETER = #{parametro} AND SGROUP = #{grupo}")
	@Results(value= {
			@Result(property="scode",column="codigo")
			,@Result(property="sparameter",column="parametro")
			,@Result(property="sgroup",column="grupo")
	})
	public List<ParametroGrupoDTO> gets(ParametroGrupoDTO dto);
	
	
}
