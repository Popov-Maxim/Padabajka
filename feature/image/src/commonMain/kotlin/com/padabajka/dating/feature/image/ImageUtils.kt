package com.padabajka.dating.feature.image

import coil3.request.ImageRequest

expect fun ImageRequest.Builder.allowHardwarePlatform(allow: Boolean): ImageRequest.Builder
