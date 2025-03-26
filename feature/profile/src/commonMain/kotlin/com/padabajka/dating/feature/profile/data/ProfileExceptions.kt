@file:Suppress("Filename", "AnnotationOnSeparateLine")
package com.padabajka.dating.feature.profile.data

import com.padabajka.dating.core.repository.api.exception.ProfileException

data object ProfileIsNullException : ProfileException("Profile is null!")
