# MedConnect – Aplicación Android

Aplicación móvil desarrollada con **Kotlin + Jetpack Compose**, como parte de la evaluación parcial DSY1105.

---

## 👥 Integrantes
- Nombre Alumno A – Desarrollo Android
- Nombre Alumno B – Backend / Microservicio

---

# ✔ 1. Descripción General

MedConnect permite:

- Registrar y loguear usuarios
- Visualizar lista de doctores desde **API externa**
- Filtrar doctores por especialidad
- Agendar hora médica (especialidad → doctor → hora disponible)
- Guardar consultas localmente (DataStore)
- Visualizar las consultas agendadas del paciente

---

# ✔ 2. API Externa Consumida (Requisito de la Rúbrica)

La app consume la API de doctores:


Los datos obtenidos se muestran en:

- `DoctorListScreen`
- `DoctorDetailScreen`
- `AgendaScreen`

El consumo se hace mediante:

- `DoctorRepository`
- `DoctorViewModel`

Fragmento de código:

```kotlin
suspend fun getDoctors(): List<Doctor> {
    return api.getDoctors()
}

