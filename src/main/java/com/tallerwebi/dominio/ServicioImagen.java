package tu.paquete.servicio;

import org.springframework.web.multipart.MultipartFile;

public interface ServicioImagen {
    String subirImagen(MultipartFile archivo); // devuelve la URL https segura
}