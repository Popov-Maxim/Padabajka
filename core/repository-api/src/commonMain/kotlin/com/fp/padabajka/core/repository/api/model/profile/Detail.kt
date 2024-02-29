package com.fp.padabajka.core.repository.api.model.profile

sealed interface Detail {
    val name: String
}
//
//sealed class Tag(override val name: String) : Detail
//
//data class Preference<T: Topic>(val topic: T, val opinion: Opinion<T>) : Detail {
//    override val name: String
//        get() = "${topic.name}: ${opinion.name}"
//}
//
//sealed class Opinion<T: Topic> (val name: String)
//
//sealed class Topic (val name: String) {
//
//    abstract val opinions: List<Opinion<*>>
//
//
//}
//
//data object Smoking : Topic("Smoking") {
//    override val opinions: List<Opinion<Topic>>
//        get() = listOf<Opinion<Topic>>(NonSmoker, Smoker)
//
//
//}
//data object NonSmoker : Opinion<Smoking>("NonSmoker")
//data object Smoker : Opinion<Smoking>("Smoking")