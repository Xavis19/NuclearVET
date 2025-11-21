package com.nuclearvet.dominio.servicios;

import com.nuclearvet.aplicacion.dto.notificaciones.CrearRecordatorioDTO;
import com.nuclearvet.aplicacion.dto.notificaciones.RecordatorioDTO;
import com.nuclearvet.aplicacion.mapeadores.RecordatorioMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.Cita;
import com.nuclearvet.dominio.entidades.Paciente;
import com.nuclearvet.dominio.entidades.Recordatorio;
import com.nuclearvet.dominio.enumeraciones.TipoRecordatorio;
import com.nuclearvet.infraestructura.persistencia.CitaRepositorio;
import com.nuclearvet.infraestructura.persistencia.PacienteRepositorio;
import com.nuclearvet.infraestructura.persistencia.RecordatorioRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de recordatorios
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecordatorioServicio {

    private final RecordatorioRepositorio recordatorioRepositorio;
    private final PacienteRepositorio pacienteRepositorio;
    private final CitaRepositorio citaRepositorio;
    private final RecordatorioMapeador recordatorioMapeador;

    @Transactional(readOnly = true)
    public List<RecordatorioDTO> listarTodos() {
        log.debug("Listando todos los recordatorios");
        return recordatorioRepositorio.findAll().stream()
                .map(recordatorioMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecordatorioDTO> listarPorPaciente(Long pacienteId) {
        log.debug("Listando recordatorios para paciente: {}", pacienteId);
        return recordatorioRepositorio.findByPacienteConRelaciones(pacienteId).stream()
                .map(recordatorioMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecordatorioDTO> listarPendientes() {
        log.debug("Listando recordatorios pendientes");
        return recordatorioRepositorio.findByEnviadoFalse().stream()
                .map(recordatorioMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecordatorioDTO> listarPendientesPorPaciente(Long pacienteId) {
        log.debug("Listando recordatorios pendientes para paciente: {}", pacienteId);
        return recordatorioRepositorio.findPendientesPorPaciente(pacienteId).stream()
                .map(recordatorioMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecordatorioDTO> listarPorTipo(TipoRecordatorio tipo) {
        log.debug("Listando recordatorios por tipo: {}", tipo);
        return recordatorioRepositorio.findByTipoRecordatorio(tipo).stream()
                .map(recordatorioMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecordatorioDTO> listarPendientesPorTipo(TipoRecordatorio tipo) {
        log.debug("Listando recordatorios pendientes por tipo: {}", tipo);
        return recordatorioRepositorio.findPendientesPorTipo(tipo).stream()
                .map(recordatorioMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecordatorioDTO> listarPendientesDeEnvio() {
        log.debug("Listando recordatorios pendientes de envío");
        return recordatorioRepositorio.findRecordatoriosPendientesDeEnvio().stream()
                .map(recordatorioMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecordatorioDTO> listarProximosAEnviar(int horas) {
        log.debug("Listando recordatorios próximos a enviar (dentro de {} horas)", horas);
        LocalDateTime fechaLimite = LocalDateTime.now().plusHours(horas);
        return recordatorioRepositorio.findProximosAEnviar(fechaLimite).stream()
                .map(recordatorioMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecordatorioDTO> listarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        log.debug("Listando recordatorios entre {} y {}", inicio, fin);
        return recordatorioRepositorio.findByRangoFechas(inicio, fin).stream()
                .map(recordatorioMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecordatorioDTO> listarPorPropietario(Long propietarioId) {
        log.debug("Listando recordatorios pendientes para propietario: {}", propietarioId);
        return recordatorioRepositorio.findPendientesPorPropietario(propietarioId).stream()
                .map(recordatorioMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RecordatorioDTO obtenerPorId(Long id) {
        log.debug("Obteniendo recordatorio con ID: {}", id);
        Recordatorio recordatorio = buscarRecordatorioPorIdConRelaciones(id);
        return recordatorioMapeador.aDTO(recordatorio);
    }

    @Transactional
    public RecordatorioDTO crear(CrearRecordatorioDTO dto) {
        log.info("Creando nuevo recordatorio para paciente: {}", dto.getPacienteId());
        
        // Verificar que existe el paciente
        Paciente paciente = pacienteRepositorio.findById(dto.getPacienteId())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Paciente con ID " + dto.getPacienteId() + " no encontrado"));
        
        Recordatorio recordatorio = recordatorioMapeador.aEntidad(dto);
        recordatorio.setPaciente(paciente);
        
        // Si tiene citaId, asociar la cita
        if (dto.getCitaId() != null) {
            Cita cita = citaRepositorio.findById(dto.getCitaId())
                    .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Cita con ID " + dto.getCitaId() + " no encontrada"));
            recordatorio.setCita(cita);
        }
        
        Recordatorio recordatorioGuardado = recordatorioRepositorio.save(recordatorio);
        
        log.info("Recordatorio creado exitosamente con ID: {}", recordatorioGuardado.getId());
        return recordatorioMapeador.aDTO(recordatorioGuardado);
    }

    @Transactional
    public RecordatorioDTO marcarComoEnviado(Long id) {
        log.info("Marcando recordatorio como enviado: {}", id);
        Recordatorio recordatorio = buscarRecordatorioPorId(id);
        recordatorio.marcarComoEnviado();
        Recordatorio recordatorioActualizado = recordatorioRepositorio.save(recordatorio);
        return recordatorioMapeador.aDTO(recordatorioActualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando recordatorio con ID: {}", id);
        Recordatorio recordatorio = buscarRecordatorioPorId(id);
        recordatorioRepositorio.delete(recordatorio);
        log.info("Recordatorio eliminado exitosamente: {}", id);
    }

    @Transactional
    public void eliminarEnviadosAntiguos(int dias) {
        log.info("Eliminando recordatorios enviados antiguos (más de {} días)", dias);
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
        List<Recordatorio> recordatorios = recordatorioRepositorio.findEnviadosAntiguos(fechaLimite);
        recordatorioRepositorio.deleteAll(recordatorios);
        log.info("Eliminados {} recordatorios antiguos", recordatorios.size());
    }

    @Transactional(readOnly = true)
    public Map<TipoRecordatorio, Long> contarPendientesPorTipo() {
        log.debug("Contando recordatorios pendientes por tipo");
        return recordatorioRepositorio.contarPendientesPorTipo().stream()
                .collect(Collectors.toMap(
                        obj -> (TipoRecordatorio) obj[0],
                        obj -> (Long) obj[1]
                ));
    }

    @Transactional(readOnly = true)
    public Long contarVencidos() {
        return recordatorioRepositorio.contarVencidos();
    }

    @Transactional(readOnly = true)
    public Long contarPendientesPorPaciente(Long pacienteId) {
        return recordatorioRepositorio.contarPendientesPorPaciente(pacienteId);
    }

    @Transactional
    public void enviarRecordatoriosPendientes() {
        log.info("Procesando recordatorios pendientes de envío");
        List<Recordatorio> recordatorios = recordatorioRepositorio.findRecordatoriosPendientesDeEnvio();
        
        for (Recordatorio recordatorio : recordatorios) {
            if (recordatorio.debeSerEnviado()) {
                // Aquí se implementaría la lógica de envío (email, SMS, etc.)
                log.info("Enviando recordatorio ID: {} al propietario del paciente: {}", 
                        recordatorio.getId(), recordatorio.getPaciente().getNombre());
                recordatorio.marcarComoEnviado();
                recordatorioRepositorio.save(recordatorio);
            }
        }
        
        log.info("Procesados {} recordatorios", recordatorios.size());
    }

    private Recordatorio buscarRecordatorioPorId(Long id) {
        return recordatorioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Recordatorio con ID " + id + " no encontrado"));
    }

    private Recordatorio buscarRecordatorioPorIdConRelaciones(Long id) {
        return recordatorioRepositorio.findByIdConRelaciones(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Recordatorio con ID " + id + " no encontrado"));
    }
}
