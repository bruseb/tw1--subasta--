const tableSubastasCreadas = document.querySelector("#listaSubastasCreadas");
const tableSubastasOfertadas = document.querySelector("#listaSubastasOfertadas");
const tiempoActualizarOfertas = 10000; //10 segundos

actualizarListas()
const intervalActualizarOfertas = setInterval(actualizarListas, tiempoActualizarOfertas);

function actualizarListas(){
    const cantidadSubastasCreadas = tableSubastasCreadas.children.length;
    const cantidadSubastasOfertadas = tableSubastasOfertadas.children.length;

    for(let i = 0; i < cantidadSubastasCreadas; i++){
        let creada_subastaID                    = tableSubastasCreadas.children[i].children[0].children[0].children[0].children[1].children[0].value;
        let creada_inputOfertas        = tableSubastasCreadas.children[i].children[0].children[0].children[0].children[1].children[1];
        let creada_inputMontoActual    = tableSubastasCreadas.children[i].children[0].children[0].children[0].children[1].children[3];

        callOfertas(creada_subastaID,creada_inputOfertas,creada_inputMontoActual);
    }

    for(let i = 0; i < cantidadSubastasOfertadas; i++){
        let ofertada_subastaID                   = tableSubastasOfertadas.children[i].children[0].children[0].children[0].children[1].children[0].value;
        let ofertada_inputOfertas        = tableSubastasOfertadas.children[i].children[0].children[0].children[0].children[1].children[1];
        let ofertada_inputMontoActual    = tableSubastasOfertadas.children[i].children[0].children[0].children[0].children[1].children[3];

        callOfertas(ofertada_subastaID,ofertada_inputOfertas,ofertada_inputMontoActual);
    }
}

function callOfertas(subastaID, inputOfertas, inputMontoActual){
    let xhr = new XMLHttpRequest();
    let url = 'http://localhost:8080/spring/ofertar/jsonOfertas/' + subastaID;
    xhr.open("GET",url, true);

    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            let response = this.responseText;
            actualizarCardSubasta(subastaID, inputOfertas, inputMontoActual, JSON.parse(response));
        }
    }
    xhr.send();
}

function actualizarCardSubasta(subastaID, inputOfertas, inputMontoActual,responseBody){
    let listaOfertas = responseBody.listaOfertas[0];
    if(listaOfertas.length > 0){
        inputOfertas.innerHTML      = "Ofertas: " + listaOfertas.length;
        inputMontoActual.innerHTML  = "$ " + listaOfertas[listaOfertas.length-1][2].toFixed(2);
    }
}