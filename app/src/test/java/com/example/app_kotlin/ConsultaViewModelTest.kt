package com.example.app_kotlin

import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.repository.ConsultaRepository
import com.example.app_kotlin.viewmodel.ConsultaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class ConsultaViewModelTest {

    private lateinit var viewModel: ConsultaViewModel
    private lateinit var repository: ConsultaRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        repository = mock()
        viewModel = ConsultaViewModel()

        // ⚠ Reemplazo del repositorio vía reflexión
        val repoField = ConsultaViewModel::class.java.getDeclaredField("repository")
        repoField.isAccessible = true
        repoField.set(viewModel, repository)
    }

    @Test
    fun `loadAll debe actualizar la lista de consultas`() = runTest {
        val fakeConsultas = listOf(Consulta(id = 1), Consulta(id = 2))
        whenever(repository.getAll()).thenReturn(fakeConsultas)

        viewModel.loadAll()

        assertEquals(fakeConsultas, viewModel.consultas)
        assertNull(viewModel.errorMessage)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `loadByDoctor debe filtrar correctamente`() = runTest {
        val doctorId = 10L
        val fakeList = listOf(Consulta(id = 1))
        whenever(repository.getByDoctor(doctorId)).thenReturn(fakeList)

        viewModel.loadByDoctor(doctorId)

        assertEquals(fakeList, viewModel.consultas)
    }

    @Test
    fun `deleteConsulta debe llamar repository_delete`() = runTest {
        whenever(repository.getAll()).thenReturn(emptyList())

        viewModel.deleteConsulta(1L)

        verify(repository).delete(1L)
    }

    @Test
    fun `loadById debe actualizar consultaSeleccionada`() = runTest {
        val consulta = Consulta(id = 99)
        whenever(repository.getById(99)).thenReturn(consulta)

        viewModel.loadById(99)

        assertEquals(consulta, viewModel.consultaSeleccionada)
    }
}