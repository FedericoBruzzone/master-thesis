module neverlang.core.typelang.test.types.ConcretePriority {
    reference syntax {
        ConcretePriority <-- "module";
    }
}

module neverlang.core.typelang.test.types.ConcreteAttributes {
    reference syntax {
        ConcreteAttribute <-- "modifier";
        ConcreteAttribute <-- "visibility";
        ConcreteAttribute <-- "retention";
    }
}

module neverlang.core.typelang.test.types.ConcreteType {
    reference syntax {
        ConcreteType <-- "module";
        ConcreteType <-- "file";
        ConcreteType <-- "function";
    }
}

module neverlang.core.typelang.test.types.ConcreteSignature {
    reference syntax {
        ConcreteSignature <-- "identifier";
        ConcreteSignature <-- "function";
    }
}

module neverlang.core.typelang.test.types.ConcreteVariance {
    reference syntax {
        ConcreteVariance <-- "controvariant";
        ConcreteVariance <-- "covariant";
        ConcreteVariance <-- "invariant";
    }
}

module neverlang.core.typelang.test.types.ConcreteCallback {
    reference syntax {
        ConcreteCallback <-- "validate";
    }
}

bundle neverlang.core.typelang.test.types.ConcreteTypes {
    slices
        neverlang.core.typelang.test.types.ConcreteSignature
        neverlang.core.typelang.test.types.ConcretePriority
        neverlang.core.typelang.test.types.ConcreteType
        neverlang.core.typelang.test.types.ConcreteAttributes
        neverlang.core.typelang.test.types.ConcreteVariance
        neverlang.core.typelang.test.types.ConcreteCallback

    rename {
        ConcretePriority --> Priority;
        ConcreteType --> Type;
        ConcreteSignature --> Signature;
        ConcreteAttribute --> Detail;
        ConcreteVariance --> TypeVariance;
        ConcreteCallback --> Callback;
    }
}

language neverlang.core.typelang.test.TypeLang {
    slices
        bundle (neverlang.core.typelang.blck.BlockBundle)
        bundle (neverlang.core.typelang.stmt.StmtBundle)
        bundle (neverlang.core.typelang.types.TypesBundle)
        bundle (neverlang.core.typelang.test.types.ConcreteTypes)
        neverlang.core.typelang.TypeLangMain

    endemic slices
        neverlang.core.typelang.EndemicSlices

    roles syntax < translate
}