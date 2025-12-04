package com.example.app_kotlin

import com.example.app_kotlin.model.Doctor
import com.example.app_kotlin.repository.DoctorRepository
import com.example.app_kotlin.viewmodel.DoctorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class DoctorViewModelTest {

    private lateinit var viewModel: DoctorViewModel
    private lateinit var repository: DoctorRepository
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        repository = mock()
        viewModel = DoctorViewModel()

        val repoField = DoctorViewModel::class.java.getDeclaredField("repository")
        repoField.isAccessible = true
        repoField.set(viewModel, repository)
    }

    @Test
    fun `fetchDoctors debe cargar doctores correctamente`() = runTest {
        val fakeList = listOf(Doctor(id = 1, nombre = "Juan"))
        whenever(repository.getDoctors()).thenReturn(fakeList)

        viewModel.fetchDoctors()

        assertEquals(fakeList, viewModel.doctors)
        assertNull(viewModel.errorMessage)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `fetchDoctors debe capturar errores`() = runTest {
        whenever(repository.getDoctors()).thenThrow(RuntimeException("Error API"))

        viewModel.fetchDoctors()

        assertEquals("Error API", viewModel.errorMessage)
        assertTrue(viewModel.doctors.isEmpty())
    }
}
