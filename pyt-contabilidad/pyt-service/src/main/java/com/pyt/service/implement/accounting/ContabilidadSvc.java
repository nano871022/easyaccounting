package com.pyt.service.implement.accounting;

import java.math.BigDecimal;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.common.ListUtils;
import org.pyt.common.common.OperacionesUtil;
import org.pyt.common.common.ValidationUtils;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.accounting.ContabilidadException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.DetalleDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.contabilidad.IContabilidadSvc;

import co.com.japl.ea.dto.contabilidad.CajaMayorDTO;
import co.com.japl.ea.dto.contabilidad.CajaMenorDTO;
import co.com.japl.ea.dto.contabilidad.CuotaDTO;
import co.com.japl.ea.dto.contabilidad.GenerarCuotaDTO;
import co.com.japl.ea.dto.contabilidad.PagoDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;

public class ContabilidadSvc extends Services implements IContabilidadSvc {

	@Inject
	private IQuerySvc query;
	
	@Override
	public void loadPayed(PagoDTO pago,UsuarioDTO user)throws ContabilidadException {
		try {
			if(pago.getPagoCuota()) {
				pagoCuota(pago,user);
			}else if(pago.getPagoParcial()) {
				pagoParcial(pago,user);
			}else if(pago.getPagoTotal()) {
				pagoTotal(pago,user);
			}else {
				throw new ContabilidadException(messageI18n("err.svc.accounting.loadpayed.invalid.option.pay"));
			}
			if(DtoUtils.haveCode(pago.getBanco()) && StringUtils.isNotBlank(pago.getNumeroCuenta())) {
				cashBank(pago,user);
			}else {
				cashDesk(pago,user);
			}
			if(!query.insert(pago, user)) {
				throw new ContabilidadException(messageI18n("err.svc.accounting.loadpayed.pago.not.insert"));
			}
		}catch(QueryException e) {
			throw new ContabilidadException(messageI18n("err.svc.accounting.loadpayed.query"),e);
		}
	}
	
	private void cashDesk(PagoDTO pago,UsuarioDTO user)throws ContabilidadException{
		try {
			var cash = new CajaMenorDTO();
			cash.setValor(pago.getValorPagadoNeto());
			cash.setPago(pago);
			if(pago.getCuota().getDocumento().getTipoDocumento() == getTipoDocumento(ParametroConstants.CONST_VALOR2_DOCUMENTO_POR_COBRAR)){
				cash.setFechaIngreso(pago.getFechaPagado());
			}
			if(pago.getCuota().getDocumento().getTipoDocumento() == getTipoDocumento(ParametroConstants.CONST_VALOR2_DOCUMENTO_POR_COBRAR)){
				cash.setFechaSalida(pago.getFechaPagado());
			}
			if(!query.insert(cash, user)) {
				throw new ContabilidadException(messageI18n("err.svc.accounting.cashdesk.not.inserted"));
			}
		}catch(QueryException e) {
			throw new ContabilidadException(messageI18n("err.svc.accounting.cashdesk.query"),e);
		}
	}
	
	private void cashBank(PagoDTO pago,UsuarioDTO user)throws ContabilidadException{
		try{
			var cash = new CajaMayorDTO();
			cash.setBanco(pago.getBanco());
			cash.setValor(pago.getValorPagadoNeto());
			cash.setPago(pago);
			if(pago.getCuota().getDocumento().getTipoDocumento() == getTipoDocumento(ParametroConstants.CONST_VALOR2_DOCUMENTO_POR_COBRAR)){
				cash.setFechaIngreso(pago.getFechaPagado());
			}
			if(pago.getCuota().getDocumento().getTipoDocumento() == getTipoDocumento(ParametroConstants.CONST_VALOR2_DOCUMENTO_POR_COBRAR)){
				cash.setFechaSalida(pago.getFechaPagado());
			}
			if(!query.insert(cash, user)) {
				throw new ContabilidadException(messageI18n("err.svc.accounting.cashbank.not.inserted"));
			}
		}catch(QueryException e) {
			throw new ContabilidadException(messageI18n("err.svc.accounting.cashbank.query"),e);
		}
	}
	
	private void pagoCuota(PagoDTO pago,UsuarioDTO user) throws QueryException {
		var cuota = pago.getCuota();
		cuota.setValorPagadoNeto(pago.getValorPagadoNeto());
		cuota.setValorImpuestos(pago.getValorImpuestos());
		cuota.setEstado(getEstadoCuota(ParametroConstants.CONST_VALOR2_PAYED));
		if(!query.update(cuota, user)) {
			throw new QueryException(messageI18n("err.svc.accounting.payed.not.update"));
		}
	}
	
