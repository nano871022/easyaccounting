package com.pyt.service.implement.accounting;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DateUtils;
import org.pyt.common.common.ListUtils;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.accounting.RecordatorioException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.contabilidad.IRecordatorioSvc;

import co.com.japl.ea.dto.contabilidad.CuotaDTO;
import co.com.japl.ea.dto.contabilidad.DetRecordatorioPagoDTO;
import co.com.japl.ea.dto.contabilidad.RecordatorioPagoDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;

public class RecordatorioSvc extends Services implements IRecordatorioSvc {
	
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	@Override
	public List<CuotaDTO> findNextQuotes(UsuarioDTO usuario) throws RecordatorioException {
			var dto = new CuotaDTO();
			return findNextQuotes(dto);
		
	}

	@SuppressWarnings("unchecked")
	private List<CuotaDTO> findNextQuotes(CuotaDTO dto)throws RecordatorioException {
		try {
			var estado = getEstado(ParametroConstants.CONST_VALOR2_PAYED);
			var dto2 = new CuotaDTO();
			dto2.setEstado(estado);
			Map<String,String> fieldOrder = new HashMap<>();
			fieldOrder.put("documento", "ASC");
			fieldOrder.put("fechaCreacion", "ASC");
			var dateNext = DateUtils.add(LocalDate.now(), "P1M");
			var list = querySvc.gets(dto,querySvc.filterLessThat(dto2, "fechaPago",dateNext.toString() ),querySvc.different(dto2, "estado"),querySvc.order(dto, fieldOrder));
			return list.stream().filter( filtro -> DateUtils.lessThat(filtro.getFechaPago(),DateUtils.add(LocalDate.now(), filtro.getPeriodo().getValor()))).collect(Collectors.toList());
		}catch(QueryException e) {
			throw new RecordatorioException(messageI18n("err.svc.recordatorio.findnextquotes"));
		}	
	}
	
	@Override
	public List<CuotaDTO> findNextQuotes(String numeroFactura,UsuarioDTO usuario) throws RecordatorioException {
		try {
			var dto = new CuotaDTO();
			var documento = new DocumentoDTO();
			documento.setNumeroNota(numeroFactura);
			var documentos = querySvc.gets(documento);
			if(!ListUtils.haveOneItem(documentos)) {
				throw new RecordatorioException(this.messageI18n("err.svc.recordatorio.findnextquotes.one.not.found"));
			}
			dto.setDocumento(documento);
			return findNextQuotes(dto);
		}catch(QueryException e) {
			throw new RecordatorioException(messageI18n("err.svc.recordatorio.findnextquotes"));
		}
	}

	@Override
	public RecordatorioPagoDTO generarRecordatorio(List<CuotaDTO> cuotas,UsuarioDTO usuario) throws RecordatorioException {
		try {
			if(ListUtils.isBlank(cuotas)) {
				throw new RecordatorioException(this.messageI18n("err.svc.recordatoriosvc.generarrecordatorio.cuotas.empty"));
			}
			var numeroNota = cuotas.get(0).getDocumento().getNumeroNota();
			var valid = cuotas.stream().allMatch( cuota -> numeroNota.compareTo(cuota.getDocumento().getNumeroNota()) == 0);
			if(!valid) {
				throw new RecordatorioException(this.messageI18n("err.svc.recordatoriosvc.generarrecordatorio.cuotas.not.equals.number.note"));
			}
			var estadoParcial = getEstado(ParametroConstants.CONST_VALOR2_PARTIAL_PAY);
			var recordatorio = generar(cuotas.get(0).getDocumento());
			var detalles = generar(cuotas,estadoParcial);
			var netoTotal = detalles.stream().map(detail->detail.getValorNeto()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
			var impuestoTotal = detalles.stream().map(detail->detail.getValorImpuesto()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
			recordatorio.setValorNeto(netoTotal);
			recordatorio.setValorImpuesto(impuestoTotal);
			if( querySvc.insert(recordatorio, usuario) ) {
				detalles.forEach(detail->{
					try {
						detail.setRecordatorio(recordatorio);
						querySvc.insert(detail, usuario);
					}catch(QueryException e) {
						throw new RuntimeException(e);
					}
				});
			}
		}catch(QueryException | RuntimeException e) {
			throw new RecordatorioException(messageI18n("err.svc.recordatorio.generarrecordatorio.query"),e);
		}
		return null;
	}
	
	private List<DetRecordatorioPagoDTO> generar(List<CuotaDTO> cuotas,ParametroDTO estadoParcial){
		var ordered = cuotas.stream().sorted(Comparator.comparing(CuotaDTO::getFechaPago));
		var dets = ordered.map(cuota ->{
			var dto = new DetRecordatorioPagoDTO();
			dto.setCuota(cuota);
			if(!estadoParcial.getCodigo().contentEquals(cuota.getEstado().getCodigo())) {
				dto.setValorNeto(cuota.getValorNeto());
				dto.setValorImpuesto(getTaxesValue(cuota.getValorNeto(),cuota.getIva()));
			}else {
				dto.setValorNeto(cuota.getValorNeto().subtract(cuota.getValorPagadoNeto()));
				dto.setValorImpuesto(getTaxesValue(dto.getValorNeto(),cuota.getIva()));
			}
			return dto;
		});
		return dets.collect(Collectors.toList());
	}
	
	private BigDecimal getTaxesValue(BigDecimal neto,Double impuesto) {
		if(impuesto == 0.0) {
			return BigDecimal.ZERO;
		}
		if(impuesto > 0) {
			impuesto = impuesto/100;
		}
		return neto.multiply(new BigDecimal(impuesto));
	}
	
	private RecordatorioPagoDTO generar(DocumentoDTO documento) {
		var dto = new RecordatorioPagoDTO();
		dto.setFechaPago(generarFechaPago());
		dto.setDocumento(documento);
		return dto;
	}
	
	private LocalDate generarFechaPago() {
		return LocalDate.now();
	}
	
	private List<CuotaDTO> getsByState(List<CuotaDTO> cuotas,ParametroDTO estado){
		return cuotas.stream().filter( cuota -> estado.getCodigo().compareTo(cuota.getEstado().getCodigo())==0).collect(Collectors.toList());
	}
	
	private ParametroDTO getEstado(String valor2)throws RecordatorioException {
		try {
			var parametro = new ParametroDTO();
			parametro.setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
			parametro.setValor2(valor2);
			parametro.setGrupo(ParametroConstants.GRUPO_ESTADO_PAY);
			var list = querySvc.gets(parametro);
			if(ListUtils.haveOneItem(list)) {
				return list.get(0);
			}
		}catch(QueryException e) {
			throw new RecordatorioException(messageI18n("err.svc.remain.getstate.parameter",valor2,ParametroConstants.GRUPO_ESTADO_PAY),e);
		}
		throw new RecordatorioException(messageI18n("err.svc.remain.getstate.not.found",valor2));
	}
	
	private BigDecimal getNeto(List<CuotaDTO> cuotas) {
		if(ListUtils.isBlank(cuotas)) {
			return new BigDecimal(0);
		}
		return cuotas.stream().map( CuotaDTO::getValorNeto).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
	}
}
