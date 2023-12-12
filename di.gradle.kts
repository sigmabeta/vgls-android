plugins {
    kotlin("kapt")
}

dependencies {
    api(project(path = ":core:di"))
    kapt(Dagger.AndroidProcessor.library)
    kapt(Dagger.Compiler.library)
}