	private void pagoParcial(PagoDTO pago,UsuarioDTO user)throws QueryException {
		var cuota = pago.getCuota();
		cuota.setValorPagadoNeto(pago.getValorPagadoNeto());
		cuota.setValorImpuestos(pago.getValorImpuestos());
		cuota.setEstado(getEstadoCuota(ParametroConstants.CONST_VALOR2_PARTIAL_PAY));
		if(!query.update(cuota, user)) {
			throw new QueryException(messageI18n("err.svc.accounting.partialpay.not.update"));
		}
	}
	
	private void pagoTotal(PagoDTO pago,UsuarioDTO user) throws QueryException{
		var cuotas = new CuotaDTO();
		var impuestoPagado = pago.getValorImpuestos();
		var valorNeto = pago.getValorPagadoNeto();
		cuotas.setDocumento(pago.getCuota().getDocumento());
		cuotas.setEstado(getEstadoCuota(ParametroConstants.CONST_VALOR2_PENDING_PAY));
		Map<String,String> fieldOrder = new HashMap<>();
		fieldOrder.put("fechaPago","ASC");
		var list = query.gets(cuotas,query.order(cuotas, fieldOrder));
		for(var cuota : list) {
			var neto = cuota.getValorNeto();
			var impuesto = OperacionesUtil.impuesto(cuota.getValorNeto(),cuota.getIva());
			cuota.setValorImpuestos(getGreatherAndSubstract(impuestoPagado, impuesto));
			cuota.setValorPagadoNeto(getGreatherAndSubstract(valorNeto, neto));
			if(!query.update(cuota, user)) {
				throw new QueryException(messageI18n("err.svc.accounting.totalpay.not.update",cuota.getCodigo(),cuota.getValorPagadoNeto(),cuota.getValorImpuestos()));
			}
		}
	}
	
	private BigDecimal getGreatherAndSubstract(BigDecimal origen,BigDecimal compare) {
		if(origen.compareTo(compare)>=0) {
			origen = origen.subtract(compare);
			return compare;
		}
		origen = BigDecimal.ZERO;
		return origen;
	}
	
	private ParametroDTO getEstadoCuota(String valor2) throws QueryException{
		var parametro = new ParametroDTO();
		parametro.setGrupo(ParametroConstants.GRUPO_ESTADO_PAY);
		parametro.setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
		parametro.setValor2(valor2);
		var estado = query.gets(parametro);
		if(ListUtils.haveOneItem(estado)){
			return estado.get(0);
		}
		throw new QueryException(messageI18n("err.svc.accounting.getStatePay.not.found",valor2,ParametroConstants.GRUPO_ESTADO_PAY));
	}
	
	private ParametroDTO getTipoDocumento(String valor2)throws QueryException{
		var parametro = new ParametroDTO();
		parametro.setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
		parametro.setGrupo(ParametroConstants.GRUPO_TIPO_DOCUMENTO);
		parametro.setValor2(valor2);
		var list = query.gets(parametro);
		if(ListUtils.haveOneItem(list)) {
			return list.get(0);
		}
		throw new QueryException(messageI18n("err.svc.accounting.getdocumenttype.not.found",valor2,ParametroConstants.GRUPO_TIPO_DOCUMENTO));
	}

