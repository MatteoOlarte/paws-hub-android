package com.software3.paws_hub_android.model.dal.entity.user

class CityObject {
    fun getAll(): List<String> {
        val localidadesBogota = listOf(
            "Usaquén",
            "Suba",
            "Chapinero",
            "Teusaquillo",
            "Barrios Unidos",
            "Engativá",
            "Fontibón",
            "Santa Fe",
            "Candelaria",
            "San Cristóbal",
            "Usme",
            "Tunjuelito",
            "Rafael Uribe Uribe",
            "Ciudad Bolívar",
            "Kennedy",
            "Puente Aranda",
            "Antonio Nariño",
            "Los Mártires",
            "Bosa"
        )
        return localidadesBogota
    }
}