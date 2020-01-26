package com.terraforged.feature.matcher.dynamic;

import com.terraforged.feature.predicate.FeaturePredicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DynamicList implements Iterable<DynamicPredicate> {

    private List<DynamicPredicate> list = Collections.emptyList();

    public void add(DynamicMatcher matcher, FeaturePredicate predicate) {
        if (list.isEmpty()) {
            list = new ArrayList<>();
        }
        list.add(new DynamicPredicate(matcher, predicate));
    }

    @Override
    public Iterator<DynamicPredicate> iterator() {
        return list.iterator();
    }
}
