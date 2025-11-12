package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.infraestructura.RepositorioOfertaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller

public class ControladorPago {

    private final ServicioOferta servicioOferta;
    private final RepositorioUsuario repositorioUsuario;
    private final ServicioSubasta servicioSubasta;
    private final RepositorioOfertaImpl repositorioOferta;
    private final ServicioPago servicioPago;
    private final ServicioPerfil servicioPerfil;


    @Autowired
    public ControladorPago(ServicioOferta servicioOferta,
                           RepositorioUsuario repositorioUsuario,
                           ServicioSubasta servicioSubasta,
                           RepositorioOfertaImpl repositorioOferta, ServicioPago servicioPago, ServicioPerfil servicioPerfil) {
        this.servicioOferta = servicioOferta;
        this.repositorioUsuario = repositorioUsuario;
        this.servicioSubasta = servicioSubasta;
        this.repositorioOferta = repositorioOferta;
        this.servicioPago = servicioPago;
        this.servicioPerfil = servicioPerfil;

    }


    @GetMapping("/formPago/{idSubasta}")
    public String mostrarFormPago(@PathVariable Long idSubasta,
                                  Model model,
                                  HttpServletRequest request) {

        String email = (String) request.getSession().getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }

        // 🚨 Obtener la sesión para recuperar el costo de envío
        HttpSession session = request.getSession();

        Usuario usuario = servicioPerfil.obtenerPerfil(email);


        // 2. Obtener la subasta desde el servicio o repositorio
        Subasta subasta = servicioSubasta.buscarSubasta(idSubasta);

        // **Validación de Subasta** (Asegúrate de tener esto para evitar 500)
        if (subasta == null) {
            return "redirect:/compras";
        }


        // 3. CÁLCULO DE COSTOS BASE
        Float montoActual = (subasta.getPrecioActual() != null) ? subasta.getPrecioActual()
                : subasta.getPrecioInicial();

        Float reserva = servicioPago.calcularMontoConReserva(subasta);
        model.addAttribute("reservaPrevia", reserva);


        // 🚨 4. RECUPERAR COSTO DE ENVÍO Y CALCULAR TOTAL

        Float costoEnvio = 0.0f;
        Long idSubastaEnSesion = (Long) session.getAttribute("idSubastaEnvio");

        if (idSubastaEnSesion != null && idSubastaEnSesion.equals(idSubasta)) {

            // 🚨 CAMBIO CLAVE: Obtener el valor como Double y luego convertirlo a Float
            Object costoSesionObject = session.getAttribute("costoEnvioCalculado");

            if (costoSesionObject != null) {

                // Si el objeto es un Double (lo más probable):
                if (costoSesionObject instanceof Double) {
                    Double costoDouble = (Double) costoSesionObject;
                    costoEnvio = costoDouble.floatValue(); // Conversión segura
                }
                // Si el objeto ya fuera un Float (segunda opción):
                else if (costoSesionObject instanceof Float) {
                    costoEnvio = (Float) costoSesionObject;
                }

                // 🚨 Limpiar la sesión
                session.removeAttribute("costoEnvioCalculado");
                session.removeAttribute("idSubastaEnvio");
            }
        }

        // 🚨 CALCULAR EL TOTAL FINAL
        Float costoTotal = montoActual + costoEnvio - reserva;


        // 5. ENVIAR LOS DATOS AL HTML

        model.addAttribute("usuario", usuario);
        model.addAttribute("subasta", subasta);
        model.addAttribute("montoActual", montoActual);

        // 🚨 NUEVOS ATRIBUTOS PARA EL HTML
        model.addAttribute("costoEnvio", costoEnvio);
        model.addAttribute("costoTotal", costoTotal);

        return "formPago";
    }

    @PostMapping("/formPago")
    public String procesarPago(@RequestParam("emailUsuario") String email, // Email del usuario (campo oculto)
                               @RequestParam("idSubastaPagada") Long idSubasta, // ID de la subasta (campo oculto)
                               @RequestParam("montoTotalPagado") Float costoTotal, // Monto total (campo oculto)
                               // Aquí irían los datos de la tarjeta, si los manejas con otro objeto
                               // @ModelAttribute("datosTarjeta") DatosTarjeta datosTarjeta,
                               HttpServletRequest request,
                               Model model) {

        // 1. **AUTENTICACIÓN/SEGURIDAD** (Opcional, pero buena práctica)
        // Se verifica que el email recibido corresponda al usuario logueado.
        String emailSesion = (String) request.getSession().getAttribute("email");
        if (emailSesion == null || !emailSesion.equals(email)) {
            // Manejar error o redirigir a login
            return "redirect:/login";
        }

        // 2. **PROCESAR PAGO / GUARDAR TRANSACCIÓN**

        // 🚨 Este es el paso clave: debes llamar a un servicio que guarde la transacción
        // Asumiendo que tienes un servicioPago con un método para guardar
        try {
            // servicePago.registrarTransaccion(email, idSubasta, costoTotal, datosTarjeta);

            // 🌟 SIMULACIÓN: Creación de un objeto Pago para guardar en el repositorio
            Pago pago = new Pago();
            pago.setEmailUsuario(email);
            pago.setIdSubasta(idSubasta);
            pago.setCostoTotal(costoTotal);
            // ... setear fecha, estado, etc.

            // 🚨 Aquí guardarías el objeto 'pago' en tu Repositorio/Servicio
            // servicioPago.guardarPago(pago);

        } catch (Exception e) {
            // Manejo de errores de pago (ej. tarjeta rechazada)
            model.addAttribute("error", "Error al procesar el pago: " + e.getMessage());
            // Podrías devolver a la vista de pago con el error
            // return "formPago";
            return "redirect:/pagoFallido";
        }

        // 3. **REDIRECCIÓN A CONFIRMACIÓN** (Paso 3)
        // Redirigimos a la página de éxito, a menudo pasando el ID de la subasta/transacción.
        return "redirect:/confirmacionPagoEnvio?idSubasta=" + idSubasta;
    }

}
