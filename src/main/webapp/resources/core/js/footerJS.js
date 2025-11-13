const inputNotificaciones = document.querySelector("#contadorNotificaciones");

const tiempoActualizarContadorNotificaciones = 3000; //3 segundo

const intervalActualizarContadorNotificaciones = setInterval(obtenerCantidadNotificacionesNoLeidas,tiempoActualizarContadorNotificaciones);

function obtenerCantidadNotificacionesNoLeidas(){
    let xhr = new XMLHttpRequest();
    let url = 'cantidadNotificacionesNoLeidas';
    xhr.open("GET",url, true);

    xhr.onreadystatechange = function () {
        if(this.readyState == 4 && this.status == 200) {
            let response = this.responseText;
            if(response.length != 0){
                cambiarCantidadNotificaciones(JSON.parse(response));
            }
        }
    }
    xhr.send();
}

function cambiarCantidadNotificaciones(payload){
    let cantidadActual = Number(inputNotificaciones.textContent);
    let nuevaCantidad = Number(payload.cantidadNotificaciones);

    if(cantidadActual !== nuevaCantidad){
        inputNotificaciones.textContent = nuevaCantidad;
    }
}