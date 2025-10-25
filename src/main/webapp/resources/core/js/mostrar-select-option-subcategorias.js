document.addEventListener("DOMContentLoaded",  function() {
    const selectDeCategoria = document.getElementById("selectDeCategoria");
    const selectDeSubcategoria = document.getElementById("selectDeSubcategoria");

    selectDeCategoria.addEventListener("change", async function() {
        console.log("ID de la categoría seleccionada:", this.value);

        const idCategoriaSeleccionada = this.value;
        if (!idCategoriaSeleccionada) return;

        await actualizarSelectDeSubcategorias(selectDeCategoria, selectDeSubcategoria)
    });
});

async function subcategoriasParaSelect(idCategoriaSeleccionada) {
    try {
        const respuesta = await fetch(`/spring/subcategorias-por-categoria/${idCategoriaSeleccionada}`);
        if (!respuesta.ok) throw new Error(`Error HTTP ${respuesta.status}`);
        return await respuesta.json();
    } catch (error) {
        console.error("Error al cargar las subcategorías:", error);
    }
}

function crearArrayDeOptionsParaSubcategorias(subcategorias) {
    const arrayDeOptions = []
    const optionDeSeleccioneSubcategoria = new Option("Seleccione una subcategoría", "-1");
    arrayDeOptions.push(optionDeSeleccioneSubcategoria);

    if(!subcategorias || subcategorias.length === 0) {
        return arrayDeOptions;
    }else {
        for (let i = 0; i < subcategorias.length; i++) {
            const optionPrueba = document.createElement("option");
            const idSubcategoria = subcategorias[i].id;

            optionPrueba.textContent = subcategorias[i].nombre;
            optionPrueba.value = idSubcategoria;
            optionPrueba.setAttribute("id", idSubcategoria);
            arrayDeOptions.push(optionPrueba);
        }
        return arrayDeOptions;
    }
}

async function actualizarSelectDeSubcategorias(selectDeCategoria, selectDeSubcategoria) {
    const idCategoriaSeleccionada = parseInt(selectDeCategoria.value);

    if (idCategoriaSeleccionada < 0) {
        const options = crearArrayDeOptionsParaSubcategorias(null);
        selectDeSubcategoria.disabled = true;
        selectDeSubcategoria.replaceChildren(...options);
    } else {
        const subcategorias = await subcategoriasParaSelect(idCategoriaSeleccionada);
        const options = crearArrayDeOptionsParaSubcategorias(subcategorias);
        selectDeSubcategoria.disabled = false;
        selectDeSubcategoria.replaceChildren(...options);
    }
}




