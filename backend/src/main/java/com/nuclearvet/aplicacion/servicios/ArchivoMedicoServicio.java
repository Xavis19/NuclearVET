package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.ArchivoMedicoDTO;
import com.nuclearvet.aplicacion.mapeadores.ArchivoMedicoMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.ArchivoMedico;
import com.nuclearvet.dominio.entidades.Consulta;
import com.nuclearvet.dominio.entidades.HistoriaClinica;
import com.nuclearvet.dominio.entidades.Usuario;
import com.nuclearvet.dominio.enums.TipoAccion;
import com.nuclearvet.dominio.enums.TipoArchivo;
import com.nuclearvet.infraestructura.persistencia.ArchivoMedicoRepositorio;
import com.nuclearvet.infraestructura.persistencia.ConsultaRepositorio;
import com.nuclearvet.infraestructura.persistencia.HistoriaClinicaRepositorio;
import com.nuclearvet.infraestructura.persistencia.UsuarioRepositorio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Servicio para gestión de archivos médicos
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArchivoMedicoServicio {

    private final ArchivoMedicoRepositorio archivoMedicoRepositorio;
    private final HistoriaClinicaRepositorio historiaClinicaRepositorio;
    private final ConsultaRepositorio consultaRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final ArchivoMedicoMapeador archivoMedicoMapeador;
    private final RegistroActividadServicio registroActividadServicio;

    @Value("${nuclearvet.archivos.ruta-base:uploads/archivos-medicos}")
    private String rutaBase;

    @Value("${nuclearvet.archivos.tamano-maximo:10485760}") // 10 MB por defecto
    private Long tamanoMaximo;

    /**
     * Subir archivo médico
     */
    public ArchivoMedicoDTO subirArchivo(
            MultipartFile archivo,
            TipoArchivo tipoArchivo,
            Long historiaClinicaId,
            Long consultaId,
            String descripcion,
            HttpServletRequest request
    ) {
        log.info("Subiendo archivo médico para historia clínica ID: {}", historiaClinicaId);

        // Validar archivo
        if (archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        if (archivo.getSize() > tamanoMaximo) {
            throw new IllegalArgumentException("El archivo excede el tamaño máximo permitido de " + (tamanoMaximo / 1048576) + " MB");
        }

        // Validar historia clínica
        HistoriaClinica historiaClinica = historiaClinicaRepositorio.findById(historiaClinicaId)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Historia clínica no encontrada con ID: " + historiaClinicaId));

        // Validar consulta si se proporciona
        Consulta consulta = null;
        if (consultaId != null) {
            consulta = consultaRepositorio.findById(consultaId)
                    .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Consulta no encontrada con ID: " + consultaId));
        }

        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoElectronico = authentication.getName();
        Usuario usuario = usuarioRepositorio.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Usuario no encontrado"));

        try {
            // Guardar archivo en el sistema de archivos
            String rutaArchivo = guardarArchivoEnDisco(archivo, historiaClinica.getNumeroHistoria());

            // Crear registro en la base de datos
            ArchivoMedico archivoMedico = ArchivoMedico.builder()
                    .tipoArchivo(tipoArchivo)
                    .nombreArchivo(archivo.getOriginalFilename())
                    .rutaArchivo(rutaArchivo)
                    .tipoContenido(archivo.getContentType())
                    .tamanoBytes(archivo.getSize())
                    .descripcion(descripcion)
                    .historiaClinica(historiaClinica)
                    .consulta(consulta)
                    .subidoPor(usuario)
                    .build();

            archivoMedico = archivoMedicoRepositorio.save(archivoMedico);

            // Agregar archivo a la historia clínica
            historiaClinica.agregarArchivo(archivoMedico);
            historiaClinicaRepositorio.save(historiaClinica);

            // Registrar actividad
            registroActividadServicio.registrarActividad(
                    null,
                    TipoAccion.SUBIR_ARCHIVO_MEDICO,
                    "Archivo médico subido: " + archivo.getOriginalFilename() + " para historia: " + historiaClinica.getNumeroHistoria(),
                    request
            );

            log.info("Archivo médico subido exitosamente: {}", archivo.getOriginalFilename());
            return archivoMedicoMapeador.aDTO(archivoMedico);

        } catch (IOException e) {
            log.error("Error al guardar archivo", e);
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage());
        }
    }

    /**
     * Obtener archivo por ID
     */
    @Transactional(readOnly = true)
    public ArchivoMedicoDTO obtenerPorId(Long id) {
        log.debug("Obteniendo archivo médico por ID: {}", id);
        
        ArchivoMedico archivoMedico = archivoMedicoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Archivo médico no encontrado con ID: " + id));
        
        return archivoMedicoMapeador.aDTO(archivoMedico);
    }

    /**
     * Listar archivos por historia clínica
     */
    @Transactional(readOnly = true)
    public List<ArchivoMedicoDTO> listarPorHistoriaClinica(Long historiaClinicaId) {
        log.debug("Listando archivos para historia clínica ID: {}", historiaClinicaId);
        
        // Verificar que existe la historia clínica
        if (!historiaClinicaRepositorio.existsById(historiaClinicaId)) {
            throw new RecursoNoEncontradoExcepcion("Historia clínica no encontrada con ID: " + historiaClinicaId);
        }
        
        List<ArchivoMedico> archivos = archivoMedicoRepositorio.findByHistoriaClinicaIdOrderByFechaSubidaDesc(historiaClinicaId);
        return archivoMedicoMapeador.aDTOLista(archivos);
    }

    /**
     * Listar archivos por consulta
     */
    @Transactional(readOnly = true)
    public List<ArchivoMedicoDTO> listarPorConsulta(Long consultaId) {
        log.debug("Listando archivos para consulta ID: {}", consultaId);
        
        // Verificar que existe la consulta
        if (!consultaRepositorio.existsById(consultaId)) {
            throw new RecursoNoEncontradoExcepcion("Consulta no encontrada con ID: " + consultaId);
        }
        
        List<ArchivoMedico> archivos = archivoMedicoRepositorio.findByConsultaIdOrderByFechaSubidaDesc(consultaId);
        return archivoMedicoMapeador.aDTOLista(archivos);
    }

    /**
     * Listar archivos por tipo
     */
    @Transactional(readOnly = true)
    public List<ArchivoMedicoDTO> listarPorTipo(TipoArchivo tipoArchivo) {
        log.debug("Listando archivos de tipo: {}", tipoArchivo);
        
        List<ArchivoMedico> archivos = archivoMedicoRepositorio.findByTipoArchivoOrderByFechaSubidaDesc(tipoArchivo);
        return archivoMedicoMapeador.aDTOLista(archivos);
    }

    /**
     * Listar archivos recientes
     */
    @Transactional(readOnly = true)
    public List<ArchivoMedicoDTO> listarArchivosRecientes() {
        log.debug("Listando archivos recientes");
        
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
        List<ArchivoMedico> archivos = archivoMedicoRepositorio.findArchivosRecientes(hace30Dias);
        return archivoMedicoMapeador.aDTOLista(archivos);
    }

    /**
     * Descargar archivo
     */
    @Transactional(readOnly = true)
    public Resource descargarArchivo(Long id) {
        log.debug("Descargando archivo médico ID: {}", id);
        
        ArchivoMedico archivoMedico = archivoMedicoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Archivo médico no encontrado con ID: " + id));
        
        try {
            Path rutaArchivo = Paths.get(archivoMedico.getRutaArchivo());
            Resource resource = new UrlResource(rutaArchivo.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se puede leer el archivo: " + archivoMedico.getNombreArchivo());
            }
        } catch (MalformedURLException e) {
            log.error("Error al descargar archivo", e);
            throw new RuntimeException("Error al descargar el archivo: " + e.getMessage());
        }
    }

    /**
     * Eliminar archivo
     */
    public void eliminarArchivo(Long id, HttpServletRequest request) {
        log.info("Eliminando archivo médico ID: {}", id);

        ArchivoMedico archivoMedico = archivoMedicoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Archivo médico no encontrado con ID: " + id));

        try {
            // Eliminar archivo del sistema de archivos
            Path rutaArchivo = Paths.get(archivoMedico.getRutaArchivo());
            Files.deleteIfExists(rutaArchivo);

            // Eliminar registro de la base de datos
            archivoMedicoRepositorio.delete(archivoMedico);

            // Registrar actividad
            registroActividadServicio.registrarActividad(
                    null,
                    TipoAccion.ELIMINAR_ARCHIVO_MEDICO,
                    "Archivo médico eliminado: " + archivoMedico.getNombreArchivo(),
                    request
            );

            log.info("Archivo médico eliminado exitosamente");

        } catch (IOException e) {
            log.error("Error al eliminar archivo del sistema de archivos", e);
            throw new RuntimeException("Error al eliminar el archivo: " + e.getMessage());
        }
    }

    /**
     * Guardar archivo en el disco
     */
    private String guardarArchivoEnDisco(MultipartFile archivo, String numeroHistoria) throws IOException {
        // Crear directorio base si no existe
        Path directorioBase = Paths.get(rutaBase);
        if (!Files.exists(directorioBase)) {
            Files.createDirectories(directorioBase);
        }

        // Crear subdirectorio por historia clínica
        Path directorioHistoria = directorioBase.resolve(numeroHistoria);
        if (!Files.exists(directorioHistoria)) {
            Files.createDirectories(directorioHistoria);
        }

        // Generar nombre único para el archivo
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = nombreOriginal != null && nombreOriginal.contains(".") 
                ? nombreOriginal.substring(nombreOriginal.lastIndexOf(".")) 
                : "";
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String nombreArchivo = timestamp + "_" + uuid + extension;

        // Guardar archivo
        Path rutaDestino = directorioHistoria.resolve(nombreArchivo);
        Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

        return rutaDestino.toString();
    }
}
