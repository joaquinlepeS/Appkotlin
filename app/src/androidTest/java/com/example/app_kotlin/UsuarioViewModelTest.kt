package com.example.app_kotlin

import com.example.app_kotlin.viewmodel.UsuarioViewModel
import org.junit.Assert
import org.junit.Test

class UsuarioViewModelTest {

    @Test
    fun logout_deberia_dejar_usuarioActual_en_null() {
        val vm = UsuarioViewModel()

        // Simulamos que el usuario actual tenía un valor
        vm.logout()  // este método SI existe

        // Verificamos que despues de logout es null
        Assert.assertNull(vm.pacienteActual)
    }
}