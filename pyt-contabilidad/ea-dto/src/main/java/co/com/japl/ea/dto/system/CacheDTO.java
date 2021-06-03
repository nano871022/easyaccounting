package co.com.japl.ea.dto.system;

import co.com.japl.ea.common.abstracts.ADto;

public class CacheDTO extends ADto {
	private String name;
	private Boolean cached;
	
	public CacheDTO() {}
	public CacheDTO(String name,Boolean cached) {
		this.name = name;
		this.cached = cached;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void setCache(boolean state) {
		cached = state;
	}
	
	public Boolean isCached() {
		return cached;
	}
	
	
}
