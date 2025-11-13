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
    public String procesarPago(@RequestParam("emailUsuario") String email,
                               @RequestParam("idSubasta") Long idSubasta,
                               @RequestParam("montoTotalPagado") Float costoTotal,

                               HttpServletRequest request,
                               Model model) {


        String emailSesion = (String) request.getSession().getAttribute("email");
        if (emailSesion == null || !emailSesion.equals(email)) {
            return "redirect:/login";
        }

        // 🚨 Validación adicional de ID (Aunque debe ser garantizada por el GET)
        if (idSubasta == null || idSubasta <= 0) {
            return "redirect:/compras";
        }

        // 2. **PROCESAR PAGO / GUARDAR TRANSACCIÓN**

        try {

            // Guardamos: idSubasta, email, montoTotal, y el estado 2 (Pagado)
            servicioPago.registrarTransaccion(idSubasta, email, costoTotal, 2);

            // 💡 Aquí también iría la lógica para cambiar el estado de la Subasta (ej: a PAGADA)

        } catch (Exception e) {
            // Manejo de errores de pago (ej. tarjeta rechazada o fallo de DB)
            System.err.println("Error al procesar y guardar el pago: " + e.getMessage());
            model.addAttribute("error", "Error al procesar el pago: " + e.getMessage());
            return "redirect:/pagoFallido";
        }


        // 3. **REDIRECCIÓN A CONFIRMACIÓN** (Paso 3)
        // Redirigimos a la página de éxito, a menudo pasando el ID de la subasta/transacción.
        return "redirect:/confirmacionPagoEnvio?idSubasta=" + idSubasta;
    }




}
