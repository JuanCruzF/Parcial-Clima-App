package com.example.template.router

import org.junit.Assert.assertEquals
import org.junit.Test

class AppRouterTest {

    @Test
    fun whenCityIsNull_startInSelector() {
        val dest = AppRouter.getStartDestination(null)
        assertEquals("selector", dest)
    }

    @Test
    fun whenCityIsBlank_startInSelector() {
        val dest = AppRouter.getStartDestination("   ")
        assertEquals("selector", dest)
    }

    @Test
    fun whenCityExists_startInClimaRoute() {
        val dest = AppRouter.getStartDestination("Buenos Aires")
        assertEquals("clima/Buenos Aires", dest)
    }
}
