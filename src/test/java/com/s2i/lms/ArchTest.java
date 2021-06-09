package com.s2i.lms;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.s2i.lms");

        noClasses()
            .that()
            .resideInAnyPackage("com.s2i.lms.service..")
            .or()
            .resideInAnyPackage("com.s2i.lms.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.s2i.lms.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
