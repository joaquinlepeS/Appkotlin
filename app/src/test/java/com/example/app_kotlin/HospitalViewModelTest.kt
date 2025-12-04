package com.example.app_kotlin.viewmodel

import com.example.app_kotlin.model.Hospital
import com.example.app_kotlin.repository.HospitalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class HospitalViewModelTest {

    private lateinit var viewModel: HospitalViewModel
    private lateinit var repository: HospitalRepository
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        repository = mock()
        viewModel = HospitalViewModel()

        val repoField = HospitalViewModel::class.java.getDeclaredField("repository")
        repoField.isAccessible = true
        repoField.set(viewModel, repository)
    }

    @Test
    fun `buscarHospitales devuelve lista con resultados`() = runTest {
        val fakeResult = listOf(Hospital(name = "Hospital Test"))

        whenever(repository.buscarPorCiudad("Santiago")).thenReturn(fakeResult)

        viewModel.buscarHospitales("Santiago")

        assertEquals(fakeResult, viewModel.hospitales)
        assertNull(viewModel.errorMessage)
    }

    @Test
    fun `buscarHospitales muestra error si no encuentra resultados`() = runTest {
        whenever(repository.buscarPorCiudad("CiudadX")).thenReturn(emptyList())

        viewModel.buscarHospitales("CiudadX")

        assertEquals("No se encontraron hospitales en 'CiudadX'", viewModel.errorMessage)
    }

    @Test
    fun `buscarHospitales captura excepciones`() = runTest {
        whenever(repository.buscarPorCiudad("A")).thenThrow(RuntimeException("Falló petición"))

        viewModel.buscarHospitales("A")

        assertEquals("Error: Falló petición", viewModel.errorMessage)
    }
}
