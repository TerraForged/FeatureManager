package com.terraforged.fm.util;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredRandomFeatureList;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.MultipleRandomFeatureConfig;
import net.minecraft.world.gen.feature.MultipleWithChanceRandomFeatureConfig;
import net.minecraft.world.gen.feature.SingleRandomFeature;
import net.minecraft.world.gen.feature.TwoFeatureChoiceConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface FeatureVisitor {

    void visit(Feature<?> feature, IFeatureConfig config);

    void visit(Placement<?> decorator, IPlacementConfig config);

    default void visitConfigured(ConfiguredFeature<?, ?> feature) {
        if (feature.config instanceof DecoratedFeatureConfig) {
            visitDecorated((DecoratedFeatureConfig) feature.config);
            return;
        }

        // note SingleRandomFeature & SingleRandomFeatureConfig names a mixed up
        if (feature.config instanceof SingleRandomFeature) {
            visitSingle((SingleRandomFeature) feature.config);
            return;
        }

        if (feature.config instanceof TwoFeatureChoiceConfig) {
            visitTwoChoice((TwoFeatureChoiceConfig) feature.config);
            return;
        }

        if (feature.config instanceof MultipleRandomFeatureConfig) {
            visitMulti((MultipleRandomFeatureConfig) feature.config);
            return;
        }

        if (feature.config instanceof MultipleWithChanceRandomFeatureConfig) {
            visitMultiChance((MultipleWithChanceRandomFeatureConfig) feature.config);
            return;
        }

        visit(feature.feature, feature.config);
    }

    default void visitDecorated(DecoratedFeatureConfig config) {
        visitConfigured(config.feature);

        visit(config.decorator.decorator, config.decorator.config);
    }

    default void visitSingle(SingleRandomFeature config) {
        for (ConfiguredFeature<?, ?> feature : config.features) {
            visitConfigured(feature);
        }
    }

    default void visitTwoChoice(TwoFeatureChoiceConfig config) {
        visitConfigured(config.field_227285_a_);
        visitConfigured(config.field_227286_b_);
    }

    default void visitMulti(MultipleRandomFeatureConfig config) {
        for (ConfiguredRandomFeatureList<?> feature : config.features) {
            visitConfigured(feature.feature);
        }
    }

    default void visitMultiChance(MultipleWithChanceRandomFeatureConfig config) {
        for (ConfiguredFeature<?, ?> feature : config.features) {
            visitConfigured(feature);
        }
    }

    interface FeatureV extends FeatureVisitor {

        default void visit(Placement<?> decorator, IPlacementConfig config) {

        }
    }

    interface DecoratorV extends FeatureVisitor {

        default void visit(Feature<?> feature, IFeatureConfig config) {

        }
    }

    static Predicate<ConfiguredFeature<?, ?>> featureFilter(Predicate<Feature<?>> predicate) {
        return feature -> {
            AtomicBoolean result = new AtomicBoolean();
            FeatureVisitor.feature(f -> {
                if (predicate.test(f)) {
                    result.set(true);
                }
            }).visitConfigured(feature);
            return result.get();
        };
    }

    static FeatureVisitor feature(Consumer<Feature<?>> consumer) {
        return create((f, c) -> consumer.accept(f), (d, c) -> {});
    }

    static FeatureVisitor featureConfig(Consumer<IFeatureConfig> consumer) {
        return create((f, c) -> consumer.accept(c), (d, c) -> {});
    }

    static <T extends Feature<?>> FeatureVisitor feature(Class<T> type, Consumer<T> consumer) {
        return feature(consumerOf(type::isInstance, type::cast, consumer));
    }

    static <T extends IFeatureConfig> FeatureVisitor featureConfig(Class<T> type, Consumer<T> consumer) {
        return featureConfig(consumerOf(type::isInstance, type::cast, consumer));
    }

    static FeatureVisitor feature(BiConsumer<Feature<?>, IFeatureConfig> consumer) {
        return create(consumer, (d, c) -> {});
    }

    static FeatureVisitor decorator(Consumer<Placement<?>> consumer) {
        return create((f, c) -> {}, (d, c) -> consumer.accept(d));
    }

    static FeatureVisitor decoratorConfig(Consumer<IPlacementConfig> consumer) {
        return create((f, c) -> {}, (d, c) -> consumer.accept(c));
    }

    static <T extends Placement<?>> FeatureVisitor decorator(Class<T> type, Consumer<T> consumer) {
        return decorator(consumerOf(type::isInstance, type::cast, consumer));
    }

    static <T extends IPlacementConfig> FeatureVisitor decoratorConfig(Class<T> type, Consumer<T> consumer) {
        return decoratorConfig(consumerOf(type::isInstance, type::cast, consumer));
    }

    static FeatureVisitor decorator(BiConsumer<Placement<?>, IPlacementConfig> consumer) {
        return create((f, c) -> {}, consumer);
    }

    static <In, Out> Consumer<In> consumerOf(Predicate<In> predicate, Function<In, Out> mapper, Consumer<Out> consumer) {
        return in -> {
            if (predicate.test(in)) {
                Out out = mapper.apply(in);
                consumer.accept(out);
            }
        };
    }

    static FeatureVisitor create(BiConsumer<Feature<?>, IFeatureConfig> features, BiConsumer<Placement<?>, IPlacementConfig> decorators) {
        return new FeatureVisitor() {
            @Override
            public void visit(Feature<?> feature, IFeatureConfig config) {
                features.accept(feature, config);
            }

            @Override
            public void visit(Placement<?> decorator, IPlacementConfig config) {
                decorators.accept(decorator, config);
            }
        };
    }
}
