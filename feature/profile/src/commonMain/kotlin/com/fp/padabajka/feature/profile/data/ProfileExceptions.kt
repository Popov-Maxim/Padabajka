@file:Suppress("Filename", "AnnotationOnSeparateLine")
package com.fp.padabajka.feature.profile.data

import com.fp.padabajka.core.repository.api.exception.ProfileException

data object ProfileIsNullException : ProfileException("Profile is null!")
