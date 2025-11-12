
const inputImporte = document.querySelector("#montoOfertado");
const inputMontoActual = document.querySelector("#montoActual");
const input_ES_PROPIETARIO = document.querySelector("#esPropietario");
const inputUsuarioActual = document.querySelector("#usuarioActual");
const buttonSubmit = document.querySelector("#btn-ofertar")
const buttonEliminarSubasta = document.querySelector("#btn-eliminarSubasta");
const valorIdSubasta = document.querySelector("#idSubasta");
const valorTiempoSubasta = document.querySelector("#tiempoSubasta");
const valorFechaFin = document.querySelector("#fechaFin");
const valorMontoActual = document.querySelector("#precioActual");
const tableListaOfertas = document.querySelector("#listaOfertas");

const ES_PROPIETARIO = input_ES_PROPIETARIO.value;
const usuarioActual = inputUsuarioActual.value;

const importeMinimo = 1;
const intervalActualizarOferta = 5000; //5 segundos
const intervalActualizarTimer = 1000; //1 segundo

const finSubasta = new Date(valorFechaFin.value).getTime();

//inputImporte.value = Number(inputMontoActual.textContent) + importeMinimo;
if (ES_PROPIETARIO === 'false' && inputImporte && inputMontoActual) {
    inputImporte.value = Number(inputMontoActual.textContent) + importeMinimo;
}
if(ES_PROPIETARIO === 'true'){
    buttonEliminarSubasta.setAttribute("onclick","if(confirm('¿Estas seguro de eliminar la subasta?\\nESTA ACCION ES IRREVERSIBLE')) {\nwindow.location.replace('eliminarSubasta?idSubasta=" + valorIdSubasta.value + "');\n}");
}

//Llamada inicial, para que no tarde
callOfertas();

const intervalActualizarOfertas = setInterval(callOfertas, intervalActualizarOferta);
const intervalTemporizador = setInterval(temporizador, intervalActualizarTimer);
const invervalCheckCancelarOferta = setInterval(checkCancelarOferta, intervalActualizarTimer);

function callOfertas(){
    let xhr = new XMLHttpRequest();
    let url = 'jsonOfertas/' + valorIdSubasta.value;
    xhr.open("GET",url, true);

    xhr.onreadystatechange = function () {
        if(this.readyState == 4 && this.status == 200) {
            let response = this.responseText;
            if(response.length != 0){
                checkListaOfertas(JSON.parse(response));
            }
        }
    }
    xhr.send();
}

function checkListaOfertas(payload){
    let listaOfertas = payload.listaOfertas[0];
    if(tableListaOfertas.children.length == 0 && listaOfertas.length == 0 ){
        crearListaVacia();
    }
    if(tableListaOfertas.children.length == 0 && listaOfertas.length != 0){
        crearListaOfertas(listaOfertas);
    }
    if(tableListaOfertas.children.length != 0 && listaOfertas.length != 0){
        let idUltimaOfertaCall  = Number(listaOfertas[listaOfertas.length-1][0]);
        let idUltimaOfertaTabla = Number(tableListaOfertas.childNodes[0].id);
        if(idUltimaOfertaCall != idUltimaOfertaTabla){
            valorMontoActual.textContent = listaOfertas[listaOfertas.length-1][2].toFixed(2);
            crearListaOfertas(listaOfertas);
        }
    }
}

function crearListaVacia(){
    const mensajeSinOfertas = "Esta subasta no tiene ofertas. Se el primero!";
    tableListaOfertas.innerHTML = '';
    let nodeSinOfertas = document.createElement('b');
    nodeSinOfertas.innerText = mensajeSinOfertas;
    tableListaOfertas.appendChild(nodeSinOfertas);
}

