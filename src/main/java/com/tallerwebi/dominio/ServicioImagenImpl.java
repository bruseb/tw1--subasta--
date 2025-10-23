package tu.paquete.servicio;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class ServicioImagenImpl implements ServicioImagen {

    private final Cloudinary cloudinary;

    public ServicioImagenImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String subirImagen(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }
        try {
            // Sube el binario y obtiene metadatos (url, secure_url, public_id, etc.)
            Map uploadResult = cloudinary.uploader()
                    .upload(archivo.getBytes(), ObjectUtils.emptyMap());

            // Cloudinary devuelve varias URLs; usamos la segura (https)
            Object secureUrl = uploadResult.get("secure_url");
            if (secureUrl == null) {
                throw new IllegalStateException("No se recibió secure_url desde Cloudinary");
            }
            return secureUrl.toString();
        } catch (Exception e) {
            // En una app real, registra con un logger y mapea a una respuesta adecuada
            throw new RuntimeException("Error subiendo imagen a Cloudinary", e);
        }
    }
}