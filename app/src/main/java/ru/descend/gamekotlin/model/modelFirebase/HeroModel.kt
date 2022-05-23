package ru.descend.gamekotlin.model.modelFirebase

data class HeroModel(
    var sourceFile: String? = null,
    var md5hash: String? = null
) : FireStoreModel()