function crearListaOfertas(listaOfertas){
    tableListaOfertas.innerHTML = '';
    for(let i = 1; i <= listaOfertas.length; i++){
        let nodeOferta = document.createElement('li');
        let stringFechaOferta = listaOfertas[listaOfertas.length-i][1][0] + "-" + listaOfertas[listaOfertas.length-i][1][1] + "-" + listaOfertas[listaOfertas.length-i][1][2] + " "
                                     + listaOfertas[listaOfertas.length-i][1][3] + ":" + listaOfertas[listaOfertas.length-i][1][4] + ":" + listaOfertas[listaOfertas.length-i][1][5];
        let fechaOferta = new Date(stringFechaOferta);
        nodeOferta.setAttribute('id',listaOfertas[listaOfertas.length-i][0]);
        let texto =  fechaOferta.getDate().toString().padStart(2,0)      + "/" +
                            fechaOferta.getMonth().toString().padStart(2,0)     + "/" +
                            fechaOferta.getFullYear().toString()                + " " +
                            fechaOferta.getHours().toString().padStart(2,0)     + ":" +
                            fechaOferta.getMinutes().toString().padStart(2,0)   + ":" +
                            fechaOferta.getSeconds().toString().padStart(2,0)   + " - " +
                            listaOfertas[listaOfertas.length-i][4]              + " " +
                            listaOfertas[listaOfertas.length-i][5]              + " - $ " +
                            listaOfertas[listaOfertas.length-i][2].toFixed(2);


        if(listaOfertas[listaOfertas.length-i][6] === usuarioActual ){
            texto = "<b>" + texto + "</b>";
            if(listaOfertas.length-i === listaOfertas.length-1 && rangoTiempoOferta(fechaOferta.getDate().toString().padStart(2,0), fechaOferta.getMonth().toString().padStart(2,0),fechaOferta.getFullYear().toString(),fechaOferta.getHours().toString().padStart(2,0) , fechaOferta.getMinutes().toString().padStart(2,0) , fechaOferta.getSeconds().toString().padStart(2,0) ) ){
                let nodeBotonCancelarOferta = document.createElement("button");
                nodeBotonCancelarOferta.id = "botonCancelarOferta";
                nodeBotonCancelarOferta.setAttribute("onclick","if(confirm('¿Estas seguro de cancelar tu oferta?\\nESTA ACCION ES IRREVERSIBLE')) {\nwindow.location.replace('eliminarOferta?idSubasta=" + valorIdSubasta.value + "&idOferta=" + listaOfertas[listaOfertas.length-i][0] + "');\n}");
                nodeBotonCancelarOferta.setAttribute("class", "btn btn-danger");
                nodeBotonCancelarOferta.innerText = "Cancelar Oferta";
                nodeBotonCancelarOferta.style.padding = "2px 8px";
                nodeBotonCancelarOferta.style.margin = "0px 10px";

                let nodeHiddenTiempo = document.createElement("input");
                let tiempoOferta =  new Date(fechaOferta.getFullYear().toString(), fechaOferta.getMonth().toString().padStart(2,0), fechaOferta.getDate().toString().padStart(2,0),fechaOferta.getHours().toString().padStart(2,0) , fechaOferta.getMinutes().toString().padStart(2,0) , fechaOferta.getSeconds().toString().padStart(2,0)).getTime();
                nodeHiddenTiempo.id = "TiempoBotonCancelarSubasta";
                nodeHiddenTiempo.setAttribute("hidden","true");
                nodeHiddenTiempo.value = tiempoOferta.toString();

                nodeOferta.innerHTML = texto;
                nodeOferta.appendChild(nodeBotonCancelarOferta);
                nodeOferta.appendChild(nodeHiddenTiempo);
                tableListaOfertas.appendChild(nodeOferta);
            }else{
                nodeOferta.innerHTML = texto;
                tableListaOfertas.appendChild(nodeOferta);
            }
        }else{
            nodeOferta.innerHTML = texto;
            tableListaOfertas.appendChild(nodeOferta);
        }
    }
}

function rangoTiempoOferta(dia,mes,anio,hora,minuto,segundo){
    let fechaOferta = new Date(anio,mes,dia,hora,minuto,segundo).getTime();
    let fechaActual = new Date().getTime();
    let diferencia = fechaOferta - fechaActual + 60000;
    let diferenciaTiempoRestante = finSubasta - fechaActual - 60000;
    if(diferencia >= 0 && diferenciaTiempoRestante >= 0){
        return true;    //Dentro de rango de poder cancelar oferta
    }else{
        return false;   //Fuera de rango de poder cancelar oferta
    }
}

function temporizador(){
    let ahora       = new Date().getTime();
    let diferencia  = finSubasta - ahora;

    let subastaDia = Math.floor( ( diferencia / (1000 * 60 * 60 * 24) ) );
    let subastaHor = Math.floor( ( diferencia % (1000 * 60 * 60 * 24) )  / (1000 * 60 * 60) );
    let subastaMin = Math.floor( ( diferencia % (1000 * 60 * 60) )  / (1000 * 60) );
    let subastaSeg = Math.floor( ( diferencia % (1000 * 60) )  / (1000) );

    subastaHor = subastaHor + (subastaDia * 24);
    valorTiempoSubasta.textContent = subastaHor.toString().padStart(2,0) + "Hrs " + subastaMin.toString().padStart(2,0) + "Min " + subastaSeg.toString().padStart(2,0) + "Seg";
    if(diferencia < 0){
        if(buttonSubmit != null){
            buttonSubmit.disabled = true;
        }
        clearInterval(intervalActualizarOfertas);
        clearInterval(intervalTemporizador);
        callOfertas();
        valorTiempoSubasta.textContent = "FINALIZADO";
    }
}

function checkCancelarOferta(){
    let buttonCancelarOferta = document.querySelector("#botonCancelarOferta");
    let inputTiempoOferta = document.querySelector("#TiempoBotonCancelarSubasta");

    if(buttonCancelarOferta == null){
        clearInterval(invervalCheckCancelarOferta);
        return;
    }

    let tiempoOferta = new Date(Number(inputTiempoOferta.value)).getTime();
    let tiempoActual = new Date().getTime();
    let diferenciaBoton = tiempoOferta - tiempoActual + 60000;  // diferenciaBoton < 0 Significa que paso un minuto desde que se creo el boton
    let diferenciaTiempoRestante = finSubasta - tiempoActual - 60000;   // diferenciaTiempoRestante < 0 Significa que falta menos de 1 minuto para que termine la subasta

    if(diferenciaBoton < 0 || diferenciaTiempoRestante < 0){
        buttonCancelarOferta.disabled = true;
        clearInterval(invervalCheckCancelarOferta);
    }
}