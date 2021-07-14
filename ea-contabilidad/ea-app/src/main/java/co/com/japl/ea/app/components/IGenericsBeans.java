package co.com.japl.ea.app.components;

import co.com.japl.ea.common.abstracts.ADto;
import javafx.scene.Parent;

public interface IGenericsBeans<T extends ADto> {

	public void initialize() throws Exception;

	public Parent load(Class<T> classDto) throws Exception;

}
