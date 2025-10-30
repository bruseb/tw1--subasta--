package com.tallerwebi.punta_a_punta.vistas;

public class vistaHeader {
    package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaHeader extends VistaWeb {

  private final Page page;

  public VistaHeader(Page page) {
    super(page);
    this.page = page;
  }

  // Selectores (como constantes o métodos String)
  public String headerSelector() { return "header[th\\:fragment='userHeader'], header"; }
  public String navSelector() { return "nav.navbar"; }

  // Brand
  public String brandLinkSelector() { return "a.navbar-brand"; }
  public String brandLogoSelector() { return "a.navbar-brand img.logo-img"; }

  // Menú
  public String linkInicioSelector() { return "a.nav-link:has-text('Inicio')"; }
  public String linkSubastarSelector() { return "a.nav-link:has-text('Subastar')"; }
  public String linkCalcularEnvioSelector() { return "a.nav-link:has-text('Calcular envío')"; }
  public String linkPerfilSelector() { return "a.nav-link:has-text('Mi perfil')"; }

  // Logout
  public String logoutSelector() { return "a.nav-link:has(button:has-text('Cerrar sesión'))"; }

  // Buscador
  public String inputBusquedaSelector() { return "form[role='search'] input[name='titulo']"; }
  public String botonBuscarSelector() { return "form[role='search'] button[type='submit']"; }

  // Métodos con acción (opcional)
  public void clickEnInicio() { page.click(linkInicioSelector()); }
  public void escribirEnBusqueda(String texto) { page.fill(inputBusquedaSelector(), texto); }
  public void enviarBusqueda() { page.click(botonBuscarSelector()); }
}

}
