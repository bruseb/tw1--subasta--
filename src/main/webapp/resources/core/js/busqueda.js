const tableSubastas = document.querySelector("#listaSubastas");
const intervalActualizarOfertas = 10000; //10 segundos

obtenerLista();
setInterval(obtenerLista, intervalActualizarOfertas);

function obtenerLista(){
    const cantidadSubastas = tableSubastas.children.length;

    for(let i = 0; i < cantidadSubastas; i++){
        let subastaID           = tableSubastas.children[i].children[0].children[0].children[0].children[1].children[0].value;
        let inputOfertas        = tableSubastas.children[i].children[0].children[0].children[0].children[1].children[1];
        let inputMontoActual    = tableSubastas.children[i].children[0].children[0].children[0].children[1].children[3];

        callOfertas(subastaID,inputOfertas,inputMontoActual);
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