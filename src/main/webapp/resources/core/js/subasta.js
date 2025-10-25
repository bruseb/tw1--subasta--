const fileInput = document.querySelector("#imagenSubasta");

fileInput.addEventListener('change', (e) => {
    const archivos = fileInput.files;

    if(archivos.length > 3){
        alert("Solo podes subir 3 archivos como maximo.");
        fileInput.value = null;
    }
});