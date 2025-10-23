package tu.paquete.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        // Lee CLOUDINARY_URL del entorno. Ej: cloudinary://key:secret@cloudname
        String url = System.getenv("CLOUDINARY_URL");
        if (url == null || url.isBlank()) {
            throw new IllegalStateException("Falta la variable de entorno CLOUDINARY_URL");
        }
        return new Cloudinary(url);
    }
}