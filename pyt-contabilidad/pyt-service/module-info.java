
module com.pyt.service {
	requires com.pyt.query;
	requires com.pyt.query.interfaces;
	requires org.pyt.common.common;
	requires org.pyt.common.properties;
	requires org.pyt.common.reflection;
	requires org.pyt.common.exceptions;
	requires org.pyt.common.annotation;
	requires commons.lang3;
	requires ea.exceptions;

	uses com.pyt.query.interfaces.IQuerySvc;
}
