package com.example.app_kotlin.viewmodel

import com.example.app_kotlin.model.Paciente
import com.example.app_kotlin.repository.PacienteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class PacienteViewModelTest {

    private lateinit var viewModel: PacienteViewModel
    private lateinit var repository: PacienteRepository

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        repository = mock()
        viewModel = PacienteViewModel()

        // Reemplazar repositorio privado (reflection)
        val repoField = PacienteViewModel::class.java.getDeclaredField("repository")
        repoField.isAccessible = true
        repoField.set(viewModel, repository)
    }


    // TEST registrarPaciente()

    @Test
    fun `registrarPaciente debe crear un nuevo paciente`() = runTest {
        val pacienteMock = Paciente(id = 1, nombre = "Juan", email = "juan@test.com", password = "1234")

        whenever(repository.createPaciente(any())).thenReturn(pacienteMock)

        viewModel.registrarPaciente("Juan", "juan@test.com", "1234")

        assertEquals(pacienteMock, viewModel.pacienteActual)
        assertNull(viewModel.errorMessage)
        assertFalse(viewModel.isLoading)
    }


    // TEST buscarPorId()

    @Test
    fun `buscarPorId debe retornar paciente correcto`() = runTest {
        val paciente = Paciente(id = 10, nombre = "Paula", email = "paula@test.com", password = "pass")

        whenever(repository.getPacienteById(10)).thenReturn(paciente)

        viewModel.buscarPorId(10)

        assertEquals(paciente, viewModel.pacienteActual)
    }


    // TEST buscarPorEmail()

    @Test
    fun `buscarPorEmail debe obtener paciente por email`() = runTest {
        val paciente = Paciente(id = 5, nombre = "Carlos", email = "carlos@test.com", password = "abc")

        whenever(repository.getPacienteByEmail("carlos@test.com")).thenReturn(paciente)

        viewModel.buscarPorEmail("carlos@test.com")

        assertEquals(paciente, viewModel.pacienteActual)
    }

    // TEST login()

    @Test
    fun `login exitoso debe establecer pacienteActual y llamar onSuccess`() = runTest {
        var successCalled = false
        val paciente = Paciente(id = 1, nombre = "María", email = "maria@test.com", password = "1234")

        whenever(repository.getPacienteByEmail("maria@test.com")).thenReturn(paciente)

        viewModel.login("maria@test.com", "1234") {
            successCalled = true
        }

        assertTrue(successCalled)
        assertEquals(paciente, viewModel.pacienteActual)
        assertNull(viewModel.errorMessage)
    }

    @Test
    fun `login falla si la contraseña es incorrecta`() = runTest {
        val paciente = Paciente(id = 1, nombre = "Luis", email = "luis@test.com", password = "correcta")

        whenever(repository.getPacienteByEmail("luis@test.com")).thenReturn(paciente)

        viewModel.login("luis@test.com", "mala") { fail("No debería ejecutarse onSuccess") }

        assertEquals("Contraseña incorrecta", viewModel.errorMessage)
        assertNull(viewModel.pacienteActual)
    }

    @Test
    fun `login falla si el email no existe`() = runTest {
        whenever(repository.getPacienteByEmail(any())).thenThrow(RuntimeException("No encontrado"))

        viewModel.login("x@test.com", "1234") {}

        assertEquals("Email no encontrado", viewModel.errorMessage)
        assertNull(viewModel.pacienteActual)
    }


    // TEST logout()

    @Test
    fun `logout debe limpiar pacienteActual`() = runTest {
        viewModel.pacienteActual = Paciente(id = 1, nombre = "Test", email = "t@test.com", password = "pass")

        viewModel.logout()

        assertNull(viewModel.pacienteActual)
    }
}
