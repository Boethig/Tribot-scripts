import org.tribot.wastedbro.gradle.plugin.TribotPlugin

subprojects {
    apply<TribotPlugin>()
}

subprojects {
    tasks {
        getAt("copyToBin").dependsOn(assemble)
        getAt("repoPackage").dependsOn(assemble)

        classes {
            finalizedBy(getAt("copyToBin"))
            finalizedBy(getAt("repoPackage"))
        }
    }
    dependencies {
        implementation(project(":libraries:dax_api"))
        implementation(project(":libraries:DaxTracker"))
        implementation(project(":libraries:boe_api"))
        implementation(project(":libraries:rsitem_services"))
    }
}