	@Override
	public void generarCuotas(GenerarCuotaDTO cuotas, UsuarioDTO user) throws ContabilidadException {
		try {
			if(!DtoUtils.haveCode(user)) {
				throw new ContabilidadException(messageI18n("err.svc.accounting.generatequotes.users.not.send"));
			}
			if(!DtoUtils.haveCode(cuotas.getDocumento())) {
				throw new ContabilidadException(messageI18n("err.svc.accounting.generatequotes.document.invalid"));
			}
			if(!ValidationUtils.greaterZero(cuotas.getNumeroCuotas())) {
				throw new ContabilidadException(messageI18n("err.svc.accounting.generatequotes.numberquotes.invalid"));
			}
			if(cuotas.getFechaInicio() == null) {
				throw new ContabilidadException(messageI18n("err.svc.accounting.generatequotes.initdate.empty"));
			}
			if(cuotas.getNumeroCuotas() > 1 && !DtoUtils.haveCode(cuotas.getPeriocidad())){
				throw new ContabilidadException(messageI18n("err.svc.accounting.generatequotes.period.invalid"));
			}
			var percent = OperacionesUtil.plus(cuotas.getPorcentaje());
			if(percent > 1.0 && percent*100 > 100) {
				throw new ContabilidadException(messageI18n("err.svc.accounting.generatequotes.percent.max.invalid",percent));
			}
			cuotas.setPorcentaje(obtenerPorcentajeFaltantes(cuotas.getPorcentaje(), cuotas.getNumeroCuotas()));
			var detalle = new DetalleDTO();
			detalle.setCodigoDocumento(cuotas.getDocumento().getCodigo());
			var listDetail = query.gets(detalle);
			var valorNeto = listDetail.stream().map(detail->detail.getValorNeto()).reduce(BigDecimal.ZERO, BigDecimal::add);
			var impuesto = listDetail.stream().map(detail->detail.getValorIva()).distinct().reduce(BigDecimal.ZERO,BigDecimal::add);
			List<CuotaDTO> list = new ArrayList<>();
			var statePendingPay = getEstadoCuota(ParametroConstants.CONST_VALOR2_PENDING_PAY);
			list.add(generateQuoteOne(cuotas,valorNeto,impuesto.doubleValue(),statePendingPay));
			for(var i = 1; i < cuotas.getNumeroCuotas();i++) {
				list.add(generateNextQuote(ListUtils.lastValue(list), cuotas,valorNeto,impuesto.doubleValue(),statePendingPay));
			}
			for(var cuota : list) {
				if(!query.insert(cuota, user)) {
					throw new ContabilidadException(messageI18n("err.svc.accounting.generatequotes.insertquote",cuota.toString()));
				}
			}
			if(!query.insert(cuotas, user)) {
				throw new ContabilidadException(messageI18n("err.svc.accounting.generatequotes.insertgeneratequote"));
			}
		}catch(QueryException e) {
			throw new ContabilidadException(messageI18n("err.svc.accounting.generatequotes.insertsquote",cuotas.toString()));
		}
	}
	
	private Double[] obtenerPorcentajeFaltantes(Double[] percent,Integer numeroCuotas) {
		var list = Arrays.asList(percent);
		var plus = OperacionesUtil.plus(percent);
		var faltante = plus > 1D ?(100D - plus)/100D:1D-plus;
		if(percent.length > 0 && percent.length < numeroCuotas) {
			var diferencia = numeroCuotas - percent.length;
			var porcentaje = faltante/diferencia;
			for(int i = 0; i < diferencia;i++) {
				list.add(porcentaje);
			}
		}
		return (Double[]) list.stream().map(value->value>1D?value/100:value).toArray(Double[]::new);
	}
	
	private CuotaDTO generateQuoteOne(GenerarCuotaDTO cuotas,BigDecimal valor,Double impuesto,ParametroDTO statePendingPay) throws QueryException{
		var cuota = new CuotaDTO();
		cuota.setDocumento(cuotas.getDocumento());
		cuota.setFechaPago(cuotas.getFechaInicio());
		cuota.setNumeroCuota(1);
		cuota.setPeriodo(cuotas.getPeriocidad());
		cuota.setEstado(statePendingPay);
		cuota.setValorNeto(calcularValorNetoCuota(cuota.getNumeroCuota(),valor,cuotas.getPorcentaje()));
		cuota.setIva(impuesto);
		cuota.setValor(OperacionesUtil.impuesto(cuota.getValorNeto(), impuesto));
		return cuota;
	}
	private BigDecimal calcularValorNetoCuota(Integer numeroCuota,BigDecimal valor,Double[] porcentage) {
		BigDecimal valorCuota = new BigDecimal(valor.toString());
		valorCuota = OperacionesUtil.impuesto(valor, porcentage[numeroCuota-1]);
		return valorCuota;
	}

	private CuotaDTO generateNextQuote(CuotaDTO ultimaCuota,GenerarCuotaDTO cuotas,BigDecimal valor,Double impuesto,ParametroDTO statePendingPay) throws QueryException {
		var cuota = new CuotaDTO();
		cuota.setDocumento(cuotas.getDocumento());
		cuota.setNumeroCuota(ultimaCuota.getNumeroCuota()+1);
		cuota.setPeriodo(cuotas.getPeriocidad());
		cuota.setEstado(statePendingPay);
		cuota.setFechaPago(ultimaCuota.getFechaPago().plus(Period.parse(cuotas.getPeriocidad().getValor())));
		cuota.setValorNeto(calcularValorNetoCuota(cuota.getNumeroCuota(),valor,cuotas.getPorcentaje()));
		cuota.setIva(impuesto);
		cuota.setValor(OperacionesUtil.impuesto(cuota.getValorNeto(), impuesto));
		return cuota;
	}
	
}
