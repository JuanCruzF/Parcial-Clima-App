package com.example.template

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.template.data.CiudadStorage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CiudadStorageTest {

    @Test
    fun guardarYObtenerCiudad_funcionaCorrectamente() {
        val context = ApplicationProvider.getApplicationContext<Context>()


        CiudadStorage.borrarCiudad(context)


        CiudadStorage.guardarCiudad(context, "Buenos Aires")


        val ciudad = CiudadStorage.obtenerCiudad(context)
        assertEquals("Buenos Aires", ciudad)


        CiudadStorage.borrarCiudad(context)
        val ciudadLuegoDeBorrar = CiudadStorage.obtenerCiudad(context)
        assertNull(ciudadLuegoDeBorrar)
    }
}
